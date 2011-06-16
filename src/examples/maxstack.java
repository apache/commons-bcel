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
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;

/**
 * Read class file(s) and examine all of its methods, determining the
 * maximum stack depth used by analyzing control flow.
 *
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public final class maxstack {
  public static void main(String[] argv) { 
    try {
      for(int i = 0; i < argv.length; i++) {
    String    class_name = argv[i];
    JavaClass java_class = Repository.lookupClass(class_name);

    if(java_class == null) {
        java_class = new ClassParser(class_name).parse();
    }

    ConstantPoolGen cp      = new ConstantPoolGen(java_class.getConstantPool());
       Method[]        methods = java_class.getMethods();
    
    for(int j = 0; j < methods.length; j++) {
      Method    m  = methods[j];

      if(!(m.isAbstract() || m.isNative())) {
        MethodGen mg = new MethodGen(m, argv[i], cp);

        int compiled_stack  = mg.getMaxStack();
        int compiled_locals = mg.getMaxLocals();
        mg.setMaxStack(); // Recompute value
        mg.setMaxLocals();
        int computed_stack  = mg.getMaxStack();
        int computed_locals = mg.getMaxLocals();

        mg.getInstructionList().dispose(); // Reuse instruction handles
        
        System.out.println(m);
        
        if(computed_stack == compiled_stack) {
            System.out.println("Stack ok(" + computed_stack + ")");
        } else {
            System.out.println("\nCompiled stack size " + compiled_stack +
                       " computed size " + computed_stack);
        }
        
        if(computed_locals == compiled_locals) {
            System.out.println("Locals ok(" + computed_locals + ")");
        } else {
            System.out.println("\nCompiled locals " + compiled_locals +
                     " computed size " + computed_locals);
        }
      }
    }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
