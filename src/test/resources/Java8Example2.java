import java.io.Serializable;
import java.util.*;
import java.util.stream.*;

public class Java8Example2 implements java.io.Serializable, Runnable {

    private static final long serialVersionUID = 1234567891234567891L;
    public static final float E = 2.7182818284590452354f;
    public static final double PI = 3.14159265358979323846;
    public static final char DOT = '.';
    public static final short PORT = 22;
    public static final int ICONST_M1 = -1;
    public static final int ICONST_0 = 0;
    public static final int ICONST_1 = 1;
    public static final int ICONST_2 = 2;
    public static final int ICONST_3 = 3;
    public static final int ICONST_4 = 4;
    public static final int ICONST_5 = 5;
    public static final long LCONST_0 = 0;
    public static final long LCONST_1 = 1;
    public static final float FCONST_0 = 0f;
    public static final float FCONST_1 = 1f;
    public static final float FCONST_2 = 2f;
    public static final double DCONST_0 = 0d;
    public static final double DCONST_1 = 1d;
    public static final Float FNULL = null;
    public static final Double DNULL = null;
    public static final Long LNULL = null;

    public static final byte INVOKESTATIC = (byte) 184;
    public static final int[][] MULTI_ARRAY = {{0}, {1}};
    public static final int[][] MULTI_ARRAY2 = new int[2][2];

    private static transient volatile StringBuffer STRING_BUFFER = new StringBuffer();

    public synchronized void run() {
        try {
            hello("Hello", "World", "hi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void check(String... args) throws Exception {
        if (!(args instanceof String[]) || args.getClass() != String[].class) {
            throw new Exception();
        }
    }
    
    void hello(String... args) throws Exception {
        check(args);
        Arrays.stream(args).forEach(System.out::println);
        STRING_BUFFER.append(ICONST_M1);
        STRING_BUFFER.append(ICONST_0);
        STRING_BUFFER.append(ICONST_1);
        STRING_BUFFER.append(ICONST_2);
        STRING_BUFFER.append(ICONST_3);
        STRING_BUFFER.append(ICONST_4);
        STRING_BUFFER.append(ICONST_5);
        STRING_BUFFER.append(LCONST_0);
        STRING_BUFFER.append(LCONST_1);
        STRING_BUFFER.append(FCONST_0);
        STRING_BUFFER.append(FCONST_1);
        STRING_BUFFER.append(FCONST_2);
        STRING_BUFFER.append(DCONST_0);
        STRING_BUFFER.append(DCONST_1);
        STRING_BUFFER.append(FNULL);
        STRING_BUFFER.append(DNULL);
        STRING_BUFFER.append(LNULL);
        STRING_BUFFER.append(DOT);
        STRING_BUFFER.append(INVOKESTATIC);
        STRING_BUFFER.append(frem(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY));
        STRING_BUFFER.append(frem(Float.NaN, Float.NaN));
        STRING_BUFFER.append(frem(E, E));
        STRING_BUFFER.append(drem(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
        STRING_BUFFER.append(drem(Double.NaN, Double.NaN));
        STRING_BUFFER.append(drem(PI, PI));
        STRING_BUFFER.append(serialVersionUID);
        STRING_BUFFER.append(Arrays.deepToString(MULTI_ARRAY));
    }

    double drem(double a, double b) {
        return a % b;
    }
    
    float frem(float a, float b) {
        return a % b;
    }
}
