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

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Utility;

/**
 * A class for simple graphical class file verification. Use the main(String []) method with fully qualified class names
 * as arguments to use it as a stand-alone application. Use the VerifyDialog(String) constructor to use this class in
 * your application. [This class was created using VisualAge for Java, but it does not work under VAJ itself (Version
 * 3.02 JDK 1.2)]
 *
 * @see #main(String[])
 * @see #VerifyDialog(String)
 */
public class VerifyDialog extends JDialog {

    /** Machine-generated, made final. */
    final class IvjEventHandler implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() == getPass1Button()) {
                connEtoC1(e);
            }
            if (e.getSource() == getPass2Button()) {
                connEtoC2(e);
            }
            if (e.getSource() == getPass3Button()) {
                connEtoC3(e);
            }
            if (e.getSource() == getFlushButton()) {
                connEtoC4(e);
            }
        }
    }

    private static final long serialVersionUID = -6374807677043142313L;

    /**
     * This field is here to count the number of open VerifyDialog instances so the JVM can be exited afer every Dialog had
     * been closed.
     */
    private static int classesToVerify;

    /**
     * Verifies one or more class files. Verification results are presented graphically: Red means 'rejected', green means
     * 'passed' while yellow means 'could not be verified yet'.
     *
     * @param args String[] fully qualified names of classes to verify.
     */
    public static void main(final String[] args) {
        classesToVerify = args.length;
        for (final String arg : args) {
            try {
                final VerifyDialog aVerifyDialog;
                aVerifyDialog = new VerifyDialog(arg);
                aVerifyDialog.setModal(true);
                aVerifyDialog.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(final WindowEvent e) {
                        classesToVerify--;
                        if (classesToVerify == 0) {
                            System.exit(0);
                        }
                    }
                });
                aVerifyDialog.setVisible(true);
            } catch (final Throwable exception) {
                System.err.println("Exception occurred in main() of JDialog");
                exception.printStackTrace(System.out);
            }
        }
    }

    /** Machine-generated. */
    private JPanel ivjJDialogContentPane;
    /** Machine-generated. */
    private JPanel ivjPass1Panel;
    /** Machine-generated. */
    private JPanel ivjPass2Panel;
    /** Machine-generated. */
    private JPanel ivjPass3Panel;
    /** Machine-generated. */
    private JButton ivjPass1Button;
    /** Machine-generated. */
    private JButton ivjPass2Button;
    /** Machine-generated. */
    private JButton ivjPass3Button;

    /** Machine-generated. */
    private final IvjEventHandler ivjEventHandler = new IvjEventHandler();

    /**
     * The class to verify. Default set to 'java.lang.Object' in case this class is instantiated via one of the many
     * machine-generated constructors.
     */
    private String className = "java.lang.Object";

    /** Machine-generated. */
    private JButton ivjFlushButton;

    /** Machine-generated. */
    public VerifyDialog() {
        initialize();
    }

    /** Machine-generated. */
    public VerifyDialog(final Dialog owner) {
        super(owner);
    }

    /** Machine-generated. */
    public VerifyDialog(final Dialog owner, final boolean modal) {
        super(owner, modal);
    }

    /** Machine-generated. */
    public VerifyDialog(final Dialog owner, final String title) {
        super(owner, title);
    }

    /** Machine-generated. */
    public VerifyDialog(final Dialog owner, final String title, final boolean modal) {
        super(owner, title, modal);
    }

    /** Machine-generated. */
    public VerifyDialog(final Frame owner) {
        super(owner);
    }

    /** Machine-generated. */
    public VerifyDialog(final Frame owner, final boolean modal) {
        super(owner, modal);
    }

    /** Machine-generated. */
    public VerifyDialog(final Frame owner, final String title) {
        super(owner, title);
    }

    /** Machine-generated. */
    public VerifyDialog(final Frame owner, final String title, final boolean modal) {
        super(owner, title, modal);
    }

    /**
     * Use this constructor if you want a possibility to verify other class files than {@link Object}.
     *
     * @param fullyQualifiedClassName "java.lang.String"
     */
    public VerifyDialog(String fullyQualifiedClassName) {
        final int dotclasspos = fullyQualifiedClassName.lastIndexOf(JavaClass.EXTENSION);
        if (dotclasspos != -1) {
            fullyQualifiedClassName = fullyQualifiedClassName.substring(0, dotclasspos);
        }
        fullyQualifiedClassName = Utility.pathToPackage(fullyQualifiedClassName);
        this.className = fullyQualifiedClassName;
        initialize();
    }

    /** Machine-generated. */
    private void connEtoC1(final ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            pass1Button_ActionPerformed(arg1);
            // user code begin {2}
            // user code end
        } catch (final Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /** Machine-generated. */
    private void connEtoC2(final ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            pass2Button_ActionPerformed(arg1);
            // user code begin {2}
            // user code end
        } catch (final Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /** Machine-generated. */
    private void connEtoC3(final ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            pass4Button_ActionPerformed(arg1);
            // user code begin {2}
            // user code end
        } catch (final Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /** Machine-generated. */
    private void connEtoC4(final ActionEvent arg1) {
        try {
            // user code begin {1}
            // user code end
            flushButton_ActionPerformed(arg1);
            // user code begin {2}
            // user code end
        } catch (final Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }

    /** Machine-generated. */
    public void flushButton_ActionPerformed(final ActionEvent actionEvent) {
        VerifierFactory.getVerifier(className).flush();
        Repository.removeClass(className); // Make sure it will be reloaded.
        getPass1Panel().setBackground(Color.gray);
        getPass1Panel().repaint();
        getPass2Panel().setBackground(Color.gray);
        getPass2Panel().repaint();
        getPass3Panel().setBackground(Color.gray);
        getPass3Panel().repaint();
    }

    /** Machine-generated. */
    private JButton getFlushButton() {
        if (ivjFlushButton == null) {
            try {
                ivjFlushButton = new JButton();
                ivjFlushButton.setName("FlushButton");
                ivjFlushButton.setText("Flush: Forget old verification results");
                ivjFlushButton.setBackground(SystemColor.controlHighlight);
                ivjFlushButton.setBounds(60, 215, 300, 30);
                ivjFlushButton.setForeground(Color.red);
                ivjFlushButton.setActionCommand("FlushButton");
                // user code begin {1}
                // user code end
            } catch (final Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjFlushButton;
    }

    /** Machine-generated. */
    private JPanel getJDialogContentPane() {
        if (ivjJDialogContentPane == null) {
            try {
                ivjJDialogContentPane = new JPanel();
                ivjJDialogContentPane.setName("JDialogContentPane");
                ivjJDialogContentPane.setLayout(null);
                getJDialogContentPane().add(getPass1Panel(), getPass1Panel().getName());
                getJDialogContentPane().add(getPass3Panel(), getPass3Panel().getName());
                getJDialogContentPane().add(getPass2Panel(), getPass2Panel().getName());
                getJDialogContentPane().add(getPass1Button(), getPass1Button().getName());
                getJDialogContentPane().add(getPass2Button(), getPass2Button().getName());
                getJDialogContentPane().add(getPass3Button(), getPass3Button().getName());
                getJDialogContentPane().add(getFlushButton(), getFlushButton().getName());
                // user code begin {1}
                // user code end
            } catch (final Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    /** Machine-generated. */
    private JButton getPass1Button() {
        if (ivjPass1Button == null) {
            try {
                ivjPass1Button = new JButton();
                ivjPass1Button.setName("Pass1Button");
                ivjPass1Button.setText("Pass1: Verify binary layout of .class file");
                ivjPass1Button.setBackground(SystemColor.controlHighlight);
                ivjPass1Button.setBounds(100, 40, 300, 30);
                ivjPass1Button.setActionCommand("Button1");
                // user code begin {1}
                // user code end
            } catch (final Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPass1Button;
    }

    /** Machine-generated. */
    private JPanel getPass1Panel() {
        if (ivjPass1Panel == null) {
            try {
                ivjPass1Panel = new JPanel();
                ivjPass1Panel.setName("Pass1Panel");
                ivjPass1Panel.setLayout(null);
                ivjPass1Panel.setBackground(SystemColor.controlShadow);
                ivjPass1Panel.setBounds(30, 30, 50, 50);
                // user code begin {1}
                // user code end
            } catch (final Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPass1Panel;
    }

    /** Machine-generated. */
    private JButton getPass2Button() {
        if (ivjPass2Button == null) {
            try {
                ivjPass2Button = new JButton();
                ivjPass2Button.setName("Pass2Button");
                ivjPass2Button.setText("Pass 2: Verify static .class file constraints");
                ivjPass2Button.setBackground(SystemColor.controlHighlight);
                ivjPass2Button.setBounds(100, 100, 300, 30);
                ivjPass2Button.setActionCommand("Button2");
                // user code begin {1}
                // user code end
            } catch (final Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPass2Button;
    }

    /** Machine-generated. */
    private JPanel getPass2Panel() {
        if (ivjPass2Panel == null) {
            try {
                ivjPass2Panel = new JPanel();
                ivjPass2Panel.setName("Pass2Panel");
                ivjPass2Panel.setLayout(null);
                ivjPass2Panel.setBackground(SystemColor.controlShadow);
                ivjPass2Panel.setBounds(30, 90, 50, 50);
                // user code begin {1}
                // user code end
            } catch (final Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPass2Panel;
    }

    /** Machine-generated. */
    private JButton getPass3Button() {
        if (ivjPass3Button == null) {
            try {
                ivjPass3Button = new JButton();
                ivjPass3Button.setName("Pass3Button");
                ivjPass3Button.setText("Passes 3a+3b: Verify code arrays");
                ivjPass3Button.setBackground(SystemColor.controlHighlight);
                ivjPass3Button.setBounds(100, 160, 300, 30);
                ivjPass3Button.setActionCommand("Button2");
                // user code begin {1}
                // user code end
            } catch (final Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPass3Button;
    }

    /** Machine-generated. */
    private JPanel getPass3Panel() {
        if (ivjPass3Panel == null) {
            try {
                ivjPass3Panel = new JPanel();
                ivjPass3Panel.setName("Pass3Panel");
                ivjPass3Panel.setLayout(null);
                ivjPass3Panel.setBackground(SystemColor.controlShadow);
                ivjPass3Panel.setBounds(30, 150, 50, 50);
                // user code begin {1}
                // user code end
            } catch (final Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjPass3Panel;
    }

    /** Machine-generated. */
    private void handleException(final Throwable exception) {
        /* Uncomment the following lines to print uncaught exceptions to stdout */
        System.out.println("--------- UNCAUGHT EXCEPTION ---------");
        exception.printStackTrace(System.out);
        // manually added code
        if (exception instanceof ThreadDeath) {
            throw (ThreadDeath) exception;
        }
        if (exception instanceof VirtualMachineError) {
            throw (VirtualMachineError) exception;
        }
    }

    /** Machine-generated. */
    private void initConnections() {
        // user code begin {1}
        // user code end
        getPass1Button().addActionListener(ivjEventHandler);
        getPass2Button().addActionListener(ivjEventHandler);
        getPass3Button().addActionListener(ivjEventHandler);
        getFlushButton().addActionListener(ivjEventHandler);
    }

    /** Machine-generated. */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("VerifyDialog");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(430, 280);
            setVisible(true);
            setModal(true);
            setResizable(false);
            setContentPane(getJDialogContentPane());
            initConnections();
        } catch (final Throwable ivjExc) {
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle("'" + className + "' verification - JustIce / BCEL");
        // user code end
    }

    /** Machine-generated. */
    public void pass1Button_ActionPerformed(final ActionEvent actionEvent) {
        final Verifier v = VerifierFactory.getVerifier(className);
        final VerificationResult vr = v.doPass1();
        if (vr.getStatus() == VerificationResult.VERIFIED_OK) {
            getPass1Panel().setBackground(Color.green);
            getPass1Panel().repaint();
        }
        if (vr.getStatus() == VerificationResult.VERIFIED_REJECTED) {
            getPass1Panel().setBackground(Color.red);
            getPass1Panel().repaint();
        }
    }

    /** Machine-generated. */
    public void pass2Button_ActionPerformed(final ActionEvent actionEvent) {
        pass1Button_ActionPerformed(actionEvent);
        final Verifier v = VerifierFactory.getVerifier(className);
        final VerificationResult vr = v.doPass2();
        if (vr.getStatus() == VerificationResult.VERIFIED_OK) {
            getPass2Panel().setBackground(Color.green);
            getPass2Panel().repaint();
        }
        if (vr.getStatus() == VerificationResult.VERIFIED_NOTYET) {
            getPass2Panel().setBackground(Color.yellow);
            getPass2Panel().repaint();
        }
        if (vr.getStatus() == VerificationResult.VERIFIED_REJECTED) {
            getPass2Panel().setBackground(Color.red);
            getPass2Panel().repaint();
        }
    }

    /** Machine-generated. */
    public void pass4Button_ActionPerformed(final ActionEvent actionEvent) {
        pass2Button_ActionPerformed(actionEvent);
        Color color = Color.green;
        final Verifier v = VerifierFactory.getVerifier(className);
        VerificationResult vr = v.doPass2();
        if (vr.getStatus() == VerificationResult.VERIFIED_OK) {
            JavaClass jc = null;
            try {
                jc = Repository.lookupClass(className);
                final int nr = jc.getMethods().length;
                for (int i = 0; i < nr; i++) {
                    vr = v.doPass3b(i);
                    if (vr.getStatus() != VerificationResult.VERIFIED_OK) {
                        color = Color.red;
                        break;
                    }
                }
            } catch (final ClassNotFoundException ex) {
                // FIXME: report the error
                ex.printStackTrace();
            }
        } else {
            color = Color.yellow;
        }
        getPass3Panel().setBackground(color);
        getPass3Panel().repaint();
    }
}
