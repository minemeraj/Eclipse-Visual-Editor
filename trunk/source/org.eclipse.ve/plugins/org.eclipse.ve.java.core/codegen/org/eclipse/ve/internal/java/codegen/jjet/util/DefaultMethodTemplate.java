/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


package org.eclipse.ve.internal.java.codegen.jjet.util;

import org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator;
import org.eclipse.ve.internal.java.codegen.util.IMethodTemplate;

/*********************************************************************
 * This class was generated automatically from a javajet template.
 * !!!!!!!!!!!!!!! DO NOT MAKE CHANGES TO THIS CLASS !!!!!!!!!!!!!!!!!
 * 
 *********************************************************************/
public class DefaultMethodTemplate implements IMethodTemplate {
	
  protected static String nl;
  public static synchronized DefaultMethodTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    DefaultMethodTemplate result = new DefaultMethodTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t/**";
  protected final String TEXT_2 = NL + "\t * ";
  protected final String TEXT_3 = "\t";
  protected final String TEXT_4 = NL + "\t */    " + NL + "\tprivate ";
  protected final String TEXT_5 = " ";
  protected final String TEXT_6 = "() {" + NL + "\t\tif (";
  protected final String TEXT_7 = " == null) {" + NL + "\t\t\t";
  protected final String TEXT_8 = " = ";
  protected final String TEXT_9 = ";" + NL + "\t\t}" + NL + "\t\treturn ";
  protected final String TEXT_10 = ";" + NL + "\t}";
  protected final String TEXT_11 = NL + " ";

 public String generateMethod(AbstractMethodTextGenerator.MethodInfo info)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a getter-like, lazy initializaton method skelaton 
 // see AbstractMethodTextGenerator.MethodInfo for more information regarding the info structure
  

    stringBuffer.append(TEXT_1);
    // Generate method's JavaDoc/Comments
	for(int i=0; i<info.fComments.length; i++) { 
    stringBuffer.append(TEXT_2);
    stringBuffer.append(info.fComments[i]);
    stringBuffer.append(TEXT_3);
    }

    stringBuffer.append(TEXT_4);
    stringBuffer.append(info.freturnType);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(info.fmethodName);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(info.finitBeanName);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(info.finitBeanName);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(info.finitbeanInitString);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(info.finitBeanName);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(TEXT_11);
    return stringBuffer.toString();
  }
}
