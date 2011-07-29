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
import java.util.Arrays;
import java.util.regex.Pattern;
import org.apache.bcel.Constants;
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
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.ClassQueue;
import org.apache.bcel.util.ClassSet;

/**
 * Find all classes referenced by given start class and all classes referenced
 * by those and so on. In other words: Compute the transitive hull of classes
 * used by a given class. This is done by checking all ConstantClass entries and
 * all method and field signatures.<br>
 * This may be useful in order to put all class files of an application into a
 * single JAR file, e.g..
 * <p>
 * It fails however in the presence of reflexive code aka introspection.
 * <p>
 * You'll need Apache's regular expression library supplied together with BCEL
 * to use this class.
 * 
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class TransitiveHull extends org.apache.bcel.classfile.EmptyVisitor {
    private ClassQueue                        _queue;
    private ClassSet                            _set;
    private ConstantPool                    _cp;
    private String[]                            _ignored    = IGNORED;

    public static final String[]    IGNORED        = { "java[.].*", "javax[.].*", "sun[.].*", "sunw[.].*",
            "com[.]sun[.].*", "org[.]omg[.].*", "org[.]w3c[.].*", "org[.]xml[.].*", "net[.]jini[.].*" };

    public TransitiveHull(JavaClass clazz) {
        _queue = new ClassQueue();
        _queue.enqueue(clazz);
        _set = new ClassSet();
        _set.add(clazz);
    }

    public JavaClass[] getClasses() {
        return _set.toArray();
    }

    public String[] getClassNames() {
        return _set.getClassNames();
    }

    /**
     * Start traversal using DescendingVisitor pattern.
     */
    public void start() {
        while (!_queue.empty()) {
            JavaClass clazz = _queue.dequeue();
            _cp = clazz.getConstantPool();

            new org.apache.bcel.classfile.DescendingVisitor(clazz, this).visit();
        }
    }

    private void add(String class_name) {
        class_name = class_name.replace('/', '.');

        for (int i = 0; i < _ignored.length; i++) {
            if (Pattern.matches(_ignored[i], class_name)) {
                return;
            }
        }

        try {
            JavaClass clazz = Repository.lookupClass(class_name);

            if (_set.add(clazz)) {
                _queue.enqueue(clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Missing class: " + e.toString());
        }
    }

    @Override
    public void visitConstantClass(ConstantClass cc) {
        String class_name = (String) cc.getConstantValue(_cp);
        add(class_name);
    }

    private void checkType(Type type) {
        if (type instanceof ArrayType) {
            type = ((ArrayType) type).getBasicType();
        }

        if (type instanceof ObjectType) {
            add(((ObjectType) type).getClassName());
        }
    }

    private void visitRef(ConstantCP ccp, boolean method) {
        String class_name = ccp.getClass(_cp);
        add(class_name);

        ConstantNameAndType cnat = (ConstantNameAndType) _cp.getConstant(ccp.getNameAndTypeIndex(),
                Constants.CONSTANT_NameAndType);

        String signature = cnat.getSignature(_cp);

        if (method) {
            Type type = Type.getReturnType(signature);

            checkType(type);

            Type[] types = Type.getArgumentTypes(signature);

            for (int i = 0; i < types.length; i++) {
                checkType(types[i]);
            }
        } else {
            checkType(Type.getType(signature));
        }
    }

    @Override
    public void visitConstantMethodref(ConstantMethodref cmr) {
        visitRef(cmr, true);
    }

    @Override
    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref cimr) {
        visitRef(cimr, true);
    }

    @Override
    public void visitConstantFieldref(ConstantFieldref cfr) {
        visitRef(cfr, false);
    }

    public String[] getIgnored() {
        return _ignored;
    }

    /**
     * Set the value of _ignored.
     * 
     * @param v
     *          Value to assign to _ignored.
     */
    public void setIgnored(String[] v) {
        _ignored = v;
    }

    public static void main(String[] argv) {
        JavaClass java_class;

        try {
            if (argv.length == 0) {
                System.err.println("transitive: No input files specified");
            } else {
                if ((java_class = Repository.lookupClass(argv[0])) == null) {
                    java_class = new ClassParser(argv[0]).parse();
                }

                TransitiveHull hull = new TransitiveHull(java_class);

                hull.start();
                System.out.println(Arrays.asList(hull.getClassNames()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
