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
package org.apache.commons.bcel6.classfile;

import org.apache.commons.bcel6.Constants;

/**
 * Super class for all objects that have modifiers like private, final, ...
 * I.e. classes, fields, and methods.
 *
 * @version $Id$
 */
public abstract class AccessFlags implements java.io.Serializable {

    private static final long serialVersionUID = 2845404654039163061L;
    private final int access_flags;


    public AccessFlags() {
        this(0);
    }


    /**
     * @param a inital access flags
     */
    public AccessFlags(int a) {
        access_flags = a;
    }


    /** 
     * @return Access flags of the object aka. "modifiers".
     */
    public final int getAccessFlags() {
        return access_flags;
    }


    /** 
     * @return Access flags of the object aka. "modifiers".
     */
    public final int getModifiers() {
        return access_flags;
    }


    public final boolean isPublic() {
        return (access_flags & Constants.ACC_PUBLIC) != 0;
    }


    public final boolean isPrivate() {
        return (access_flags & Constants.ACC_PRIVATE) != 0;
    }


    public final boolean isProtected() {
        return (access_flags & Constants.ACC_PROTECTED) != 0;
    }


    public final boolean isStatic() {
        return (access_flags & Constants.ACC_STATIC) != 0;
    }


    public final boolean isFinal() {
        return (access_flags & Constants.ACC_FINAL) != 0;
    }


    public final boolean isSynchronized() {
        return (access_flags & Constants.ACC_SYNCHRONIZED) != 0;
    }


    public final boolean isVolatile() {
        return (access_flags & Constants.ACC_VOLATILE) != 0;
    }


    public final boolean isTransient() {
        return (access_flags & Constants.ACC_TRANSIENT) != 0;
    }


    public final boolean isNative() {
        return (access_flags & Constants.ACC_NATIVE) != 0;
    }


    public final boolean isInterface() {
        return (access_flags & Constants.ACC_INTERFACE) != 0;
    }


    public final boolean isAbstract() {
        return (access_flags & Constants.ACC_ABSTRACT) != 0;
    }


    public final boolean isStrictfp() {
        return (access_flags & Constants.ACC_STRICT) != 0;
    }


    public final boolean isSynthetic() {
        return (access_flags & Constants.ACC_SYNTHETIC) != 0;
    }


    public final boolean isAnnotation() {
        return (access_flags & Constants.ACC_ANNOTATION) != 0;
    }


    public final boolean isEnum() {
        return (access_flags & Constants.ACC_ENUM) != 0;
    }
}
