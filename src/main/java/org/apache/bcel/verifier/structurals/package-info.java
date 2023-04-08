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
 */

/**
 * A PassVerifier class mostly used internally by JustIce, yielding a control flow graph for public use as a nice side effect.
 * <h2>Package Specification</h2>
 * <p>
 * This package contains a PassVerifier class for use with the JustIce verifier and its utility classes. Only the pass performing what Sun calls "Structural
 * Constraints on Java Virtual Machine Code" has a PassVerifier class here. JustIce calls this pass "Pass 3b".
 * </p>
 */
package org.apache.bcel.verifier.structurals;
