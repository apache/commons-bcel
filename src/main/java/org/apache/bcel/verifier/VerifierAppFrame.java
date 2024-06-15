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
 */
package org.apache.bcel.verifier;

import java.awt.AWTEvent;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.lang3.ArrayUtils;

/**
 * This class implements a machine-generated frame for use with the GraphicalVerfifier.
 *
 * @see GraphicalVerifier
 */
public class VerifierAppFrame extends JFrame {

    private static final long serialVersionUID = -542458133073307640L;
    private JPanel contentPane;
    private final JSplitPane jSplitPane1 = new JSplitPane();
    private final JPanel jPanel1 = new JPanel();
    private final JPanel jPanel2 = new JPanel();
    private final JSplitPane jSplitPane2 = new JSplitPane();
    private final JPanel jPanel3 = new JPanel();
    private final JList<String> classNamesJList = new JList<>();
    private final GridLayout gridLayout1 = new GridLayout();
    private final JPanel messagesPanel = new JPanel();
    private final GridLayout gridLayout2 = new GridLayout();
    private final JMenuBar jMenuBar1 = new JMenuBar();
    private final JMenu jMenu1 = new JMenu();
    private final JScrollPane jScrollPane1 = new JScrollPane();
    private final JScrollPane messagesScrollPane = new JScrollPane();
    private final JScrollPane jScrollPane3 = new JScrollPane();
    private final GridLayout gridLayout4 = new GridLayout();
    private final JScrollPane jScrollPane4 = new JScrollPane();
    private final CardLayout cardLayout1 = new CardLayout();
    private String currentClass;
    private final GridLayout gridLayout3 = new GridLayout();
    private final JTextPane pass1TextPane = new JTextPane();
    private final JTextPane pass2TextPane = new JTextPane();
    private final JTextPane messagesTextPane = new JTextPane();
    private final JMenuItem newFileMenuItem = new JMenuItem();
    private final JSplitPane jSplitPane3 = new JSplitPane();
    private final JSplitPane jSplitPane4 = new JSplitPane();
    private final JScrollPane jScrollPane2 = new JScrollPane();
    private final JScrollPane jScrollPane5 = new JScrollPane();
    private final JScrollPane jScrollPane6 = new JScrollPane();
    private final JScrollPane jScrollPane7 = new JScrollPane();
    private final JList<String> pass3aJList = new JList<>();
    private final JList<String> pass3bJList = new JList<>();
    private final JTextPane pass3aTextPane = new JTextPane();
    private final JTextPane pass3bTextPane = new JTextPane();
    private final JMenu jMenu2 = new JMenu();
    private final JMenuItem whatisMenuItem = new JMenuItem();
    private final JMenuItem aboutMenuItem = new JMenuItem();

    /** Constructs a new instance. */
    public VerifierAppFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    void aboutMenuItemActionPerformed(final ActionEvent e) {
        JOptionPane.showMessageDialog(this, Verifier.BANNER, Verifier.NAME, JOptionPane.INFORMATION_MESSAGE);
    }

    synchronized void classNamesJListValueChanged(final ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        currentClass = classNamesJList.getSelectedValue();
        try {
            verify();
        } catch (final ClassNotFoundException ex) {
            // FIXME: report the error using the GUI
            ex.printStackTrace();
        }
        classNamesJList.setSelectedValue(currentClass, true);
    }

    /**
     * @return the classNamesJList
     */
    JList<String> getClassNamesJList() {
        return classNamesJList;
    }

    /** Initizalization of the components. */
    private void jbInit() {
        // setIconImage(Toolkit.getDefaultToolkit().createImage(Frame1.class.getResource("[Ihr Symbol]")));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(cardLayout1);
        this.setJMenuBar(jMenuBar1);
        this.setSize(new Dimension(708, 451));
        this.setTitle("JustIce");
        jPanel1.setMinimumSize(new Dimension(100, 100));
        jPanel1.setPreferredSize(new Dimension(100, 100));
        jPanel1.setLayout(gridLayout1);
        jSplitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jPanel2.setLayout(gridLayout2);
        jPanel3.setMinimumSize(new Dimension(200, 100));
        jPanel3.setPreferredSize(new Dimension(400, 400));
        jPanel3.setLayout(gridLayout4);
        messagesPanel.setMinimumSize(new Dimension(100, 100));
        messagesPanel.setLayout(gridLayout3);
        jPanel2.setMinimumSize(new Dimension(200, 100));
        jMenu1.setText("File");
        jScrollPane1.getViewport().setBackground(Color.red);
        messagesScrollPane.getViewport().setBackground(Color.red);
        messagesScrollPane.setPreferredSize(new Dimension(10, 10));
        classNamesJList.addListSelectionListener(this::classNamesJListValueChanged);
        classNamesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setBorder(BorderFactory.createLineBorder(Color.black));
        jScrollPane3.setPreferredSize(new Dimension(100, 100));
        gridLayout4.setRows(4);
        gridLayout4.setColumns(1);
        gridLayout4.setHgap(1);
        jScrollPane4.setBorder(BorderFactory.createLineBorder(Color.black));
        jScrollPane4.setPreferredSize(new Dimension(100, 100));
        pass1TextPane.setBorder(BorderFactory.createRaisedBevelBorder());
        pass1TextPane.setToolTipText("");
        pass1TextPane.setEditable(false);
        pass2TextPane.setBorder(BorderFactory.createRaisedBevelBorder());
        pass2TextPane.setEditable(false);
        messagesTextPane.setBorder(BorderFactory.createRaisedBevelBorder());
        messagesTextPane.setEditable(false);
        newFileMenuItem.setText("New...");
        newFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(78, InputEvent.CTRL_MASK, true));
        newFileMenuItem.addActionListener(this::newFileMenuItemActionPerformed);
        pass3aTextPane.setEditable(false);
        pass3bTextPane.setEditable(false);
        pass3aJList.addListSelectionListener(this::pass3aJList_valueChanged);
        pass3bJList.addListSelectionListener(this::pass3bJList_valueChanged);
        jMenu2.setText("Help");
        whatisMenuItem.setText("What is...");
        whatisMenuItem.addActionListener(this::whatisMenuItemActionPerformed);
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(this::aboutMenuItemActionPerformed);
        jSplitPane2.add(messagesPanel, JSplitPane.BOTTOM);
        messagesPanel.add(messagesScrollPane, null);
        messagesScrollPane.getViewport().add(messagesTextPane, null);
        jSplitPane2.add(jPanel3, JSplitPane.TOP);
        jPanel3.add(jScrollPane3, null);
        jScrollPane3.getViewport().add(pass1TextPane, null);
        jPanel3.add(jScrollPane4, null);
        jPanel3.add(jSplitPane3, null);
        jSplitPane3.add(jScrollPane2, JSplitPane.LEFT);
        jScrollPane2.getViewport().add(pass3aJList, null);
        jSplitPane3.add(jScrollPane5, JSplitPane.RIGHT);
        jScrollPane5.getViewport().add(pass3aTextPane, null);
        jPanel3.add(jSplitPane4, null);
        jSplitPane4.add(jScrollPane6, JSplitPane.LEFT);
        jScrollPane6.getViewport().add(pass3bJList, null);
        jSplitPane4.add(jScrollPane7, JSplitPane.RIGHT);
        jScrollPane7.getViewport().add(pass3bTextPane, null);
        jScrollPane4.getViewport().add(pass2TextPane, null);
        jSplitPane1.add(jPanel2, JSplitPane.TOP);
        jPanel2.add(jScrollPane1, null);
        jSplitPane1.add(jPanel1, JSplitPane.BOTTOM);
        jPanel1.add(jSplitPane2, null);
        jScrollPane1.getViewport().add(classNamesJList, null);
        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);
        contentPane.add(jSplitPane1, "jSplitPane1");
        jMenu1.add(newFileMenuItem);
        jMenu2.add(whatisMenuItem);
        jMenu2.add(aboutMenuItem);
        jSplitPane2.setDividerLocation(300);
        jSplitPane3.setDividerLocation(150);
        jSplitPane4.setDividerLocation(150);
    }

    void newFileMenuItemActionPerformed(final ActionEvent e) {
        final String className = JOptionPane.showInputDialog("Please enter the fully qualified name of a class or interface to verify:");
        if (className == null || className.isEmpty()) {
            return;
        }
        VerifierFactory.getVerifier(className); // let observers do the rest.
        classNamesJList.setSelectedValue(className, true);
    }

    synchronized void pass3aJList_valueChanged(final ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        final Verifier v = VerifierFactory.getVerifier(currentClass);
        final StringBuilder all3amsg = new StringBuilder();
        boolean all3aok = true;
        boolean rejected = false;
        for (int i = 0; i < pass3aJList.getModel().getSize(); i++) {
            if (pass3aJList.isSelectedIndex(i)) {
                final VerificationResult vr = v.doPass3a(i);
                if (vr.getStatus() == VerificationResult.VERIFIED_REJECTED) {
                    all3aok = false;
                    rejected = true;
                }
                JavaClass jc = null;
                try {
                    jc = Repository.lookupClass(v.getClassName());
                    all3amsg.append("Method '").append(jc.getMethods()[i]).append("': ").append(vr.getMessage().replace('\n', ' ')).append("\n\n");
                } catch (final ClassNotFoundException ex) {
                    // FIXME: handle the error
                    ex.printStackTrace();
                }
            }
        }
        pass3aTextPane.setText(all3amsg.toString());
        pass3aTextPane.setBackground(all3aok ? Color.green : rejected ? Color.red : Color.yellow);
    }

    synchronized void pass3bJList_valueChanged(final ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        final Verifier v = VerifierFactory.getVerifier(currentClass);
        final StringBuilder all3bmsg = new StringBuilder();
        boolean all3bok = true;
        boolean rejected = false;
        for (int i = 0; i < pass3bJList.getModel().getSize(); i++) {
            if (pass3bJList.isSelectedIndex(i)) {
                final VerificationResult vr = v.doPass3b(i);
                if (vr.getStatus() == VerificationResult.VERIFIED_REJECTED) {
                    all3bok = false;
                    rejected = true;
                }
                JavaClass jc = null;
                try {
                    jc = Repository.lookupClass(v.getClassName());
                    all3bmsg.append("Method '").append(jc.getMethods()[i]).append("': ").append(vr.getMessage().replace('\n', ' ')).append("\n\n");
                } catch (final ClassNotFoundException ex) {
                    // FIXME: handle the error
                    ex.printStackTrace();
                }
            }
        }
        pass3bTextPane.setText(all3bmsg.toString());
        pass3bTextPane.setBackground(all3bok ? Color.green : rejected ? Color.red : Color.yellow);
    }

    /** Overridden to stop the application on a closing window. */
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

    private void verify() throws ClassNotFoundException {
        setTitle("PLEASE WAIT");
        final Verifier v = VerifierFactory.getVerifier(currentClass);
        v.flush(); // Don't cache the verification result for this class.
        VerificationResult vr;
        vr = v.doPass1();
        if (vr.getStatus() == VerificationResult.VERIFIED_REJECTED) {
            pass1TextPane.setText(vr.getMessage());
            pass1TextPane.setBackground(Color.red);
            pass2TextPane.setText("");
            pass2TextPane.setBackground(Color.yellow);
            pass3aTextPane.setText("");
            pass3aJList.setListData(ArrayUtils.EMPTY_STRING_ARRAY);
            pass3aTextPane.setBackground(Color.yellow);
            pass3bTextPane.setText("");
            pass3bJList.setListData(ArrayUtils.EMPTY_STRING_ARRAY);
            pass3bTextPane.setBackground(Color.yellow);
        } else { // Must be VERIFIED_OK, Pass 1 does not know VERIFIED_NOTYET
            pass1TextPane.setBackground(Color.green);
            pass1TextPane.setText(vr.getMessage());
            vr = v.doPass2();
            if (vr.getStatus() == VerificationResult.VERIFIED_REJECTED) {
                pass2TextPane.setText(vr.getMessage());
                pass2TextPane.setBackground(Color.red);
                pass3aTextPane.setText("");
                pass3aTextPane.setBackground(Color.yellow);
                pass3aJList.setListData(ArrayUtils.EMPTY_STRING_ARRAY);
                pass3bTextPane.setText("");
                pass3bTextPane.setBackground(Color.yellow);
                pass3bJList.setListData(ArrayUtils.EMPTY_STRING_ARRAY);
            } else { // must be Verified_OK, because Pass1 was OK (cannot be Verified_NOTYET).
                pass2TextPane.setText(vr.getMessage());
                pass2TextPane.setBackground(Color.green);
                final JavaClass jc = Repository.lookupClass(currentClass);
                /*
                 * boolean all3aok = true; boolean all3bok = true; String all3amsg = ""; String all3bmsg = "";
                 */
                final String[] methodNames = new String[jc.getMethods().length];
                Arrays.setAll(methodNames, i -> jc.getMethods()[i].toString().replace('\n', ' ').replace('\t', ' '));
                pass3aJList.setListData(methodNames);
                pass3aJList.setSelectionInterval(0, jc.getMethods().length - 1);
                pass3bJList.setListData(methodNames);
                pass3bJList.setSelectionInterval(0, jc.getMethods().length - 1);
            }
        }
        final String[] msgs = v.getMessages();
        messagesTextPane.setBackground(msgs.length == 0 ? Color.green : Color.yellow);
        final StringBuilder allmsgs = new StringBuilder();
        for (int i = 0; i < msgs.length; i++) {
            msgs[i] = msgs[i].replace('\n', ' ');
            allmsgs.append(msgs[i]).append("\n\n");
        }
        messagesTextPane.setText(allmsgs.toString());
        setTitle(currentClass + " - " + Verifier.NAME);
    }

    void whatisMenuItemActionPerformed(final ActionEvent e) {
        JOptionPane.showMessageDialog(this,
            "The upper four boxes to the right reflect verification passes according to"
                + " The Java Virtual Machine Specification.\nThese are (in that order):"
                + " Pass one, Pass two, Pass three (before data flow analysis), Pass three (data flow analysis).\n"
                + "The bottom box to the right shows (warning) messages; warnings do not cause a class to be rejected.",
            Verifier.NAME, JOptionPane.INFORMATION_MESSAGE);
    }

}
