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
package org.apache.bcel.generic;

import org.apache.bcel.Const;

/**
 * Denotes basic type such as int.
 */
public final class BasicType extends Type {

    // @since 6.0 no longer final
    public static BasicType getType(final byte type) {
        switch (type) {
        case Const.T_VOID:
            return VOID;
        case Const.T_BOOLEAN:
            return BOOLEAN;
        case Const.T_BYTE:
            return BYTE;
        case Const.T_SHORT:
            return SHORT;
        case Const.T_CHAR:
            return CHAR;
        case Const.T_INT:
            return INT;
        case Const.T_LONG:
            return LONG;
        case Const.T_DOUBLE:
            return DOUBLE;
        case Const.T_FLOAT:
            return FLOAT;
        default:
            throw new ClassGenException("Invalid type: " + type);
        }
    }

    /**
     * Constructor for basic types such as int, long, 'void'
     *
     * @param type one of T_INT, T_BOOLEAN, ..., T_VOID
     * @see Const
     */
    BasicType(final byte type) {
        super(type, Const.getShortTypeName(type));
        if (type < Const.T_BOOLEAN || type > Const.T_VOID) {
            throw new ClassGenException("Invalid type: " + type);
        }
    }

    /**
     * @return true if both type objects refer to the same type
     */
    @Override
    public boolean equals(final Object type) {
        return type instanceof BasicType && ((BasicType) type).getType() == this.getType();
    }

    /**
     * @return a hash code value for the object.
     */
    @Override
    public int hashCode() {
        return super.getType();
    }
}
