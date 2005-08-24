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
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.cde.core.EditDomain;
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
	public static String[] FILL_INITSTRINGS = new String [] {
		"java.awt.GridBagConstraints.NONE", //$NON-NLS-1$
		"java.awt.GridBagConstraints.HORIZONTAL", //$NON-NLS-1$
		"java.awt.GridBagConstraints.VERTICAL", //$NON-NLS-1$
		"java.awt.GridBagConstraints.BOTH" //$NON-NLS-1$
	};	
	
public GridBagConstraintsFillCellEditor(Composite aComposite){
	// Create the combo editor with the list of possible fill values
	super(aComposite, GridBagConstraintsFillLabelProvider.FILL_VALUES);
}
/**
 * Return a MOF class that represents the constraint bean
 */
protected Object doGetObject(int index){
	String initString = ""; //$NON-NLS-1$
	if (index < FILL_INITSTRINGS.length)
		initString = FILL_INITSTRINGS[index];
	else
		initString = FILL_INITSTRINGS[0];
	
	return BeanUtilities.createJavaObject(
		"int", //$NON-NLS-1$
		JavaEditDomainHelper.getResourceSet(fEditDomain),
		initString
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
		for (int i = 0; i < FILL_VALUES.length; i++) {
			if (fillValue == FILL_VALUES[i]) {
				return i;
			}
		}
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
