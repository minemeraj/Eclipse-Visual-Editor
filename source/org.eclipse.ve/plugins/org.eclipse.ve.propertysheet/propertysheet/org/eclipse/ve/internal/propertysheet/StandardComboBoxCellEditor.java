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
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: StandardComboBoxCellEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:44:29 $ 
 */



import org.eclipse.swt.widgets.Composite;
import java.text.MessageFormat;
/**
 * This is standard combo box cell editor where
 * the items/values are passed in.
 * The passed in values are used when the corresponding
 * index has been selected. This should only be used for
 * datatypes where the values can be in more than one object.
 * It should not be used for objects that require identity since
 * new ones are not created on each call. The only time this would make
 * sense if the object is being shared. In other words, if the
 * setting is an attribute, the values should be datatypes because
 * attributes are owned by the source and so the identity can't
 * be shared except. If the setting is a reference to an object that
 * owned by something else, then identity is important and this
 * editor can then be used. 
 *
 * Also, the values must respond to equals so that two different instances of the
 * datatype return true if they are semantically equal.
 */

public class StandardComboBoxCellEditor extends ObjectComboBoxCellEditor {
	protected Object[] fItems;

public StandardComboBoxCellEditor(Composite parent){
	super(parent);
}
public StandardComboBoxCellEditor(Composite parent, String[] displayStrings, Object[] items){
	super(parent, displayStrings);
	
	fItems = items;
}

public void setItems(String[] displayStrings, Object[] items) {
	fItems = items;
	setItems(displayStrings);
}

/**
 * Return an error message if this is not a valid value.
 * This can be overridden if a specific message should
 * be returned instead of the default one.
 * This default implementation will simply see if it
 * is one of the items in fItems or if it is null. If
 * it isn't it will return a generic invalid value message.
 */
protected String isCorrectObject(Object value) {
	if (value == null || doGetIndex(value) != NO_SELECTION)
		return null;
		
	return MessageFormat.format(PropertysheetMessages.not_valid_WARN_, new Object[] {value});
}
	
/**
 * Subclassed need to implement returning the object
 * that the index represents. This is called when
 * editing and a selection from the combobox is sent
 * in and we need to send the object that it represents
 * up to the validators. The index to convert will be
 * passed in.
 */
protected Object doGetObject(int index){
	return (fItems != null && index >= 0 && index < fItems.length) ? fItems[index] : null;
}

/**
 * The object is being passed in, return
 * the index to be used in the editor.
 *
 * It should return sNoSelection if the value can't be
 * converted to a index. The errormsg will have
 * already been set in this case.
 */
protected int doGetIndex(Object value){
	if (fItems != null) {
		for (int i=0; i<fItems.length; i++) {
			if (fItems[i] == null)
				if (value == null)
					return i;
				else
					;
			else if (fItems[i].equals(value))
				return i;
		}
	}
			
	return NO_SELECTION;
}


}
