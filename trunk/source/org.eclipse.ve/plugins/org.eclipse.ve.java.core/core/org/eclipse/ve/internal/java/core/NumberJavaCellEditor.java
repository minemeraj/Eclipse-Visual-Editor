package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NumberJavaCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.text.MessageFormat;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.java.JavaHelpers;
import org.eclipse.jem.internal.java.impl.JavaClassImpl;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.propertysheet.*;
import org.eclipse.jem.internal.proxy.core.INumberBeanProxy;
/**
 * Cell Editor for Integer beans
 */
public class NumberJavaCellEditor extends NumberCellEditor implements INeedData , IJavaCellEditor {
	
	protected JavaHelpers fNumberType;
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
			fTypeName = "void";	// Default to nothing allowed. //$NON-NLS-1$
			
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
			super.setInitializationData(null, null, "short");		 //$NON-NLS-1$
		} else if (fTypeName.equals("long")) { //$NON-NLS-1$
			fInitializationString = "{0}L"; //$NON-NLS-1$
			super.setInitializationData(null, null, "long");		 //$NON-NLS-1$
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
		} else if (fTypeName.equals("float")){ //$NON-NLS-1$
			fInitializationString = "{0}F"; //$NON-NLS-1$
			super.setInitializationData(null, null, "float"); //$NON-NLS-1$
		} else if (fTypeName.equals("double")){ //$NON-NLS-1$
			fInitializationString = "{0}D"; //$NON-NLS-1$
			super.setInitializationData(null, null, "double"); //$NON-NLS-1$
		} else if (fTypeName.equals("java.lang.Double")){ //$NON-NLS-1$
			fInitializationString = "new java.lang.Double({0}D)"; //$NON-NLS-1$
			super.setInitializationData(null, null, "double"); //$NON-NLS-1$
		};
		//TODO When init string parser can handle "new Double(3.5D)" we can change them all back to not
		// having java.lang prepended. See defect 248326 and 248342 for full details.
	}
	
	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
		fNumberType = JavaClassImpl.reflect(fTypeName, JavaEditDomainHelper.getResourceSet(fEditDomain));
	}
	

	/**
	 * Create an instance of the MOF IJavaInstance of the specified type
	 */
	protected Object doGetObject(String value){
		Number newNumber = (Number) super.doGetObject(value);
		return newNumber != null ? BeanUtilities.createJavaObject(fNumberType, JavaEditDomainHelper.getResourceSet(fEditDomain), getJavaInitializationString(newNumber)) : null;
	}
	
	/**
	 * Returns the string for the value.
	 */
	protected String doGetString(Object value) {
		if (fNumberType.isInstance(value)) {
			return super.doGetString(((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain))).numberValue());
		} else
			return null;
	}		
		
	protected String isCorrectObject(Object value) {
		if (value == null)
			if (fNumberType.isPrimitive())
				return PropertysheetMessages.getString(PropertysheetMessages.NULL_INVALID);	// null is invalid for primitives
			else
				return super.isCorrectObject(null);
		else if (fNumberType.isInstance(value))
			return super.isCorrectObject(((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain))).numberValue());
		else
			return (fFormatter.isParseIntegerOnly() ? sNotIntegerError : sNotNumberError);
	}
	
	/**
	 * getJavaInitializationString method comment.
	 */
	public String getJavaInitializationString() {
		if (isValueValid()) {
			Object value = getSetValue();
			return (value != null) ? 
				getJavaInitializationString(((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain))).numberValue()) : 
				null;
		}
		return null;
	}	
	
	
	/**
	 * getJavaInitializationString method comment.
	 */
	protected String getJavaInitializationString(Number aNumber) {
		// fInitializationString was set in the ctor based on the Mof type
		return MessageFormat.format(fInitializationString, new Object[] {aNumber.toString()});
	}
	
}
