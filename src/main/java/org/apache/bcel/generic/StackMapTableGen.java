/*
Copyright (c) 2000-2011 INRIA, France Telecom
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.

3. Neither the name of the copyright holders nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.apache.bcel.generic;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Code kopiert vom ASM Framework
 * http://asm.ow2.org/doc/developer-guide.html#controlflow
 */

/**
 * A Frame starts and ends at a frame endpoint
 */
class Frame{
    List<Frame> successors = new ArrayList<>();
    List<InstructionHandle> instructions;
    ConstantPoolGen cp;
    JVMState stateChange;
    JVMState startState;
    public Frame(List<InstructionHandle> instructions, ConstantPoolGen cp){
        this.cp = cp;
        stateChange = new JVMState();
        this.instructions = instructions;
    }

    /**
     * Runs the Instructions in this state with the given startState
     * @param startState
     * @return true if the frame state did change
     */
    public boolean updateState(JVMState startState){
        this.startState = startState;
        for(InstructionHandle ins : instructions){
            startState = StackMapTableGen.applyStatement(ins, startState, cp);
        }
        boolean changed = ! stateChange.equals(startState);
        this.stateChange = startState;
        return changed;
    }

    public void setSuccessors(List<Frame> successors) {
        this.successors = successors;
    }

    public List<Frame> getSuccessors() {
        return successors;
    }

    public JVMState getState() {
        return stateChange;
    }

    public JVMState getStartState() {
        return startState;
    }

    @Override
    public String toString(){
        return "Frame: "+instructions.toString() + " Successors " + successors.size();
    }

}

/**
 * Stores a current state of a Frame
 * Is used to record changes to the frame stack and local vars
 */
class JVMState{
    public InstructionHandle currentInstruction;
    Map<Integer, StackMapType> localVars = new HashMap<>();
    Stack<StackMapType> stack = new Stack();
    int numLocals = 0;
    int stackSize = 0;

    public JVMState(){
    }

    public JVMState(JVMState state){
        localVars = new HashMap<>();
        for(Integer key : state.localVars.keySet()){
            localVars.put(key, state.localVars.get(key));
        }
        stack = (Stack<StackMapType>) state.stack.clone();
    }

    public JVMState(Map<Integer, StackMapType> knownLocalVars){
        localVars = knownLocalVars;
    }

    public StackMapType getLocalType(int index) {
        if(!localVars.containsKey(index)){
            throw new NullPointerException();
        }
        return localVars.get(index);
    }

    /**
     * Sets the type of a local variable for this state
     * @param index
     * @param objectRef
     */
    public void set(int index, StackMapType objectRef) {
        if(objectRef == null)throw new NullPointerException();
        if(index>numLocals+1){
            while(numLocals<index){
                numLocals++;
                localVars.put(numLocals, new StackMapType(Const.ITEM_InitObject, 0, null));
            }
        }
        localVars.put(index, objectRef);
    }

    public void push(StackMapType si){
        stack.push(si);
    }

    public void push(List<StackMapType> items){
        for(StackMapType item : items){
            this.push(item);
        }
    }

    public void pop(int i){
            while(i>0){
                pop();
                i--;
            }
    }

    public StackMapType pop() {
        return stack.pop();
    }

    public List<StackMapType> getStackTypes(ConstantPoolGen cp){
        return stack;
    }

    /**
     * Calculates the additional localVariables as needed for the locals array in a *_frame_structure
     * @return
     */
    public List<StackMapType> getLocals(){
        List<StackMapType> ret = new ArrayList<StackMapType>();
        for(Integer index : localVars.keySet()) {
            //Es wird ignoriert an welchen Positionen die Lokalen Variablen stehen und davon ausgegangen, dass keine Lücke zwischen den Positionen besteht
            //ret.add(index, localVars.get(index));
            ret.add(localVars.get(index));
        }
        return ret;
    }

    public int getLocalCount() {
        return this.getLocals().size();
    }


    public boolean equals(Object e){
        if(! (e instanceof JVMState))return false;
        JVMState equals = (JVMState) e;
        if(! this.stack.equals(equals.stack))return false;
        if(! this.localVars.equals(equals.localVars))return false;
        return true;
    }

    public String toString(){
        return  "Stack: " + this.stack + "\n" + "Locals: " + this.localVars;
    }
}

class StackMapTypeFactory{
    public static StackMapType getNull(ConstantPoolGen cp){
        return new StackMapType(Const.ITEM_Null, 0, cp.getConstantPool());
    }
    public static StackMapType getFloat(ConstantPoolGen cp){
        return new StackMapType(Const.ITEM_Float, 0, cp.getConstantPool());
    }
    public static StackMapType getDouble(ConstantPoolGen cp){
        return new StackMapType(Const.ITEM_Float, 0, cp.getConstantPool());
    }
    public static StackMapType getLong(ConstantPoolGen cp) {
        return new StackMapType(Const.ITEM_Float, 0, cp.getConstantPool());
    }
    public static StackMapType getInt(ConstantPoolGen cp){
        return new StackMapType(Const.ITEM_Integer, 0, cp.getConstantPool());
    }
    public static StackMapType getReferenceType(ObjectType t,ConstantPoolGen cp){
        int classInfo = cp.addClass(t);
        return new StackMapType(Const.ITEM_Object,classInfo,cp.getConstantPool());
    }
    public static StackMapType getArray(ConstantPoolGen cp){
        return null; //TODO
    }
    public static StackMapType getTop(ConstantPoolGen cp){
        return new StackMapType(Const.ITEM_Bogus,0,cp.getConstantPool());
    }

    /**
     * @param handle - The handle of the new instruction
     * @param cp
     * @return
     */
    public static StackMapType getUninitialized(InstructionHandle handle, ConstantPoolGen cp){
        return new StackMapType(Const.ITEM_NewObject,handle.getPosition(),cp.getConstantPool());
    }
    public static StackMapType getUninitializedThis(ConstantPoolGen cp){
        return new StackMapType(Const.ITEM_InitObject, 0, cp.getConstantPool());
    }

}

public class StackMapTableGen {

    private final MethodGen method;
    private List<StackMapEntry> stackMapEntries;
    private final ConstantPoolGen cp;

    public StackMapTableGen(MethodGen forMethod, ConstantPoolGen cp){
        this.method = forMethod;
        this.cp = cp;
    }

    public StackMap getStackMap(){
        int length=2; //+2 wegen NumberOfEntries
        List<StackMapEntry> entries = getStackMapEntries(); //StackMapTableGen.getStackMapEntries(code, methodParams,cp);
        if(entries.size()==0)return null;
        StackMapEntry[] entryArray = new StackMapEntry[entries.size()];
        for(int i = 0; i<entries.size();i++){
            entryArray[i] = entries.get(i);
            ByteArrayOutputStream testOut = new ByteArrayOutputStream();
            try {
                entries.get(i).dump(new DataOutputStream(testOut));
            } catch (IOException e) {
                //Kann nicht auftreten. Es wird in Byte-Array geschrieben.
                e.printStackTrace();
            }
            length+=testOut.size();
        }
        return new StackMap(cp.addUtf8(Const.getAttributeName(Const.ATTR_STACK_MAP_TABLE))
                ,length,	entryArray,cp.getConstantPool());
    }

    private class FrameState{
        Frame frame;
        JVMState state;
    }

    /**
     * Generiert die StackMapEntrys zu einer gegebenen Methode
     * @param forMethod
     * @return
     */
    private void generateStackMapEntries(MethodGen forMethod, ConstantPoolGen cp){
        InstructionList forCode = forMethod.getInstructionList();
        List<Frame> frames = splitIntoBlocks(forCode, cp);
        //if(frames.size() == 0) return; //No Frames -> No stackmap
        //Generate first frame from Method Parameters:
        Map<Integer, StackMapType> firstState = new HashMap<>();
        Type[] argumentTypes = forMethod.getArgumentTypes();
        int i = -1;
        if(! forMethod.isStatic()){ //If Method is not static: local at 0 is "this" (parent class)
            i++;
            firstState.put(0, convert(new ObjectType(forMethod.getClassName()),cp).get(0));
        }
        for(; i<argumentTypes.length;i++) {
            firstState.put(i + 1, convert(argumentTypes[i],cp).get(0)); //Every ArgumsentType has to represent at least one StackType
        }
        Frame firstFrame = frames.get(0);
        JVMState inputState = new JVMState(firstState);
        recursiveMergeFrame(firstFrame, inputState); //Find Fixpoint for Frame States

        //Gather Stack Map Entrys from Frames:
        for(Frame frame : frames){
            int position = frame.instructions.get(0).getPosition();
            if(position != 0)this.addFrameState(position, frame.getStartState(), cp);
        }

    }

    public List<StackMapEntry> getStackMapEntries(){
        this.stackMapEntries = new ArrayList<>();
        generateStackMapEntries(this.method, cp); //generate the stackMapEntries
        return stackMapEntries;
    }

    private int currentOffset = 0;
    /**
     * Adds another Framestate to the StackMapTableGen
     * This method builds the stackMapEntries-List
     * The frames must be added in order in which they appear in the StackMapTable Atribute afterwards
     * @param frameOffset - Offset of the first instruction in the frame
     * @param state
     */
    private void addFrameState(int frameOffset, JVMState state, ConstantPoolGen cp){
        StackMapEntry lastEntry = null;
        if(this.stackMapEntries == null || this.stackMapEntries.size() == 0){
            //This is the first entry, he must be the difference from the first Framestate computed by the JRE
            this.stackMapEntries = new ArrayList<>();
            currentOffset = 0;
        }else{
            lastEntry = this.stackMapEntries.get(this.stackMapEntries.size()-1);
            currentOffset += lastEntry.getByteCodeOffset() + 1;
        }
        //Frame Offset Diff berechnen:
        if(lastEntry != null){
            frameOffset -= currentOffset;
        }
        //Zum Testen wird jedesmal nur ein FullStackFrame erstellt
        StackMapEntry fullFrame = generateFullFrame(frameOffset, state.getStackTypes(cp), state.getLocals(), cp);
        this.stackMapEntries.add(fullFrame);
    }

    /**
     * Calculates the difference between the two framestates and returns the corresponding StackMapEntry
     * @param currentState
     * @param nextState
     * @return
     */
    private StackMapEntry diffFrameState(JVMState currentState, JVMState nextState){
        //TODO: Implement
        return null;
    }

    private static void recursiveMergeFrame(Frame frame, JVMState inputState){
        //System.out.println("Frame State davor: "+frame.getState());
        boolean changed = frame.updateState(inputState);
        //System.out.println("Frame State danach: "+frame.getState());
        if(changed){
            for(Frame f : frame.getSuccessors()){
                recursiveMergeFrame(f, frame.getState());
            }
        }
    }

    private static StackMapEntry generateSameLocalsOneStackFrame(int bytecodeOffset, StackMapType stackType, ConstantPoolGen cp) {
        StackMapType[] lt = null;
        StackMapType[] st = new StackMapType[1];
        st[0]=stackType;
        StackMapEntry ret = new StackMapEntry(Const.SAME_LOCALS_1_STACK_ITEM_FRAME,bytecodeOffset, lt, st, cp.getConstantPool());
        return ret;
    }

    public static StackMapEntry generateFullFrame(int bytecodeOffset, List<StackMapType> stackTypes, List<StackMapType> localTypes, ConstantPoolGen cp){
        StackMapType[] st = null;
        StackMapType[] lt = null;
        if(!stackTypes.isEmpty())st = listToArray(stackTypes);
        if(!localTypes.isEmpty())lt = listToArray(localTypes);
        StackMapEntry ret = new StackMapEntry(Const.FULL_FRAME,bytecodeOffset, lt, st, cp.getConstantPool());
        return ret;
    }

    private static StackMapType[] listToArray(List<StackMapType> list){
        StackMapType[] ret = new StackMapType[list.size()];
        for(int i = 0; i<list.size();i++){
            ret[i] = list.get(i);
        }
        return ret;
    }

    private static <A> A[] toArray(List<A> list){
        A[] ret = (A[]) new Object[list.size()];
        for(int i = 0; i<list.size();i++){
            ret[i] = list.get(i);
        }
        return ret;
    }

    private static Map<Integer, StackMapType> generateLocalVars(List<Type> types, int startIndex, ConstantPoolGen cp){
        HashMap<Integer, StackMapType> ret = new HashMap<>();
        for(int i = 0; i<types.size();i++){
            int index = i+startIndex;
            StackMapType localVar = convert(types.get(i), cp).get(0); //Typkonvert muss bei lokalen Variablen immer mindestens einen StackItem Typ zurückgeben
            ret.put(index,localVar );
        }
        return ret;
    }

    private static List<StackMapType> convert(Type t, ConstantPoolGen cp){
        List<StackMapType> ret = new ArrayList<>(2);
        if(t.equals(Type.STRING)){
            ret.add(StackMapTypeFactory.getReferenceType(Type.STRING, cp));
        }else if(t.equals(Type.INT)){
            ret.add(StackMapTypeFactory.getInt(cp));
        }else if(t.equals(Type.BOOLEAN)){
            ret.add(StackMapTypeFactory.getInt(cp));
        }else if(t.equals(Type.BYTE)){
            ret.add(StackMapTypeFactory.getInt(cp));
        }else if(t.equals(Type.CHAR)){
            ret.add(StackMapTypeFactory.getInt(cp));
        }else if(t.equals(Type.FLOAT)){
            ret.add(StackMapTypeFactory.getFloat(cp));
        }else if(t.equals(Type.DOUBLE)||t.equals(Type.LONG)){
            ret.add(StackMapTypeFactory.getDouble(cp));
            ret.add(StackMapTypeFactory.getTop(cp));
        }else if(t instanceof ObjectType){
            ret.add(StackMapTypeFactory.getReferenceType((ObjectType) t,cp));
        }else if(t.equals(Type.NULL)){
            StackMapTypeFactory.getNull(cp);
        }else if(t.equals(Type.VOID)){
            return ret;
        }else if(t instanceof ArrayType){

        }else{
            throw new NullPointerException();
        }
        return ret;
    }

    /**
     * Ein Frame hört bei einem unconditionalBranch auf bzw. fängt bei dem Sprungziel eines Branches an.
     * * @see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.10.1
     * @param position
     * @param inCode
     * @return
     */
    private static boolean isFrameEndpoint(InstructionHandle position, InstructionList inCode){
        if(position.getInstruction() instanceof BranchInstruction//GotoInstruction
                || position.getInstruction() instanceof ReturnInstruction){
            //Falls Instruktion ein Branch ist:
            return true;
        }
        if(position.hasTargeters()){
            return true;
        }
        return false;
    }

    private static boolean isFrameBeginning(InstructionHandle position, InstructionList inCode){
        if(position.hasTargeters()){
            return true;
        }else {
            return false;
        }
    }

    public static List<Frame> splitIntoBlocks(InstructionList instructions, ConstantPoolGen cp){
        ArrayList<ArrayList<InstructionHandle>> blocks = new ArrayList<>();
        ArrayList<InstructionHandle> block = new ArrayList<>();
        ArrayList<Frame> frames = new ArrayList<>();
        for(InstructionHandle ins : instructions){
            if(isFrameBeginning(ins, instructions)){
                if( ! block.isEmpty()){
                    blocks.add(block);
                }
                block = new ArrayList<>();
            }
            block.add(ins);
        }
        if( ! block.isEmpty())blocks.add(block);

        for(List<InstructionHandle> blockIns : blocks){
            Frame newFrame = new Frame(blockIns, cp);
            frames.add(newFrame);
        }

        //Set the Frame Successors:
        for(int i = 0; i< frames.size(); i++){
            List<Frame> successors = new ArrayList<>();
            Frame currentFrame = frames.get(i);
            //Last instruction in the current Frame
            Instruction lastIns = currentFrame.instructions.get(currentFrame.instructions.size()-1).getInstruction();
            if(lastIns instanceof InstructionTargeter){
                for(Frame possibleSuccessor : frames){ //Check all possible frames:
                    InstructionHandle firstInstruction = possibleSuccessor.instructions.get(0);
                    if(((InstructionTargeter) lastIns).containsTarget(firstInstruction)){
                        // If the last Instruction in the current Block points at the first instruction of this possible Successor-Block:
                        successors.add(possibleSuccessor); // add successor
                    }
                    /*
                    for(InstructionTargeter targeter : firstInstruction.getTargeters()){
                        if(targeter.equals(lastIns.getInstruction()))
                            successors.add(possibleSuccessor); // add successor
                    }
                    */
                }
            }

            if(i + 1 < frames.size() //if there is another Frame left
                    && ! (lastIns instanceof GotoInstruction)){ //and the current Frame does not end with a goto instruction
                successors.add(frames.get(i+1)); //add the next frame as a successor
            }
            currentFrame.setSuccessors(successors);
        }
        System.out.println(frames);
        //Wurden keine Frames gefunden, so gibt es keine Sprünge und es wird der gesamte Frame zurückgegeben
        if(frames.size() == 0)frames.add(new Frame(block,cp));
        return frames;
    }

    /**
     * Berechnet die Änderungen des state durch die übergebene Instruktion
     * @param state
     * @param cp
     * @return Der geänderte State
     */
    static JVMState applyStatement(InstructionHandle instruction, JVMState state, ConstantPoolGen cp){
    	/*
    	 * Berechnet den Output auf den Stack und ändert den LocalVarState und den StackState je nach Instruktion ab
    	 */
        Instruction ins = instruction.getInstruction();
        JVMState ret = new JVMState(state);
        int opcode = ins.getOpcode();
        StackMapType t1,t2,t3,t4;
        switch (opcode) {
            case Const.NOP:
            case Const.INEG:
            case Const.LNEG:
            case Const.FNEG:
            case Const.DNEG:
            case Const.I2B:
            case Const.I2C:
            case Const.I2S:
            case Const.GOTO:
            case Const.RETURN:
                break;
            case Const.ACONST_NULL:
                ret.push(StackMapTypeFactory.getNull(cp));
                break;
            case Const.ICONST_M1:
            case Const.ICONST_0:
            case Const.ICONST_1:
            case Const.ICONST_2:
            case Const.ICONST_3:
            case Const.ICONST_4:
            case Const.ICONST_5:
            case Const.BIPUSH:
            case Const.SIPUSH:
            case Const.ILOAD_0:
            case Const.ILOAD_1:
            case Const.ILOAD_2:
            case Const.ILOAD_3:
            case Const.ILOAD:
                ret.push(StackMapTypeFactory.getInt(cp));
                break;
            case Const.LCONST_0:
            case Const.LCONST_1:
            case Const.LLOAD:
                ret.push(StackMapTypeFactory.getDouble(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.FCONST_0:
            case Const.FCONST_1:
            case Const.FCONST_2:
            case Const.FLOAD_0:
            case Const.FLOAD_1:
            case Const.FLOAD_2:
            case Const.FLOAD_3:
            case Const.FLOAD:
                ret.push(StackMapTypeFactory.getFloat(cp));
                break;
            case Const.DCONST_0:
            case Const.DCONST_1:
            case Const.DLOAD_0:
            case Const.DLOAD_1:
            case Const.DLOAD_2:
            case Const.DLOAD_3:
            case Const.DLOAD:
                ret.push(StackMapTypeFactory.getDouble(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.LDC:
                LDC ldcIns = (LDC) ins;
                Type t = ldcIns.getType(cp);
                ret.push(convert(t, cp));
        	/*
            switch (item.type) {
            case ClassWriter.INT:
                ret.push(StackMapTypeFactory.getInt(cp));
                break;
            case ClassWriter.StackMapTypeFactory.getLong(cp):
                ret.push(StackMapTypeFactory.getLong(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case ClassWriter.StackMapTypeFactory.getFloat(cp):
                ret.push(StackMapTypeFactory.getFloat(cp));
                break;
            case ClassWriter.StackMapTypeFactory.getDouble(cp):
                ret.push(StackMapTypeFactory.getDouble(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case ClassWriter.CLASS:
                ret.push(new StackItemOBJECT("java/lang/Class"));
                break;
            case ClassWriter.STR:
            	ret.push(new StackItemOBJECT("java/lang/String"));
            	break;
            case ClassWriter.MTYPE:
            	ret.push(new StackItemOBJECT("java/lang/invoke/MethodType"));
            	break;
            // case ClassWriter.HANDLE_BASE + [1..9]:
            default:
            	ret.push(new StackItemOBJECT("java/lang/invoke/MethodHandle"));
            	break;
            }
            */
                break;
            case Const.ALOAD_0:
            case Const.ALOAD_1:
            case Const.ALOAD_2:
            case Const.ALOAD_3:
            case Const.ALOAD:
                ALOAD aloadIns = (ALOAD) ins;
                ret.push(ret.getLocalType(aloadIns.getIndex()));
                break;
            case Const.IALOAD:
            case Const.BALOAD:
            case Const.CALOAD:
            case Const.SALOAD:
                ret.pop(2);
                ret.push(StackMapTypeFactory.getInt(cp));
                break;
            case Const.LALOAD:
            case Const.D2L:
                ret.pop(2);
                ret.push(StackMapTypeFactory.getLong(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.FALOAD:
                ret.pop(2);
                ret.push(StackMapTypeFactory.getFloat(cp));
                break;
            case Const.DALOAD:
            case Const.L2D:
                ret.pop(2);
                ret.push(StackMapTypeFactory.getDouble(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.AALOAD:
                ret.pop(1);
                StackMapType arrayReference = ret.pop();
                ret.push(arrayReference); //TODO: Fixen, es darf nicht die ArrayReference sonder der Typ eines Elements des Arrays auf den Stack
                //ret.push(ELEMENT_OF + t1);
                break;
            case Const.ISTORE:
            case Const.FSTORE:
            case Const.ASTORE_0:
            case Const.ASTORE_1:
            case Const.ASTORE_2:
            case Const.ASTORE_3:
            case Const.ASTORE:
                StoreInstruction storeIns = (StoreInstruction)ins;
                t1 = ret.pop();
                ret.set(storeIns.getIndex(), t1);
                if (storeIns.getIndex() > 0) {
                    t2 = ret.getLocalType(storeIns.getIndex() - 1);
                    // if t2 is of kind STACK or LOCAL we cannot know its size!
                    if (t2 == StackMapTypeFactory.getLong(cp) || t2 == StackMapTypeFactory.getDouble(cp)) {
                        ret.set(storeIns.getIndex() - 1, StackMapTypeFactory.getTop(cp));
                    }
                    //TODO:
                /*else if ((t2 & KIND) != BASE) {
                    set(arg - 1, t2 | StackMapTypeFactory.getTop(cp)_IF_StackMapTypeFactory.getLong(cp)_OR_StackMapTypeFactory.getDouble(cp));
                }*/
                }
                break;
            case Const.LSTORE_0:
            case Const.LSTORE_1:
            case Const.LSTORE_2:
            case Const.LSTORE_3:
            case Const.LSTORE:
            case Const.DSTORE_0:
            case Const.DSTORE_1:
            case Const.DSTORE_2:
            case Const.DSTORE_3:
            case Const.DSTORE:
                StoreInstruction largeStoreIns = (StoreInstruction)ins;
                ret.pop(1);
                t1 = ret.pop();
                ret.set(largeStoreIns.getIndex(), t1);
                ret.set(largeStoreIns.getIndex() + 1, StackMapTypeFactory.getTop(cp));
                if (largeStoreIns.getIndex() > 0) {
                    t2 = ret.getLocalType(largeStoreIns.getIndex() - 1);
                    // if t2 is of kind STACK or LOCAL we cannot know its size!
                    if (t2 == StackMapTypeFactory.getLong(cp) || t2 == StackMapTypeFactory.getDouble(cp)) {
                        ret.set(largeStoreIns.getIndex() - 1, StackMapTypeFactory.getTop(cp));
                    } //TODO:
                /*else if ((t2 & KIND) != BASE) {
                    set(arg - 1, t2 | StackMapTypeFactory.getTop(cp)_IF_StackMapTypeFactory.getLong(cp)_OR_StackMapTypeFactory.getDouble(cp));
                }*/
                }
                break;
            case Const.IASTORE:
            case Const.BASTORE:
            case Const.CASTORE:
            case Const.SASTORE:
            case Const.FASTORE:
            case Const.AASTORE:
                ret.pop(3);
                break;
            case Const.LASTORE:
            case Const.DASTORE:
                ret.pop(4);
                break;
            case Const.POP:
            case Const.IFEQ:
            case Const.IFNE:
            case Const.IFLT:
            case Const.IFGE:
            case Const.IFGT:
            case Const.IFLE:
            case Const.IRETURN:
            case Const.FRETURN:
            case Const.ARETURN:
            case Const.TABLESWITCH:
            case Const.LOOKUPSWITCH:
            case Const.ATHROW:
            case Const.MONITORENTER:
            case Const.MONITOREXIT:
            case Const.IFNULL:
            case Const.IFNONNULL:
                if(ret.stack.isEmpty()){
                    System.out.println("Stak lehr");
                }
                ret.pop(1);
                break;
            case Const.POP2:
            case Const.IF_ICMPEQ:
            case Const.IF_ICMPNE:
            case Const.IF_ICMPLT:
            case Const.IF_ICMPGE:
            case Const.IF_ICMPGT:
            case Const.IF_ICMPLE:
            case Const.IF_ACMPEQ:
            case Const.IF_ACMPNE:
            case Const.LRETURN:
            case Const.DRETURN:
                ret.pop(2);
                break;
            case Const.DUP:
                t1 = ret.pop();
                ret.push(t1);
                ret.push(t1);
                break;
            case Const.DUP_X1:
                t1 = ret.pop();
                t2 = ret.pop();
                ret.push(t1);
                ret.push(t2);
                ret.push(t1);
                break;
            case Const.DUP_X2:
                t1 = ret.pop();
                t2 = ret.pop();
                t3 = ret.pop();
                ret.push(t1);
                ret.push(t3);
                ret.push(t2);
                ret.push(t1);
                break;
            case Const.DUP2:
                t1 = ret.pop();
                t2 = ret.pop();
                ret.push(t2);
                ret.push(t1);
                ret.push(t2);
                ret.push(t1);
                break;
            case Const.DUP2_X1:
                t1 = ret.pop();
                t2 = ret.pop();
                t3 = ret.pop();
                ret.push(t2);
                ret.push(t1);
                ret.push(t3);
                ret.push(t2);
                ret.push(t1);
                break;
            case Const.DUP2_X2:
                t1 = ret.pop();
                t2 = ret.pop();
                t3 = ret.pop();
                t4 = ret.pop();
                ret.push(t2);
                ret.push(t1);
                ret.push(t4);
                ret.push(t3);
                ret.push(t2);
                ret.push(t1);
                break;
            case Const.SWAP:
                t1 = ret.pop();
                t2 = ret.pop();
                ret.push(t1);
                ret.push(t2);
                break;
            case Const.IADD:
            case Const.ISUB:
            case Const.IMUL:
            case Const.IDIV:
            case Const.IREM:
            case Const.IAND:
            case Const.IOR:
            case Const.IXOR:
            case Const.ISHL:
            case Const.ISHR:
            case Const.IUSHR:
            case Const.L2I:
            case Const.D2I:
            case Const.FCMPL:
            case Const.FCMPG:
                ret.pop(2);
                ret.push(StackMapTypeFactory.getInt(cp));
                break;
            case Const.LADD:
            case Const.LSUB:
            case Const.LMUL:
            case Const.LDIV:
            case Const.LREM:
            case Const.LAND:
            case Const.LOR:
            case Const.LXOR:
                ret.pop(4);
                ret.push(StackMapTypeFactory.getLong(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.FADD:
            case Const.FSUB:
            case Const.FMUL:
            case Const.FDIV:
            case Const.FREM:
            case Const.L2F:
            case Const.D2F:
                ret.pop(2);
                ret.push(StackMapTypeFactory.getFloat(cp));
                break;
            case Const.DADD:
            case Const.DSUB:
            case Const.DMUL:
            case Const.DDIV:
            case Const.DREM:
                ret.pop(4);
                ret.push(StackMapTypeFactory.getDouble(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.LSHL:
            case Const.LSHR:
            case Const.LUSHR:
                ret.pop(3);
                ret.push(StackMapTypeFactory.getLong(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.IINC:
                ret.set(((IINC)ins).getIndex(), StackMapTypeFactory.getInt(cp));
                break;
            case Const.I2L:
            case Const.F2L:
                ret.pop(1);
                ret.push(StackMapTypeFactory.getLong(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.I2F:
                ret.pop(1);
                ret.push(StackMapTypeFactory.getFloat(cp));
                break;
            case Const.I2D:
            case Const.F2D:
                ret.pop(1);
                ret.push(StackMapTypeFactory.getDouble(cp));
                ret.push(StackMapTypeFactory.getTop(cp));
                break;
            case Const.F2I:
            case Const.ARRAYLENGTH:
            case Const.INSTANCEOF:
                ret.pop(1);
                ret.push(StackMapTypeFactory.getInt(cp));
                break;
            case Const.LCMP:
            case Const.DCMPL:
            case Const.DCMPG:
                ret.pop(4);
                ret.push(StackMapTypeFactory.getInt(cp));
                break;
            case Const.JSR:
            case Const.RET:
                throw new RuntimeException(
                        "JSR/RET are not supported with computeFrames option"); //TODO: Fehlermeldung anpassen
            case Const.GETSTATIC:
                //ret.push(cw, item.strVal3); //TODO
                break;
            case Const.PUTSTATIC:
                //ret.pop(item.strVal3); //TODO
                break;
            case Const.GETFIELD:
                GETFIELD getfieldIns = (GETFIELD) ins;
                ret.pop(1);
                //ret.push(StackMapTypeFactory.getReferenceType((ObjectType) getfieldIns.getFieldType(cp),cp));
                ret.push(convert(getfieldIns.getFieldType(cp),cp));
                break;
            case Const.PUTFIELD:
                PUTFIELD putfieldIns = (PUTFIELD) ins;
                ret.pop(putfieldIns.consumeStack(cp));
                break;
            case Const.INVOKEVIRTUAL:
            case Const.INVOKESPECIAL:
            case Const.INVOKESTATIC:
            case Const.INVOKEINTERFACE:
            case Const.INVOKEDYNAMIC:
                InvokeInstruction invokeIns = (InvokeInstruction) ins;
                ret.pop(invokeIns.consumeStack(cp));
                ret.push(convert(invokeIns.getReturnType(cp),cp));
        	/*
        	 * TODO:
        	 * Hier return new StackItemOBJECT(returnTypeDerMethode);
        	 * returnTypeDerMethode bestimmen indem man im ConstantPool nach dem Argument für die Instruktion nachsieht.
        	 * Wie kann man bestimmen, ob es sich um Object oder primären Datentyp handelt. bcel-Typen verwenden? Müsste dann weiter oben auch gepatcht werden.
        	 */
            /*
             ret.pop(item.strVal3);

            if (opcode != Const.INVOKESTATIC) {
                t1 = ret.pop();
                if (opcode == Const.INVOKESPECIAL
                        && item.strVal2.charAt(0) == '<') {
                    init(t1);
                }
            }

            ret.push(cw, item.strVal3);
            */
                /*
                Type retType = invokeIns.getReturnType(cp);
                if(retType instanceof BasicType){
                    ret.push(convert((BasicType)retType,cp));
                }else if(retType instanceof ArrayType){
                    //TODO
                }else if(retType instanceof ReferenceType){
                    ret.push(convert((ReferenceType) retType,cp));
                }
                */
                break;
            case Const.NEW:
                NEW newIns = (NEW) ins;
                ret.push(StackMapTypeFactory.getUninitialized(instruction, cp));
                //ret.push(UNINITIALIZED | cw.addUninitializedType(item.strVal1, arg));
                break;
            /*
        case Const.NEWARRAY:
            ret.pop();
            switch (arg) {
            case Const.T_BOOLEAN:
                ret.push(ARRAY_OF | BOOLEAN);
                break;
            case Const.T_CHAR:
                ret.push(ARRAY_OF | CHAR);
                break;
            case Const.T_BYTE:
                ret.push(ARRAY_OF | BYTE);
                break;
            case Const.T_SHORT:
                ret.push(ARRAY_OF | SHORT);
                break;
            case Const.T_INT:
                ret.push(ARRAY_OF | StackMapTypeFactory.getInt(cp));
                break;
            case Const.T_StackMapTypeFactory.getFloat(cp):
                ret.push(ARRAY_OF | StackMapTypeFactory.getFloat(cp));
                break;
            case Const.T_StackMapTypeFactory.getDouble(cp):
                ret.push(ARRAY_OF | StackMapTypeFactory.getDouble(cp));
                break;
            // case Const.T_StackMapTypeFactory.getLong(cp):
            default:
                ret.push(ARRAY_OF | StackMapTypeFactory.getLong(cp));
                break;
            }
            break;
        case Const.ANEWARRAY:
            String s = item.strVal1;
            ret.pop();
            if (s.charAt(0) == '[') {
                ret.push(cw, '[' + s);
            } else {
                ret.push(ARRAY_OF | OBJECT | cw.addType(s));
            }
            break;
        case Const.CHECKCAST:
            s = item.strVal1;
            ret.pop();
            if (s.charAt(0) == '[') {
                ret.push(cw, s);
            } else {
                ret.push(OBJECT | cw.addType(s));
            }
            break;
        // case Const.MULTIANEWARRAY:
        default:
            ret.pop(arg);
            ret.push(cw, item.strVal1);
            break;
        */
        }
        return ret;
    }

}
