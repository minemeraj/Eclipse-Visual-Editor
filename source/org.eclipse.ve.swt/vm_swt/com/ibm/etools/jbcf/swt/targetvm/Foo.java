package com.ibm.etools.jbcf.swt.targetvm;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class Foo {
	
	public static void main(String[] args) {
		
		Display d = new Display();
		Shell s = new Shell(d);
		s.setText("Title");
		s.setSize(200,200);
		s.open();
		
		while(!s.isDisposed()){
			if(!d.readAndDispatch())d.sleep();
		}
		
		d.dispose();
		
	}

}
