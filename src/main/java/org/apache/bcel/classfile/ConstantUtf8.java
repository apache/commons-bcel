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
 * and represents a reference to a UTF-8 encoded string.
 *
 * <p>When working with large number of class files, caching {@link ConstantUtf8} instances that
 * have the same underlying string saves run-time memory usage.
 * By default, the instances are not cached; for example, {@code ConstantUtf8("java.lang.String")}
 * in different {@link org.apache.bcel.util.ClassPath.ClassFile} are different instances.
 * The following system properties control the behavior of the caching:
 *
 * <dl>
 *   <dt>{@code bcel.constant.cache.max.size}</dt>
 *   <dd>The maximum size of cache table. When the cache table exceeds the size, it removes the
 *   eldest entry from the table. By default it is {@code 0} (no caching). To remove the limit, set
 *   this value to {@code -1}.</dd>
 *   <dt>{@code bcel.constant.cache.entry.max.length}</dt>
 *   <dd>The maximum length of the UTF-8 encoded string to store in the cache table. By default it
 *   is {@code 200}. </dd>
 * </dl>
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

    // If this system property is more than zero, instances are reused for the same value.
    // By default, the instances are not reused to avoid performance degradation (BCEL-186).
    static final String MAX_CACHED_SIZE_KEY = "bcel.constant.cache.max.size";
    // The maximum length of cached ConstantUtf8 string. Default: 200.
    static final String MAX_CACHED_ENTRY_LENGTH_KEY = "bcel.constant.cache.entry.max.length";

    private static final CachingGenerator cachingGenerator = new CachingGenerator();
    private static final DefaultGenerator defaultGenerator = new DefaultGenerator();

    private static final Generator generator = selectGenerator();

    static Generator selectGenerator() {

        // Not static so that unit test can change the value before instantiating them
        int maxCachedSize =  Integer.getInteger(MAX_CACHED_SIZE_KEY, 0);
        return maxCachedSize > 0 ? cachingGenerator : defaultGenerator;
    }

    private static final boolean BCEL_STATISTICS = Boolean.getBoolean("bcel.statistics");

    interface Generator {
        ConstantUtf8 getInstance(String s);
    }

    static final class DefaultGenerator implements Generator {
        @Override
        public ConstantUtf8 getInstance(String s) {
            return new ConstantUtf8(s);
        }
    }

    private static final class CachingGenerator implements Generator {
        // Not static so that unit test can change the value before instantiating them
        private final int maxCacheEntries =
                Integer.getInteger(MAX_CACHED_SIZE_KEY, 20000);// CHECKSTYLE IGNORE MagicNumber

        private final int maxCachedSize = Integer.getInteger(MAX_CACHED_ENTRY_LENGTH_KEY, 200);

        private final HashMap<String, ConstantUtf8> cache;

        private CachingGenerator() {
            int initialCacheCapacity = (int)(maxCacheEntries /0.75);
            if (maxCacheEntries == -1) {
                // No capacity limit
                cache = new HashMap<>(initialCacheCapacity);
            } else {
                cache = new LinkedHashMap<String, ConstantUtf8>(initialCacheCapacity, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(final Map.Entry<String, ConstantUtf8> eldest) {
                        return size() > maxCacheEntries;
                    }
                };
            }
        }

        public ConstantUtf8 getInstance(final String s) {
            if (s.length() > maxCachedSize) {
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

    /**
     * @since 6.0
     */
    public static ConstantUtf8 getCachedInstance(final String s) {
        return cachingGenerator.getInstance(s);
    }

    /**
     * @since 6.0
     */
    public static ConstantUtf8 getInstance(final String s) {
        return generator.getInstance(s);
    }

    /**
     * @since 6.0
     */
    public static ConstantUtf8 getInstance(final DataInput input)  throws IOException {
        return getInstance(input.readUTF());
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
