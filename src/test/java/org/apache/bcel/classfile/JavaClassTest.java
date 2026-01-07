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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Date;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Currency;
import java.util.Deque;
import java.util.Dictionary;
import java.util.DoubleSummaryStatistics;
import java.util.DuplicateFormatFlagsException;
import java.util.EmptyStackException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.EventListenerProxy;
import java.util.EventObject;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.IllegalFormatCodePointException;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatException;
import java.util.IllegalFormatFlagsException;
import java.util.IllegalFormatPrecisionException;
import java.util.IllegalFormatWidthException;
import java.util.IllformedLocaleException;
import java.util.InputMismatchException;
import java.util.IntSummaryStatistics;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.MissingFormatWidthException;
import java.util.MissingResourceException;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.PropertyPermission;
import java.util.PropertyResourceBundle;
import java.util.Queue;
import java.util.Random;
import java.util.RandomAccess;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.SplittableRandom;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.UnknownFormatConversionException;
import java.util.UnknownFormatFlagsException;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link JavaClass}.
 */
class JavaClassTest {

    private static final String CLASS_NAME = "TargetClass";

    private static final Class<?>[] CLASSES_JAVA_LANG = { AbstractMethodError.class, Appendable.class, ArithmeticException.class,
            ArrayIndexOutOfBoundsException.class, ArrayStoreException.class, AssertionError.class, AutoCloseable.class, Boolean.class,
            BootstrapMethodError.class, Byte.class, Character.class, Character.Subset.class, Character.UnicodeBlock.class, Character.UnicodeScript.class,
            CharSequence.class, Class.class, ClassCastException.class, ClassCircularityError.class, ClassFormatError.class, ClassLoader.class,
            ClassNotFoundException.class, ClassValue.class, Cloneable.class, CloneNotSupportedException.class, Comparable.class,
            /* Compiler.class, */ Deprecated.class, Double.class, Enum.class, EnumConstantNotPresentException.class, Error.class, Exception.class,
            ExceptionInInitializerError.class, Float.class, FunctionalInterface.class, IllegalAccessError.class, IllegalAccessException.class,
            IllegalArgumentException.class, IllegalMonitorStateException.class, IllegalStateException.class, IllegalThreadStateException.class,
            IncompatibleClassChangeError.class, IndexOutOfBoundsException.class, InheritableThreadLocal.class, InstantiationError.class,
            InstantiationException.class, Integer.class, InternalError.class, InterruptedException.class, Iterable.class, LinkageError.class, Long.class,
            Math.class, NegativeArraySizeException.class, NoClassDefFoundError.class, NoSuchFieldError.class, NoSuchFieldException.class,
            NoSuchMethodError.class, NoSuchMethodException.class, NullPointerException.class, Number.class, NumberFormatException.class, Object.class,
            OutOfMemoryError.class, Override.class, Package.class, Process.class, ProcessBuilder.class, ProcessBuilder.Redirect.class,
            ProcessBuilder.Redirect.Type.class, Readable.class, ReflectiveOperationException.class, Runnable.class, Runtime.class, RuntimeException.class,
            RuntimePermission.class, SafeVarargs.class, SecurityException.class, SecurityManager.class, Short.class, StackOverflowError.class,
            StackTraceElement.class, StrictMath.class, String.class, StringBuffer.class, StringBuilder.class, StringIndexOutOfBoundsException.class,
            SuppressWarnings.class, System.class, Thread.class, Thread.State.class, Thread.UncaughtExceptionHandler.class, ThreadDeath.class, ThreadGroup.class,
            ThreadLocal.class, Throwable.class, TypeNotPresentException.class, UnknownError.class, UnsatisfiedLinkError.class,
            UnsupportedClassVersionError.class, UnsupportedOperationException.class, VerifyError.class, VirtualMachineError.class, Void.class };
// Doesn't compile due to cyclic inheritance
//    private interface InterfaceA extends InterfaceB {
//    }
//
//    private interface InterfaceB extends InterfaceA {
//    }

    private static final Class<?>[] CLASSES_JAVA_TIME = { Clock.class, DateTimeException.class, DayOfWeek.class, Duration.class, Instant.class, LocalDate.class, LocalDateTime.class,
            LocalTime.class, Month.class, MonthDay.class, OffsetDateTime.class, OffsetTime.class, Period.class, Year.class, YearMonth.class,
            ZonedDateTime.class, ZoneId.class, ZoneOffset.class };

    private static final Class<?>[] CLASSES_JAVA_UTIL = { AbstractCollection.class, AbstractList.class, AbstractMap.class, AbstractMap.SimpleEntry.class,
            AbstractMap.SimpleImmutableEntry.class, AbstractQueue.class, AbstractSequentialList.class, AbstractSet.class, ArrayDeque.class, ArrayList.class,
            Arrays.class, Base64.class, Base64.Decoder.class, Base64.Encoder.class, BitSet.class, Calendar.class, Calendar.Builder.class, Collection.class,
            Collections.class, Comparator.class, ConcurrentModificationException.class, Currency.class, Date.class, Deque.class, Dictionary.class,
            DoubleSummaryStatistics.class, DuplicateFormatFlagsException.class, EmptyStackException.class, EnumMap.class, Enumeration.class, EnumSet.class,
            EventListener.class, EventListenerProxy.class, EventObject.class, Formattable.class, FormattableFlags.class,
            FormatFlagsConversionMismatchException.class, Formatter.class, Formatter.BigDecimalLayoutForm.class, FormatterClosedException.class,
            GregorianCalendar.class, HashMap.class, HashSet.class, Hashtable.class, IdentityHashMap.class, IllegalFormatCodePointException.class,
            IllegalFormatConversionException.class, IllegalFormatException.class, IllegalFormatFlagsException.class, IllegalFormatPrecisionException.class,
            IllegalFormatWidthException.class, IllformedLocaleException.class, InputMismatchException.class, IntSummaryStatistics.class,
            InvalidPropertiesFormatException.class, Iterator.class, LinkedHashMap.class, LinkedHashSet.class, LinkedList.class, List.class, ListIterator.class,
            ListResourceBundle.class, Locale.class, Locale.Builder.class, Locale.Category.class, Locale.FilteringMode.class, Locale.LanguageRange.class,
            LongSummaryStatistics.class, Map.class, Map.Entry.class, MissingFormatArgumentException.class, MissingFormatWidthException.class,
            MissingResourceException.class, NavigableMap.class, NavigableSet.class, NoSuchElementException.class, Objects.class, Observable.class,
            Observer.class, Optional.class, OptionalDouble.class, OptionalInt.class, OptionalLong.class, PrimitiveIterator.class,
            PrimitiveIterator.OfDouble.class, PrimitiveIterator.OfInt.class, PrimitiveIterator.OfLong.class, PriorityQueue.class, Properties.class,
            PropertyPermission.class, PropertyResourceBundle.class, Queue.class, Random.class, RandomAccess.class, ResourceBundle.class,
            ResourceBundle.Control.class, Scanner.class, ServiceConfigurationError.class, ServiceLoader.class, Set.class, SimpleTimeZone.class, SortedMap.class,
            SortedSet.class, Spliterator.class, Spliterator.OfDouble.class, Spliterator.OfInt.class, Spliterator.OfLong.class, Spliterator.OfPrimitive.class,
            Spliterators.class, Spliterators.AbstractDoubleSpliterator.class, Spliterators.AbstractIntSpliterator.class,
            Spliterators.AbstractLongSpliterator.class, Spliterators.AbstractSpliterator.class, SplittableRandom.class, Stack.class, StringJoiner.class,
            StringTokenizer.class, Timer.class, TimerTask.class, TimeZone.class, TooManyListenersException.class, TreeMap.class, TreeSet.class,
            UnknownFormatConversionException.class, UnknownFormatFlagsException.class, UUID.class, Vector.class, WeakHashMap.class };

    private static final Class<?>[] CLASSES_JAVA_UTIL_STREAM = { BiConsumer.class, BiFunction.class, BinaryOperator.class, BiPredicate.class, BooleanSupplier.class, Consumer.class,
            DoubleBinaryOperator.class, DoubleConsumer.class, DoubleFunction.class, DoublePredicate.class, DoubleSupplier.class, DoubleToIntFunction.class,
            DoubleToLongFunction.class, DoubleUnaryOperator.class, Function.class, IntBinaryOperator.class, IntConsumer.class, IntFunction.class,
            IntPredicate.class, IntSupplier.class, IntToDoubleFunction.class, IntToLongFunction.class, IntUnaryOperator.class, LongBinaryOperator.class,
            LongConsumer.class, LongFunction.class, LongPredicate.class, LongSupplier.class, LongToDoubleFunction.class, LongToIntFunction.class,
            LongUnaryOperator.class, ObjDoubleConsumer.class, ObjIntConsumer.class, ObjLongConsumer.class, Predicate.class, Supplier.class,
            ToDoubleBiFunction.class, ToDoubleFunction.class, ToIntBiFunction.class, ToIntFunction.class, ToLongBiFunction.class, ToLongFunction.class,
            UnaryOperator.class };

    @TempDir
    static Path tempDir;

    @BeforeAll
    static void beforeAll() throws Exception {
        // Create InterfaceA that extends InterfaceB (will create cycle)
        createInterfaceA();
        // Create InterfaceB that extends InterfaceA (completes the cycle)
        createInterfaceB();
        // Create a class that implements InterfaceA
        createTargetClass();
        // Cycle: InterfaceA -> InterfaceB -> InterfaceA -> ...
    }

    private static byte[] createClass(final String name, final String extendsClass) throws IOException {
        final ClassGen cg = new ClassGen(name, extendsClass, name + ".java", Const.ACC_PUBLIC, new String[] {});
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cg.getJavaClass().dump(baos);
        return baos.toByteArray();
    }

    private static byte[] createClass(final String name, final String extendsClass, final String implementsInterface) throws IOException {
        final ClassGen cg = new ClassGen(name, extendsClass, name + ".java", Const.ACC_PUBLIC, new String[] { implementsInterface });
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cg.getJavaClass().dump(baos);
        return baos.toByteArray();
    }

    private static byte[] createInterface(final String name, final String extendsInterface) throws IOException {
        final ClassGen cg = new ClassGen(name, "java.lang.Object", name + ".java", Const.ACC_PUBLIC | Const.ACC_INTERFACE | Const.ACC_ABSTRACT,
                new String[] { extendsInterface });
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cg.getJavaClass().dump(baos);
        return baos.toByteArray();
    }

    private static void createInterfaceA() throws Exception {
        // Create InterfaceA that extends InterfaceB (creating part of the cycle)
        final ClassGen cg = new ClassGen("InterfaceA", // class name
                "java.lang.Object", // super class
                "InterfaceA.java", // source file
                Const.ACC_PUBLIC | Const.ACC_INTERFACE | Const.ACC_ABSTRACT, // access flags
                new String[] { "InterfaceB" } // interfaces (extends InterfaceB)
        );
        // Create the class file
        cg.getJavaClass().dump(tempDir.resolve("InterfaceA.class").toString());
    }

    private static void createInterfaceB() throws Exception {
        // Create InterfaceB that extends InterfaceA (completing the cycle)
        final ClassGen cg = new ClassGen("InterfaceB", // class name
                "java.lang.Object", // super class
                "InterfaceB.java", // source file
                Const.ACC_PUBLIC | Const.ACC_INTERFACE | Const.ACC_ABSTRACT, // access flags
                new String[] { "InterfaceA" } // interfaces (extends InterfaceA)
        );
        // Create the class file
        cg.getJavaClass().dump(tempDir.resolve("InterfaceB.class").toString());
    }

    private static void createTargetClass() throws Exception {
        // Create a class that implements InterfaceA
        final ClassGen cg = new ClassGen(CLASS_NAME, // class name
                "java.lang.Object", // super class
                "VulnerableClass.java", // source file
                Const.ACC_PUBLIC, // access flags
                new String[] { "InterfaceA" } // interfaces
        );
        // Add default constructor
        final InstructionList il = new InstructionList();
        final MethodGen constructor = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {}, "<init>", CLASS_NAME, il, cg.getConstantPool());
        final InstructionFactory factory = new InstructionFactory(cg);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Const.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.VOID));
        constructor.setMaxStack();
        constructor.setMaxLocals();
        cg.addMethod(constructor.getMethod());
        il.dispose();
        // Create the class file
        cg.getJavaClass().dump(tempDir.resolve(CLASS_NAME + ".class").toString());
    }

    static Class<?>[] getClassesJavaLang() {
        return CLASSES_JAVA_LANG;
    }

    static Class<?>[] getClassesJavaTime() {
        return CLASSES_JAVA_TIME;
    }

    static Class<?>[] getClassesJavaUtil() {
        return CLASSES_JAVA_UTIL;
    }

    static Class<?>[] getClassesJavaUtilStream() {
        return CLASSES_JAVA_UTIL_STREAM;
    }

    private Field findFieldDoesNotExist(final Class<?> clazz) throws ClassNotFoundException {
        return Repository.lookupClass(clazz.getName()).findField("nonExistentField", Type.INT);
    }

    @Test
    void testFindFieldCustomClass() throws Exception {
        final byte[] classABytes = createClass("CyclicClassA", "CyclicClassB");
        final byte[] classBBytes = createInterface("CyclicClassB", "CyclicClassA");
        final byte[] testClassBytes = createClass("CyclicTestClass", "CyclicClassA");
        final JavaClass interfaceA = new ClassParser(new ByteArrayInputStream(classABytes), "CyclicClassA.class").parse();
        final JavaClass interfaceB = new ClassParser(new ByteArrayInputStream(classBBytes), "CyclicClassB.class").parse();
        final JavaClass testClass = new ClassParser(new ByteArrayInputStream(testClassBytes), "CyclicTestClass.class").parse();
        final SyntheticRepository repo = SyntheticRepository.getInstance();
        try {
            repo.storeClass(interfaceA);
            repo.storeClass(interfaceB);
            repo.storeClass(testClass);
            Repository.setRepository(repo);
            assertThrows(ClassCircularityError.class, () -> testClass.findField("nonExistentField", Type.INT));
        } finally {
            repo.removeClass(interfaceA);
            repo.removeClass(interfaceB);
            repo.removeClass(testClass);
        }
    }

    @Test
    void testFindFieldCustomInterface1() throws ClassNotFoundException {
        // Set up repository to load classes from the malicious_classes directory
        final String classPath = tempDir.toString() + System.getProperty("path.separator") + System.getProperty("java.class.path");
        Repository.setRepository(SyntheticRepository.getInstance(new ClassPath(classPath)));
        assertThrows(ClassCircularityError.class, () -> Repository.lookupClass(CLASS_NAME).findField("nonExistentField", Type.INT));
    }

    @Test
    void testFindFieldCustomInterface2() throws Exception {
        final byte[] interfaceABytes = createInterface("CyclicInterfaceA", "CyclicInterfaceB");
        final byte[] interfaceBBytes = createInterface("CyclicInterfaceB", "CyclicInterfaceA");
        final byte[] testClassBytes = createClass("CyclicTestClass", "java.lang.Object", "CyclicInterfaceA");
        final JavaClass interfaceA = new ClassParser(new ByteArrayInputStream(interfaceABytes), "CyclicInterfaceA.class").parse();
        final JavaClass interfaceB = new ClassParser(new ByteArrayInputStream(interfaceBBytes), "CyclicInterfaceB.class").parse();
        final JavaClass testClass = new ClassParser(new ByteArrayInputStream(testClassBytes), "CyclicTestClass.class").parse();
        final SyntheticRepository repo = SyntheticRepository.getInstance();
        try {
            repo.storeClass(interfaceA);
            repo.storeClass(interfaceB);
            repo.storeClass(testClass);
            Repository.setRepository(repo);
            assertThrows(ClassCircularityError.class, () -> testClass.findField("nonExistentField", Type.INT));
        } finally {
            repo.removeClass(interfaceA);
            repo.removeClass(interfaceB);
            repo.removeClass(testClass);
        }
    }

    @ParameterizedTest
    @MethodSource("getClassesJavaLang")
    @MethodSource("getClassesJavaTime")
    @MethodSource("getClassesJavaUtil")
    @MethodSource("getClassesJavaUtilStream")
    void testFindFieldJavaLang(final Class<?> clazz) throws ClassNotFoundException {
        assertNull(findFieldDoesNotExist(clazz));
    }

    @ParameterizedTest
    @MethodSource("getClassesJavaLang")
    @MethodSource("getClassesJavaTime")
    @MethodSource("getClassesJavaUtil")
    @MethodSource("getClassesJavaUtilStream")
    void testGetAllInterfaces(final Class<?> clazz) throws ClassNotFoundException {
        assertNotNull(Repository.lookupClass(clazz.getName()).getAllInterfaces());
    }

    @ParameterizedTest
    @MethodSource("getClassesJavaLang")
    @MethodSource("getClassesJavaTime")
    @MethodSource("getClassesJavaUtil")
    @MethodSource("getClassesJavaUtilStream")
    void testGetSuperClasses(final Class<?> clazz) throws ClassNotFoundException {
        assertNotNull(Repository.lookupClass(clazz.getName()).getSuperClasses());
    }

}
