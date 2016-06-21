/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.bcel.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Responsible for loading (class) files from the CLASSPATH. Inspired by
 * sun.tools.ClassPath.
 *
 * @version $Id$
 */
public class ClassPath {

    public static final ClassPath SYSTEM_CLASS_PATH = new ClassPath(getClassPath());

    private static final FilenameFilter ARCHIVE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept( final File dir, String name ) {
            name = name.toLowerCase(Locale.ENGLISH);
            return name.endsWith(".zip") || name.endsWith(".jar");
        }
    };

    private final PathEntry[] paths;
    private final String class_path;
    private ClassPath parent;

    public ClassPath(final ClassPath parent, final String class_path) {
        this(class_path);
        this.parent = parent;
    }

    /**
     * Search for classes in given path.
     * 
     * @param class_path
     */
    public ClassPath(final String class_path) {
        this.class_path = class_path;
        final List<PathEntry> list = new ArrayList<>();
        for (final StringTokenizer tok = new StringTokenizer(class_path, File.pathSeparator); tok.hasMoreTokens();) {
            final String path = tok.nextToken();
            if (!path.isEmpty()) {
                final File file = new File(path);
                try {
                    if (file.exists()) {
                        if (file.isDirectory()) {
                            list.add(new Dir(path));
                        } else {
                            list.add(new Zip(new ZipFile(file)));
                        }
                    }
                } catch (final IOException e) {
                    if (path.endsWith(".zip") || path.endsWith(".jar")) {
                        System.err.println("CLASSPATH component " + file + ": " + e);
                    }
                }
            }
        }
        paths = new PathEntry[list.size()];
        list.toArray(paths);
    }

    /**
     * Search for classes in CLASSPATH.
     * @deprecated Use SYSTEM_CLASS_PATH constant
     */
    @Deprecated
    public ClassPath() {
        this(getClassPath());
    }

    /** @return used class path string
     */
    @Override
    public String toString() {
        if (parent != null) {
            return parent + File.pathSeparator + class_path;
        }
        return class_path;
    }

    @Override
    public int hashCode() {
        if (parent != null) {
            return class_path.hashCode() + parent.hashCode();            
        }
        return class_path.hashCode();
    }


    @Override
    public boolean equals( final Object o ) {
        if (o instanceof ClassPath) {
            final ClassPath cp = (ClassPath)o;
            return class_path.equals(cp.toString());
        }
        return false;
    }


    private static void getPathComponents( final String path, final List<String> list ) {
        if (path != null) {
            final StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);
            while (tok.hasMoreTokens()) {
                final String name = tok.nextToken();
                final File file = new File(name);
                if (file.exists()) {
                    list.add(name);
                }
            }
        }
    }


    /** Checks for class path components in the following properties:
     * "java.class.path", "sun.boot.class.path", "java.ext.dirs"
     *
     * @return class path as used by default by BCEL
     */
    // @since 6.0 no longer final
    public static String getClassPath() {
        final String class_path = System.getProperty("java.class.path");
        final String boot_path = System.getProperty("sun.boot.class.path");
        final String ext_path = System.getProperty("java.ext.dirs");
        final List<String> list = new ArrayList<>();
        getPathComponents(class_path, list);
        getPathComponents(boot_path, list);
        final List<String> dirs = new ArrayList<>();
        getPathComponents(ext_path, dirs);
        for (final String d : dirs) {
            final File ext_dir = new File(d);
            final String[] extensions = ext_dir.list(ARCHIVE_FILTER);
            if (extensions != null) {
                for (final String extension : extensions) {
                    list.add(ext_dir.getPath() + File.separatorChar + extension);
                }
            }
        }
        final StringBuilder buf = new StringBuilder();
        String separator = "";
        for (final String path : list) {
            buf.append(separator);
            separator = File.pathSeparator;
            buf.append(path);
        }
        return buf.toString().intern();
    }


    /**
     * @param name fully qualified class name, e.g. java.lang.String
     * @return input stream for class
     */
    public InputStream getInputStream( final String name ) throws IOException {
        return getInputStream(name.replace('.', '/'), ".class");
    }


    /**
     * Return stream for class or resource on CLASSPATH.
     *
     * @param name fully qualified file name, e.g. java/lang/String
     * @param suffix file name ends with suff, e.g. .java
     * @return input stream for file on class path
     */
    public InputStream getInputStream( final String name, final String suffix ) throws IOException {
        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(name + suffix); // may return null
        } catch (final Exception e) {
            // ignored
        }
        if (is != null) {
            return is;
        }
        return getClassFile(name, suffix).getInputStream();
    }

    /**
     * @param name fully qualified resource name, e.g. java/lang/String.class
     * @return InputStream supplying the resource, or null if no resource with that name.
     * @since 6.0
     */
    public InputStream getResourceAsStream(final String name) {
        for (final PathEntry path : paths) {
            InputStream is;
            if ((is = path.getResourceAsStream(name)) != null) {
                return is;
            }
        }
        return null;
    }

    /**
     * @param name fully qualified resource name, e.g. java/lang/String.class
     * @return URL supplying the resource, or null if no resource with that name.
     * @since 6.0
     */
    public URL getResource(final String name) {
        for (final PathEntry path : paths) {
            URL url;
            if ((url = path.getResource(name)) != null) {
                return url;
            }
        }
        return null;
    }

    /**
     * @param name fully qualified resource name, e.g. java/lang/String.class
     * @return An Enumeration of URLs supplying the resource, or an
     * empty Enumeration if no resource with that name.
     * @since 6.0
     */
    public Enumeration<URL> getResources(final String name) {
        final Vector<URL> results = new Vector<>();
        for (final PathEntry path : paths) {
            URL url;
            if ((url = path.getResource(name)) != null) {
                results.add(url);
            }
        }
        return results.elements();
    }

    /**
     * @param name fully qualified file name, e.g. java/lang/String
     * @param suffix file name ends with suff, e.g. .java
     * @return class file for the java class
     */
    public ClassFile getClassFile( final String name, final String suffix ) throws IOException {
        ClassFile cf = null;

        if (parent != null) {
            cf = parent.getClassFileInternal(name, suffix);
        }

        if (cf == null) {
            cf = getClassFileInternal(name, suffix);
        }

        if (cf != null) {
            return cf;
        }

        throw new IOException("Couldn't find: " + name + suffix);
    }

    private ClassFile getClassFileInternal(final String name, final String suffix) throws IOException {

      for (final PathEntry path : paths) {
          final ClassFile cf = path.getClassFile(name, suffix);

          if(cf != null) {
              return cf;
          }
      }

      return null;
   }


    /**
     * @param name fully qualified class name, e.g. java.lang.String
     * @return input stream for class
     */
    public ClassFile getClassFile( final String name ) throws IOException {
        return getClassFile(name, ".class");
    }


    /**
     * @param name fully qualified file name, e.g. java/lang/String
     * @param suffix file name ends with suffix, e.g. .java
     * @return byte array for file on class path
     */
    public byte[] getBytes(final String name, final String suffix) throws IOException {
        DataInputStream dis = null;
        try (InputStream is = getInputStream(name, suffix)) {
            if (is == null) {
                throw new IOException("Couldn't find: " + name + suffix);
            }
            dis = new DataInputStream(is);
            final byte[] bytes = new byte[is.available()];
            dis.readFully(bytes);
            return bytes;
        } finally {
            if (dis != null) {
                dis.close();
            }
        }
    }


    /**
     * @return byte array for class
     */
    public byte[] getBytes( final String name ) throws IOException {
        return getBytes(name, ".class");
    }


    /**
     * @param name name of file to search for, e.g. java/lang/String.java
     * @return full (canonical) path for file
     */
    public String getPath( String name ) throws IOException {
        final int index = name.lastIndexOf('.');
        String suffix = "";
        if (index > 0) {
            suffix = name.substring(index);
            name = name.substring(0, index);
        }
        return getPath(name, suffix);
    }


    /**
     * @param name name of file to search for, e.g. java/lang/String
     * @param suffix file name suffix, e.g. .java
     * @return full (canonical) path for file, if it exists
     */
    public String getPath( final String name, final String suffix ) throws IOException {
        return getClassFile(name, suffix).getPath();
    }

    private abstract static class PathEntry {

        abstract ClassFile getClassFile( String name, String suffix ) throws IOException;
        abstract URL getResource(String name);
        abstract InputStream getResourceAsStream(String name);
    }

    /** Contains information about file/ZIP entry of the Java class.
     */
    public interface ClassFile {

        /** @return input stream for class file.
         */
        InputStream getInputStream() throws IOException;


        /** @return canonical path to class file.
         */
        String getPath();


        /** @return base path of found class, i.e. class is contained relative
         * to that path, which may either denote a directory, or zip file
         */
        String getBase();


        /** @return modification time of class file.
         */
        long getTime();


        /** @return size of class file.
         */
        long getSize();
    }

    private static class Dir extends PathEntry {

        private final String dir;


        Dir(final String d) {
            dir = d;
        }

        @Override
        URL getResource(final String name) {
            // Resource specification uses '/' whatever the platform
            final File file = new File(dir + File.separatorChar + name.replace('/', File.separatorChar));
            try {
                return file.exists() ? file.toURI().toURL() : null;
            } catch (final MalformedURLException e) {
               return null;
            }
        }

        @Override
        InputStream getResourceAsStream(final String name) {
            // Resource specification uses '/' whatever the platform
            final File file = new File(dir + File.separatorChar + name.replace('/', File.separatorChar));
            try {
               return file.exists() ? new FileInputStream(file) : null;
            } catch (final IOException e) {
               return null;
            }
        }

        @Override
        ClassFile getClassFile( final String name, final String suffix ) throws IOException {
            final File file = new File(dir + File.separatorChar
                    + name.replace('.', File.separatorChar) + suffix);
            return file.exists() ? new ClassFile() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return new FileInputStream(file);
                }


                @Override
                public String getPath() {
                    try {
                        return file.getCanonicalPath();
                    } catch (final IOException e) {
                        return null;
                    }
                }


                @Override
                public long getTime() {
                    return file.lastModified();
                }


                @Override
                public long getSize() {
                    return file.length();
                }


                @Override
                public String getBase() {
                    return dir;
                }
            } : null;
        }


        @Override
        public String toString() {
            return dir;
        }
    }

    private static class Zip extends PathEntry {

        private final ZipFile zip;


        Zip(final ZipFile z) {
            zip = z;
        }

        @Override
        URL getResource(final String name) {
            final ZipEntry entry = zip.getEntry(name);
            try {
                return (entry != null) ? new URL("jar:file:" + zip.getName() + "!/" + name) : null;
            } catch (final MalformedURLException e) {
                return null;
           }
        }

        @Override
        InputStream getResourceAsStream(final String name) {
            final ZipEntry entry = zip.getEntry(name);
            try {
                return (entry != null) ? zip.getInputStream(entry) : null;
            } catch (final IOException e) {
                return null;
            }
        }

        @Override
        ClassFile getClassFile( final String name, final String suffix ) throws IOException {
            final ZipEntry entry = zip.getEntry(name.replace('.', '/') + suffix);

            if (entry == null) {
                return null;
            }

            return new ClassFile() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return zip.getInputStream(entry);
                }


                @Override
                public String getPath() {
                    return entry.toString();
                }


                @Override
                public long getTime() {
                    return entry.getTime();
                }


                @Override
                public long getSize() {
                    return entry.getSize();
                }


                @Override
                public String getBase() {
                    return zip.getName();
                }
            };
        }
    }
}
