package org.eclipse.ve.internal.propertysheet;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: StringCellEditor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-22 15:28:34 $ 
 */


import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * An text cell editor that allows non-strings to be
 * sent in without throwing an exception. It just
 * sets an error message.
 */
public class StringCellEditor extends TextCellEditor {
	protected boolean fWasNull = false;
		
	public StringCellEditor(Composite parent) {
		super(parent);
	}	
	
	/**
	 * Verify that the value is a string, and if it isn't 
	 * then set an error msg. If it is a string,
	 * let the super handle it.
	 */
	protected boolean isCorrect(Object value) {
		if (value == null || value instanceof String) {
			return super.isCorrect(value);
		} 
		
		setErrorMessage(PropertysheetMessages.not_string_WARN_);
		return false;
	}
	
	/**
	 * The value is being requested. If the original value
	 * was null, and if the new value is "", then return null.
	 */
	protected Object doGetValue() {
		Object v = super.doGetValue();
		if (fWasNull && "".equals(v)) //$NON-NLS-1$
			return null;
		else
			return v;
	}
	
	/**
	 * A value is being set, allow non-string with error msg
	 * instead of throwing an exception. (duh) Setting
	 * anything other than a string will set the value
	 * to "" so that something can be displayed. The isCorrect
	 * routine will take care of the validity of the value.
	 *
	 * @param value a text string (type <code>String</code>)
	 */
	protected void doSetValue(Object value) {
		fWasNull = value == null;
		if (value instanceof String)
			super.doSetValue(value);
		else
			super.doSetValue(""); //$NON-NLS-1$
	}
}


