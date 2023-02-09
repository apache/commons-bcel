package org.apache.bcel.classfile;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InvalidMethodSigantureTestCase {

    class TestVisitor extends org.apache.bcel.classfile.EmptyVisitor {
        @Override
        public void visitField(final Field field) {
            field.getType();
        }
    }

    @Test
    public void testMethodWithParens() throws Exception {
        try (final InputStream inputStream = Files.newInputStream(Paths.get("src/test/resources/invalidmethodsig/test$method name with () in it$1.class"))) {
            final ClassParser classParser = new ClassParser(inputStream, "test.class");
            final JavaClass javaClass = classParser.parse();
            final TestVisitor visitor = new TestVisitor();
            new org.apache.bcel.classfile.DescendingVisitor(javaClass, visitor).visit();
        }
    }
}
