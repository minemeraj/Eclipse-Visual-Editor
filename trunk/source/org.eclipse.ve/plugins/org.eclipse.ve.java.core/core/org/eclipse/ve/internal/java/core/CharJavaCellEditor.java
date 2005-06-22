/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;

/*
 *  $RCSfile: CharJavaCellEditor.java,v $
 *  $Revision: 1.8 $  $Date: 2005-06-22 15:21:39 $ 
 */

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaDataTypeInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.*;

/**
 * Provides property sheet editor functions for char types.
 * 
 * @author Rolf Kalim
 */
public class CharJavaCellEditor extends ObjectCellEditor implements IExecutableExtension, INeedData, IJavaCellEditor {

	protected JavaHelpers fDataType;
	protected ResourceSet fResourceSet;
	protected EditDomain fEditDomain;
	protected boolean fIsCharacterClass = false;

	public CharJavaCellEditor(Composite parent) {
		super(parent);
	}
	
	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		fIsCharacterClass = initData != null && initData instanceof String && "class".equalsIgnoreCase((String)initData);	// The initdata is "class", then this is for type class. //$NON-NLS-1$
	}

	/**
	 * Returns the object that the string represents.
	 * This is called when editing and a string from
	 * the text editor is sent in and we need to send
	 * the object that it represents up to the validators.
	 * The string to convert will be passed in.
	 */
	public Object doGetObject(String aString) {
		
		String initString;

		// The first character of the string, to be used as the character
		char charEntered = aString.charAt(0); //$NON-NLS-1$
		
		if ( charEntered == '\'' || charEntered == '\\' ) {
			// properly escape the ' and \ characters
			initString = "'\\" + charEntered + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		} else if (charEntered == '0' && aString.length() > 1) {
			// handle characters defined by number (preceeded by 0)
			initString = "(char) " + aString.substring(1); //$NON-NLS-1$
		} else {
		    // Create a char so the user gets to see it as a user-visible string in their source
		    initString = "'" + charEntered + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (!fIsCharacterClass) {
			return BeanUtilities.createJavaObject(fDataType, fResourceSet, initString);
		} else {
			initString = "new Character(" + initString + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			return BeanUtilities.createJavaObject(fDataType, fResourceSet, initString);
		}
		
	}

	/**
	 * The object is being passed in,
	 * return the string to be used in the editor.
	 *
	 * It returns an empty string if the value can't be
	 * converted to a string.
	 */
	public String doGetString(Object anObject) {
		if (anObject == null) {
			return ""; //$NON-NLS-1$
		} else if (anObject instanceof IJavaInstance) {
			return ((ICharacterBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) anObject, fResourceSet)).characterValue().toString();
		} else {
			return getCharacterLabel((IJavaDataTypeInstance) anObject, fEditDomain);
		}
	}

	/**
	 * Verifies the passed in string represents a good object.
	 * 
	 * This is usually from the text editor itself. Though it could be
	 * coming directly in from the setting, but we don't know this.
	 */
	public String isCorrectString(String aString) {

		// Check the string for invalid format
		if (aString.length() == 0) {
			return JavaMessages.CellEditor_CharJava_ErrMsg_ERROR_; 
		} else if (aString == null) {
			return JavaMessages.CellEditor_CharJava_InvalidMsg_ERROR_; 
		} else if (aString.length() > 1) {
			if (aString.charAt(0) != '0') {
				// TODO: Give a more descriptive error message when we're allowed to drop for NLS
				// Multicharacter string not starting with 0
				return JavaMessages.CellEditor_CharJava_InvalidMsg_ERROR_; 
			} else {
				try {
					int num = Integer.parseInt(aString.substring(1));
					if (num > Character.MAX_VALUE || num < Character.MIN_VALUE) {
						// TODO: Give a more descriptive error message when we're allowed to drop for NLS
						// Number outside of char bounds
						return JavaMessages.CellEditor_CharJava_InvalidMsg_ERROR_; 
					}
				} catch (NumberFormatException e) {
					// TODO: Give a more descriptive error message when we're allowed to drop for NLS
					// invalid number after 0
					return JavaMessages.CellEditor_CharJava_InvalidMsg_ERROR_; 
				}
			}
		}

		return null;
	}

	/**
	 * Implement this method to verify if this is a good
	 * object. This would be coming from a direct setting.
	 */
	public String isCorrectObject(Object value) {
		if (value == null)
			if (!fIsCharacterClass)
				return PropertysheetMessages.getString("null_invalid_WARN_");	// null is invalid for primitives
			else
				return null;
		else if (fDataType.isInstance(value))
			return null;
		else
			// TODO: Give a more descriptive error message when we're allowed to drop for NLS
			// Not a valid instance of Character class
			return JavaMessages.CellEditor_CharJava_InvalidMsg_ERROR_; 
	}

	/**
	 * Set the data. (Used to perform validations?)
	 */
	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
		fResourceSet = JavaEditDomainHelper.getResourceSet(fEditDomain);
		if (fIsCharacterClass) {
			fDataType = JavaRefFactory.eINSTANCE.reflectType("java.lang.Character", fResourceSet); //$NON-NLS-1$
		} else {
			fDataType = JavaRefFactory.eINSTANCE.reflectType("char", fResourceSet); //$NON-NLS-1$
		}
	}

	/**
	 * The IJBCFCellEditor interface
	 */
	public String getJavaInitializationString() {
		if (getValue() == null) {
			return null;
		} else if (!fIsCharacterClass){
			// Make a label for a char, in which case the label has quotes, e.g. 'S' or 't'
			String label = getCharacterLabel((IJavaDataTypeInstance) getValue(), fEditDomain);
			return "'" + label + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			String label = getCharacterLabel((IJavaDataTypeInstance) getValue(), fEditDomain);
			return "new Character(" + label + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	public static String getCharacterLabel(IJavaDataTypeInstance intOrCharInstance, EditDomain editDomain){
		IBeanProxy intOrCharProxy = BeanProxyUtilities.getBeanProxy(intOrCharInstance, JavaEditDomainHelper.getResourceSet(editDomain));
		if ( intOrCharProxy instanceof IIntegerBeanProxy ) {
			int intValue = ((IIntegerBeanProxy)intOrCharProxy).intValue();

			// See whether the int value can be displayed to the user 
			if ( Character.isLetterOrDigit((char)intValue)){
				// display any characters or digits
				return new Character((char)intValue).toString();
			} else if ( intValue <= 255 && !Character.isISOControl((char)intValue) ) {
				// display common non-letter characters (like symbols) within the standard ascii/Latin
				return new Character((char)intValue).toString();
			} else {
				// the character is not displayable as char

				// If the number is between 0 and 9, show it with a zero in front of it
				// to distinquish bewteen a number a character. (e.g. '3' versus 03)
				if (intValue >= 0 && intValue <= 9)
					return "0" + new Integer(intValue).toString(); //$NON-NLS-1$
				else
					// all remaining characters are displayed as their value
					return new Integer(intValue).toString();
			}

		} else if ( intOrCharProxy instanceof ICharacterBeanProxy ) {
			char charValue = ((ICharacterBeanProxy)intOrCharProxy).charValue();
			if ( charValue <= 255 && !Character.isISOControl(charValue) ) {
				// display common non-letter characters (like symbols) within the standard ascii/Latin
				return new Character(charValue).toString();
			} else {
				// the character is not displayable as char
				// If the number is between 0 and 9, show it with a zero in front of it
				// to distinquish bewteen a number a character. (e.g. '3' versus 03)
				return "0" + new Integer(charValue).toString(); //$NON-NLS-1$
			}
		} else {
			return ""; //$NON-NLS-1$
		}
	}
}
