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
package org.apache.commons.bcel6.generic;

import org.apache.commons.bcel6.Constants;

/** 
 * Denotes basic type such as int.
 *
 * @version $Id$
 */
public final class BasicType extends Type {

    /**
     * Constructor for basic types such as int, long, `void'
     *
     * @param type one of T_INT, T_BOOLEAN, ..., T_VOID
     * @see Constants
     */
    BasicType(byte type) {
        super(type, Constants.SHORT_TYPE_NAMES[type]);
        if ((type < Constants.T_BOOLEAN) || (type > Constants.T_VOID)) {
            throw new ClassGenException("Invalid type: " + type);
        }
    }


    // @since 6.0 no longer final
    public static BasicType getType( byte type ) {
        switch (type) {
            case Constants.T_VOID:
                return VOID;
            case Constants.T_BOOLEAN:
                return BOOLEAN;
            case Constants.T_BYTE:
                return BYTE;
            case Constants.T_SHORT:
                return SHORT;
            case Constants.T_CHAR:
                return CHAR;
            case Constants.T_INT:
                return INT;
            case Constants.T_LONG:
                return LONG;
            case Constants.T_DOUBLE:
                return DOUBLE;
            case Constants.T_FLOAT:
                return FLOAT;
            default:
                throw new ClassGenException("Invalid type: " + type);
        }
    }


    /** @return a hash code value for the object.
     */
    @Override
    public int hashCode() {
        return super.getType();
    }


    /** @return true if both type objects refer to the same type
     */
    @Override
    public boolean equals( Object _type ) {
        return (_type instanceof BasicType) ? ((BasicType) _type).getType() == this.getType() : false;
    }
}
