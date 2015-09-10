package org.apache.commons.bcel6;

public abstract class ExceptionConst implements ExceptionConstants {

    /**
     * Pre-defined exception arrays according to chapters 5.1-5.4 of the Java Virtual
     * Machine Specification 
     */
    private static final Class<?>[] EXCS_CLASS_AND_INTERFACE_RESOLUTION = {
            NO_CLASS_DEF_FOUND_ERROR, CLASS_FORMAT_ERROR, VERIFY_ERROR, ABSTRACT_METHOD_ERROR,
            EXCEPTION_IN_INITIALIZER_ERROR, ILLEGAL_ACCESS_ERROR
    }; // Chapter 5.1
    private static final Class<?>[] EXCS_FIELD_AND_METHOD_RESOLUTION = {
            NO_SUCH_FIELD_ERROR, ILLEGAL_ACCESS_ERROR, NO_SUCH_METHOD_ERROR
    }; // Chapter 5.2
    private static final Class<?>[] EXCS_INTERFACE_METHOD_RESOLUTION = new Class[0]; // Chapter 5.3 (as below)
    private static final Class<?>[] EXCS_STRING_RESOLUTION = new Class[0];
    // Chapter 5.4 (no errors but the ones that _always_ could happen! How stupid.)
    private static final Class<?>[] EXCS_ARRAY_EXCEPTION = {
            NULL_POINTER_EXCEPTION, ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION
    };

    /** 
     * Enum corresponding to the various Exception Class arrays, 
     * used by {@link ExceptionConstants#createExceptions(EXCS, Class...)}
     */
    public enum EXCS {
        EXCS_CLASS_AND_INTERFACE_RESOLUTION,
        EXCS_FIELD_AND_METHOD_RESOLUTION,
        EXCS_INTERFACE_METHOD_RESOLUTION,
        EXCS_STRING_RESOLUTION,
        EXCS_ARRAY_EXCEPTION,
    };

    // helper method to merge exception class arrays
    private static Class<?>[] mergeExceptions(Class<?>[] input, Class<?> ... extraClasses) {
        int extraLen = extraClasses == null ? 0 : extraClasses.length;
        Class<?>[] excs = new Class<?>[input.length + extraLen];
        System.arraycopy(input, 0, excs, 0, input.length);
        if (extraLen > 0) {
            System.arraycopy(extraClasses, 0, excs, input.length, extraLen);            
        }
        return excs;
    }

    /**
     * Creates a copy of the specified Exception Class array combined with any additional Exception classes.
     * @param type the basic array type
     * @param extraClasses additional classes, if any
     * @return the merged array
     */
    public static Class<?>[] createExceptions(EXCS type, Class<?> ... extraClasses) {
        switch (type) {
        case EXCS_CLASS_AND_INTERFACE_RESOLUTION:
            return mergeExceptions(EXCS_CLASS_AND_INTERFACE_RESOLUTION, extraClasses);
        case EXCS_ARRAY_EXCEPTION:
            return mergeExceptions(EXCS_ARRAY_EXCEPTION, extraClasses);
        case EXCS_FIELD_AND_METHOD_RESOLUTION:
            return mergeExceptions(EXCS_FIELD_AND_METHOD_RESOLUTION, extraClasses);
        case EXCS_INTERFACE_METHOD_RESOLUTION:
            return mergeExceptions(EXCS_INTERFACE_METHOD_RESOLUTION, extraClasses);
        case EXCS_STRING_RESOLUTION:
            return mergeExceptions(EXCS_STRING_RESOLUTION, extraClasses);
        default:
            throw new AssertionError("Cannot happen; unexpected enum value: " + type);
        }
    }


}
