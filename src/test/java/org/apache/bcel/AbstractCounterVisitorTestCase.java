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

package org.apache.bcel;

import org.apache.bcel.classfile.DescendingVisitor;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.visitors.CounterVisitor;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractCounterVisitorTestCase extends AbstractTestCase
{
    protected abstract JavaClass getTestClass() throws ClassNotFoundException;

    private CounterVisitor visitor = null;

    @BeforeEach
    public void setUp() throws ClassNotFoundException
    {
        visitor = new CounterVisitor();
        new DescendingVisitor(getTestClass(), getVisitor()).visit();
    }

    public CounterVisitor getVisitor()
    {
        if (visitor == null) {
            visitor = new CounterVisitor();
        }
        return visitor;
    }

    public void setVisitor(final CounterVisitor visitor)
    {
        this.visitor = visitor;
    }
}
