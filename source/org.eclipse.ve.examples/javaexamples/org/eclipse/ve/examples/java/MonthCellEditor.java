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
package org.eclipse.ve.examples.java;
/*
 *  $RCSfile: MonthCellEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
/**
 * This is an example of a CellEditor that is described directly on the 
 * Month Feature of Area.  It shows how to write a CellEditor that does
 * editing of the IJavaObjectInstance directly on the IDE side rather than
 * BeanFeatureEditor which hosts a java.beans.PropertyEditor and involes
 * doing a setAsText(String) and getValue()
 * This is a ComboBoxCellEditor that shows the list of months in a drop down list
 * The month itself is an int from 1 to 12
 */
public class MonthCellEditor extends ComboBoxCellEditor implements INeedData {
	
	protected EditDomain fEditDomain;
	
public MonthCellEditor(Composite aComposite){
	// Create the combo editor with the list of months
	super(aComposite,MonthLabelProvider.MONTHS, SWT.READ_ONLY);
}
/* 
 * Create an instance of the Month bean
 * The init string for the month is just the index of the month itself
 * If we wanted to it could be a static references as is the case
 * for Shape on Area
 */
protected Object doGetValue(){
	
	Integer comboBoxIndex = (Integer)super.doGetValue();
	String initString = String.valueOf(comboBoxIndex.intValue() + 1); // Add 1 so the first zeroeth element is January
	return BeanUtilities.createJavaObject(
		"int", //$NON-NLS-1$
		JavaEditDomainHelper.getResourceSet(fEditDomain),
		initString
		);
}
/**
 * Sets the value of the cell editor to the given value.
 * Because booleans cannot be null it should not be possible for this to be given
 * null
 */
protected void doSetValue(Object value) {

	if (value instanceof IJavaInstance) {
		// get the init string from the real value and set the editor value
		IIntegerBeanProxy monthProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)value);
		// The proxy is an int.  1 = January which is the zeroeth element
		// Zero is unset so we don't set the combo box to a month in this case
		if ( monthProxy.intValue() > 0 ) {
			super.doSetValue(new Integer(monthProxy.intValue() - 1));
		}
	}
}

public void setData(Object data) {
	fEditDomain = (EditDomain) data;
}	
}
