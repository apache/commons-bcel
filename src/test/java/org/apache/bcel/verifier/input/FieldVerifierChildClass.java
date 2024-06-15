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
 */

package org.apache.bcel.verifier.input;

public class FieldVerifierChildClass extends FieldVerifierSuperClass {

    public FieldVerifierChildClass(final FieldVerifierChildClass c) {
        super(c.publicField, c.getPrivateField(), c.protectedField, c.packagePrivateField);
    }

    public int getPackagePrivateField() {
        return packagePrivateField;
    }

    public int getProtectedField() {
        return protectedField;
    }

    public int getPublicField() {
        return publicField;
    }

    public void setPackagePrivateField(final int packagePrivateField) {
        this.packagePrivateField = packagePrivateField;
    }

    public void setProtectedField(final int protectedField) {
        this.protectedField = protectedField;
    }

    public void setPublicField(final int publicField) {
        this.publicField = publicField;
    }
}
