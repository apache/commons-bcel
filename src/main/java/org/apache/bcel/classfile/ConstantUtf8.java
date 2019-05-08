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
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.bcel.Const;

/**
 * This class is derived from the abstract {@link Constant}
 * and represents a reference to a Utf8 encoded string.
 *
 * @version $Id$
 * @see     Constant
 */
public final class ConstantUtf8 extends Constant {

    private final String bytes;

    // TODO these should perhaps be AtomicInt?
    private static volatile int considered = 0;
    private static volatile int hits = 0;
    private static volatile int skipped = 0;
    private static volatile int created = 0;

    // Set the size to 0 or below to skip caching entirely
    private static final int MAX_CACHED_SIZE =
            Integer.getInteger("bcel.maxcached.size", 200);// CHECKSTYLE IGNORE MagicNumber

    private static final Generator generator = MAX_CACHED_SIZE > 0 ? new CachedGenerator() :
            new NormalGenerator();

    private static final boolean BCEL_STATISTICS = Boolean.getBoolean("bcel.statistics");

    static {
        if (BCEL_STATISTICS) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    printStats();
                }
            });
        }
    }

    interface Generator {
        ConstantUtf8 getInstance(String s);
    }

    static class NormalGenerator implements Generator {
        @Override
        public ConstantUtf8 getInstance(String s) {
            return new ConstantUtf8(s);
        }
    }

    static class CachedGenerator implements Generator {
        private static final int MAX_CACHE_ENTRIES =
            Integer.getInteger("bcel.maxcache.entries", 20000);// CHECKSTYLE IGNORE MagicNumber
        private static final int INITIAL_CACHE_CAPACITY = (int)(MAX_CACHE_ENTRIES/0.75);

        private final HashMap<String, ConstantUtf8> cache =
                new LinkedHashMap<String, ConstantUtf8>(INITIAL_CACHE_CAPACITY, 0.75f, true) {
            private static final long serialVersionUID = -8506975356158971767L;

            @Override
            protected boolean removeEldestEntry(final Map.Entry<String, ConstantUtf8> eldest) {
                 return size() > MAX_CACHE_ENTRIES;
            }
        };

        public ConstantUtf8 getInstance(final String s) {
            if (s.length() > MAX_CACHED_SIZE) {
                skipped++;
                return  new ConstantUtf8(s);
            }
            considered++;
            synchronized (cache) {
                ConstantUtf8 result = cache.get(s);
                if (result != null) {
                    hits++;
                    return result;
                }
                result = new ConstantUtf8(s);
                cache.put(s, result);
                return result;
            }
        }
    }

    // for accesss by test code
    static void printStats() {
        System.err.println("Cache hit " + hits + "/" + considered +", " + skipped + " skipped");
        System.err.println("Total of " + created + " ConstantUtf8 objects created");
    }

    // for accesss by test code
    static void clearStats() {
        hits = considered = skipped = created = 0;
    }

    /**
     * @since 6.0
     */
    public static ConstantUtf8 getInstance (final DataInput input)  throws IOException {
        return generator.getInstance(input.readUTF());
    }

    /**
     * Initialize from another object.
     */
    public ConstantUtf8(final ConstantUtf8 c) {
        this(c.getBytes());
    }


    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantUtf8(final DataInput file) throws IOException {
        super(Const.CONSTANT_Utf8);
        bytes = file.readUTF();
        created++;
    }


    /**
     * @param bytes Data
     */
    public ConstantUtf8(final String bytes) {
        super(Const.CONSTANT_Utf8);
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
    public void accept( final Visitor v ) {
        v.visitConstantUtf8(this);
    }


    /**
     * Dump String in Utf8 format to file stream.
     *
     * @param file Output file stream
     * @throws IOException
     */
    @Override
    public final void dump( final DataOutputStream file ) throws IOException {
        file.writeByte(super.getTag());
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
     * @deprecated (since 6.0)
     */
    @java.lang.Deprecated
    public final void setBytes( final String bytes ) {
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
