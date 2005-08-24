/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: InsetsPropertyEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.util.StringTokenizer;
/**
 * Insert the type's description here.
 * Creation date: (1/28/00 4:06:52 AM)
 * @author: Joe Winchester
 */
public class InsetsPropertyEditor extends java.beans.PropertyEditorSupport {
/**
 * InsetsPropertyEditor constructor comment.
 */
public InsetsPropertyEditor() {
	super();
}
/* Format the rectangle as follows
 * x:xValue y:yValue width:widthValue height:heightValue
 */
public String getAsText(){

	return VisualBeanInfoMessages.getString("InsetsPropertyEditor.top")+":" + getTop() + " "+VisualBeanInfoMessages.getString("InsetsPropertyEditor.left")+":" + getLeft() + " "+VisualBeanInfoMessages.getString("InsetsPropertyEditor.bottom")+":" + getBottom() + " "+VisualBeanInfoMessages.getString("InsetsPropertyEditor.right")+":" + getRight(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
	
}
/* Return the bottom value
 */
protected int getBottom(){

	return ((java.awt.Insets)getValue()).bottom;
	
}
/* Return the left value
 */
protected int getLeft(){

	return ((java.awt.Insets)getValue()).left;
	
}
/* Return the right value
 */
protected int getRight(){

	return ((java.awt.Insets)getValue()).right;
	
}
/* Return the top value
 */
protected int getTop(){

	return ((java.awt.Insets)getValue()).top;
	
}
/* Lazy initialize to a default insets
 */
public Object getValue(){

	if ( super.getValue() == null ){
		setValue(new java.awt.Insets(0,0,0,0));
	}

	return super.getValue();
	
}
/* Format the rectangle as follows
 * x:xValue y:yValue width:widthValue height:heightValue
 */
public void setAsText(String aString){

	try {
		StringTokenizer stringTokenizer = new StringTokenizer(aString , ":"); //$NON-NLS-1$
// Get the first token off ( which is the top ) and then go up to the next space
		stringTokenizer.nextToken(":"); //$NON-NLS-1$
		String topValue = stringTokenizer.nextToken(" "); //$NON-NLS-1$
// Do the same for the left value
		stringTokenizer.nextToken(":"); //$NON-NLS-1$
		String leftValue = stringTokenizer.nextToken(" "); //$NON-NLS-1$
// And the bottom
		stringTokenizer.nextToken(":"); //$NON-NLS-1$
		String bottomValue = stringTokenizer.nextToken(" "); //$NON-NLS-1$
// And the right
		stringTokenizer.nextToken(":"); //$NON-NLS-1$
		String rightValue = stringTokenizer.nextToken(" "); //$NON-NLS-1$
// Now we have all four values that have : at the front.  Convert them to four ints
		int top = new Integer(topValue.substring(1,topValue.length())).intValue();
		int left = new Integer(leftValue.substring(1,leftValue.length())).intValue();
		int bottom = new Integer(bottomValue.substring(1,bottomValue.length())).intValue();
		int right = new Integer(rightValue.substring(1,rightValue.length())).intValue();
// Create a insets as our value
		setValue(new java.awt.Insets(top,left,bottom,right));
	} catch ( Exception e ) {
		throw new IllegalArgumentException();
	}
	
}
}
