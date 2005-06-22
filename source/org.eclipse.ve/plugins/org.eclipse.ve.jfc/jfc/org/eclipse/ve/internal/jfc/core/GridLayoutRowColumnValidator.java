/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GridLayoutRowColumnValidator.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-22 14:36:44 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

import org.eclipse.ve.internal.propertysheet.*;

/**
 * A java.awt.GridLayout validator used to ensure the rows and colunmns fields are not both set to zero which throws and exception in the remote VM
 * and the running app.
 * 
 * @since 1.0.0
 */
public class GridLayoutRowColumnValidator implements ICellEditorValidator, ISourced, INeedData {

	protected static final String sRowColumnEqualZeroError = JFCMessages.GridLayout_rowcolumn_not_valid_WARN; 

	protected EditDomain domain;

	protected EReference sfRows;

	protected EReference sfColumns;

	IPropertySource[] pSources;

	IPropertyDescriptor[] fDescriptors;

	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public GridLayoutRowColumnValidator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		// Here's where we check if 'value' is okay by checking it
		// against it's corresponding property value
		// If it's not valid, return an error message and it will fail and not
		// set the value in the cell editor.
		if (value instanceof IJavaDataTypeInstance) {
			int new_value = -1;
			IIntegerBeanProxy intBeanProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value);
			if (intBeanProxy != null)
				new_value = intBeanProxy.intValue();
			if (new_value != 0)
				return null;
			int row_value = 1, col_value = 0;
			Object rowsValue = pSources[0].getPropertyValue(sfRows);
			Object colsValue = pSources[0].getPropertyValue(sfColumns);
			if (rowsValue instanceof IJavaDataTypeInstance)
				row_value = ((IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) rowsValue)).intValue();
			if (colsValue instanceof IJavaDataTypeInstance)
				col_value = ((IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) colsValue)).intValue();
			EReference propertyId = (EReference) fDescriptors[0].getId();
			if (propertyId == sfRows)
				return (col_value == 0) ? sRowColumnEqualZeroError : null;
			else if (propertyId == sfColumns)
				return (row_value == 0) ? sRowColumnEqualZeroError : null;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.propertysheet.ISourced#setSources(java.lang.Object[], org.eclipse.ui.views.properties.IPropertySource[],
	 *      org.eclipse.ui.views.properties.IPropertyDescriptor[])
	 */
	public void setSources(Object[] sources, IPropertySource[] propertySources, IPropertyDescriptor[] descriptors) {
		pSources = propertySources;
		fDescriptors = descriptors;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.propertysheet.INeedData#setData(java.lang.Object)
	 */
	public void setData(Object data) {
		if (data != null && data instanceof EditDomain) {
			domain = (EditDomain) data;
			ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
			sfRows = JavaInstantiation.getReference(rset, JFCConstants.SF_GRIDLAYOUT_ROWS);
			sfColumns = JavaInstantiation.getReference(rset, JFCConstants.SF_GRIDLAYOUT_COLUMNS);
		}
	}

}