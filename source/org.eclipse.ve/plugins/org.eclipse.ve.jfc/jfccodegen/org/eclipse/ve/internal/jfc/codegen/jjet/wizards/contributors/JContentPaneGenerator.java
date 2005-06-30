/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
  protected final String TEXT_1 = "import javax.swing.JPanel;" + NL + "import java.awt.BorderLayout;" + NL + "" + NL + "public class ";
  protected final String TEXT_2 = " {" + NL + "" + NL + "\tprivate JPanel jContentPane = null;" + NL + "\t" + NL + "\t/**" + NL + "\t * This is the default constructor" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_3 = "() {" + NL + "\t\tsuper(); ";
  protected final String TEXT_4 = "initialize();";
  protected final String TEXT_5 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * This method initializes this" + NL + "\t * " + NL + "\t * @return void" + NL + "\t */" + NL + "\t";
  protected final String TEXT_6 = "public ";
  protected final String TEXT_7 = "private ";
  protected final String TEXT_8 = "void ";
  protected final String TEXT_9 = "init()";
  protected final String TEXT_10 = "initialize()";
  protected final String TEXT_11 = " {" + NL + "\t\tthis.setSize(300,200);" + NL + "\t\tthis.setContentPane(getJContentPane());";
  protected final String TEXT_12 = NL + "\t\tthis.setTitle(\"";
  protected final String TEXT_13 = "\");";
  protected final String TEXT_14 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * This method initializes jContentPane" + NL + "\t * " + NL + "\t * @return javax.swing.JPanel" + NL + "\t */" + NL + "\tprivate JPanel getJContentPane() {" + NL + "\t\tif(jContentPane == null) {" + NL + "\t\t\tjContentPane = new JPanel();" + NL + "\t\t\tjContentPane.setLayout(new BorderLayout());" + NL + "\t\t}" + NL + "\t\treturn jContentPane;" + NL + "\t}" + NL + "}";

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
    
			if(!"javax.swing.JApplet".equals(superClassName)){
				
    stringBuffer.append(TEXT_4);
    
		}
    stringBuffer.append(TEXT_5);
    
		if("javax.swing.JApplet".equals(superClassName)){
			
    stringBuffer.append(TEXT_6);
    
		}else{
			
    stringBuffer.append(TEXT_7);
    
		}
    stringBuffer.append(TEXT_8);
    
			if("javax.swing.JApplet".equals(superClassName)){
				
    stringBuffer.append(TEXT_9);
    
			}else{
				
    stringBuffer.append(TEXT_10);
    
		}
    stringBuffer.append(TEXT_11);
    
	if ("javax.swing.JFrame".equals(superClassName)) {
    stringBuffer.append(TEXT_12);
    stringBuffer.append(superClassName.substring(superClassName.lastIndexOf(".")+1));
    stringBuffer.append(TEXT_13);
    
	}
    stringBuffer.append(TEXT_14);
    return stringBuffer.toString();
  }
}
