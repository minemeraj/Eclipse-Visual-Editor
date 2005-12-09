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


public class JContentPaneGenerator implements org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceGenerator {

  protected static String nl;
  public static synchronized JContentPaneGenerator create(String lineSeparator)
  {
    nl = lineSeparator;
    JContentPaneGenerator result = new JContentPaneGenerator();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t";
  protected final String TEXT_2 = NL + "import javax.swing.JPanel;" + NL + "import java.awt.BorderLayout;" + NL + "\t";
  protected final String TEXT_3 = NL + "import javax.swing.SwingUtilities;" + NL + "\t";
  protected final String TEXT_4 = NL + NL + NL + "public class ";
  protected final String TEXT_5 = " {" + NL + "" + NL + "\tprivate static final long serialVersionUID = 1L;" + NL + "\t" + NL + "\tprivate JPanel jContentPane = null;" + NL + "\t" + NL + "\t/**" + NL + "\t * This is the default constructor" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_6 = "() {" + NL + "\t\tsuper();" + NL + "\t\tinitialize();" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * This method initializes this" + NL + "\t * " + NL + "\t * @return void" + NL + "\t */" + NL + "\tprivate void initialize() {" + NL + "\t\tthis.setSize(300,200);" + NL + "\t\tthis.setContentPane(getJContentPane());";
  protected final String TEXT_7 = NL + "\t\tthis.setTitle(\"";
  protected final String TEXT_8 = "\");";
  protected final String TEXT_9 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * This method initializes jContentPane" + NL + "\t * " + NL + "\t * @return javax.swing.JPanel" + NL + "\t */" + NL + "\tprivate JPanel getJContentPane() {" + NL + "\t\tif(jContentPane == null) {" + NL + "\t\t\tjContentPane = new JPanel();" + NL + "\t\t\tjContentPane.setLayout(new BorderLayout());" + NL + "\t\t}" + NL + "\t\treturn jContentPane;" + NL + "\t}\t";
  protected final String TEXT_10 = NL + "\t" + NL + "\tpublic static void main(String[] args) {" + NL + "\t\tSwingUtilities.invokeLater(new Runnable() {" + NL + "\t\t\tpublic void run() {" + NL + "\t\t\t\t";
  protected final String TEXT_11 = " thisClass = new ";
  protected final String TEXT_12 = "();" + NL + "\t\t\t\tthisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);" + NL + "\t\t\t\tthisClass.setVisible(true);" + NL + "\t\t\t}" + NL + "\t\t});" + NL + "\t}";
  protected final String TEXT_13 = "\t" + NL + "}" + NL;
  protected final String TEXT_14 = NL;

public String generateSource(String typeName, String superClassName, HashMap argumentMatrix)
  {
    StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */

    stringBuffer.append(TEXT_1);
    
	boolean createMain = (argumentMatrix != null && ((String)argumentMatrix.get(CREATE_MAIN)).equals("true"));	

    stringBuffer.append(TEXT_2);
    if(createMain){
    stringBuffer.append(TEXT_3);
    }
    stringBuffer.append(TEXT_4);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_6);
    
	if ("javax.swing.JFrame".equals(superClassName)) {
    stringBuffer.append(TEXT_7);
    stringBuffer.append(superClassName.substring(superClassName.lastIndexOf(".")+1));
    stringBuffer.append(TEXT_8);
    
	}
    stringBuffer.append(TEXT_9);
    
	if (createMain) {
    stringBuffer.append(TEXT_10);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_12);
    
	}
    stringBuffer.append(TEXT_13);
    stringBuffer.append(TEXT_14);
    return stringBuffer.toString();
  }
}
