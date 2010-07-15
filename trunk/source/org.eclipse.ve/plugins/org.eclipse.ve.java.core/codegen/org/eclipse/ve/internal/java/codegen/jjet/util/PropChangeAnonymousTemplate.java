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
public class PropChangeAnonymousTemplate implements IEventTemplate {

  protected static String nl;
  public static synchronized PropChangeAnonymousTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    PropChangeAnonymousTemplate result = new PropChangeAnonymousTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = ".";
  protected final String TEXT_3 = "(new ";
  protected final String TEXT_4 = "() { ";
  protected final String TEXT_5 = NL;
  protected final String TEXT_6 = ".";
  protected final String TEXT_7 = "(\"";
  protected final String TEXT_8 = "\", new ";
  protected final String TEXT_9 = "() { ";
  protected final String TEXT_10 = NL;
  protected final String TEXT_11 = "\tpublic void ";
  protected final String TEXT_12 = "(";
  protected final String TEXT_13 = " e) { ";
  protected final String TEXT_14 = NL;
  protected final String TEXT_15 = "\t\tif ((e.getPropertyName().equals(\"";
  protected final String TEXT_16 = "\"))) { ";
  protected final String TEXT_17 = NL;
  protected final String TEXT_18 = "\t\t\tSystem.out.println(\"";
  protected final String TEXT_19 = "(";
  protected final String TEXT_20 = ")\"); // TODO Auto-generated property Event stub \"";
  protected final String TEXT_21 = "\" ";
  protected final String TEXT_22 = NL;
  protected final String TEXT_23 = "\t\t} ";
  protected final String TEXT_24 = NL;
  protected final String TEXT_25 = "\t}";
  protected final String TEXT_26 = "     ";
  protected final String TEXT_27 = NL;
  protected final String TEXT_28 = "\t\t\tSystem.out.println(\"";
  protected final String TEXT_29 = "()\"); // TODO Auto-generated Event stub ";
  protected final String TEXT_30 = "()";
  protected final String TEXT_31 = NL;
  protected final String TEXT_32 = "\t}";
  protected final String TEXT_33 = NL;
  protected final String TEXT_34 = "\tpublic void ";
  protected final String TEXT_35 = "(";
  protected final String TEXT_36 = " e) {} ";
  protected final String TEXT_37 = NL;
  protected final String TEXT_38 = "});";
  protected final String TEXT_39 = NL;

	public IEventTemplate createNLTemplate(String nl) {
		return create(nl);
	}
	public String generateEvent(AbstractEventSrcGenerator.EventInfo info)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a complete Anonymous event listener for a bound property. 
 // see AbstractEventSrcGenerator.EventInfo for more information regarding the info structure
  

      // User one or two argument add Method
   if (info.fselectorArgCount==1) {
    stringBuffer.append(TEXT_1);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(info.fReceiver);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(info.fSelector);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(info.fAllocatedClass);
    stringBuffer.append(TEXT_4);
     
   }
   else {
    stringBuffer.append(TEXT_5);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(info.fReceiver);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(info.fSelector);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(info.fPropertyNames[0]);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(info.fAllocatedClass);
    stringBuffer.append(TEXT_9);
    
   }
   
   // Generate the methods that were requested
   for(int i=0; i<info.fMethods.length; i++) {    

    stringBuffer.append(TEXT_10);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(info.fEventType);
    stringBuffer.append(TEXT_13);
     
    if(info.fPropertyNames!=null && info.fPropertyNames.length>0) {
        for (int j=0; j<info.fPropertyNames.length; j++) {
           if (info.fPropertyIfFlag[i]) {
     
    stringBuffer.append(TEXT_14);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(info.fPropertyNames[j]);
    stringBuffer.append(TEXT_16);
    
           } 
    stringBuffer.append(TEXT_17);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(info.fPropertyNames[j]);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(info.fPropertyNames[j]);
    stringBuffer.append(TEXT_21);
    
           if (info.fPropertyIfFlag[j]) { 
    stringBuffer.append(TEXT_22);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_23);
    
           } 
    stringBuffer.append(TEXT_24);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_25);
    
        }
     }
     else {       

    stringBuffer.append(TEXT_26);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(info.fMethods[i]);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_32);
    
     }
   }
   
   // Generate required (interface) ethods 
   for(int i=0; i<info.fMStubs.length; i++) {   

    stringBuffer.append(TEXT_33);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(info.fMStubs[i]);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(info.fEventType);
    stringBuffer.append(TEXT_36);
    
   }
    stringBuffer.append(TEXT_37);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(TEXT_39);
    return stringBuffer.toString();
  }
}
