/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;

/**
 * represents one annotation in the annotation table
 * 
 * @version $Id: AnnotationEntry
 * @author  <A HREF="mailto:dbrosius@qis.net">D. Brosius</A>
 * @since 5.2
 */
public class AnnotationEntry implements Node, Constants {

    private int type_index;
    private int num_element_value_pairs;
    private ElementValuePair[] element_value_pairs;
    private ConstantPool constant_pool;


    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     */
    AnnotationEntry(DataInputStream file, ConstantPool constant_pool) throws IOException {
        type_index = (file.readUnsignedShort());
        num_element_value_pairs = (file.readUnsignedShort());
        element_value_pairs = new ElementValuePair[num_element_value_pairs];
        for (int i = 0; i < num_element_value_pairs; i++) {
            element_value_pairs[i] = new ElementValuePair(file, constant_pool);
        }
        this.constant_pool = constant_pool;
    }


    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    public void accept( Visitor v ) {
        //	    v.visitAnnotationEntry(this);
    }


    /**
     * @return the annotation type name
     */
    public String getAnnotationType() {
        ConstantUtf8 c;
        c = (ConstantUtf8) constant_pool.getConstant(type_index, CONSTANT_Utf8);
        return c.getBytes();
    }


    /**
     * @return the number of element value pairs in this annotation entry
     */
    public final int getNumElementValuePairs() {
        return num_element_value_pairs;
    }


    /**
     * @return the element value pairs in this annotation entry
     */
    public ElementValuePair[] getElementValuePairs() {
        return element_value_pairs;
    }


	public void dump(DataOutputStream dos)
	{
		// TODO Auto-generated method stub
	}
}
