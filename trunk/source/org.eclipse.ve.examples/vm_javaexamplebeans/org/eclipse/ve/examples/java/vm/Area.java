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
/**
 * This draws a shape inside a border of a given color
 * It has a number of property editors and customizers
 */
public class Area extends Canvas {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 4103489323542566189L;
	public static final int NO_SHAPE = 0;
	public static final int OVAL = 1;
	public static final int DIAMOND = 2;
	protected Color fFillColor = Color.orange;
	protected int fBorderWidth = 15;
	protected int fShape = OVAL;
	protected int fMonth; // Represents a month, 1=January, 12=December
	protected Continent fContinent;
	protected int fEvenNumber; // Property editor only allows even numbers
	protected int fDay; // Day between 0 and 6 for Monday through Sunday.  Edited as day names
	protected Font fFont;

	public final static int SUNDAY = 0;


	public final static int MONDAY = 1;


	public final static int TUESDAY= 2;


	public final static int WEDNESDAY = 3;


	public final static int THURSDAY = 4;


	public final static int FRIDAY = 5;


	public final static int SATURDAY = 6;
	private Object fObject;	
	
 // Font so we can test custom editors
/**
 * Area constructor comment.
 */
public Area() {
	System.out.println("Area bean created");
	setSize(100,100);
}
public int getMonth(){
	return fMonth;
}
public void setMonth(int aMonth){
	fMonth = aMonth;
}
public int getDay(){
	return fDay;
}
public void setDay(int aDay){
	fDay = aDay;
}
/**
 * Sun's font editor can't deal with null so return a non-null value if one is not 
 * explicitly set
 */
public Font getFont(){
	if ( fFont == null ) {
		return Font.decode(null);
	} else {
		return fFont;
	}
}
public void setFont(Font aFont){
	fFont = aFont;
}
public Continent getContinent(){
	return fContinent;
}
public void setContinent(Continent aContinent){
	fContinent = aContinent;
}
/**
 * Get the border width
 */
public int getBorderWidth() {
	return fBorderWidth;
}
/**
 * Get the fill color
 */
public Color getFillColor() {
	return fFillColor;
}
/**
 * Return our preferred size to be our size
 */
public Dimension getPreferredSize() {
	return getSize();
}
/**
 * Set the shape to be painted
 */
public int getShape() {
	return fShape;
}
/**
 * Paint the area
 */
public void paint(Graphics g){

	Dimension d = getSize();
	int width = d.width;
	int height = d.height;
	
	if ( fFillColor != null ){
		g.setColor(fFillColor);
		// Fill four rectangles for the border
		g.fillRect(0,0,fBorderWidth,height);
	 	g.fillRect(0,0,width,fBorderWidth);
		g.fillRect(width-fBorderWidth,0,fBorderWidth,height);
		g.fillRect(0,height-fBorderWidth,width,fBorderWidth);

	}
	// Paint the shape inside the border
	g.setColor(Color.black);
	switch (fShape) {
		case OVAL: {
			// Draw a circle
			g.drawOval(fBorderWidth,fBorderWidth,width-fBorderWidth*2,height-fBorderWidth*2);
			break;
		}
		case DIAMOND: {
			// Draw a diamond of four corners
			g.drawPolygon(
				new int[] { fBorderWidth , width/2 , width - fBorderWidth , width/2 }, 
				new int[] { height/2, fBorderWidth , height /2 , height-fBorderWidth }, 
				4);
		}
	}
}
/**
 * Set the raised state
 */
public void setBorderWidth(int aBorderWidth) {
	fBorderWidth = aBorderWidth;
}
/**
 * Set the fill color
 */
public void setFillColor(Color aColor) {
	fFillColor = aColor;
}
/**
 * Set the shape to be painted
 */
public void setShape(int aShape) {
	fShape = aShape;
}

public int getEvenNumber(){
	return fEvenNumber;
}
public void setEvenNumber(int aNumber){
	fEvenNumber = aNumber;
}
public Object getObject(){
	return fObject;
}
public void setObject(Object anObject){
	fObject = anObject;
}
}
