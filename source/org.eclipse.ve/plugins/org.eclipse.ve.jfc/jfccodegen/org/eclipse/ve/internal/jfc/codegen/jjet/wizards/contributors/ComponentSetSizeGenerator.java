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


public class ComponentSetSizeGenerator implements org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceGenerator {

  protected static String nl;
  public static synchronized ComponentSetSizeGenerator create(String lineSeparator)
  {
    nl = lineSeparator;
    ComponentSetSizeGenerator result = new ComponentSetSizeGenerator();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "import ";
  protected final String TEXT_2 = ";" + NL + "" + NL + "public class ";
  protected final String TEXT_3 = " {" + NL + "" + NL + "\tprivate static final long serialVersionUID = 1L;" + NL + "" + NL + "\t/**" + NL + "\t * This is the default constructor" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_4 = "() {" + NL + "\t\tsuper(); \t" + NL + "\t";
  protected final String TEXT_5 = "\tinitialize();" + NL + "\t";
  protected final String TEXT_6 = "}" + NL + "" + NL + "\t" + NL + "\t/**" + NL + "\t * This method initializes this" + NL + "\t * " + NL + "\t * @return void" + NL + "\t */" + NL + "\t";
  protected final String TEXT_7 = "public ";
  protected final String TEXT_8 = "private ";
  protected final String TEXT_9 = " void ";
  protected final String TEXT_10 = "init()";
  protected final String TEXT_11 = "initialize()";
  protected final String TEXT_12 = " {" + NL + "\t\tthis.setSize(300,200);";
  protected final String TEXT_13 = NL + "\t\tthis.setTitle(\"";
  protected final String TEXT_14 = "\");";
  protected final String TEXT_15 = NL + "\t";
  protected final String TEXT_16 = "this.setLayout(null);";
  protected final String TEXT_17 = "this.setLayout(new ";
  protected final String TEXT_18 = "());";
  protected final String TEXT_19 = NL + "\t}" + NL + "}";

public String generateSource(String typeName, String superClassName, HashMap argumentMatrix)
  {
    StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */

    
	String layout = org.eclipse.ve.internal.jfc.core.JFCVisualPlugin.getPlugin().getPluginPreferences().getString(org.eclipse.ve.internal.jfc.core.JFCVisualPlugin.DEFAULT_LAYOUTMANAGER);
	boolean isNullLayout = layout.equals(org.eclipse.ve.internal.jfc.core.JFCVisualPlugin.NULL_LAYOUT);
	String layoutName = "null";
	if (!isNullLayout)
		layoutName = layout.substring(layout.lastIndexOf('.')+1, layout.length());

    if("java.awt.Panel".equals(superClassName) || "javax.swing.JPanel".equals(superClassName))
    stringBuffer.append(TEXT_1);
    stringBuffer.append(layout);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_4);
    		if(!"java.applet.Applet".equals(superClassName)){
	
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
    
	if("java.awt.Panel".equals(superClassName) || "javax.swing.JPanel".equals(superClassName)) {
		if (isNullLayout){
		
    stringBuffer.append(TEXT_16);
    
		} else {
		
    stringBuffer.append(TEXT_17);
    stringBuffer.append(layoutName);
    stringBuffer.append(TEXT_18);
    
		}
	}
    stringBuffer.append(TEXT_19);
    return stringBuffer.toString();
  }
}
