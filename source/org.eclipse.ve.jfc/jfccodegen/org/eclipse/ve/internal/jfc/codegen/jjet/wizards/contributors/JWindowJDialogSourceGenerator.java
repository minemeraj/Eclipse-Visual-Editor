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


public class JWindowJDialogSourceGenerator implements org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceGenerator {

  protected static String nl;
  public static synchronized JWindowJDialogSourceGenerator create(String lineSeparator)
  {
    nl = lineSeparator;
    JWindowJDialogSourceGenerator result = new JWindowJDialogSourceGenerator();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "import java.awt.BorderLayout;" + NL + "import java.awt.Frame;" + NL + "" + NL + "import javax.swing.JPanel;" + NL + "" + NL + "public class ";
  protected final String TEXT_2 = " {" + NL + "" + NL + "\tprivate static final long serialVersionUID = 1L;" + NL + "\t" + NL + "\tprivate JPanel jContentPane = null;" + NL + "\t" + NL + "\t/**" + NL + "\t * @param owner" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_3 = "(Frame owner) {" + NL + "\t\tsuper(owner);" + NL + "\t\tinitialize();" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * This method initializes this" + NL + "\t * " + NL + "\t * @return void" + NL + "\t */" + NL + "\tprivate void initialize(){" + NL + "\t\tthis.setSize(300,200);" + NL + "\t\tthis.setContentPane(getJContentPane());" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * This method initializes jContentPane" + NL + "\t * " + NL + "\t * @return javax.swing.JPanel" + NL + "\t */" + NL + "\tprivate JPanel getJContentPane() {" + NL + "\t\tif(jContentPane == null) {" + NL + "\t\t\tjContentPane = new JPanel();" + NL + "\t\t\tjContentPane.setLayout(new BorderLayout());" + NL + "\t\t}" + NL + "\t\treturn jContentPane;" + NL + "\t}" + NL + "}";

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
    return stringBuffer.toString();
  }
}
