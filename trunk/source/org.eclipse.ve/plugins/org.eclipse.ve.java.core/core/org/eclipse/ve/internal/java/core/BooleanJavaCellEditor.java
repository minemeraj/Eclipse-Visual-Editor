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
 * $RCSfile: BooleanJavaCellEditor.java,v $ $Revision: 1.9 $ $Date: 2005-08-24 23:30:45 $
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBooleanBeanProxy;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.*;

/**
 * Boolean type property editor. It will default to boolean unless the initData string is "class". In that case it will be for java.lang.Boolean.
 */
public class BooleanJavaCellEditor extends BooleanCellEditor implements IExecutableExtension, INeedData, IJavaCellEditor {

	protected EditDomain fEditDomain;

	protected boolean fIsBooleanClass; // Is this a boolean class editor or a boolean type editor.

	protected JavaHelpers fBooleanHelper; // The appropriate classifer for the kind of boolean.

	public BooleanJavaCellEditor(Composite parent) {
		super(parent);
	}

	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		fIsBooleanClass = initData instanceof String && "class".equalsIgnoreCase((String) initData); // The initdata is "class", then this is for																								 // type class. //$NON-NLS-1$
	}

	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
		if (!fIsBooleanClass) {
			fBooleanHelper = JavaRefFactory.eINSTANCE.reflectType("boolean", JavaEditDomainHelper.getResourceSet(fEditDomain)); //$NON-NLS-1$
		} else
			fBooleanHelper = JavaRefFactory.eINSTANCE.reflectType("java.lang.Boolean", JavaEditDomainHelper.getResourceSet(fEditDomain)); //$NON-NLS-1$
	}

	protected String isCorrectObject(Object value) {
		if (value == null)
			if (!fIsBooleanClass)
				return PropertysheetMessages.null_invalid_WARN_; // Null is invalid for primitives.
			else
				return null;
		else if (fBooleanHelper.isInstance(value))
			return null;
		else
			return PropertysheetMessages.bad_bool_WARN_;
	}

	public String getJavaInitializationString() {
		return getJavaInitializationString(getSelectionIndex());
	}

	private String getJavaInitializationString(int index) {
		if (fIsBooleanClass) {
			if (index == 0) {
				return "Boolean.TRUE"; //$NON-NLS-1$
			} else {
				return "Boolean.FALSE"; //$NON-NLS-1$
			}
		} else {
			if (index == 0) {
				return "true"; //$NON-NLS-1$
			} else {
				return "false"; //$NON-NLS-1$
			}
		}
	}

	/**
	 * The object is being passed in, return the index to be used in the editor.
	 * 
	 * It should return sNoSelection if the value can't be converted to a index. The errormsg will have already been set in this case.
	 */
	protected int doGetIndex(Object value) {
		if (((EClassifier) fBooleanHelper).isInstance(value))
			return ((IBooleanBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain)))
					.booleanValue() ? TRUE_INDEX : FALSE_INDEX;
		else
			return NO_SELECTION;
	}

	/**
	 * This is called when editing and a selection from the combobox is sent in and we need to send the object that it represents up to the
	 * validators. The index to convert will be passed in.
	 */
	protected Object doGetObject(int index) {
		switch (index) {
			case NO_SELECTION:
				return null;
			case TRUE_INDEX:
			case FALSE_INDEX:
				return BeanUtilities.createJavaObject(fBooleanHelper, JavaEditDomainHelper.getResourceSet(fEditDomain), getJavaInitializationString(index));
		}
		return null;
	}

}
