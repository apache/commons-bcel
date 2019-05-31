/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
package org.apache.bcel.verifier;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.UIManager;

import org.apache.bcel.generic.Type;

/**
 * A graphical user interface application demonstrating JustIce.
 *
 */
public class GraphicalVerifier {

    private final boolean packFrame = false;


    /** Constructor. */
    public GraphicalVerifier() {
        final VerifierAppFrame frame = new VerifierAppFrame();
        //Frames �berpr�fen, die voreingestellte Gr��e haben
        //Frames packen, die nutzbare bevorzugte Gr��eninformationen enthalten, z.B. aus ihrem Layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        //Das Fenster zentrieren
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        frame.getClassNamesJList().setModel(new VerifierFactoryListModel());
        VerifierFactory.getVerifier(Type.OBJECT.getClassName()); // Fill list with java.lang.Object
        frame.getClassNamesJList().setSelectedIndex(0); // default, will verify java.lang.Object
    }


    /** Main method. */
    public static void main( final String[] args ) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        new GraphicalVerifier();
    }
}
