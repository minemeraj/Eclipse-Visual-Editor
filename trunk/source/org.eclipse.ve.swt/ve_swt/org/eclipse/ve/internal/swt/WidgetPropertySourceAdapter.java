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
 *  $Revision: 1.5 $  $Date: 2004-03-09 00:07:48 $ 
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
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.jem.internal.beaninfo.BeanDecorator;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.beaninfo.impl.BeanDecoratorImpl;
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
	
	private static String[] EXPERT_FILTER_FLAGS = new String[] {IPropertySheetEntry.FILTER_ID_EXPERT};	
	
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
		String fDisplayName;
		String[] fNames;
		String[] fInitStrings;
		Integer[] fValues;
		boolean fIsExpert;
		private ILabelProvider labelProvider;  // Performance cache because property sheets asks for this twice always		
		public StyleBitPropertyDescriptor(String propertyName, String displayName , boolean isExpert ,  String[] names, String[] initStrings, Integer[] values){
			super(new StyleBitPropertyID(propertyName,values),propertyName);
			fPropertyName = propertyName;
			fNames = names;
			fDisplayName = displayName;
			fIsExpert = isExpert;
			fInitStrings = initStrings;
			fValues = values; 
		}
		public CellEditor createPropertyEditor(Composite parent){
			return new EnumeratedIntValueCellEditor(parent,fNames,fValues,fInitStrings);
		}
		public ILabelProvider getLabelProvider() {
			if(labelProvider == null){
				return new EnumeratedIntValueLabelProvider(fNames,fValues);
			}
			return labelProvider;
		}
		public String[] getFilterFlags() {
			return fIsExpert ? EXPERT_FILTER_FLAGS : null;
		}
		public String getDisplayName() {
			return fDisplayName;
		}
	};
	
public IPropertyDescriptor[] getPropertyDescriptors() {

	IPropertyDescriptor[] propertyDescriptors = super.getPropertyDescriptors();
	List descriptorsList = new ArrayList(propertyDescriptors.length + 5);
	for (int i = 0; i < propertyDescriptors.length; i++) {
		descriptorsList.add(propertyDescriptors[i]);
	}
	mergeStyleBits(descriptorsList,((EObject)getTarget()).eClass());
	IPropertyDescriptor[] resultArray = new IPropertyDescriptor[descriptorsList.size()];
	descriptorsList.toArray(resultArray);
	return resultArray;
	
}

/**
 * Collects the style bits, creates property descriptors for them, and adds them to the argument 
 * @param propertyDescriptors list into which new property descriptors are added for the constructor style bits
 * 
 * @since 1.0.0
 */
protected void mergeStyleBits(List propertyDescriptors, EClass eClass){

	BeanDecorator beanDecor = Utilities.getBeanDecorator(eClass);
	Map styleDetails = beanDecor.getStyleDetails();
	while(styleDetails.size() == 0){
		eClass = ((JavaClass)eClass).getSupertype();
		if(eClass == null) return;
		beanDecor = Utilities.getBeanDecorator(eClass);
		styleDetails = beanDecor.getStyleDetails();		
	}
	
	Iterator sweetProperties = styleDetails.keySet().iterator();
	while(sweetProperties.hasNext()){
		// The properties are instances of BeanDecoratorImpl.Sweet
		// and the third is an Integer[] of actual values
		String propertyName = (String)sweetProperties.next();
		BeanDecoratorImpl.SweetStyleBits styleBits = (BeanDecoratorImpl.SweetStyleBits) styleDetails.get(propertyName);
		String[] names = styleBits.fNames;
		String[] initStrings = styleBits.fInitStrings;	
		Integer[] values = styleBits.fValues;
		
		// If there is just one value then change this to be ON and add another one with OFF
		// The propertyName is also changed to be the first name
		// This is for style bits like SWT.BORDER that become called "border" with "BORDER" and "UNSET";
		if(names.length == 1){
			names = new String[] { names[0] , "UNSET" };
			initStrings = new String[] { initStrings[0] , "" };
			values = new Integer[] { values[0] , new Integer(-1) };
		}
		IPropertyDescriptor property = new StyleBitPropertyDescriptor(propertyName,styleBits.fDisplayName,styleBits.fIsExpert,names,initStrings,values);
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
		// The tree for creating the example is
		// PTClassInstanceCreation
		// 2 arguments>
		// 		First is PTInstanceReference to the parent container
		// 		Second is PTNumberLiteral with the token of the style bits
	}
}
}
