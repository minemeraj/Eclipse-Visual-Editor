package org.eclipse.ve.internal.java.codegen.jjet.util;

import org.eclipse.ve.internal.java.codegen.util.AbstractExpressionGenerator;
import org.eclipse.ve.internal.java.codegen.util.IExpressionTemplate;

/*********************************************************************
 * This class was generated automatically from a javajet template.
 * !!!!!!!!!!!!!!! DO NOT MAKE CHANGES TO THIS CLASS !!!!!!!!!!!!!!!!!
 * 
 *********************************************************************/
public class InitExpressionTemplate implements IExpressionTemplate {
	
  protected static String nl;
  public static synchronized InitExpressionTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    InitExpressionTemplate result = new InitExpressionTemplate();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = " ";
  protected final String TEXT_2 = " =";
  protected final String TEXT_3 = " new ";
  protected final String TEXT_4 = "(";
  protected final String TEXT_5 = ", ";
  protected final String TEXT_6 = ") ";
  protected final String TEXT_7 = " ";
  protected final String TEXT_8 = ";";
  protected final String TEXT_9 = NL;

 public String generateExpression(AbstractExpressionGenerator.ExprInfo info)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a method invocation expression
 // see DefaultMethodInvocationGenerator.InvocationInfo for more information regarding the info structure
  

    stringBuffer.append(info.fIndent);
    stringBuffer.append(info.finitBeanType);
    stringBuffer.append(TEXT_1);
    stringBuffer.append(info.finitBeanName);
    stringBuffer.append(TEXT_2);
    
	if (info.finitbeanConstructionString == null) {
       
    stringBuffer.append(TEXT_3);
    stringBuffer.append(info.finitBeanType);
    stringBuffer.append(TEXT_4);
    
		boolean first = true ;
		// Generate arguments, if needed
		if (info.finitBeanArgs != null) 
		   for (int i=0; i<info.finitBeanArgs.length; i++) {
		      if (!first) {
    stringBuffer.append(TEXT_5);
    
		      }
		      first=false;
		      
    stringBuffer.append(info.finitBeanArgs[i]);
    
		   }  // Note, need the '\n' at the end of an expression 
		   
    stringBuffer.append(TEXT_6);
    
	} else {
	
    stringBuffer.append(TEXT_7);
    stringBuffer.append(info.finitbeanConstructionString);
    
	}
	
    stringBuffer.append(TEXT_8);
    stringBuffer.append(TEXT_9);
    return stringBuffer.toString();
  }
}
