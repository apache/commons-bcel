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

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;

/**
 * Patch all Utf8 constants in the given class file <em>file</em>.class
 * and save the result in _<em>file</em>.class.
 *
 * Usage: patch <oldstring> <newstring> files
 *
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @version $Id$
 */
public class patchclass {

    public static void main(String[] argv) throws Exception {
        String[] file_name = new String[argv.length];
        int files = 0;

        if (argv.length < 3) {
            System.err.println("Usage: patch <oldstring> <newstring> file1.class ...");
            System.exit(-1);
        }

        for (int i = 2; i < argv.length; i++) {
            file_name[files++] = argv[i];
        }

        for (int i = 0; i < files; i++) {
            ClassParser parser = new ClassParser(file_name[i]);
            JavaClass java_class = parser.parse();

            patchIt(argv[0], argv[1], java_class.getConstantPool().getConstantPool());

            // Dump the changed class to a new file
            java_class.dump("_" + file_name[i]);
            System.out.println("Results saved in: _" + file_name[i]);
        }
    }

    /*
     * Replace all occurences of string "<em>old</em>" with 
     * "<em>replacement</em>" in all Utf8 constants
     */
    private static void patchIt(String old, String replacement, Constant[] constant_pool) {
        ConstantUtf8 c;
        String str;
        int index, old_index;
        StringBuffer buf;

        // Loop through constant pool
        for (short i = 0; i < constant_pool.length; i++) {
            if (constant_pool[i] instanceof ConstantUtf8) { // Utf8 string found
                try {
                    c = (ConstantUtf8) constant_pool[i]; // Get the string
                    str = c.getBytes();

                    if ((index = str.indexOf(old)) != -1) { // `old' found in str
                        buf = new StringBuffer();           // target buffer
                        old_index = 0;                      // String start offset

                        // While we have something to replace
                        while ((index = str.indexOf(old, old_index)) != -1) {
                            buf.append(str.substring(old_index, index)); // append prefix
                            buf.append(replacement);                     // append `replacement'

                            old_index = index + old.length(); // Skip `old'.length chars
                        }

                        buf.append(str.substring(old_index)); // append rest of string
                        str = buf.toString();

                        // Finally push the new string back to the constant pool
                        c = new ConstantUtf8(str);
                        constant_pool[i] = c;
                    }
                } catch (StringIndexOutOfBoundsException e) { // Should not occur
                    System.err.println(e);
                }
            }
        }
    }
}
