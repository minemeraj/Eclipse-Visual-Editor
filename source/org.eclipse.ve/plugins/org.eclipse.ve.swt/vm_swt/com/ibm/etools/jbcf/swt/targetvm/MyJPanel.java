package com.ibm.etools.jbcf.swt.targetvm;

import java.awt.Graphics;

import javax.swing.JPanel;

public class MyJPanel extends JPanel {
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(java.awt.Color.blue);
		g.fillOval(60,60,20,20);
		System.out.println("Graphic called for MyJPanel");
	}

}
