/*
 * Copyright (c) 2003, 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * The following code is taken from the JDK 8 sources for sun.reflect.generics.
 * It has been modified to compile stand alone within the BCEL test environment.
 * The code is not executed but the classfile is used as input to a BCEL test case.
 */

package org.apache.bcel.data;

import java.lang.reflect.GenericSignatureFormatError;
import java.util.*;

interface TypeTreeVisitor<T> {

    /**
     * Returns the result of the visit.
     * @return the result of the visit
     */
    T getResult();

    // Visitor methods, per node type

    void visitFormalTypeParameter(FormalTypeParameter ftp);

    void visitClassTypeSignature(ClassTypeSignature ct);
    void visitArrayTypeSignature(ArrayTypeSignature a);
    void visitTypeVariableSignature(TypeVariableSignature tv);
    void visitWildcard(Wildcard w);

    void visitSimpleClassTypeSignature(SimpleClassTypeSignature sct);
    void visitBottomSignature(BottomSignature b);

    //  Primitives and Void
    void visitByteSignature(ByteSignature b);
    void visitBooleanSignature(BooleanSignature b);
    void visitShortSignature(ShortSignature s);
    void visitCharSignature(CharSignature c);
    void visitIntSignature(IntSignature i);
    void visitLongSignature(LongSignature l);
    void visitFloatSignature(FloatSignature f);
    void visitDoubleSignature(DoubleSignature d);

    void visitVoidDescriptor(VoidDescriptor v);
}

interface Visitor<T> extends TypeTreeVisitor<T> {

    void visitClassSignature(ClassSignature cs);
    void visitMethodTypeSignature(MethodTypeSignature ms);
}

class ArrayTypeSignature implements FieldTypeSignature {
    private final TypeSignature componentType;

    private ArrayTypeSignature(TypeSignature ct) {componentType = ct;}

    static ArrayTypeSignature make(TypeSignature ct) {
        return new ArrayTypeSignature(ct);
    }

    TypeSignature getComponentType(){return componentType;}

    public void accept(TypeTreeVisitor<?> v){
        v.visitArrayTypeSignature(this);
    }
}

interface BaseType
    extends TypeSignature{}

class BooleanSignature implements BaseType {
    private static final BooleanSignature singleton = new BooleanSignature();

    private BooleanSignature(){}

    static BooleanSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){
        v.visitBooleanSignature(this);
    }
}

class BottomSignature implements FieldTypeSignature {
    private static final BottomSignature singleton = new BottomSignature();

    private BottomSignature(){}

    static BottomSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){v.visitBottomSignature(this);}
}

class ByteSignature implements BaseType {
    private static final ByteSignature singleton = new ByteSignature();

    private ByteSignature(){}

    static ByteSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){
        v.visitByteSignature(this);
    }
}

class CharSignature implements BaseType {
    private static final CharSignature singleton = new CharSignature();

    private CharSignature(){}

    static CharSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){
        v.visitCharSignature(this);
    }
}

class ClassSignature implements Signature {
    private final FormalTypeParameter[] formalTypeParams;
    private final ClassTypeSignature superclass;
    private final ClassTypeSignature[] superInterfaces;

    private ClassSignature(FormalTypeParameter[] ftps,
                                      ClassTypeSignature sc,
                                      ClassTypeSignature[] sis) {
        formalTypeParams = ftps;
        superclass = sc;
        superInterfaces = sis;
    }

    static ClassSignature make(FormalTypeParameter[] ftps,
                                      ClassTypeSignature sc,
                                      ClassTypeSignature[] sis) {
        return new ClassSignature(ftps, sc, sis);
    }

    public FormalTypeParameter[] getFormalTypeParameters(){
        return formalTypeParams;
    }
    ClassTypeSignature getSuperclass(){return superclass;}
    ClassTypeSignature[] getSuperInterfaces(){return superInterfaces;}

    public void accept(Visitor<?> v){v.visitClassSignature(this);}
}

class ClassTypeSignature implements FieldTypeSignature {
    private final List<SimpleClassTypeSignature> path;


    private ClassTypeSignature(List<SimpleClassTypeSignature> p) {
        path = p;
    }

    static ClassTypeSignature make(List<SimpleClassTypeSignature> p) {
        return new ClassTypeSignature(p);
    }

    List<SimpleClassTypeSignature> getPath(){return path;}

    public void accept(TypeTreeVisitor<?> v){v.visitClassTypeSignature(this);}
}

class DoubleSignature implements BaseType {
    private static final DoubleSignature singleton = new DoubleSignature();

    private DoubleSignature(){}

    static DoubleSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){v.visitDoubleSignature(this);}
}

interface FieldTypeSignature
    extends BaseType, TypeSignature, TypeArgument {}

class FloatSignature implements BaseType {
    private static final FloatSignature singleton = new FloatSignature();

    private FloatSignature(){}

    static FloatSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){v.visitFloatSignature(this);}
}

class FormalTypeParameter implements TypeTree {
    private final String name;
    private final FieldTypeSignature[] bounds;

    private FormalTypeParameter(String n, FieldTypeSignature[] bs) {
        name = n;
        bounds = bs;
    }

    /**
     * Factory method.
     * Returns a formal type parameter with the requested name and bounds.
     * @param n  the name of the type variable to be created by this method.
     * @param bs - the bounds of the type variable to be created by this method.
     * @return a formal type parameter with the requested name and bounds
     */
    static FormalTypeParameter make(String n, FieldTypeSignature[] bs){
        return new FormalTypeParameter(n,bs);
    }

    FieldTypeSignature[] getBounds(){return bounds;}
    String getName(){return name;}

    public void accept(TypeTreeVisitor<?> v){v.visitFormalTypeParameter(this);}
}

class IntSignature implements BaseType {
    private static final IntSignature singleton = new IntSignature();

    private IntSignature(){}

    static IntSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){v.visitIntSignature(this);}
}

class LongSignature implements BaseType {
    private static final LongSignature singleton = new LongSignature();

    private LongSignature(){}

    static LongSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){v.visitLongSignature(this);}
}

class MethodTypeSignature implements Signature {
    private final FormalTypeParameter[] formalTypeParams;
    private final TypeSignature[] parameterTypes;
    private final ReturnType returnType;
    private final FieldTypeSignature[] exceptionTypes;

    private MethodTypeSignature(FormalTypeParameter[] ftps,
                                TypeSignature[] pts,
                                ReturnType rt,
                                FieldTypeSignature[] ets) {
        formalTypeParams = ftps;
        parameterTypes = pts;
        returnType = rt;
        exceptionTypes = ets;
    }

    static MethodTypeSignature make(FormalTypeParameter[] ftps,
                                           TypeSignature[] pts,
                                           ReturnType rt,
                                           FieldTypeSignature[] ets) {
        return new MethodTypeSignature(ftps, pts, rt, ets);
    }

    public FormalTypeParameter[] getFormalTypeParameters(){
        return formalTypeParams;
    }
    TypeSignature[] getParameterTypes(){return parameterTypes;}
    ReturnType getReturnType(){return returnType;}
    FieldTypeSignature[] getExceptionTypes(){return exceptionTypes;}

    public void accept(Visitor<?> v){v.visitMethodTypeSignature(this);}
}

interface ReturnType extends TypeTree{}

class ShortSignature implements BaseType {
    private static final ShortSignature singleton = new ShortSignature();

    private ShortSignature(){}

    static ShortSignature make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){
        v.visitShortSignature(this);
    }
}

class SimpleClassTypeSignature implements FieldTypeSignature {
    private final boolean dollar;
    private final String name;
    private final TypeArgument[] typeArgs;

    private SimpleClassTypeSignature(String n, boolean dollar, TypeArgument[] tas) {
        name = n;
        this.dollar = dollar;
        typeArgs = tas;
    }

    static SimpleClassTypeSignature make(String n,
                                                boolean dollar,
                                                TypeArgument[] tas){
        return new SimpleClassTypeSignature(n, dollar, tas);
    }

    /*
     * Should a '$' be used instead of '.' to separate this component
     * of the name from the previous one when composing a string to
     * pass to Class.forName; in other words, is this a transition to
     * a nested class.
     */
    boolean getDollar(){return dollar;}
    String getName(){return name;}
    TypeArgument[] getTypeArguments(){return typeArgs;}

    public void accept(TypeTreeVisitor<?> v){
        v.visitSimpleClassTypeSignature(this);
    }
}

interface Tree{}

interface TypeArgument extends TypeTree {}

interface TypeSignature extends ReturnType {}

interface Signature extends Tree{
    FormalTypeParameter[] getFormalTypeParameters();
}

interface TypeTree extends Tree {
    /**
     * Accept method for the visitor pattern.
     * @param v - a <tt>TypeTreeVisitor</tt> that will process this
     * tree
     */
    public void accept(TypeTreeVisitor<?> v);
}

class TypeVariableSignature implements FieldTypeSignature {
    private final String identifier;

    private TypeVariableSignature(String id) {identifier = id;}


    static TypeVariableSignature make(String id) {
        return new TypeVariableSignature(id);
    }

    String getIdentifier(){return identifier;}

    public void accept(TypeTreeVisitor<?> v){
        v.visitTypeVariableSignature(this);
    }
}


class VoidDescriptor implements ReturnType {
    private static final VoidDescriptor singleton = new VoidDescriptor();

    private VoidDescriptor(){}

    static VoidDescriptor make() {return singleton;}

    public void accept(TypeTreeVisitor<?> v){v.visitVoidDescriptor(this);}
}

class Wildcard implements TypeArgument {
    private FieldTypeSignature[] upperBounds;
    private FieldTypeSignature[] lowerBounds;

    private Wildcard(FieldTypeSignature[] ubs, FieldTypeSignature[] lbs) {
        upperBounds = ubs;
        lowerBounds = lbs;
    }

    private static final FieldTypeSignature[] emptyBounds = new FieldTypeSignature[0];

    static Wildcard make(FieldTypeSignature[] ubs,
                                FieldTypeSignature[] lbs) {
        return new Wildcard(ubs, lbs);
    }

    FieldTypeSignature[] getUpperBounds(){
        return upperBounds;
    }

    FieldTypeSignature[] getLowerBounds(){
        if (lowerBounds.length == 1 &&
            lowerBounds[0] == BottomSignature.make())
            return emptyBounds;
        else
            return lowerBounds;
    }

    public void accept(TypeTreeVisitor<?> v){v.visitWildcard(this);}
}


/**
 * Parser for type signatures, as defined in the Java Virtual
 * Machine Specification (JVMS) chapter 4.
 * Converts the signatures into an abstract syntax tree (AST) representation.
 * See the package sun.reflect.generics.tree for details of the AST.
 */
public class DummySignatureParser {
    // The input is conceptually a character stream (though currently it's
    // a string). This is slightly different than traditional parsers,
    // because there is no lexical scanner performing tokenization.
    // Having a separate tokenizer does not fit with the nature of the
    // input format.
    // Other than the absence of a tokenizer, this parser is a classic
    // recursive descent parser. Its structure corresponds as closely
    // as possible to the grammar in the JVMS.
    //
    // A note on asserts vs. errors: The code contains assertions
    // in situations that should never occur. An assertion failure
    // indicates a failure of the parser logic. A common pattern
    // is an assertion that the current input is a particular
    // character. This is often paired with a separate check
    // that this is the case, which seems redundant. For example:
    //
    // assert(current() != x);
    // if (current != x {error("expected an x");
    //
    // where x is some character constant.
    // The assertion indicates, that, as currently written,
    // the code should never reach this point unless the input is an
    // x. On the other hand, the test is there to check the legality
    // of the input wrt to a given production. It may be that at a later
    // time the code might be called directly, and if the input is
    // invalid, the parser should flag an error in accordance
    // with its logic.

    private char[] input; // the input signature
    private int index = 0; // index into the input
    // used to mark end of input
    private static final char EOI = ':';
    private static final boolean DEBUG = false;

    // private constructor - enforces use of static factory
    private DummySignatureParser(){}

    // Utility methods.

    // Most parsing routines use the following routines to access the
    // input stream, and advance it as necessary.
    // This makes it easy to adapt the parser to operate on streams
    // of various kinds as well as strings.

    // returns current element of the input and advances the input
    private char getNext(){
        assert(index <= input.length);
        try {
            return input[index++];
        } catch (ArrayIndexOutOfBoundsException e) { return EOI;}
    }

    // returns current element of the input
    private char current(){
        assert(index <= input.length);
        try {
            return input[index];
        } catch (ArrayIndexOutOfBoundsException e) { return EOI;}
    }

    // advance the input
    private void advance(){
        assert(index <= input.length);
        index++;
    }

    // For debugging, prints current character to the end of the input.
    private String remainder() {
        return new String(input, index, input.length-index);
    }

    // Match c against a "set" of characters
    private boolean matches(char c, char... set) {
        for (char e : set) {
            if (c == e) return true;
        }
        return false;
    }

    // Error handling routine. Encapsulates error handling.
    // Takes a string error message as argument.
    // Currently throws a GenericSignatureFormatError.

    private Error error(String errorMsg) {
        return new GenericSignatureFormatError("Signature Parse error: " + errorMsg +
                                               "\n\tRemaining input: " + remainder());
    }

    /**
     * Verify the parse has made forward progress; throw an exception
     * if no progress.
     */
    private void progress(int startingPosition) {
        if (index <= startingPosition)
            throw error("Failure to make progress!");
    }

    /**
     * Static factory method. Produces a parser instance.
     * @return an instance of <tt>SignatureParser</tt>
     */
    public static DummySignatureParser make() {
        return new DummySignatureParser();
    }

    /**
     * Parses a class signature (as defined in the JVMS, chapter 4)
     * and produces an abstract syntax tree representing it.
     * @param s a string representing the input class signature
     * @return An abstract syntax tree for a class signature
     * corresponding to the input string
     * @throws GenericSignatureFormatError if the input is not a valid
     * class signature
     */
    public ClassSignature parseClassSig(String s) {
        if (DEBUG) System.out.println("Parsing class sig:" + s);
        input = s.toCharArray();
        return parseClassSignature();
    }

    /**
     * Parses a method signature (as defined in the JVMS, chapter 4)
     * and produces an abstract syntax tree representing it.
     * @param s a string representing the input method signature
     * @return An abstract syntax tree for a method signature
     * corresponding to the input string
     * @throws GenericSignatureFormatError if the input is not a valid
     * method signature
     */
    public MethodTypeSignature parseMethodSig(String s) {
        if (DEBUG) System.out.println("Parsing method sig:" + s);
        input = s.toCharArray();
        return parseMethodTypeSignature();
    }


    /**
     * Parses a type signature
     * and produces an abstract syntax tree representing it.
     *
     * @param s a string representing the input type signature
     * @return An abstract syntax tree for a type signature
     * corresponding to the input string
     * @throws GenericSignatureFormatError if the input is not a valid
     * type signature
     */
    public TypeSignature parseTypeSig(String s) {
        if (DEBUG) System.out.println("Parsing type sig:" + s);
        input = s.toCharArray();
        return parseTypeSignature();
    }

    // Parsing routines.
    // As a rule, the parsing routines access the input using the
    // utilities current(), getNext() and/or advance().
    // The convention is that when a parsing routine is invoked
    // it expects the current input to be the first character it should parse
    // and when it completes parsing, it leaves the input at the first
    // character after the input parses.

    /*
     * Note on grammar conventions: a trailing "*" matches zero or
     * more occurrences, a trailing "+" matches one or more occurrences,
     * "_opt" indicates an optional component.
     */

    /**
     * ClassSignature:
     *     FormalTypeParameters_opt SuperclassSignature SuperinterfaceSignature*
     */
    private ClassSignature parseClassSignature() {
        // parse a class signature based on the implicit input.
        assert(index == 0);
        return ClassSignature.make(parseZeroOrMoreFormalTypeParameters(),
                                   parseClassTypeSignature(), // Only rule for SuperclassSignature
                                   parseSuperInterfaces());
    }

    private FormalTypeParameter[] parseZeroOrMoreFormalTypeParameters(){
        if (current() == '<') {
            return parseFormalTypeParameters();
        } else {
            return new FormalTypeParameter[0];
        }
    }

    /**
     * FormalTypeParameters:
     *     "<" FormalTypeParameter+ ">"
     */
    private FormalTypeParameter[] parseFormalTypeParameters(){
        List<FormalTypeParameter> ftps =  new ArrayList<>(3);
        assert(current() == '<'); // should not have been called at all
        if (current() != '<') { throw error("expected '<'");}
        advance();
        ftps.add(parseFormalTypeParameter());
        while (current() != '>') {
            int startingPosition = index;
            ftps.add(parseFormalTypeParameter());
            progress(startingPosition);
        }
        advance();
        return ftps.toArray(new FormalTypeParameter[ftps.size()]);
    }

    /**
     * FormalTypeParameter:
     *     Identifier ClassBound InterfaceBound*
     */
    private FormalTypeParameter parseFormalTypeParameter(){
        String id = parseIdentifier();
        FieldTypeSignature[] bs = parseBounds();
        return FormalTypeParameter.make(id, bs);
    }

    private String parseIdentifier(){
        StringBuilder result = new StringBuilder();
        while (!Character.isWhitespace(current())) {
            char c = current();
            switch(c) {
            case ';':
            case '.':
            case '/':
            case '[':
            case ':':
            case '>':
            case '<':
                return result.toString();
            default:{
                result.append(c);
                advance();
            }

            }
        }
        return result.toString();
    }
    /**
     * FieldTypeSignature:
     *     ClassTypeSignature
     *     ArrayTypeSignature
     *     TypeVariableSignature
     */
    private FieldTypeSignature parseFieldTypeSignature() {
        return parseFieldTypeSignature(true);
    }

    private FieldTypeSignature parseFieldTypeSignature(boolean allowArrays) {
        switch(current()) {
        case 'L':
           return parseClassTypeSignature();
        case 'T':
            return parseTypeVariableSignature();
        case '[':
            if (allowArrays)
                return parseArrayTypeSignature();
            else
                throw error("Array signature not allowed here.");
        default: throw error("Expected Field Type Signature");
        }
    }

    /**
     * ClassTypeSignature:
     *     "L" PackageSpecifier_opt SimpleClassTypeSignature ClassTypeSignatureSuffix* ";"
     */
    private ClassTypeSignature parseClassTypeSignature(){
        assert(current() == 'L');
        if (current() != 'L') { throw error("expected a class type");}
        advance();
        List<SimpleClassTypeSignature> scts = new ArrayList<>(5);
        scts.add(parsePackageNameAndSimpleClassTypeSignature());

        parseClassTypeSignatureSuffix(scts);
        if (current() != ';')
            throw error("expected ';' got '" + current() + "'");

        advance();
        return ClassTypeSignature.make(scts);
    }

    /**
     * PackageSpecifier:
     *     Identifier "/" PackageSpecifier*
     */
    private SimpleClassTypeSignature parsePackageNameAndSimpleClassTypeSignature() {
        // Parse both any optional leading PackageSpecifier as well as
        // the following SimpleClassTypeSignature.

        String id = parseIdentifier();

        if (current() == '/') { // package name
            StringBuilder idBuild = new StringBuilder(id);

            while(current() == '/') {
                advance();
                idBuild.append(".");
                idBuild.append(parseIdentifier());
            }
            id = idBuild.toString();
        }

        switch (current()) {
        case ';':
            return SimpleClassTypeSignature.make(id, false, new TypeArgument[0]); // all done!
        case '<':
            if (DEBUG) System.out.println("\t remainder: " + remainder());
            return SimpleClassTypeSignature.make(id, false, parseTypeArguments());
        default:
            throw error("expected '<' or ';' but got " + current());
        }
    }

    /**
     * SimpleClassTypeSignature:
     *     Identifier TypeArguments_opt
     */
    private SimpleClassTypeSignature parseSimpleClassTypeSignature(boolean dollar){
        String id = parseIdentifier();
        char c = current();

        switch (c) {
        case ';':
        case '.':
            return SimpleClassTypeSignature.make(id, dollar, new TypeArgument[0]) ;
        case '<':
            return SimpleClassTypeSignature.make(id, dollar, parseTypeArguments());
        default:
            throw error("expected '<' or ';' or '.', got '" + c + "'.");
        }
    }

    /**
     * ClassTypeSignatureSuffix:
     *     "." SimpleClassTypeSignature
     */
    private void parseClassTypeSignatureSuffix(List<SimpleClassTypeSignature> scts) {
        while (current() == '.') {
            advance();
            scts.add(parseSimpleClassTypeSignature(true));
        }
    }

    private TypeArgument[] parseTypeArgumentsOpt() {
        if (current() == '<') {return parseTypeArguments();}
        else {return new TypeArgument[0];}
    }

    /**
     * TypeArguments:
     *     "<" TypeArgument+ ">"
     */
    private TypeArgument[] parseTypeArguments() {
        List<TypeArgument> tas = new ArrayList<>(3);
        assert(current() == '<');
        if (current() != '<') { throw error("expected '<'");}
        advance();
        tas.add(parseTypeArgument());
        while (current() != '>') {
                //(matches(current(),  '+', '-', 'L', '[', 'T', '*')) {
            tas.add(parseTypeArgument());
        }
        advance();
        return tas.toArray(new TypeArgument[tas.size()]);
    }

    /**
     * TypeArgument:
     *     WildcardIndicator_opt FieldTypeSignature
     *     "*"
     */
    private TypeArgument parseTypeArgument() {
        FieldTypeSignature[] ub, lb;
        ub = new FieldTypeSignature[1];
        lb = new FieldTypeSignature[1];
        TypeArgument[] ta = new TypeArgument[0];
        char c = current();
        switch (c) {
        case '+': {
            advance();
            ub[0] = parseFieldTypeSignature();
            lb[0] = BottomSignature.make(); // bottom
            return Wildcard.make(ub, lb);
        }
        case '*':{
            advance();
            ub[0] = SimpleClassTypeSignature.make("java.lang.Object", false, ta);
            lb[0] = BottomSignature.make(); // bottom
            return Wildcard.make(ub, lb);
        }
        case '-': {
            advance();
            lb[0] = parseFieldTypeSignature();
            ub[0] = SimpleClassTypeSignature.make("java.lang.Object", false, ta);
            return Wildcard.make(ub, lb);
        }
        default:
            return parseFieldTypeSignature();
        }
    }

    /**
     * TypeVariableSignature:
     *     "T" Identifier ";"
     */
    private TypeVariableSignature parseTypeVariableSignature() {
        assert(current() == 'T');
        if (current() != 'T') { throw error("expected a type variable usage");}
        advance();
        TypeVariableSignature ts = TypeVariableSignature.make(parseIdentifier());
        if (current() != ';') {
            throw error("; expected in signature of type variable named" +
                  ts.getIdentifier());
        }
        advance();
        return ts;
    }

    /**
     * ArrayTypeSignature:
     *     "[" TypeSignature
     */
    private ArrayTypeSignature parseArrayTypeSignature() {
        if (current() != '[') {throw error("expected array type signature");}
        advance();
        return ArrayTypeSignature.make(parseTypeSignature());
    }

    /**
     * TypeSignature:
     *     FieldTypeSignature
     *     BaseType
     */
    private TypeSignature parseTypeSignature() {
        switch (current()) {
        case 'B':
        case 'C':
        case 'D':
        case 'F':
        case 'I':
        case 'J':
        case 'S':
        case 'Z':
            return parseBaseType();

        default:
            return parseFieldTypeSignature();
        }
    }

    private BaseType parseBaseType() {
        switch(current()) {
        case 'B':
            advance();
            return ByteSignature.make();
        case 'C':
            advance();
            return CharSignature.make();
        case 'D':
            advance();
            return DoubleSignature.make();
        case 'F':
            advance();
            return FloatSignature.make();
        case 'I':
            advance();
            return IntSignature.make();
        case 'J':
            advance();
            return LongSignature.make();
        case 'S':
            advance();
            return ShortSignature.make();
        case 'Z':
            advance();
            return BooleanSignature.make();
        default: {
            assert(false);
            throw error("expected primitive type");
        }
        }
    }

    /**
     * ClassBound:
     *     ":" FieldTypeSignature_opt
     *
     * InterfaceBound:
     *     ":" FieldTypeSignature
     */
    private FieldTypeSignature[] parseBounds() {
        List<FieldTypeSignature> fts = new ArrayList<>(3);

        if (current() == ':') {
            advance();
            switch(current()) {
            case ':': // empty class bound
                break;

            default: // parse class bound
                fts.add(parseFieldTypeSignature());
            }

            // zero or more interface bounds
            while (current() == ':') {
                advance();
                fts.add(parseFieldTypeSignature());
            }
        } else
            error("Bound expected");

        return fts.toArray(new FieldTypeSignature[fts.size()]);
    }

    /**
     * SuperclassSignature:
     *     ClassTypeSignature
     */
    private ClassTypeSignature[] parseSuperInterfaces() {
        List<ClassTypeSignature> cts = new ArrayList<>(5);
        while(current() == 'L') {
            cts.add(parseClassTypeSignature());
        }
        return cts.toArray(new ClassTypeSignature[cts.size()]);
    }


    /**
     * MethodTypeSignature:
     *     FormalTypeParameters_opt "(" TypeSignature* ")" ReturnType ThrowsSignature*
     */
    private MethodTypeSignature parseMethodTypeSignature() {
        // Parse a method signature based on the implicit input.
        FieldTypeSignature[] ets;

        assert(index == 0);
        return MethodTypeSignature.make(parseZeroOrMoreFormalTypeParameters(),
                                        parseFormalParameters(),
                                        parseReturnType(),
                                        parseZeroOrMoreThrowsSignatures());
    }

    // "(" TypeSignature* ")"
    private TypeSignature[] parseFormalParameters() {
        if (current() != '(') {throw error("expected '('");}
        advance();
        TypeSignature[] pts = parseZeroOrMoreTypeSignatures();
        if (current() != ')') {throw error("expected ')'");}
        advance();
        return pts;
    }

    // TypeSignature*
    private TypeSignature[] parseZeroOrMoreTypeSignatures() {
        List<TypeSignature> ts = new ArrayList<>();
        boolean stop = false;
        while (!stop) {
            switch(current()) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
            case 'L':
            case 'T':
            case '[': {
                ts.add(parseTypeSignature());
                break;
            }
            default: stop = true;
            }
        }
        return ts.toArray(new TypeSignature[ts.size()]);
    }

    /**
     * ReturnType:
     *     TypeSignature
     *     VoidDescriptor
     */
    private ReturnType parseReturnType(){
        if (current() == 'V') {
            advance();
            return VoidDescriptor.make();
        } else
            return parseTypeSignature();
    }

    // ThrowSignature*
    private FieldTypeSignature[] parseZeroOrMoreThrowsSignatures(){
        List<FieldTypeSignature> ets = new ArrayList<>(3);
        while( current() == '^') {
            ets.add(parseThrowsSignature());
        }
        return ets.toArray(new FieldTypeSignature[ets.size()]);
    }

    /**
     * ThrowsSignature:
     *     "^" ClassTypeSignature
     *     "^" TypeVariableSignature
     */
    private FieldTypeSignature parseThrowsSignature() {
        assert(current() == '^');
        if (current() != '^') { throw error("expected throws signature");}
        advance();
        return parseFieldTypeSignature(false);
    }
 }
