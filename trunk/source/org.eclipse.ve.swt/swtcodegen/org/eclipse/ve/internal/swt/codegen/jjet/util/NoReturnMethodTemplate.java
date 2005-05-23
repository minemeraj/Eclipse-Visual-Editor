/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


package org.eclipse.ve.internal.swt.codegen.jjet.util;

import org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator;
import org.eclipse.ve.internal.java.codegen.util.IMethodTemplate;

/*********************************************************************
 * This class was generated automatically from a javajet template.
 * !!!!!!!!!!!!!!! DO NOT MAKE CHANGES TO THIS CLASS !!!!!!!!!!!!!!!!!
 * 
 *********************************************************************/
public class NoReturnMethodTemplate implements IMethodTemplate {
	
  protected final String NL = System.getProperties().getProperty("line.separator");
  protected final String TEXT_1 = "\t/**";
  protected final String TEXT_2 = NL + "\t * ";
  protected final String TEXT_3 = "\t";
  protected final String TEXT_4 = NL + "\t *" + NL + "\t */    " + NL + "\tprivate void ";
  protected final String TEXT_5 = "() {" + NL + "\t\t";
  protected final String TEXT_6 = " = ";
  protected final String TEXT_7 = ";\t\t   " + NL + "\t}";
  protected final String TEXT_8 = NL + " ";

 public String generateMethod(AbstractMethodTextGenerator.MethodInfo info)
  {
    StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a method skelaton with no returns and no arguments
 // see AbstractMethodTextGenerator.MethodInfo for more information regarding the info structure
  

    stringBuffer.append(TEXT_1);
    // Generate method's JavaDoc/Comments
	for(int i=0; i<info.fComments.length; i++) { 
    stringBuffer.append(TEXT_2);
    stringBuffer.append(info.fComments[i]);
    stringBuffer.append(TEXT_3);
    }

    stringBuffer.append(TEXT_4);
    stringBuffer.append(info.fmethodName);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(info.finitBeanName);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(info.finitbeanInitString);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(TEXT_8);
    return stringBuffer.toString();
  }
}