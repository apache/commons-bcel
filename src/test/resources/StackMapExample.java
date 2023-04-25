/*
 * Example to check if BCELifier generates the stack map table
 * so that the generated class passes the Java verifier checks.
 */
public abstract class StackMapExample {

    protected abstract void someAbstractMethod();

    public static void main(final String[] args) {
        switch (args[0]) {
        case "Hello":
            System.out.println("Hello World");
            break;
        default:
            break;
        }
    }
}
