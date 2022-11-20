import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;

public class Java4Example {

    /*
     * Example for RET instruction
     */
    public static void serialize(Serializable obj, OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out = null;
        try {
            // stream closed in the finally
            out = new ObjectOutputStream(outputStream);
            out.writeObject(obj);
            
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    void tableSwitch(int inputValue) {
        switch (inputValue) {
            case 1:
                System.out.println("One");
                break;
            case 2:
                System.out.println("Two");
                break;
            case 3:
                System.out.println("Three");
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    void lookupSwitch(int inputValue) {
        switch (inputValue) {
            case 1:
                System.out.println("One");
                break;
            case 1000:
                System.out.println("One thousand");
                break;
            case 1000000:
                System.out.println("One million");
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }
}
