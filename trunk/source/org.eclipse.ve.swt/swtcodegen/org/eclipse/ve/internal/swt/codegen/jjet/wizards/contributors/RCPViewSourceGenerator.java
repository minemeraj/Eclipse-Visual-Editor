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


public class RCPViewSourceGenerator implements org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceGenerator {

  protected final String NL = System.getProperties().getProperty("line.separator");
  protected final String TEXT_1 = "import org.eclipse.ui.part.ViewPart;" + NL + "import org.eclipse.swt.widgets.Composite;" + NL + "import org.eclipse.swt.SWT;" + NL + "" + NL + "public class ";
  protected final String TEXT_2 = " extends ViewPart {" + NL + "" + NL + "\tpublic static final String ID = \"";
  protected final String TEXT_3 = ".";
  protected final String TEXT_4 = "\"; // TODO Needs to be whatever is mentioned in plugin.xml" + NL + "\t" + NL + "\tprivate Composite top = null;" + NL + "\t" + NL + "\tpublic void createPartControl(Composite parent) {" + NL + "\t\ttop = new Composite(parent, SWT.NONE);\t\t   " + NL + "\t}" + NL + "}";

public String generateSource(String typeName, String superClassName, HashMap argumentMatrix)
  {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(superClassName);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}
