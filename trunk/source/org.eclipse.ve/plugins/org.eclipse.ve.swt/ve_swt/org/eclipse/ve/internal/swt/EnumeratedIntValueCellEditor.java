package org.eclipse.ve.internal.swt;
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
 *  $RCSfile: EnumeratedIntValueCellEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2004-03-10 01:57:15 $ 
 */

import java.util.logging.Level;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
import org.eclipse.jem.internal.proxy.initParser.InitializationStringEvaluationException;
import org.eclipse.jem.internal.proxy.initParser.InitializationStringParser;
import org.eclipse.ve.internal.propertysheet.*;
/**
 * Cell editor for an int field that is enumerated
 */
public class EnumeratedIntValueCellEditor extends ObjectComboBoxCellEditor implements INeedData {
	protected EditDomain fEditDomain;
	protected Object[] fSources; 
	protected Integer[] FILL_VALUES;
	protected String[] FILL_NAMES;
	protected String[] FILL_INITSTRINGS;
	
public EnumeratedIntValueCellEditor(Composite aComposite, String[] NAMES, Integer[] VALUES, String[] INITSTRINGS){
	// Create the combo editor with the list of possible fill values
	super(aComposite, NAMES);
	FILL_NAMES = NAMES;
	FILL_INITSTRINGS = INITSTRINGS;
	if(VALUES != null){
		FILL_VALUES = VALUES;
	} else {
		// Evaluate the initStrings to find the Init values
		FILL_VALUES = new Integer[INITSTRINGS.length];
		for (int i = 0; i < INITSTRINGS.length; i++) {
			try {
				FILL_VALUES[i] = (Integer) InitializationStringParser.evaluate(INITSTRINGS[i]);
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
	for (int i = 0; i < FILL_VALUES.length; i++) {
		if (fillValue == FILL_VALUES[i].intValue()) {
			return i;
		}
	}
	return sNoSelection;
}
public String isCorrectObject(Object anObject){
	return null;
}
public void setData(Object data){
	fEditDomain = (EditDomain) data;
}
}