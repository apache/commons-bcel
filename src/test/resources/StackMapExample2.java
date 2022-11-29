/*
 * Another version of StackMapExample using 2 types of locals String and int
 * instead of just String. 
 */
public class StackMapExample2 {

    public static void main(String[] args) {
        if (args.length == 1 && "Hello".equals(args[0])) {
            System.out.println("Hello World");
        }
    }
}
