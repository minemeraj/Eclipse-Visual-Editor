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
 *  $RCSfile: EnumeratedIntValueCellEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:23:54 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.logging.Level;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
import org.eclipse.jem.internal.proxy.initParser.InitializationStringEvaluationException;
import org.eclipse.jem.internal.proxy.initParser.InitializationStringParser;
import org.eclipse.ve.internal.propertysheet.*;
/**
 * Cell editor for an int field that is enumerated.
 * It works only with simple init strings. Not complicated ones.
 */
public class EnumeratedIntValueCellEditor extends ObjectComboBoxCellEditor implements INeedData {
	protected EditDomain fEditDomain;
	protected Object[] fSources; 
	protected Integer[] fill_values;
	protected String[] fill_names;
	protected String[] file_initStrings;
	
public EnumeratedIntValueCellEditor(Composite aComposite, String[] names, Integer[] values, String[] initStrings){
	// Create the combo editor with the list of possible fill values
	super(aComposite, names);
	fill_names = names;
	file_initStrings = initStrings;
	if(values != null){
		fill_values = values;
	} else {
		// Evaluate the initStrings to find the Init values
		fill_values = new Integer[initStrings.length];
		for (int i = 0; i < initStrings.length; i++) {
			try {
				fill_values[i] = (Integer) InitializationStringParser.evaluate(initStrings[i]);
			} catch (InitializationStringEvaluationException e) {
				JavaVEPlugin.log(e,Level.WARNING);
			}
		}
	}
	
}
/**
 * Return an EMF class that represents the value
 */
protected Object doGetObject(int index){
	String initString = ""; //$NON-NLS-1$
	if (index < file_initStrings.length)
		initString = file_initStrings[index];
	else
		initString = file_initStrings[0];
	
	return BeanUtilities.createJavaObject(
		"int", //$NON-NLS-1$
		JavaEditDomainHelper.getResourceSet(fEditDomain),
		initString
		);
}
protected int doGetIndex(Object anObject){
	int fillValue = -1;
	// The argument is an IJavaInstance.  Get its bean proxy and compare its int to what we have.
	if (anObject instanceof IJavaInstance) {
		// get the init string from the real value and set the editor value
		IIntegerBeanProxy fillValueProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)anObject, JavaEditDomainHelper.getResourceSet(fEditDomain));
		// The proxy is an int.  which represents one of the values.
		// Loop the array of fill values and return the index for the one found.
		fillValue = fillValueProxy.intValue();
	} else {
		fillValue = ((Integer)anObject).intValue();
	}
	for (int i = 0; i < fill_values.length; i++) {
		if (fillValue == fill_values[i].intValue()) {
			return i;
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