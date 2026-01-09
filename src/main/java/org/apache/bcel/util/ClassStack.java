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

import java.util.Stack;

import org.apache.bcel.classfile.JavaClass;

/**
 * Utility class implementing a (typesafe) stack of JavaClass objects.
 *
 * @see Stack
 */
public class ClassStack {

    private final Stack<JavaClass> stack = new Stack<>();

    /**
     * Checks if the stack is empty.
     *
     * @return true if the stack is empty.
     */
    public boolean empty() {
        return stack.empty();
    }

    /**
     * Pops a JavaClass from the stack.
     *
     * @return the JavaClass from the top of the stack.
     */
    public JavaClass pop() {
        return stack.pop();
    }

    /**
     * Pushes a JavaClass onto the stack.
     *
     * @param clazz the JavaClass to push.
     */
    public void push(final JavaClass clazz) {
        stack.push(clazz);
    }

    /**
     * Gets the top JavaClass from the stack without removing it.
     *
     * @return the JavaClass at the top of the stack.
     */
    public JavaClass top() {
        return stack.peek();
    }
}
