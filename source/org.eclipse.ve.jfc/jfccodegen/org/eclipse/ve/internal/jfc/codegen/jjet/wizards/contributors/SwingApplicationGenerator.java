/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.codegen.jjet.wizards.contributors;

import java.util.HashMap;


public class SwingApplicationGenerator implements org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceGenerator {

  protected static String nl;
  public static synchronized SwingApplicationGenerator create(String lineSeparator)
  {
    nl = lineSeparator;
    SwingApplicationGenerator result = new SwingApplicationGenerator();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "import javax.swing.JDialog;" + NL + "import javax.swing.JFrame;" + NL + "import javax.swing.JMenu;" + NL + "import javax.swing.JMenuBar;" + NL + "import javax.swing.JMenuItem;" + NL + "import javax.swing.JPanel;" + NL + "import javax.swing.KeyStroke;" + NL + "" + NL + "import java.awt.BorderLayout;" + NL + "import java.awt.Event;" + NL + "import java.awt.event.ActionEvent;" + NL + "import java.awt.event.ActionListener;" + NL + "import java.awt.event.KeyEvent;" + NL + "" + NL + "public class ";
  protected final String TEXT_2 = " {" + NL + "" + NL + "\tprivate JPanel jContentPane = null;" + NL + "\tprivate JMenuBar jJMenuBar = null;" + NL + "\tprivate JMenu fileMenu = null;" + NL + "\tprivate JMenu editMenu = null;" + NL + "\tprivate JMenu helpMenu = null;" + NL + "\tprivate JMenuItem exitMenuItem = null;" + NL + "\tprivate JMenuItem aboutMenuItem = null;" + NL + "\tprivate JMenuItem cutMenuItem = null;" + NL + "\tprivate JMenuItem copyMenuItem = null;" + NL + "\tprivate JMenuItem pasteMenuItem = null;" + NL + "\tprivate JMenuItem saveMenuItem = null;" + NL + "" + NL + "\t/**" + NL + "\t * This is the default constructor" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_3 = "() {" + NL + "\t\tsuper();" + NL + "\t\tinitialize();" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * This method initializes this" + NL + "\t * " + NL + "\t * @return void" + NL + "\t */" + NL + "\tprivate void initialize() {" + NL + "\t\tthis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);" + NL + "\t\tthis.setJMenuBar(getJJMenuBar());" + NL + "\t\tthis.setSize(300,200);" + NL + "\t\tthis.setContentPane(getJContentPane());" + NL + "\t\tthis.setTitle(\"Application\");" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jContentPane" + NL + "\t * " + NL + "\t * @return javax.swing.JPanel" + NL + "\t */" + NL + "\tprivate JPanel getJContentPane() {" + NL + "\t\tif(jContentPane == null) {" + NL + "\t\t\tjContentPane = new JPanel();" + NL + "\t\t\tjContentPane.setLayout(new BorderLayout());" + NL + "\t\t}" + NL + "\t\treturn jContentPane;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jJMenuBar\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenuBar\t" + NL + "\t */    " + NL + "\tprivate JMenuBar getJJMenuBar() {" + NL + "\t\tif (jJMenuBar == null) {" + NL + "\t\t\tjJMenuBar = new JMenuBar();" + NL + "\t\t\tjJMenuBar.add(getFileMenu());" + NL + "\t\t\tjJMenuBar.add(getEditMenu());" + NL + "\t\t\tjJMenuBar.add(getHelpMenu());" + NL + "\t\t}" + NL + "\t\treturn jJMenuBar;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenu\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenu\t" + NL + "\t */    " + NL + "\tprivate JMenu getFileMenu() {" + NL + "\t\tif (fileMenu == null) {" + NL + "\t\t\tfileMenu = new JMenu();" + NL + "\t\t\tfileMenu.setText(\"File\");" + NL + "\t\t\tfileMenu.add(getSaveMenuItem());" + NL + "\t\t\tfileMenu.add(getExitMenuItem());" + NL + "\t\t}" + NL + "\t\treturn fileMenu;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenu\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenu\t" + NL + "\t */    " + NL + "\tprivate JMenu getEditMenu() {" + NL + "\t\tif (editMenu == null) {" + NL + "\t\t\teditMenu = new JMenu();" + NL + "\t\t\teditMenu.setText(\"Edit\");" + NL + "\t\t\teditMenu.add(getCutMenuItem());" + NL + "\t\t\teditMenu.add(getCopyMenuItem());" + NL + "\t\t\teditMenu.add(getPasteMenuItem());" + NL + "\t\t}" + NL + "\t\treturn editMenu;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenu\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenu\t" + NL + "\t */    " + NL + "\tprivate JMenu getHelpMenu() {" + NL + "\t\tif (helpMenu == null) {" + NL + "\t\t\thelpMenu = new JMenu();" + NL + "\t\t\thelpMenu.setText(\"Help\");" + NL + "\t\t\thelpMenu.add(getAboutMenuItem());" + NL + "\t\t}" + NL + "\t\treturn helpMenu;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenuItem\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenuItem\t" + NL + "\t */    " + NL + "\tprivate JMenuItem getExitMenuItem() {" + NL + "\t\tif (exitMenuItem == null) {" + NL + "\t\t\texitMenuItem = new JMenuItem();" + NL + "\t\t\texitMenuItem.setText(\"Exit\");" + NL + "\t\t\texitMenuItem.addActionListener(new ActionListener() { " + NL + "\t\t\t\tpublic void actionPerformed(ActionEvent e) {    " + NL + "\t\t\t\t\tSystem.exit(0);" + NL + "\t\t\t\t}" + NL + "\t\t\t});" + NL + "\t\t}" + NL + "\t\treturn exitMenuItem;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenuItem\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenuItem\t" + NL + "\t */    " + NL + "\tprivate JMenuItem getAboutMenuItem() {" + NL + "\t\tif (aboutMenuItem == null) {" + NL + "\t\t\taboutMenuItem = new JMenuItem();" + NL + "\t\t\taboutMenuItem.setText(\"About\");" + NL + "\t\t\taboutMenuItem.addActionListener(new ActionListener() { " + NL + "\t\t\t\tpublic void actionPerformed(ActionEvent e) {    " + NL + "\t\t\t\t\tnew JDialog(";
  protected final String TEXT_4 = ".this, \"About\", true).show();" + NL + "\t\t\t\t}" + NL + "\t\t\t});" + NL + "\t\t}" + NL + "\t\treturn aboutMenuItem;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenuItem\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenuItem\t" + NL + "\t */    " + NL + "\tprivate JMenuItem getCutMenuItem() {" + NL + "\t\tif (cutMenuItem == null) {" + NL + "\t\t\tcutMenuItem = new JMenuItem();" + NL + "\t\t\tcutMenuItem.setText(\"Cut\");" + NL + "\t\t\tcutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, true));" + NL + "\t\t}" + NL + "\t\treturn cutMenuItem;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenuItem\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenuItem\t" + NL + "\t */    " + NL + "\tprivate JMenuItem getCopyMenuItem() {" + NL + "\t\tif (copyMenuItem == null) {" + NL + "\t\t\tcopyMenuItem = new JMenuItem();" + NL + "\t\t\tcopyMenuItem.setText(\"Copy\");" + NL + "\t\t\tcopyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, true));" + NL + "\t\t}" + NL + "\t\treturn copyMenuItem;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenuItem\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenuItem\t" + NL + "\t */    " + NL + "\tprivate JMenuItem getPasteMenuItem() {" + NL + "\t\tif (pasteMenuItem == null) {" + NL + "\t\t\tpasteMenuItem = new JMenuItem();" + NL + "\t\t\tpasteMenuItem.setText(\"Paste\");" + NL + "\t\t\tpasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK, true));" + NL + "\t\t}" + NL + "\t\treturn pasteMenuItem;" + NL + "\t}" + NL + "\t/**" + NL + "\t * This method initializes jMenuItem\t" + NL + "\t * \t" + NL + "\t * @return javax.swing.JMenuItem\t" + NL + "\t */    " + NL + "\tprivate JMenuItem getSaveMenuItem() {" + NL + "\t\tif (saveMenuItem == null) {" + NL + "\t\t\tsaveMenuItem = new JMenuItem();" + NL + "\t\t\tsaveMenuItem.setText(\"Save\");" + NL + "\t\t\tsaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, true));" + NL + "\t\t}" + NL + "\t\treturn saveMenuItem;" + NL + "\t}" + NL + "\t/**" + NL + "\t * Launches this application" + NL + "\t */" + NL + "\tpublic static void main(String[] args) {" + NL + "\t\t";
  protected final String TEXT_5 = " application = new ";
  protected final String TEXT_6 = "();" + NL + "\t\tapplication.show();" + NL + "\t}" + NL + "}";

public String generateSource(String typeName, String superClassName, HashMap argumentMatrix)
  {
    StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */

    stringBuffer.append(TEXT_1);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}
