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
public class AnonymousEventTemplate implements IEventTemplate {

  protected static String nl;
  public static synchronized AnonymousEventTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    AnonymousEventTemplate result = new AnonymousEventTemplate();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = ".";
  protected final String TEXT_3 = "(new ";
  protected final String TEXT_4 = "() { ";
  protected final String TEXT_5 = NL;
  protected final String TEXT_6 = "\tpublic void ";
  protected final String TEXT_7 = "(";
  protected final String TEXT_8 = " e) { ";
  protected final String TEXT_9 = "   ";
  protected final String TEXT_10 = NL;
  protected final String TEXT_11 = "\t\tSystem.out.println(\"";
  protected final String TEXT_12 = "()\"); // TODO Auto-generated Event stub ";
  protected final String TEXT_13 = "()";
  protected final String TEXT_14 = NL;
  protected final String TEXT_15 = "\t}";
  protected final String TEXT_16 = NL;
  protected final String TEXT_17 = "\tpublic void ";
  protected final String TEXT_18 = "(";
  protected final String TEXT_19 = " e) {} ";
  protected final String TEXT_20 = NL;
  protected final String TEXT_21 = "});";
  protected final String TEXT_22 = NL;

	public IEventTemplate createNLTemplate(String nl) {
		return create(nl);
	}
	public String generateEvent(AbstractEventSrcGenerator.EventInfo info)
  {
    StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a complete Anonymous event listener. 
 // see AbstractEventSrcGenerator.EventInfo for more information regarding the info structure
  

    stringBuffer.append(TEXT_1);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(info.fReceiver);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(info.fSelector);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(info.fAllocatedClass);
    stringBuffer.append(TEXT_4);
     
   // Generate the methods that were requested
   for(int i=0; i<info.fMethods.length; i++) {    

    stringBuffer.append(TEXT_5);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(info.fEventType);
    stringBuffer.append(TEXT_8);
     

    stringBuffer.append(TEXT_9);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_15);
    
   }
   
   // Generate required (interface) ethods 
   for(int i=0; i<info.fMStubs.length; i++) {   

    stringBuffer.append(TEXT_16);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(info.fMStubs[i]);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(info.fEventType);
    stringBuffer.append(TEXT_19);
    
   }
    stringBuffer.append(TEXT_20);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(TEXT_22);
    return stringBuffer.toString();
  }
}
