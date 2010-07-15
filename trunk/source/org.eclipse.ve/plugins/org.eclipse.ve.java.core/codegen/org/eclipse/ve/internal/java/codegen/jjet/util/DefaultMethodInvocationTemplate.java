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

import org.eclipse.ve.internal.java.codegen.util.DefaultMethodInvocationGenerator;
import org.eclipse.ve.internal.java.codegen.util.IMethodInvocationTemplate;

/*********************************************************************
 * This class was generated automatically from a javajet template.
 * !!!!!!!!!!!!!!! DO NOT MAKE CHANGES TO THIS CLASS !!!!!!!!!!!!!!!!!
 * 
 *********************************************************************/
public class DefaultMethodInvocationTemplate implements IMethodInvocationTemplate {
	
  protected static String nl;
  public static synchronized DefaultMethodInvocationTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    DefaultMethodInvocationTemplate result = new DefaultMethodInvocationTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t";
  protected final String TEXT_2 = "(";
  protected final String TEXT_3 = ", ";
  protected final String TEXT_4 = ");";
  protected final String TEXT_5 = NL;

 public String generateMethod(DefaultMethodInvocationGenerator.InvocationInfo info)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a method invocation expression
 // see DefaultMethodInvocationGenerator.InvocationInfo for more information regarding the info structure
  

    stringBuffer.append(TEXT_1);
    stringBuffer.append(info.fmethodName);
    stringBuffer.append(TEXT_2);
    
		boolean first = true ;
		// Generate arguments, if needed
		if (info.fmethodArguments != null) 
		   for (int i=0; i<info.fmethodArguments.length; i++) {
		      if (!first) {
    stringBuffer.append(TEXT_3);
    
		      }
		      first=false;
		      
    stringBuffer.append(info.fmethodArguments[i]);
    
		   }  // Note, need the \n at the end of an expression
    stringBuffer.append(TEXT_4);
    stringBuffer.append(TEXT_5);
    return stringBuffer.toString();
  }
}
