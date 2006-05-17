package org.eclipse.ve.internal.swt.codegen.jjet.util;

import org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator;
import org.eclipse.ve.internal.java.codegen.util.IMethodTemplate;

/*********************************************************************
 * This class was generated automatically from a javajet template.
 * !!!!!!!!!!!!!!! DO NOT MAKE CHANGES TO THIS CLASS !!!!!!!!!!!!!!!!!
 * 
 *********************************************************************/
public class SWTMainMethodTemplate implements IMethodTemplate {
	
  protected static String nl;
  public static synchronized SWTMainMethodTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    SWTMainMethodTemplate result = new SWTMainMethodTemplate();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t/*" + NL + "\t * Temporary main generation " + NL + "\t */    " + NL + "\tpublic static void main(String[] args) {" + NL + "\t\t// before you run this, make sure to set up the following in" + NL + "\t\t// the launch configuration (Arguments->VM Arguments) for the correct SWT lib. path" + NL + "\t\t// the following is a windows example," + NL + "\t\t// -Djava.library.path=\"installation_directory\\plugins\\org.eclipse.swt.win32_3.0.1\\os\\win32\\x86\"" + NL + "\t\torg.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();\t\t" + NL + "\t\t";
  protected final String TEXT_2 = " test = new ";
  protected final String TEXT_3 = "();" + NL + "\t\ttest.";
  protected final String TEXT_4 = "() ;" + NL + "\t\ttest.";
  protected final String TEXT_5 = ".open();" + NL + "\t\t" + NL + "\t\twhile (!test.";
  protected final String TEXT_6 = ".isDisposed()) {" + NL + "\t\t\tif (!display.readAndDispatch()) display.sleep ();" + NL + "\t\t}" + NL + "\t\tdisplay.dispose();\t\t" + NL + "\t}";
  protected final String TEXT_7 = NL + " ";

 public String generateMethod(AbstractMethodTextGenerator.MethodInfo info)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/*
 * This was created from the javajet file: 
 */
 
 // This Method is expected to generate a Main method for a SWT based application.
 // see AbstractMethodTextGenerator.MethodInfo for more information regarding the info structure
  

    stringBuffer.append(TEXT_1);
    stringBuffer.append(info.finitBeanType);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(info.finitBeanType);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(info.fmethodName);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(info.finitBeanName);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(info.finitBeanName);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(TEXT_7);
    return stringBuffer.toString();
  }
}
