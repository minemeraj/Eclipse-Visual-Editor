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
 *  $Revision: 1.2 $  $Date: 2004-03-07 14:30:08 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.beaninfo.BeanDecorator;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.initParser.InitializationStringEvaluationException;
import org.eclipse.jem.internal.proxy.initParser.InitializationStringParser;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.core.BeanPropertySourceAdapter;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.propertysheet.EToolsPropertyDescriptor;
import com.ibm.wtp.logger.proxyrender.EclipseLogger;

/**
 * 
 * @since 1.0.0
 */
public class WidgetPropertySourceAdapter extends BeanPropertySourceAdapter {
	
	class StyleBitPropertyID{
		String propertyName;
		public Integer[] bitValues;
		StyleBitPropertyID(String aPropertyName,Integer[] values){
			propertyName = aPropertyName;
			bitValues = values;
		}
	};
	
	public class StyleBitPropertyDescriptor extends EToolsPropertyDescriptor{
		String fPropertyName;
		String[] fNames;
		String[] fInitStrings;
		Integer[] fValues;
		public StyleBitPropertyDescriptor(String propertyName, String[] names, String[] initStrings, Integer[] values){
			super(new StyleBitPropertyID(propertyName,values),propertyName);
			fPropertyName = propertyName;
			fNames = names;
			fInitStrings = initStrings;
			fValues = values; 
		}
		public CellEditor createPropertyEditor(Composite parent){
			return new EnumeratedIntValueCellEditor(parent,fNames,fValues,fInitStrings);
		}
		public ILabelProvider getLabelProvider() {
			return new EnumeratedIntValueLabelProvider(fNames,fValues);
		}
	};
	
public IPropertyDescriptor[] getPropertyDescriptors() {

	IPropertyDescriptor[] propertyDescriptors = super.getPropertyDescriptors();
	List descriptorsList = new ArrayList(propertyDescriptors.length + 5);
	for (int i = 0; i < propertyDescriptors.length; i++) {
		descriptorsList.add(propertyDescriptors[i]);
	}
	mergeAllStyleBits(descriptorsList);
	IPropertyDescriptor[] resultArray = new IPropertyDescriptor[descriptorsList.size()];
	descriptorsList.toArray(resultArray);
	return resultArray;
	
}
/**
 * Collects style bits including inheritance
 * @param propertyDescriptors list into which the property descriptors are added for the constructor style bits
 * 
 * @since 1.0.0
 */
protected void mergeAllStyleBits(List propertyDescriptors){
	
	JavaClass eClass = (JavaClass) ((EObject)getTarget()).eClass();
	// Iterate around the class and its superclasses until we get to java.lang.Object (which has no style bits)
	while(!(eClass != null && eClass.getQualifiedName().equals("java.lang.Object"))){
		mergeStyleBits(propertyDescriptors,eClass);		
		eClass = eClass.getSupertype();
	}
	
}
/**
 * Collects the local style bits 
 * @param propertyDescriptors list into which new property descriptors are added for the constructor style bits
 * 
 * @since 1.0.0
 */
protected void mergeStyleBits(List propertyDescriptors, EClass eClass){

	BeanDecorator beanDecor = Utilities.getBeanDecorator(eClass);
	Map styleDetails = beanDecor.getStyleDetails();
	Iterator sweetProperties = styleDetails.keySet().iterator();
	while(sweetProperties.hasNext()){
		// The properties are an array with three elements.  The first is a String[] of names, the second is a String[] of iitializationStrings
		// and the third is an Integer[] of actual values
		String propertyName = (String)sweetProperties.next();
		Object[] sweetValues = (Object[]) styleDetails.get(propertyName);
		String[] names = (String[])sweetValues[0];
		String[] initStrings = (String[])sweetValues[1];	
		Integer[] values = (Integer[])sweetValues[2];
		
		// If there is just one value then add another with a name of NONE and value of -1
		if(names.length == 1){
			names = new String[] { "true" , "false" };
			initStrings = new String[] { initStrings[0] , "" };
			values = new Integer[] { values[0] , new Integer(-1) };
		}
		 
		IPropertyDescriptor property = new StyleBitPropertyDescriptor(propertyName,names,initStrings,values);
		propertyDescriptors.add(property);		
	}

}
	
public Object getPropertyValue(Object descriptorID) {
	if(descriptorID instanceof EStructuralFeature) {
		return super.getPropertyValue(descriptorID);
	} else if(descriptorID instanceof StyleBitPropertyID) {
		WidgetProxyAdapter widgetProxyAdapter = (WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaInstance) getTarget());
		int currentStyleValue = widgetProxyAdapter.getStyle();
		// The style value represents all the bits together.  We must return a single int value the represents the style that is set
		// for the family that this property represents
		Integer[] availableValues = ((StyleBitPropertyID)descriptorID).bitValues;
		for (int i = 0; i < availableValues.length; i++) {
			if((availableValues[i].intValue() & currentStyleValue) == availableValues[i].intValue()){
				return availableValues[i];
			}
		}
		return new Integer(-1);
	} else {
		return "???";
	}
}

public boolean isPropertySet(Object descriptorID) {
	if(descriptorID instanceof EStructuralFeature){
		return super.isPropertySet(descriptorID);
	} else {
		return false; // TODO
	}
}

public void resetPropertyValue(Object descriptorID) {
	if(descriptorID instanceof EStructuralFeature){
		super.resetPropertyValue(descriptorID);
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
