package com.ibm.etools.jbcf.swt.targetvm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
			while(true){
				try {
					display.readAndDispatch();
				} catch (RuntimeException e) {
					e.printStackTrace();
					// We don't want this to end because of some user error. It will stay running until vm is killed.
				}
			}			
		}
	};
	t.start();
}

public static Shell getFreeFormHost() {
	if (freeFormHost == null) {
		freeFormHost = new Shell(display);
		freeFormHost.setBounds(0, 0, 100, 100);
		freeFormHost.setLocation(-10000, -10000);
		freeFormHost.open();
	}
	return freeFormHost;
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
