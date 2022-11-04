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

package org.apache.bcel;

import org.apache.bcel.classfile.DescendingVisitor;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.visitors.CountingVisitor;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractCounterVisitorTestCase extends AbstractTestCase {
    private CountingVisitor visitor;

    protected abstract JavaClass getTestClass() throws ClassNotFoundException;

    public CountingVisitor getVisitor() {
        if (visitor == null) {
            visitor = new CountingVisitor();
        }
        return visitor;
    }

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        visitor = new CountingVisitor();
        new DescendingVisitor(getTestClass(), getVisitor()).visit();
    }

    public void setVisitor(final CountingVisitor visitor) {
        this.visitor = visitor;
    }
}
