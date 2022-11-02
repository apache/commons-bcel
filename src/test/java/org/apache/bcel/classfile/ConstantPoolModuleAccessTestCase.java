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
package org.apache.bcel.classfile;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests {@code module-info.class} files.
 */
class ConstantPoolModuleAccessTestCase {

    @Test
    void testJREModules() throws Exception {
        Enumeration<URL> moduleURLs = getClass().getClassLoader().getResources("module-info.class");
        while (moduleURLs.hasMoreElements()) {
            URL url = moduleURLs.nextElement();
            try (InputStream inputStream = url.openStream()) {
                final ClassParser classParser = new ClassParser(inputStream, "module-info.class");
                final JavaClass javaClass = classParser.parse();
                final ConstantPool constantPool = javaClass.getConstantPool();
                final StringBuilder sb = new StringBuilder();
                final EmptyVisitor visitor = new EmptyVisitor() {
                    @Override
                    public void visitModule(Module obj) {
                        String[] usedClassNames = obj.getUsedClassNames(constantPool, true);
                        if (url.getPath().contains("junit-jupiter-engine")) {
                            assertEquals(1, usedClassNames.length);
                            assertEquals("org.junit.jupiter.api.extension.Extension", usedClassNames[0]);
                        } else if (url.getPath().contains("junit-platform-launcher")) {
                            List<String> expected = new ArrayList<>();
                            expected.add("org.junit.platform.engine.TestEngine");
                            expected.add("org.junit.platform.launcher.LauncherDiscoveryListener");
                            expected.add("org.junit.platform.launcher.LauncherSessionListener");
                            expected.add("org.junit.platform.launcher.PostDiscoveryFilter");
                            expected.add("org.junit.platform.launcher.TestExecutionListener");
                            assertEquals(expected, Arrays.asList(usedClassNames));
                        } else {
                            assertEquals(0, usedClassNames.length);
                        }
                        super.visitModule(obj);
                    }

                    @Override
                    public void visitModuleExports(ModuleExports obj) {
                        String packageName = obj.getPackageName(constantPool);
                        String[] toModuleNames = obj.getToModuleNames(constantPool);
                        if (url.getPath().contains("junit-platform-commons")) {
                            List<String> expected = new ArrayList<>();
                            expected.add("org.junit.jupiter.api");
                            expected.add("org.junit.jupiter.engine");
                            expected.add("org.junit.jupiter.migrationsupport");
                            expected.add("org.junit.jupiter.params");
                            expected.add("org.junit.platform.console");
                            expected.add("org.junit.platform.engine");
                            expected.add("org.junit.platform.launcher");
                            expected.add("org.junit.platform.reporting");
                            expected.add("org.junit.platform.runner");
                            expected.add("org.junit.platform.suite.api");
                            switch (packageName) {
                                case "org.junit.platform.commons.util":
                                    expected.add("org.junit.platform.suite.commons");
                                    // fall through
                                case "org.junit.platform.commons.logging":
                                    expected.add("org.junit.platform.suite.engine");
                                    expected.add("org.junit.platform.testkit");
                                    expected.add("org.junit.vintage.engine");
                                    assertEquals(expected, Arrays.asList(toModuleNames));
                                    break;
                                default:
                                    assertEquals(0, toModuleNames.length);
                                    break;
                            }
                        }
                        super.visitModuleExports(obj);
                    }

                    @Override
                    public void visitModuleProvides(ModuleProvides obj) {
                        String interfaceName = obj.getInterfaceName(constantPool);
                        String[] implementationClassNames = obj.getImplementationClassNames(constantPool, true);
                        if (url.getPath().contains("junit-jupiter-engine")) {
                            assertEquals("org.junit.platform.engine.TestEngine", interfaceName);
                            assertEquals(1, implementationClassNames.length);
                            assertEquals("org.junit.jupiter.engine.JupiterTestEngine", implementationClassNames[0]);
                        } else if (url.getPath().contains("junit-platform-launcher")) {
                            assertEquals("org.junit.platform.launcher.TestExecutionListener", interfaceName);
                            assertEquals(1, implementationClassNames.length);
                            assertEquals("org.junit.platform.launcher.listeners.UniqueIdTrackingListener", implementationClassNames[0]);
                        }
                        super.visitModuleProvides(obj);
                    }
                    
                    @Override
                    public void visitModuleOpens(ModuleOpens obj) {
                        String packageName = obj.getPackageName(constantPool);
                        String[] toModuleNames = obj.getToModuleNames(constantPool);
                        if (url.getPath().contains("junit-jupiter-engine")) {
                            assertEquals("org.junit.jupiter.engine.extension", packageName);
                            assertEquals(1, toModuleNames.length);
                            assertEquals("org.junit.platform.commons", toModuleNames[0]);
                        }
                        if (url.getPath().contains("junit-jupiter-api")) {
                            assertEquals("org.junit.jupiter.api.condition", packageName);
                            assertEquals(1, toModuleNames.length);
                            assertEquals("org.junit.platform.commons", toModuleNames[0]);
                        }
                        super.visitModuleOpens(obj);
                    }
                    
                    @Override
                    public void visitModuleRequires(ModuleRequires obj) {
                        if (url.getPath().contains("junit-jupiter-engine")) {
                            String moduleName = obj.getModuleName(constantPool);
                            List<String> expected = new ArrayList<>();
                            expected.add("java.base");
                            expected.add("org.apiguardian.api");
                            expected.add("org.junit.jupiter.api");
                            expected.add("org.junit.platform.commons");
                            expected.add("org.junit.platform.engine");
                            expected.add("org.opentest4j");
                            assertTrue(expected.contains(moduleName));
                        }
                        super.visitModuleRequires(obj);
                    }
                };
                final DescendingVisitor descendingVisitor = new DescendingVisitor(javaClass, visitor);
                try {
                    javaClass.accept(descendingVisitor);
                    System.out.println(sb.toString());
                } catch (Exception | Error e) {
                    fail(visitor.toString(), e);
                }
            }
        }
    }
}
