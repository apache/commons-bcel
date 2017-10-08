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
package org.apache.bcel.verifier.statics;


import org.apache.bcel.Const;
import org.apache.bcel.generic.Type;

/**
 * This class represents the upper half of a DOUBLE variable.
 * @version $Id$
 */
public final class DOUBLE_Upper extends Type{

    /** The one and only instance of this class. */
    private static final DOUBLE_Upper singleton = new DOUBLE_Upper();

    /** The constructor; this class must not be instantiated from the outside. */
    private DOUBLE_Upper() {
        super(Const.T_UNKNOWN, "Double_Upper");
    }

    /** Use this method to get the single instance of this class. */
    public static DOUBLE_Upper theInstance() {
        return singleton;
    }
}
