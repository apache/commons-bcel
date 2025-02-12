/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;

/**
 * Utility class implementing a (typesafe) collection of JavaClass objects. Contains the most important methods of a
 * Vector.
 *
 * @deprecated as of 5.1.1 - 7/17/2005
 */
@Deprecated
public class ClassVector implements Serializable {

    private static final long serialVersionUID = 5600397075672780806L;

    @Deprecated
    protected transient List<JavaClass> vec = new ArrayList<>();

    public void addElement(final JavaClass clazz) {
        vec.add(clazz);
    }

    public JavaClass elementAt(final int index) {
        return vec.get(index);
    }

    @SuppressWarnings("unused") // SE_TRANSIENT_FIELD_NOT_RESTORED
    private void readObjectNoData() {
        vec = new ArrayList<>();
    }

    public void removeElementAt(final int index) {
        vec.remove(index);
    }

    public JavaClass[] toArray() {
        return vec.toArray(JavaClass.EMPTY_ARRAY);
    }
}
