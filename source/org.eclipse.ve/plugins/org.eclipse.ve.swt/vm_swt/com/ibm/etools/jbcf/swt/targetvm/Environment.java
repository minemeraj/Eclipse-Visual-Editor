package com.ibm.etools.jbcf.swt.targetvm;

import java.lang.reflect.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.jem.internal.proxy.initParser.InitializationStringParser;
import org.eclipse.jem.internal.proxy.initParser.InitializationStringEvaluationException;

public class Environment {
	
	public static Display display;
	public static int tCount;
	public static int rCount;
	public static Thread t;
	public static Shell freeFormHost;
	
public static void initialize(){
	
	t = new Thread(){
		public void run(){
			display = new Display();
			freeFormHost = new Shell(display);
			freeFormHost.setBounds(0,0,100,100);
			freeFormHost.setLocation(-10000,-10000);
			freeFormHost.open();
			while(true){
				display.readAndDispatch();
			}			
		}
	};
	t.start();
}

public static Object invoke(final Method method, final Object receiver, Object arg){
	return invoke(method,receiver,new Object[] {arg});
}

public static Object invoke(final Method method, final Object receiver, Object arg, Object arg1){
	return invoke(method,receiver,new Object[] {arg,arg1});
}

public static Object invoke(final Method method, final Object receiver){
	return invoke(method,receiver,null);
}

public static Object invoke(final Method method, final Object receiver, final Object[] args){
 	final Object[] result = new Object[1];
	display.syncExec(new Runnable(){
		public void run(){
			try {
				result[0] = method.invoke(receiver,args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	return result[0];
}

public static Object get(final Field field, final Object receiver){
	final Object[] result = new Object[1];
	display.syncExec(new Runnable(){
		public void run(){
			try {
				result[0] = field.get(receiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	return result[0];
}

public static Object invoke(final Constructor ctor, Object arg){
	return invoke(ctor, new Object[] {arg});
}

public static Object invoke(final Constructor ctor, Object arg1, Object arg2){
	return invoke(ctor, new Object[] {arg1,arg2});
}

public static Object invoke(final Constructor ctor, final Object[] args){
	final Object[] result = new Object[1];
	display.syncExec(new Runnable(){
		public void run(){
			try {
				result[0] =ctor.newInstance(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	return result[0];
}

public static Object evaluate(final String initializationString) throws InitializationStringEvaluationException {
	final Object[] result = new Object[1];
	display.syncExec(new Runnable(){
		public void run(){
			try{
				result[0] = InitializationStringParser.evaluate(initializationString);				
			} catch (InitializationStringEvaluationException exc){
				result[0] = exc;
			}
		}
	});
	if(result[0] instanceof InitializationStringEvaluationException){
		throw (InitializationStringEvaluationException) result[0];
	} else {
		return result[0];
	}
}
public static String getFontLabel(Font aFont){
	
	FontData fontData = aFont.getFontData()[0];
	StringBuffer fontLabelBuffer = new StringBuffer();
	fontLabelBuffer.append(fontData.getName());
	fontLabelBuffer.append(',');
	// Style is a bitmask of NORMAL , BOLD and ITALIC
	boolean styleUsed = false;
	if((fontData.getStyle() & SWT.BOLD) != 0) {	
		fontLabelBuffer.append("Bold");
		styleUsed = true;
	} 
	if((fontData.getStyle() & SWT.ITALIC) != 0) {
		if(styleUsed) fontLabelBuffer.append(' ');
		fontLabelBuffer.append("Italic");
		styleUsed = true;
	}
	if(styleUsed) fontLabelBuffer.append(',');	
	fontLabelBuffer.append(String.valueOf(fontData.getHeight()));
	return fontLabelBuffer.toString();
}
}
