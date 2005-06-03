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
package org.eclipse.ve.internal.swt.codegen.jjet.wizards.contributors;

import java.util.HashMap;


public class RCPEditorSourceGenerator implements org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceGenerator {

  protected static String nl;
  public static synchronized RCPEditorSourceGenerator create(String lineSeparator)
  {
    nl = lineSeparator;
    RCPEditorSourceGenerator result = new RCPEditorSourceGenerator();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "import org.eclipse.ui.part.EditorPart;" + NL + "import org.eclipse.swt.widgets.Composite;" + NL + "import org.eclipse.swt.SWT;" + NL + "" + NL + "public class ";
  protected final String TEXT_2 = " extends EditorPart{" + NL + "" + NL + "\tpublic static final String ID = \"";
  protected final String TEXT_3 = "\"; // TODO Needs to be whatever is mentioned in plugin.xml" + NL + "\t" + NL + "\tprivate Composite top = null;" + NL + "\t" + NL + "\tpublic void createPartControl(Composite parent) {" + NL + "\t\ttop = new Composite(parent, SWT.NONE);\t\t   " + NL + "\t}" + NL + "}";

public String generateSource(String typeName, String superClassName, HashMap argumentMatrix)
  {
    StringBuffer stringBuffer = new StringBuffer();
    
	String packageName = argumentMatrix != null ? (String)argumentMatrix.get(TARGET_PACKAGE_NAME) : "";
	if(packageName==null){
		packageName="";
	}else{
		packageName = packageName.trim();
		if(packageName.length()>0)
			packageName += ".";
	}
	
    stringBuffer.append(TEXT_1);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(packageName);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}
