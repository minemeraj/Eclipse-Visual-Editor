package com.ibm.etools.jbcf.swt.targetvm;

import java.awt.*;

public class MyFrame extends Frame {
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(java.awt.Color.red);
		g.drawLine(0,0,150,150);
	}	

}
