package org.eclipse.ve.internal.java.codegen.jjet.util;

import org.eclipse.ve.internal.java.codegen.util.AbstractEventSrcGenerator;
import org.eclipse.ve.internal.java.codegen.util.IEventTemplate;

/*********************************************************************
 * This class was generated automatically from a javajet template.
 * !!!!!!!!!!!!!!! DO NOT MAKE CHANGES TO THIS CLASS !!!!!!!!!!!!!!!!!
 * 
 *********************************************************************/
public class PropChangeAnonymousPropertyTemplate implements IEventTemplate {

  protected static String nl;
  public static synchronized PropChangeAnonymousPropertyTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    PropChangeAnonymousPropertyTemplate result = new PropChangeAnonymousPropertyTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL;
  protected final String TEXT_3 = "\t\tif ((";
  protected final String TEXT_4 = ".getPropertyName().equals(\"";
  protected final String TEXT_5 = "\"))) { ";
  protected final String TEXT_6 = NL;
  protected final String TEXT_7 = "\t\t\tSystem.out.println(\"propertyChange(";
  protected final String TEXT_8 = ")\"); // TODO Auto-generated property Event stub \"";
  protected final String TEXT_9 = "\" ";
  protected final String TEXT_10 = NL;
  protected final String TEXT_11 = "\t\t} ";

	public IEventTemplate createNLTemplate(String nl) {
		return create(nl);
	}
	public String generateEvent(AbstractEventSrcGenerator.EventInfo info)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a method for an Anonymous event listener of a bound property. 
 // see AbstractEventSrcGenerator.EventInfo for more information regarding the info structure
  

     
    if(info.fPropertyNames!=null && info.fPropertyNames.length>0) {
        for (int i=0; i<info.fPropertyNames.length; i++) {
           if (info.fPropertyIfFlag[i]) { 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(info.fEventArgName);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(info.fPropertyNames[i]);
    stringBuffer.append(TEXT_5);
    
           } 
    stringBuffer.append(TEXT_6);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(info.fPropertyNames[i]);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(info.fPropertyNames[i]);
    stringBuffer.append(TEXT_9);
    
           if (info.fPropertyIfFlag[i]) { 
    stringBuffer.append(TEXT_10);
    stringBuffer.append(info.fIndent);
    stringBuffer.append(TEXT_11);
    
           } 
    
        }
     }
    return stringBuffer.toString();
  }
}
