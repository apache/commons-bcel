package typeannotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

// Compiled with: javac --release 8 -d . TypeAnnotated.java (only TypeAnnotated.class is used by the tests)
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface Visible {
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.CLASS)
@interface Invisible {
}

public class TypeAnnotated<T extends @Visible Object> {

    @Visible
    String field;

    List<@Visible String> items;

    @Invisible
    String hidden;

    @Visible
    String find(@Visible Object key, int count) {
        return null;
    }
}
