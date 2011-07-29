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
package Mini;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Vector;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;

public class MiniC implements org.apache.bcel.Constants {
  private static Vector<String> errors   = null;
  private static Vector<String> warnings = null;
  private static String file     = null;
  private static int   pass      = 0;
        
  public static void main(String[] argv) {
    String[]   file_name = new String[argv.length];
    int        files=0;
    MiniParser parser=null;
    String     base_name=null;
    boolean    byte_code=true;

    try {
      /* Parse command line arguments.
       */
      for(int i=0; i < argv.length; i++) {
        if(argv[i].charAt(0) == '-') {  // command line switch
          if(argv[i].equals("-java")) {
        byte_code=false;
    } else if(argv[i].equals("-bytecode")) {
        byte_code=true;
    } else {
        throw new Exception("Unknown option: " + argv[i]);
    }
        }
        else { // add file name to list
          file_name[files++] = argv[i];
        }
      }
        
      if(files == 0) {
        System.err.println("Nothing to compile.");
    }

      for(int j=0; j < files; j++) {
        errors   = new Vector<String>();
        warnings = new Vector<String>();
        pass     = 0;

        if(j == 0) {
        parser = new MiniParser(new java.io.FileInputStream(file_name[0]));
    } else {
        MiniParser.ReInit(new java.io.FileInputStream(file_name[j]));
    }

        int index = file_name[j].lastIndexOf('.');
        if(index > 0) {
        base_name = file_name[j].substring(0, index);
    } else {
        base_name = file_name[j];
    }

        if((index = base_name.lastIndexOf(File.separatorChar)) > 0) {
        base_name = base_name.substring(index + 1);
    }

        file   = file_name[j];

        System.out.println("Parsing ...");
        MiniParser.Program();
        ASTProgram program = (ASTProgram)MiniParser.jjtree.rootNode();

        System.out.println("Pass 1: Optimizing parse tree ...");
        pass    = 1;
        program = program.traverse();
        // program.dump(">");

        if(errors.size() == 0) {
          System.out.println("Pass 2: Type checking (I) ...");
          program.eval(pass=2);
        }

        if(errors.size() == 0) {
          System.out.println("Pass 3: Type checking (II) ...");
          program.eval(pass=3);
        }

        for(int i=0; i < errors.size(); i++) {
            System.out.println(errors.elementAt(i));
        }

        for(int i=0; i < warnings.size(); i++) {
            System.out.println(warnings.elementAt(i));
        }

        if(errors.size() == 0) {
          if(byte_code) {
            System.out.println("Pass 5: Generating byte code ...");
            ClassGen class_gen = new ClassGen(base_name, "java.lang.Object",
                                              file_name[j],
                                              ACC_PUBLIC | ACC_FINAL |
                                              ACC_SUPER, null);
            ConstantPoolGen cp = class_gen.getConstantPool();

            program.byte_code(class_gen, cp);
            JavaClass clazz = class_gen.getJavaClass();
            clazz.dump(base_name + ".class");
          }
          else {
            System.out.println("Pass 5: Generating Java code ...");
            PrintWriter out = new PrintWriter(new FileOutputStream(base_name + ".java"));
            program.code(out, base_name);
            out.close();

            System.out.println("Pass 6: Compiling Java code ...");

            String[] args = { "javac", base_name + ".java" };
            //sun.tools.javac.Main compiler = new sun.tools.javac.Main(System.err, "javac");
            try {
              Process p = Runtime.getRuntime().exec(args);
              p.waitFor();
            } catch(Exception e) {System.out.println(e); }

            //compiler.compile(args);
          }
        }

        if((errors.size() > 0) || (warnings.size() > 0)) {
        System.out.println(errors.size() + " errors and " + warnings.size() +
                             " warnings.");
    }
      }
    } catch(Exception e) { e.printStackTrace(); }
  }


  final static void addError(int line, int column, String err) {
    if(pass != 2) {
        errors.addElement(file + ":" + fillup(line, 3) + "," + fillup(column, 2) +
                          ": " + err);
    }
  }

  final static void addWarning(int line, int column, String err) {
    warnings.addElement("Warning: " + file + ":" + fillup(line, 3) + "," +
                        fillup(column, 3) + ": " + err);
  }

  final static String fillup(int n, int len) {
    String str  = Integer.toString(n);
    int    diff = len - str.length();

    if(diff > 0) {
      char[] chs = new char[diff];
      
      for(int i=0; i < diff; i++) {
        chs[i] = ' ';
    }

      return new String(chs) + str;
    } else {
        return str;
    }
  }

  final static void addWarning(String err) { warnings.addElement(err); }
}
