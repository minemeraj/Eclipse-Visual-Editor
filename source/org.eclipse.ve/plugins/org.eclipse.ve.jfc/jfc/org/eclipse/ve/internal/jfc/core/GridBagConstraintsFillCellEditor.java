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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagConstraintsFillCellEditor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-11-15 18:53:31 $ 
 */

import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
/**
 * Cell editor for the GridBagConstraints.fill  field
 */
public class GridBagConstraintsFillCellEditor extends ObjectComboBoxCellEditor implements org.eclipse.ve.internal.propertysheet.INeedData {
	protected EditDomain fEditDomain;
	protected Object[] fSources; 	
	public static int[] FILL_VALUES = new int [] {
		GridBagConstraint.NONE,
		GridBagConstraint.HORIZONTAL,
		GridBagConstraint.VERTICAL,
		GridBagConstraint.BOTH
	};
	
	public static int[] FILLINDEX_TO_MAINFILLINDEX = new int[] {GridBagComponentPage.FILL_NONE, GridBagComponentPage.FILL_HORIZONTAL,
		GridBagComponentPage.FILL_VERTICAL, GridBagComponentPage.FILL_BOTH};
	
	public static int getFillIndexFromConstraint(int gridbagFillConstraintValue) {
		for (int i = 0; i < FILL_VALUES.length; i++) {
			if (gridbagFillConstraintValue == FILL_VALUES[i]) {
				return i;
			}
		}
		return -1;
	}
	
public GridBagConstraintsFillCellEditor(Composite aComposite){
	// Create the combo editor with the list of possible fill values
	super(aComposite, GridBagConstraintsFillLabelProvider.FILL_VALUES);
}
/**
 * Return a MOF class that represents the constraint bean
 */
protected Object doGetObject(int index){
	JavaAllocation alloc;
	if (index < FILLINDEX_TO_MAINFILLINDEX.length)
		alloc = GridBagComponentPage.createFillAllocation(FILLINDEX_TO_MAINFILLINDEX[index]);
	else
		alloc = GridBagComponentPage.createFillAllocation(FILLINDEX_TO_MAINFILLINDEX[0]);
	
	return BeanUtilities.createJavaObject(
		"int", //$NON-NLS-1$
		JavaEditDomainHelper.getResourceSet(fEditDomain),
		alloc
		);
}
protected int doGetIndex(Object anObject){
	// The argument is an IJavaInstance.  Get its bean proxy and compare its int to what we have.
	if (anObject instanceof IJavaInstance) {
		// get the init string from the real value and set the editor value
		IIntegerBeanProxy fillValueProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)anObject, JavaEditDomainHelper.getResourceSet(fEditDomain));
		// The proxy is an int.  which represents one of the GridBagConstrains fill values.
		// Loop the array of fill values and return the index for the one found.
		int fillValue = fillValueProxy.intValue();
		int fillIndex = getFillIndexFromConstraint(fillValue);
		if (fillIndex > -1)
			return fillIndex;
	}
	return NO_SELECTION;
}
public String isCorrectObject(Object anObject){
	return null;
}
public void setData(Object data){
	fEditDomain = (EditDomain) data;
}
}
