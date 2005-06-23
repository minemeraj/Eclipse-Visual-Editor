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
/*
 * $RCSfile: StringJavaClassCellEditor.java,v $ $Revision: 1.8 $ $Date: 2005-06-23 01:48:08 $
 */
package org.eclipse.ve.internal.java.core;


import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.propertysheet.PropertysheetMessages;

/**
 * Cell editor for Strings that are Beans.
 */
public class StringJavaClassCellEditor extends DefaultJavaClassCellEditor {

	public StringJavaClassCellEditor(Composite aComposite) {
		super(aComposite);
	}

	/**
	 * getJavaInitializationString method comment.
	 */
	protected String getJavaInitializationString(String aString) {
		return BeanUtilities.createStringInitString(aString);
	}

	/**
	 * Returns the string for the value.
	 */
	protected String doGetString(Object value) {
		if (isInstance(value)) {
			return BeanUtilities.getEscapedString(((IStringBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain)))
					.stringValue());
		} else if (value != null) {
			// Since this is not a string value, the text widget is disabled,
			// just return the toString of this object.
			return BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain)).toBeanString();
		} else
			return null;
	}

	/**
	 * Since we could have a value whose is bean is not a string, we need to disable the test widget so it can't be edited since we don't know what it
	 * is.
	 */
	protected void doSetValue(Object value) {
		// Correct value?
		if (isInstance(value)) {
			// if the text widget was previously disabled, enable it
			if (!text.isEnabled())
				text.setEnabled(true);
			// value not a string? disable the text widget if not already
		} else {
			if (value != null && text.isEnabled())
				text.setEnabled(false);
		}
		super.doSetValue(value);
	}

	protected String getNotValidMsg(Object value) {
		return PropertysheetMessages.not_string_WARN_;
	}

	protected String isCorrectString(String value) {
		return null; // Any string is correct string for string.
	}

	public void setData(Object data) {
		super.setData(data);
		setJavaType(JavaRefFactory.eINSTANCE.reflectType("java.lang.String", JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
	}
}
