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
 *  $RCSfile: GridBagConstraintsAnchorCellEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
//import java.awt.GridBagConstraints;

public class GridBagConstraintsAnchorCellEditor extends ObjectComboBoxCellEditor implements INeedData {
	protected EditDomain fEditDomain;
	public static int[] ANCHOR_VALUES = new int [] {
		GridBagConstraint.CENTER,
		GridBagConstraint.NORTH,
		GridBagConstraint.NORTHEAST,
		GridBagConstraint.EAST,
		GridBagConstraint.SOUTHEAST,
		GridBagConstraint.SOUTH,
		GridBagConstraint.SOUTHWEST,
		GridBagConstraint.WEST,
		GridBagConstraint.NORTHWEST
	};
	
	public static String[] ANCHOR_INITSTRINGS = new String[] {
		"java.awt.GridBagConstraints.CENTER", //$NON-NLS-1$
		"java.awt.GridBagConstraints.NORTH", //$NON-NLS-1$
		"java.awt.GridBagConstraints.NORTHEAST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.EAST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.SOUTHEAST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.SOUTH", //$NON-NLS-1$
		"java.awt.GridBagConstraints.SOUTHWEST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.WEST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.NORTHWEST" //$NON-NLS-1$
	};	
	
public GridBagConstraintsAnchorCellEditor(Composite aComposite){
	// Create the combo editor with the list of possible anchor values
	super(aComposite, GridBagConstraintsAnchorLabelProvider.ANCHOR_VALUES);
}
/**
 * Return a MOF class that represents the constraint bean
 */
protected Object doGetObject(int index){
	String initString = ""; //$NON-NLS-1$
	if (index < ANCHOR_INITSTRINGS.length)
		initString = ANCHOR_INITSTRINGS[index];
	else
		initString = ANCHOR_INITSTRINGS[0];

	return BeanUtilities.createJavaObject(
		"int", //$NON-NLS-1$
		JavaEditDomainHelper.getResourceSet(fEditDomain),
		initString
		);
}
protected int doGetIndex(Object anObject){
	// The argument is an IJavaInstance.  Get its bean proxy and compare it against
	// the values stored by us
	if (anObject instanceof IJavaInstance) {
		// get the init string from the real value and set the editor value
		IIntegerBeanProxy anchorValueProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)anObject, JavaEditDomainHelper.getResourceSet(fEditDomain));
		// The proxy is an int.  which represents one of the GridBagConstraints anchor values.
		// Loop the array of anchor values and return the index for the one found.
		int anchorValue = anchorValueProxy.intValue();
		for (int i = 0; i < ANCHOR_VALUES.length; i++) {
			if (anchorValue == ANCHOR_VALUES[i]) {
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
