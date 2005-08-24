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
package org.eclipse.ve.internal.java.core;

/*
 * $RCSfile: NumberJavaCellEditor.java,v $ $Revision: 1.9 $ $Date: 2005-08-24 23:30:45 $
 */

import java.text.MessageFormat;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.INumberBeanProxy;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.*;

/**
 * Cell Editor for Integer beans
 */
public class NumberJavaCellEditor extends NumberCellEditor implements INeedData, IJavaCellEditor {

	protected JavaHelpers fNumberClassType;

	protected String fInitializationString;

	protected EditDomain fEditDomain;

	protected String fTypeName;

	/**
	 * NumberBeanTypeCellEditor constructor comment.
	 */
	public NumberJavaCellEditor(Composite aComposite) {
		super(aComposite);
	}

	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		if (initData instanceof String)
			fTypeName = (String) initData;
		else
			fTypeName = "void"; // Default to nothing allowed. //$NON-NLS-1$

		if (fTypeName.equals("java.lang.Integer")) { //$NON-NLS-1$
			fInitializationString = "new java.lang.Integer({0})"; //$NON-NLS-1$
			super.setInitializationData(null, null, "integer"); //$NON-NLS-1$
		} else if (fTypeName.equals("int")) { //$NON-NLS-1$
			fInitializationString = "{0}"; //$NON-NLS-1$
			super.setInitializationData(null, null, "integer"); //$NON-NLS-1$
		} else if (fTypeName.equals("short")) { //$NON-NLS-1$
			fInitializationString = "(short) {0}"; //$NON-NLS-1$
			super.setInitializationData(null, null, "short"); //$NON-NLS-1$
		} else if (fTypeName.equals("java.lang.Short")) { //$NON-NLS-1$
			fInitializationString = "new Short((short) {0})"; //$NON-NLS-1$
			super.setInitializationData(null, null, "short"); //$NON-NLS-1$
		} else if (fTypeName.equals("long")) { //$NON-NLS-1$
			fInitializationString = "{0}L"; //$NON-NLS-1$
			super.setInitializationData(null, null, "long"); //$NON-NLS-1$
		} else if (fTypeName.equals("java.lang.Long")) { //$NON-NLS-1$
			fInitializationString = "new java.lang.Long({0}L)"; //$NON-NLS-1$
			super.setInitializationData(null, null, "long"); //$NON-NLS-1$
		} else if (fTypeName.equals("byte")) { //$NON-NLS-1$
			fInitializationString = "(byte) {0}"; //$NON-NLS-1$
			super.setInitializationData(null, null, "byte"); //$NON-NLS-1$
		} else if (fTypeName.equals("java.lang.Byte")) { //$NON-NLS-1$
			fInitializationString = "new Byte((byte) {0})"; //$NON-NLS-1$
			super.setInitializationData(null, null, "byte"); //$NON-NLS-1$
		} else if (fTypeName.equals("java.lang.Float")) { //$NON-NLS-1$
			fInitializationString = "new java.lang.Float({0}F)"; //$NON-NLS-1$
			super.setInitializationData(null, null, "float"); //$NON-NLS-1$
		} else if (fTypeName.equals("float")) { //$NON-NLS-1$
			fInitializationString = "{0}F"; //$NON-NLS-1$
			super.setInitializationData(null, null, "float"); //$NON-NLS-1$
		} else if (fTypeName.equals("double")) { //$NON-NLS-1$
			fInitializationString = "{0}D"; //$NON-NLS-1$
			super.setInitializationData(null, null, "double"); //$NON-NLS-1$
		} else if (fTypeName.equals("java.lang.Double")) { //$NON-NLS-1$
			fInitializationString = "new java.lang.Double({0}D)"; //$NON-NLS-1$
			super.setInitializationData(null, null, "double"); //$NON-NLS-1$
		}
		;
		// TODO When init string parser can handle "new Double(3.5D)" we can change them all back to not
		// having java.lang prepended. See defect 248326 and 248342 for full details.
	}

	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
		fNumberClassType = JavaRefFactory.eINSTANCE.reflectType(fTypeName, JavaEditDomainHelper.getResourceSet(fEditDomain));
	}

	/**
	 * Create an instance of the MOF IJavaInstance of the specified type
	 */
	protected Object doGetObject(String value) {
		Number newNumber = (Number) super.doGetObject(value);
		return newNumber != null ? BeanUtilities.createJavaObject(fNumberClassType, JavaEditDomainHelper.getResourceSet(fEditDomain),
				getJavaInitializationString(newNumber)) : null;
	}

	/**
	 * Returns the string for the value.
	 */
	protected String doGetString(Object value) {
		if (fNumberClassType.isInstance(value)) {
			return super.doGetString(((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper
					.getResourceSet(fEditDomain))).numberValue());
		} else
			return null;
	}

	protected String isCorrectObject(Object value) {
		if (value == null)
			if (fNumberClassType.isPrimitive())
				return PropertysheetMessages.null_invalid_WARN_; // null is invalid for primitives
			else
				return super.isCorrectObject(null);
		else if (fNumberClassType.isInstance(value))
			return super.isCorrectObject(((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper
					.getResourceSet(fEditDomain))).numberValue());
		else
			return (fFormatter.isParseIntegerOnly() ? sNotIntegerError : sNotNumberError);
	}

	/**
	 * getJavaInitializationString method comment.
	 */
	public String getJavaInitializationString() {
		if (isValueValid()) {
			Object value = getSetValue();
			return (value != null) ? getJavaInitializationString(((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value,
					JavaEditDomainHelper.getResourceSet(fEditDomain))).numberValue()) : null;
		}
		return null;
	}

	/**
	 * getJavaInitializationString method comment.
	 */
	protected String getJavaInitializationString(Number aNumber) {
		if (fNumberClassType.isPrimitive()) {
			// TODO Until we have parse trees instead of strings we can't do non-primitives. The init string parser 
			// gets confused on something like: "new Byte(Byte.MIN_VALUE)". With parse trees this won't occur.
			switch (fNumberType) {
				case BYTE:
					if (aNumber.byteValue() == Byte.MIN_VALUE)
						return "Byte.MIN_VALUE";
					else if (aNumber.byteValue() == Byte.MAX_VALUE)
						return "Byte.MAX_VALUE";
					break;
				case DOUBLE:
					if (aNumber.doubleValue() == Double.MIN_VALUE)
						return "Double.MIN_VALUE";
					else if (aNumber.doubleValue() == Double.MAX_VALUE)
						return "Double.MAX_VALUE";
					break;
				case FLOAT:
					if (aNumber.floatValue() == Float.MIN_VALUE)
						return "Float.MIN_VALUE";
					else if (aNumber.floatValue() == Float.MAX_VALUE)
						return "Float.MAX_VALUE";
					break;
				case INTEGER:
					if (aNumber.intValue() == Integer.MIN_VALUE)
						return "Integer.MIN_VALUE";
					else if (aNumber.intValue() == Integer.MAX_VALUE)
						return "Integer.MAX_VALUE";
					break;
				case LONG:
					if (aNumber.longValue() == Long.MIN_VALUE)
						return "Long.MIN_VALUE";
					else if (aNumber.longValue() == Long.MAX_VALUE)
						return "Long.MAX_VALUE";
					break;
				case SHORT:
					if (aNumber.shortValue() == Short.MIN_VALUE)
						return "Short.MIN_VALUE";
					else if (aNumber.shortValue() == Short.MAX_VALUE)
						return "Short.MAX_VALUE";
					break;
			}
		}

		// fInitializationString was set in the ctor based on the Mof type
		return MessageFormat.format(fInitializationString, new Object[] { aNumber.toString()});

	}

}
