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
/*
 *  $RCSfile: BeanPropertyEnumeratedCellEditor.java,v $
 *  $Revision: 1.8 $  $Date: 2005-12-02 18:41:22 $ 
 */
package org.eclipse.ve.internal.java.core;


import java.util.logging.Level;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
/*
 * Works with BeanPropertyDescriptorAdapter to show the Enumerations from the PropertyDescriptor's attribute
 * for enums.
 * 
 * <package-protected> because it works tightly with the BeanPropertyDescriptorAdapter.
 * 
 * @since 1.0.0
 */
class BeanPropertyEnumeratedCellEditor extends ObjectComboBoxCellEditor implements INeedData {
	
	protected String[] fDisplayNames;
	protected IBeanProxy[] fBeanProxies;
	protected String[] fInitStrings;
	protected EditDomain fEditDomain;
	protected JavaHelpers fFeatureType;		// This is the type of the object we are going to create
	protected ProxyFactoryRegistry fProxyFactoryRegistry;
	protected IBeanTypeProxy fBeanTypeProxy;

public BeanPropertyEnumeratedCellEditor(Composite aComposite, Object[] arrayOfValues, JavaHelpers featureType){
	super(aComposite);
	fFeatureType = featureType;
	// Iterate over the array of values, these are stored in the format
	// displayName, object, initString
	int length = arrayOfValues.length;
	int j=0;
	fDisplayNames = new String[length/3];
	fInitStrings = new String[length/3];
	for ( int i=0;i<length;i+=3 ) {
		fDisplayNames[j] = (String) arrayOfValues[i];
		fInitStrings[j] = (String) arrayOfValues[i+2];
		// NOTE - We cannot in any way use the object that came in from the values array
		// This came from the VM that did introspection that is NOT the same as the
		// one the editor is necessarily running in so we must re-create the
		// bean proxies from the init string each time
		j++;
	}
	// Set the display names as the items the user sees in the combo box
	setItems(fDisplayNames);	
}
protected String isCorrectObject(Object value) {
	return null;
}
protected Object doGetObject(int index){

	Object bean = null;
	if ( fFeatureType.isPrimitive() ) {
		bean = BeanProxyUtilities.wrapperBeanProxyAsPrimitive(
			fBeanProxies[index],
			(JavaDataType)fFeatureType,
			JavaEditDomainHelper.getResourceSet(fEditDomain),
			BeanPropertyDescriptorAdapter.createAllocation(fInitStrings[index])
		);
	} else {		
		bean = BeanProxyUtilities.wrapperBeanProxy(
			fBeanProxies[index],
			JavaEditDomainHelper.getResourceSet(fEditDomain),
			true,	// Since we are getting a new value, we own it. (By definition, editors are supposed to return new instances).
			BeanPropertyDescriptorAdapter.createAllocation(fInitStrings[index]));
	}
	return bean;	
	
}
protected int doGetIndex(Object anObject){
	// The argument is an IJavaInstance.  Get its bean proxy and compare it against
	// the values stored by us
	IBeanProxy argumentProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance)anObject, JavaEditDomainHelper.getResourceSet(fEditDomain));
	for (int i=0;i<fBeanProxies.length;i++){
		if(fBeanProxies[i].equals(argumentProxy)){
			return i;
		}
	}
	return NO_SELECTION;
}
public void setData(Object data){
	fEditDomain = (EditDomain) data;
	// Now that we have a VM we can create the proxies from the init strings
	fBeanTypeProxy = JavaEditDomainHelper.getBeanProxyDomain(fEditDomain).getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(fFeatureType instanceof JavaClass ? ((JavaClass) fFeatureType).getQualifiedNameForReflection() : fFeatureType.getQualifiedName());
	fBeanProxies = new IBeanProxy[fInitStrings.length];
	int index=0;
	try {
		for(int i=0;i<fInitStrings.length;i++){
			index = i;
			fBeanProxies[i] = fBeanTypeProxy.newInstance(fInitStrings[i]);
		}
	} catch ( ThrowableProxy exc ) {
		if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
			JavaVEPlugin.log("Unable to create enumeration value for " + fInitStrings[index], Level.WARNING); //$NON-NLS-1$
			JavaVEPlugin.log(exc, Level.WARNING);
		}
	} catch (InstantiationException exc) {
		if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
			JavaVEPlugin.log("Unable to create enumeration value for " + fInitStrings[index], Level.WARNING); //$NON-NLS-1$
			JavaVEPlugin.log(exc, Level.WARNING);
		}
	}		
}
}
