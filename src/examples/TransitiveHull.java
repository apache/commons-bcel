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
 */

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.ClassQueue;
import org.apache.bcel.util.ClassSet;

/**
 * Find all classes referenced by given start class and all classes referenced by those and so on. In other words:
 * Compute the transitive hull of classes used by a given class. This is done by checking all ConstantClass entries and
 * all method and field signatures.
 * <p>
 * This may be useful in order to put all class files of an application into a single JAR file, e.g..
 * </p>
 * <p>
 * It fails however in the presence of reflexive code aka introspection.
 * </p>
 * <p>
 * You'll need Apache's regular expression library supplied together with BCEL to use this class.
 * </p>
 */
public class TransitiveHull extends org.apache.bcel.classfile.EmptyVisitor {

    public static final String[] IGNORED = {"java[.].*", "javax[.].*", "sun[.].*", "sunw[.].*", "com[.]sun[.].*", "org[.]omg[.].*", "org[.]w3c[.].*",
        "org[.]xml[.].*", "net[.]jini[.].*"};

    public static void main(final String[] argv) {
        JavaClass javaClass;

        try {
            if (argv.length == 0) {
                System.err.println("transitive: No input files specified");
            } else {
                if ((javaClass = Repository.lookupClass(argv[0])) == null) {
                    javaClass = new ClassParser(argv[0]).parse();
                }

                final TransitiveHull hull = new TransitiveHull(javaClass);

                hull.start();
                System.out.println(Arrays.asList(hull.getClassNames()));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private final ClassQueue queue;
    private final ClassSet set;

    private ConstantPool cp;

    private String[] ignored = IGNORED;

    public TransitiveHull(final JavaClass clazz) {
        queue = new ClassQueue();
        queue.enqueue(clazz);
        set = new ClassSet();
        set.add(clazz);
    }

    private void add(String className) {
        className = Utility.pathToPackage(className);

        for (final String anIgnored : ignored) {
            if (Pattern.matches(anIgnored, className)) {
                return;
            }
        }

        try {
            final JavaClass clazz = Repository.lookupClass(className);

            if (set.add(clazz)) {
                queue.enqueue(clazz);
            }
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("Missing class: " + e.toString());
        }
    }

    private void checkType(Type type) {
        if (type instanceof ArrayType) {
            type = ((ArrayType) type).getBasicType();
        }

        if (type instanceof ObjectType) {
            add(((ObjectType) type).getClassName());
        }
    }

    public JavaClass[] getClasses() {
        return set.toArray();
    }

    public String[] getClassNames() {
        return set.getClassNames();
    }

    public String[] getIgnored() {
        return ignored;
    }

    /**
     * Sets the value of ignored.
     *
     * @param v Value to assign to ignored.
     */
    public void setIgnored(final String[] v) {
        ignored = v;
    }

    /**
     * Start traversal using DescendingVisitor pattern.
     */
    public void start() {
        while (!queue.empty()) {
            final JavaClass clazz = queue.dequeue();
            cp = clazz.getConstantPool();

            new org.apache.bcel.classfile.DescendingVisitor(clazz, this).visit();
        }
    }

    @Override
    public void visitConstantClass(final ConstantClass cc) {
        final String className = (String) cc.getConstantValue(cp);
        add(className);
    }

    @Override
    public void visitConstantFieldref(final ConstantFieldref cfr) {
        visitRef(cfr, false);
    }

    @Override
    public void visitConstantInterfaceMethodref(final ConstantInterfaceMethodref cimr) {
        visitRef(cimr, true);
    }

    @Override
    public void visitConstantMethodref(final ConstantMethodref cmr) {
        visitRef(cmr, true);
    }

    private void visitRef(final ConstantCP ccp, final boolean method) {
        final String className = ccp.getClass(cp);
        add(className);

        final ConstantNameAndType cnat = cp.getConstant(ccp.getNameAndTypeIndex(), Const.CONSTANT_NameAndType, ConstantNameAndType.class);

        final String signature = cnat.getSignature(cp);

        if (method) {
            final Type type = Type.getReturnType(signature);

            checkType(type);

            for (final Type type1 : Type.getArgumentTypes(signature)) {
                checkType(type1);
            }
        } else {
            checkType(Type.getType(signature));
        }
    }
}
