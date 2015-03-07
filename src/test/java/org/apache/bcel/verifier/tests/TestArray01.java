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

import java.io.Serializable;

public class TestArray01{

    public static Object test1(){
        String[] a = new String[4];
        a[0] = "";
        a.equals(null);
        test2(a);
        test3(a);
        test4(a);
        return a;
    }

    @SuppressWarnings("unused")
    public static void test2(Object o){
    }

    @SuppressWarnings("unused")
    public static void test3(Serializable o){
    }

    @SuppressWarnings("unused")
    public static void test4(Cloneable o){
    }

    public static Serializable test5(){
        return new Object[1];
    }

    public static Cloneable test6(){
        return new Object[1];
    }

    public static Object foo(String s){
        return s;
    }
}