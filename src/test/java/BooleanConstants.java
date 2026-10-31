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
/**
 * Provides compiled boolean constants for BCELifier round-trip tests.
 * <p>
 * Test Java code does not reference this class directly. The tests locate its compiled class file by name and pass it to {@link org.apache.bcel.util.BCELifier}
 * to generate creator source code. That source is compiled and executed to recreate this class file and verify that its boolean constants are preserved.
 * </p>
 */
public class BooleanConstants {

    /**
     * A true constant.
     */
    public static final boolean ON = true;

    /**
     * A false constant.
     */
    public static final boolean OFF = false;
}
