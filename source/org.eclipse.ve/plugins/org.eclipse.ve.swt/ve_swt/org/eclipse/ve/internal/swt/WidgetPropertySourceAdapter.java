/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: WidgetPropertySourceAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-06 11:26:03 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.beaninfo.BeanDecorator;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.ve.internal.java.core.BeanPropertySourceAdapter;

import org.eclipse.ve.internal.propertysheet.EToolsPropertyDescriptor;

/**
 * 
 * @since 1.0.0
 */
public class WidgetPropertySourceAdapter extends BeanPropertySourceAdapter {
	
public IPropertyDescriptor[] getPropertyDescriptors() {

	IPropertyDescriptor[] propertyDescriptors = super.getPropertyDescriptors();
	// The style bit property descriptors are merged in
	BeanDecorator beanDecor = Utilities.getBeanDecorator(((EObject)getTarget()).eClass());
	Map styleDetails = beanDecor.getStyleDetails();
	// Grow the result array and add in descriptors for the style properties
	int numberOfStyleProperties = styleDetails.size();
	if(numberOfStyleProperties == 0) return propertyDescriptors;
	IPropertyDescriptor[] resultDescriptors = new IPropertyDescriptor[propertyDescriptors.length + numberOfStyleProperties];
	int nextDescriptorIndex = propertyDescriptors.length; 
	System.arraycopy(propertyDescriptors,0,resultDescriptors,0,propertyDescriptors.length);
	Iterator sweetProperties = styleDetails.keySet().iterator();
	while(sweetProperties.hasNext()){
		// The properties are a two element object[].  First element is the name, second is the initializationString
		String propertyName = (String)sweetProperties.next();
		String[][] sweetValues = (String[][]) styleDetails.get(propertyName);
		final String[] names = sweetValues[0];
		final String[] initStrings = sweetValues[1];
		IPropertyDescriptor property = new EToolsPropertyDescriptor(propertyName, propertyName){
			public CellEditor createPropertyEditor(Composite parent) {
				return new EnumeratedIntValueCellEditor(parent,names,null,initStrings){
				};
			}
		};
		resultDescriptors[nextDescriptorIndex] = property;
		nextDescriptorIndex++;
	}
	return resultDescriptors;	
}	

public Object getPropertyValue(Object structuralFeature) {
	if(structuralFeature instanceof EStructuralFeature) {
		return super.getPropertyValue(structuralFeature);
	} else {
		// TODO - need to get the style big and also think of a better way of knowing this property is one of the ones we created
		// catching non EMF ids is a hack right now
		return ">>> bit value here <<<";
	}
}

public boolean isPropertySet(Object feature) {
	if(feature instanceof EStructuralFeature){
		return super.isPropertySet(feature);
	} else {
		return false; // TODO
	}
}

public void resetPropertyValue(Object feature) {
	if(feature instanceof EStructuralFeature){
		super.resetPropertyValue(feature);
	} else {
		//TODO
	}
}

public void setPropertyValue(Object feature, Object val) {
	if(feature instanceof EStructuralFeature) {
		super.setPropertyValue(feature,val);
	} else {
		// TODO - Figure out how to change the JavaAllocation and do style bit magic
	}
}
}
