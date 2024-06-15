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

package org.apache.bcel.data;

// Due to the way try finally is implemented in the standard java compiler
// from Oracle, this class generates a huge (>32767 code bytes) <init> method.
// Verified with javac versions 1.8.0_261, 11.0.10 and 17.0.1.
final class LargeMethod {
    {
        @SuppressWarnings("unused")
        int a;
        try {
            a = 0;
        } finally {
            try {
                a = 0;
            } finally {
                try {
                    a = 0;
                } finally {
                    try {
                        a = 0;
                    } finally {
                        try {
                            a = 0;
                        } finally {
                            try {
                                a = 0;
                            } finally {
                                try {
                                    a = 0;
                                } finally {
                                    try {
                                        a = 0;
                                    } finally {
                                        try {
                                            a = 0;
                                        } finally {
                                            try {
                                                a = 0;
                                            } finally {
                                                try {
                                                    a = 0;
                                                } finally {
                                                    try {
                                                        a = 0;
                                                    } finally {
                                                        a = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
