/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.apache.bcel.verifier.tests;

public class TestReturn02 {

    public static String test1(char[] data, int offset, int count) {
        return new String(data, offset, count);
    }
    
    public static Object test2(){
        return new Object();
    }
    
    public static boolean test3(){
        return true;
    }
    
    public static byte test4(){
        return 1;
    }
    
    public static short test5(){
        return 1;
    }
    
    public static char test6(){
        return 'a';
    }
    
    public static int test7(){
        return 1;
    }
    
    public static long test8(){
        return 1l;
    }
    
    public static float test9(){
        return 1.0f;
    }
    
    public static double test10(){
        return 1.0;
    }
    
    public static Object test11(){
        return null;
    }
}
