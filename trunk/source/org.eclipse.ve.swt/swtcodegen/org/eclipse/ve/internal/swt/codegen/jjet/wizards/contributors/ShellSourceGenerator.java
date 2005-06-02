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


public class ShellSourceGenerator implements org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceGenerator {

  protected static String nl;
  public static synchronized ShellSourceGenerator create(String lineSeparator)
  {
    nl = lineSeparator;
    ShellSourceGenerator result = new ShellSourceGenerator();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "import org.eclipse.swt.widgets.Display;";
  protected final String TEXT_3 = NL + "import org.eclipse.swt.widgets.Shell;" + NL + "import org.eclipse.swt.graphics.Point;" + NL + "" + NL + "public class ";
  protected final String TEXT_4 = " {" + NL + "" + NL + "\tprivate Shell sShell = null;" + NL + "\t" + NL + "\t/**" + NL + "\t * This method initializes sShell" + NL + "\t */" + NL + "\tprivate void createSShell() {" + NL + "\t\tsShell = new Shell();\t\t   " + NL + "\t\tsShell.setText(\"";
  protected final String TEXT_5 = "\");" + NL + "\t\tsShell.setSize(new Point(300,200));" + NL + "\t}" + NL + "}";
  protected final String TEXT_6 = NL + "\tpublic static void main(String[] args) {" + NL + "\t\t/* Before this is run, be sure to set up the launch configuration (Arguments->VM Arguments)" + NL + "\t\t * for the correct SWT library path in order to run with the SWT dlls. " + NL + "\t\t * The dlls are located in the SWT plugin jar.  " + NL + "\t\t * For example, on Windows the Eclipse SWT 3.1 plugin jar is:" + NL + "\t\t *       installation_directory\\plugins\\org.eclipse.swt.win32_3.1.0.jar" + NL + "\t\t */" + NL + "\t\tDisplay display = Display.getDefault();\t\t" + NL + "\t\t";
  protected final String TEXT_7 = " thisClass = new ";
  protected final String TEXT_8 = "();" + NL + "\t\tthisClass.createSShell() ;" + NL + "\t\tthisClass.sShell.open();" + NL + "\t\t" + NL + "\t\twhile (!thisClass.sShell.isDisposed()) {" + NL + "\t\t\tif (!display.readAndDispatch()) display.sleep ();" + NL + "\t\t}" + NL + "\t\tdisplay.dispose();\t\t" + NL + "\t}";
  protected final String TEXT_9 = NL;

public String generateSource(String typeName, String superClassName, HashMap argumentMatrix)
  {
    StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */

    
	boolean createMain = (argumentMatrix != null && ((String)argumentMatrix.get(CREATE_MAIN)).equals("true"));
	 
    stringBuffer.append(TEXT_1);
    if(createMain){
    stringBuffer.append(TEXT_2);
    }
    stringBuffer.append(TEXT_3);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(org.eclipse.swt.widgets.Shell.class.getName().substring(org.eclipse.swt.widgets.Shell.class.getName().lastIndexOf(".")+1));
    stringBuffer.append(TEXT_5);
     	
	if (createMain) {
    stringBuffer.append(TEXT_6);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_8);
    
	}
    stringBuffer.append(TEXT_9);
    return stringBuffer.toString();
  }
}
