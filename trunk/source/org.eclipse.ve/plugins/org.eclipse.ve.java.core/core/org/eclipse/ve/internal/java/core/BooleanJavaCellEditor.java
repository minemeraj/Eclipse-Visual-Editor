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
 *  $RCSfile: BooleanJavaCellEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2004-01-13 21:11:52 $ 
 */

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
 * Boolean type property editor.
 * It will default to boolean unless the initData string is "class". In that case it will
 * be for java.lang.Boolean.
 */
public class BooleanJavaCellEditor extends BooleanCellEditor implements IExecutableExtension, INeedData , IJavaCellEditor {

	protected EditDomain fEditDomain;
	protected boolean fIsBooleanClass;	// Is this a boolean class editor or a boolean type editor.
	protected JavaHelpers fBooleanHelper;	// The appropriate classifer for the kind of boolean.
	
	public BooleanJavaCellEditor(Composite parent){
		super(parent);
	}
	
	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		fIsBooleanClass = initData instanceof String && "class".equalsIgnoreCase((String)initData);	// The initdata is "class", then this is for type class. //$NON-NLS-1$
	}

	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
		if (!fIsBooleanClass) {
			fBooleanHelper = JavaRefFactory.eINSTANCE.reflectType("boolean", JavaEditDomainHelper.getResourceSet(fEditDomain)); //$NON-NLS-1$
			// Set into the items the data types for boolean.
			setItems(getDisplayStrings(),
				new Object[] {
					BeanUtilities.createJavaObject(fBooleanHelper, JavaEditDomainHelper.getResourceSet(fEditDomain), "true"), //$NON-NLS-1$
					BeanUtilities.createJavaObject(fBooleanHelper, JavaEditDomainHelper.getResourceSet(fEditDomain), "false") //$NON-NLS-1$
				});
		} else
			fBooleanHelper = JavaRefFactory.eINSTANCE.reflectType("java.lang.Boolean", JavaEditDomainHelper.getResourceSet(fEditDomain));		 //$NON-NLS-1$
	}
	
	protected String isCorrectObject(Object value) {
		if (value == null)
			if (!fIsBooleanClass)
				return PropertysheetMessages.getString(PropertysheetMessages.NULL_INVALID);	// Null is invalid for primitives.
			else
				return null;
				
		else if (((EClassifier) fBooleanHelper).isInstance(value))
			return null;
		else
			return PropertysheetMessages.getString(PropertysheetMessages.NOT_BOOL);
	}
		
	public String getJavaInitializationString(){
		if (getSelectionIndex() == 0){
			return "true"; //$NON-NLS-1$
		} else {
			return "false"; //$NON-NLS-1$
		}
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
		if (((EClassifier) fBooleanHelper).isInstance(value))
			return ((IBooleanBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain))).booleanValue() ? sTrueIndex : sFalseIndex;
		else
			return sNoSelection;
	}
	
	/**
	 * This is called when
	 * editing and a selection from the combobox is sent
	 * in and we need to send the object that it represents
	 * up to the validators. The index to convert will be
	 * passed in.
	 */
	protected Object doGetObject(int index) {
		if (!fIsBooleanClass)
			return super.doGetObject(index);	// Use the datatype instances passed in.
			
		// This is class boolean, so we need to new up a new one.
		switch (index) {
			case sNoSelection:
				return null;
			case sTrueIndex:
				return BeanUtilities.createJavaObject(fBooleanHelper, JavaEditDomainHelper.getResourceSet(fEditDomain), "Boolean.TRUE"); //$NON-NLS-1$
			case sFalseIndex:
				return BeanUtilities.createJavaObject(fBooleanHelper, JavaEditDomainHelper.getResourceSet(fEditDomain), "Boolean.FALSE");			 //$NON-NLS-1$
		}
		
		return null;
	}
			
}
