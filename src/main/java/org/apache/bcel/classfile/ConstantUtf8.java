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
package org.apache.bcel.classfile;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.bcel.Constants;

/** 
 * This class is derived from the abstract 
 * <A HREF="org.apache.bcel.classfile.Constant.html">Constant</A> class 
 * and represents a reference to a Utf8 encoded string.
 *
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantUtf8 extends Constant {

    private static final long serialVersionUID = -8709101585611518985L;
    private final String bytes;

    private static final int MAX_CACHE_ENTRIES = 20000;
    private static final int INITIAL_CACHE_CAPACITY = (int)(MAX_CACHE_ENTRIES/0.75);
    private static HashMap<String, ConstantUtf8> cache;
    private static int considered = 0;
    private static int hits = 0;
    private static int skipped = 0;
    private static int created = 0;
    final static boolean BCEL_STATISTICS = Boolean.getBoolean("bcel.statistics");
    final static boolean BCEL_DONT_CACHE = Boolean.getBoolean("bcel.dontCache");

    static {
        if (BCEL_STATISTICS) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.err.println("Cache hit " + hits + "/" + considered +", "
                            + skipped + " skipped");
                    System.err.println("Total of " + created + " ConstantUtf8 objects created");
                }
            });
        }
    }

    public static synchronized ConstantUtf8 getCachedInstance(String s) {
        if (BCEL_DONT_CACHE || s.length() > 200) {
            skipped++;
            return  new ConstantUtf8(s);
        }
        considered++;
        if (cache == null)  {
            cache = new LinkedHashMap<String, ConstantUtf8>(INITIAL_CACHE_CAPACITY, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, ConstantUtf8> eldest) {
                     return size() > MAX_CACHE_ENTRIES;
                }
            };
        }
        ConstantUtf8 result = cache.get(s);
        if (result != null) {
                hits++;
                return result;
            }
        result = new ConstantUtf8(s);
        cache.put(s, result);
        return result;
    }

    public static ConstantUtf8 getInstance(String s) {
        return getCachedInstance(s);
    }

    public static ConstantUtf8 getInstance (DataInputStream file)  throws IOException {
        return getInstance(file.readUTF());
    }

    /**
     * Initialize from another object.
     */
    public ConstantUtf8(ConstantUtf8 c) {
        this(c.getBytes());
    }


    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantUtf8(DataInput file) throws IOException {
        super(Constants.CONSTANT_Utf8);
        bytes = file.readUTF();
        created++;
    }


    /**
     * @param bytes Data
     */
    public ConstantUtf8(String bytes) {
        super(Constants.CONSTANT_Utf8);
        if (bytes == null) {
            throw new IllegalArgumentException("bytes must not be null!");
        }
        this.bytes = bytes;
        created++;
    }


    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( Visitor v ) {
        v.visitConstantUtf8(this);
    }


    /**
     * Dump String in Utf8 format to file stream.
     *
     * @param file Output file stream
     * @throws IOException
     */
    @Override
    public final void dump( DataOutputStream file ) throws IOException {
        file.writeByte(tag);
        file.writeUTF(bytes);
    }


    /**
     * @return Data converted to string.
     */
    public final String getBytes() {
        return bytes;
    }


    /**
     * @param bytes the raw bytes of this Utf-8
     * @deprecated
     */
    @java.lang.Deprecated
    public final void setBytes( String bytes ) {
        throw new UnsupportedOperationException();
    }


    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return super.toString() + "(\"" + Utility.replace(bytes, "\n", "\\n") + "\")";
    }
}
