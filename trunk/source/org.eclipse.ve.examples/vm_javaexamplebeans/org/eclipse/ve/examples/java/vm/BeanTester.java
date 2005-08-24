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
package org.eclipse.ve.examples.java.vm;

import java.awt.*;
import java.beans.*;

public class BeanTester extends Canvas {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -9190933947790141938L;
	static Color DARK_GREEN = new Color(0, 127, 0);
	
public Dimension getPreferredSize(){
	return new Dimension(175,55);
}

public void paint(Graphics g) {
	super.paint(g);
	boolean isDesignTime = Beans.isDesignTime();
	
	g.setColor(Color.black);

	String isDesignTimeString = "Beans.isDesignTime=";
	int width = g.getFontMetrics().stringWidth(isDesignTimeString);
	int y = 5 + g.getFontMetrics().getHeight();
	System.out.println("width=" + getWidth() + ", height=" + getHeight());
	g.drawString(isDesignTimeString,5,y);
	
	if ( Beans.isDesignTime() ) {
		g.setColor(DARK_GREEN);
	} else {
		g.setColor(Color.red);
	}
	g.drawString("" + isDesignTime,width + 10,y);
	g.setColor(Color.black);
	String isGUIAvailableString = "Beans.isGUIAvailable=";
	width = g.getFontMetrics().stringWidth(isGUIAvailableString);
	y = y + 5 + g.getFontMetrics().getHeight();
	g.drawString(isGUIAvailableString,5,y);
	if ( Beans.isGuiAvailable() ) {
		g.setColor(DARK_GREEN);
	} else {
		g.setColor(Color.red);
	}
	g.drawString("" + Beans.isGuiAvailable(),width + 10,y);

}
public static void main(String[] args){
	Frame frame = new Frame("Beans values test");
	BeanTester tester = new BeanTester();
	frame.add(tester);
	frame.pack();
	frame.setVisible(true);
}
}
