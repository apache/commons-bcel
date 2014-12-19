import java.util.*;
import java.util.stream.*;

public interface Java8Example {

    default void hello() {
        List<String> words = Arrays.asList("Hello", "World", "hi");
        System.out.println(words);
        
        List<String> words2 = words.stream().filter((String s) -> s.length() > 2).collect(Collectors.<String> toList());
        System.out.println(words2);
    }
}
