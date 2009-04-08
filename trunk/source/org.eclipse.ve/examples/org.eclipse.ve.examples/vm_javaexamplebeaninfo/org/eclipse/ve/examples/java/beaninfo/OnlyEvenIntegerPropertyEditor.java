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
package org.eclipse.ve.examples.java.beaninfo;

/**
 * Insert the type's description here.
 * Creation date: (10/17/00 4:43:02 PM)
 * @author: Joe Winchester
 */
public class OnlyEvenIntegerPropertyEditor extends sun.beans.editors.IntegerEditor {
	protected Object fSource;
/**
 * OnlyEvenIntegerPropertyEditor constructor comment.
 */
public OnlyEvenIntegerPropertyEditor() {
	super();
}
/**
 * OnlyEvenIntegerPropertyEditor constructor comment.
 */
public OnlyEvenIntegerPropertyEditor(Object aSource) {
	this();
	fSource = aSource;
}
public void setAsText(String text) throws IllegalArgumentException {
	try {
		int newValue = Integer.parseInt(text);
		if ((newValue / 2) * 2 != newValue) {
			throw new IllegalArgumentException(newValue + " is not an even number");
		}
	} catch (NumberFormatException nfexc) {
		// Let the number format exception go through to the superclass
	}
	super.setAsText(text);
}
}
