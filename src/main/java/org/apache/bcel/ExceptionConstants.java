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
package org.apache.bcel;

/**
 * Exception constants.
 *
 * @deprecated (since 6.0) DO NOT USE - use ExceptionConst instead
 */
@Deprecated
public interface ExceptionConstants {

    /** The mother of all exceptions
     */
    Class<Throwable> THROWABLE = Throwable.class;
    /** Super class of any run-time exception
     */
    Class<RuntimeException> RUNTIME_EXCEPTION = RuntimeException.class;
    /** Super class of any linking exception (aka Linkage Error)
     */
    Class<LinkageError> LINKING_EXCEPTION = LinkageError.class;
    /** Linking Exceptions
     */
    Class<ClassCircularityError> CLASS_CIRCULARITY_ERROR = ClassCircularityError.class;
    Class<ClassFormatError> CLASS_FORMAT_ERROR = ClassFormatError.class;
    Class<ExceptionInInitializerError> EXCEPTION_IN_INITIALIZER_ERROR = ExceptionInInitializerError.class;
    Class<IncompatibleClassChangeError> INCOMPATIBLE_CLASS_CHANGE_ERROR = IncompatibleClassChangeError.class;
    Class<AbstractMethodError> ABSTRACT_METHOD_ERROR = AbstractMethodError.class;
    Class<IllegalAccessError> ILLEGAL_ACCESS_ERROR = IllegalAccessError.class;
    Class<InstantiationError> INSTANTIATION_ERROR = InstantiationError.class;
    Class<NoSuchFieldError> NO_SUCH_FIELD_ERROR = NoSuchFieldError.class;
    Class<NoSuchMethodError> NO_SUCH_METHOD_ERROR = NoSuchMethodError.class;
    Class<NoClassDefFoundError> NO_CLASS_DEF_FOUND_ERROR = NoClassDefFoundError.class;
    Class<UnsatisfiedLinkError> UNSATISFIED_LINK_ERROR = UnsatisfiedLinkError.class;
    Class<VerifyError> VERIFY_ERROR = VerifyError.class;
    /* UnsupportedClassVersionError is new in JDK 1.2 */
//    Class UnsupportedClassVersionError = UnsupportedClassVersionError.class;
    /** Run-Time Exceptions
     */
    Class<NullPointerException> NULL_POINTER_EXCEPTION = NullPointerException.class;
    Class<ArrayIndexOutOfBoundsException> ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION
                                                            = ArrayIndexOutOfBoundsException.class;
    Class<ArithmeticException> ARITHMETIC_EXCEPTION = ArithmeticException.class;
    Class<NegativeArraySizeException> NEGATIVE_ARRAY_SIZE_EXCEPTION = NegativeArraySizeException.class;
    Class<ClassCastException> CLASS_CAST_EXCEPTION = ClassCastException.class;
    Class<IllegalMonitorStateException> ILLEGAL_MONITOR_STATE = IllegalMonitorStateException.class;

    /**
     * Pre-defined exception arrays according to chapters 5.1-5.4 of the Java Virtual
     * Machine Specification
     * @deprecated Do not use these arrays, use the static methods in the ExceptionConst implementation class instead
     */
    @Deprecated
    Class<?>[] EXCS_CLASS_AND_INTERFACE_RESOLUTION = {
            NO_CLASS_DEF_FOUND_ERROR, CLASS_FORMAT_ERROR, VERIFY_ERROR, ABSTRACT_METHOD_ERROR,
            EXCEPTION_IN_INITIALIZER_ERROR, ILLEGAL_ACCESS_ERROR
    }; // Chapter 5.1
    @Deprecated
    Class<?>[] EXCS_FIELD_AND_METHOD_RESOLUTION = {
            NO_SUCH_FIELD_ERROR, ILLEGAL_ACCESS_ERROR, NO_SUCH_METHOD_ERROR
    }; // Chapter 5.2
    @Deprecated
    Class<?>[] EXCS_INTERFACE_METHOD_RESOLUTION = new Class[0]; // Chapter 5.3 (as below)
    @Deprecated
    Class<?>[] EXCS_STRING_RESOLUTION = new Class[0];
    // Chapter 5.4 (no errors but the ones that _always_ could happen! How stupid.)
    @Deprecated
    Class<?>[] EXCS_ARRAY_EXCEPTION = {
            NULL_POINTER_EXCEPTION, ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION
    };

}
