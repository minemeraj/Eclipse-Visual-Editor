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


package org.eclipse.ve.internal.java.codegen.jjet.util;

import org.eclipse.ve.internal.java.codegen.util.AbstractEventSrcGenerator;
import org.eclipse.ve.internal.java.codegen.util.IEventTemplate;

/*********************************************************************
 * This class was generated automatically from a javajet template.
 * !!!!!!!!!!!!!!! DO NOT MAKE CHANGES TO THIS CLASS !!!!!!!!!!!!!!!!!
 * 
 *********************************************************************/
public class AnonymousEventMethodTemplate implements IEventTemplate {

  protected static String nl;
  public static synchronized AnonymousEventMethodTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    AnonymousEventMethodTemplate result = new AnonymousEventMethodTemplate();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "   ";
  protected final String TEXT_2 = NL;
  protected final String TEXT_3 = "\tpublic void ";
  protected final String TEXT_4 = "(";
  protected final String TEXT_5 = " ";
  protected final String TEXT_6 = ") { ";
  protected final String TEXT_7 = "   ";
  protected final String TEXT_8 = NL;
  protected final String TEXT_9 = "\t\tSystem.out.println(\"";
  protected final String TEXT_10 = "()\"); // TODO Auto-generated Event stub ";
  protected final String TEXT_11 = "()";
  protected final String TEXT_12 = NL;
  protected final String TEXT_13 = "\t}";

	public IEventTemplate createNLTemplate(String nl) {
		return create(nl);
	}
	public String generateEvent(AbstractEventSrcGenerator.EventInfo info)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a method for an Anonymous event listener. 
 // see AbstractEventSrcGenerator.EventInfo for more information regarding the info structure
  

     
   // Generate the methods that were requested
   for(int i=0; i<info.fMethods.length; i++) {  
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(info.fEventType);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(info.fEventArgName);
    stringBuffer.append(TEXT_6);
     

    stringBuffer.append(TEXT_7);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_13);
    
   }
    return stringBuffer.toString();
  }
}
