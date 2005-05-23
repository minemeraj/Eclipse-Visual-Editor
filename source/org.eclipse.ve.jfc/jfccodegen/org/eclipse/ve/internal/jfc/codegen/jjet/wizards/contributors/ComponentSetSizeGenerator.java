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


public class ComponentSetSizeGenerator implements org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceGenerator {

  protected final String NL = System.getProperties().getProperty("line.separator");
  protected final String TEXT_1 = NL + "public class ";
  protected final String TEXT_2 = " {" + NL + "" + NL + "\t/**" + NL + "\t * This is the default constructor" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_3 = "() {" + NL + "\t\tsuper();" + NL + "\t\t";
  protected final String TEXT_4 = "init()";
  protected final String TEXT_5 = "initialize()";
  protected final String TEXT_6 = ";" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * This method initializes this" + NL + "\t * " + NL + "\t * @return void" + NL + "\t */" + NL + "\t";
  protected final String TEXT_7 = "public ";
  protected final String TEXT_8 = "private ";
  protected final String TEXT_9 = " void ";
  protected final String TEXT_10 = "init()";
  protected final String TEXT_11 = "initialize()";
  protected final String TEXT_12 = " {" + NL + "\t\tthis.setSize(300,200);";
  protected final String TEXT_13 = NL + "\t\tthis.setTitle(\"";
  protected final String TEXT_14 = "\");";
  protected final String TEXT_15 = NL + "\t}" + NL + "}";

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
    
			if("java.applet.Applet".equals(superClassName)){
				
    stringBuffer.append(TEXT_4);
    
			}else{
				
    stringBuffer.append(TEXT_5);
    
		}
    stringBuffer.append(TEXT_6);
    
		if("java.applet.Applet".equals(superClassName)){
			
    stringBuffer.append(TEXT_7);
    
		}else{
			
    stringBuffer.append(TEXT_8);
    
		}
    stringBuffer.append(TEXT_9);
    
			if("java.applet.Applet".equals(superClassName)){
				
    stringBuffer.append(TEXT_10);
    
			}else{
				
    stringBuffer.append(TEXT_11);
    
		}
    stringBuffer.append(TEXT_12);
    
	if ("java.awt.Frame".equals(superClassName)) {
    stringBuffer.append(TEXT_13);
    stringBuffer.append(superClassName.substring(superClassName.lastIndexOf(".")+1));
    stringBuffer.append(TEXT_14);
    
	}
    stringBuffer.append(TEXT_15);
    return stringBuffer.toString();
  }
}
