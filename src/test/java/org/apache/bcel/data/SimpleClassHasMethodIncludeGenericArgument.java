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
package org.apache.bcel.data;

import java.util.List;

/**
 * @author sam
 */
public class SimpleClassHasMethodIncludeGenericArgument {
    public static void a(String a1, List<String> a2) {
    }

    public static void b(String b1, List b2) {
    }

    public static void c(String c1, String c2) {
    }

    public static void d(List<String> d1, String d2) {
    }
}
