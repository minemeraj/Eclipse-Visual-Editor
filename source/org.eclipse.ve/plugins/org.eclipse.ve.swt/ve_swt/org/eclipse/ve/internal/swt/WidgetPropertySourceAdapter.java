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
 *  $Revision: 1.13 $  $Date: 2004-05-13 12:27:19 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.*;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.jem.internal.beaninfo.BeanDecorator;
import org.eclipse.jem.internal.beaninfo.FeatureAttributeValue;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.BeanPropertySourceAdapter;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

import org.eclipse.ve.internal.propertysheet.EToolsPropertyDescriptor;

/**
 * 
 * @since 1.0.0
 */
public class WidgetPropertySourceAdapter extends BeanPropertySourceAdapter {
	
	private static String[] EXPERT_FILTER_FLAGS = new String[] {IPropertySheetEntry.FILTER_ID_EXPERT};
	private static final Object SWEET_STYLE_ID = "org.eclipse.ve.sweet.stylebits";
	private static final String SWT_TYPE_ID = "org.eclipse.swt.SWT.";
	private static final String NONE = "NONE";
	
	class StyleBitPropertyID{
		String propertyName;
		public Integer[] bitValues;
		private String[] fInitStrings;
		private String[] fUnqualifiedInitStrings;
		StyleBitPropertyID(String aPropertyName,Integer[] values, String[] initStrings){
			propertyName = aPropertyName;
			bitValues = values;
			fInitStrings = initStrings; 
		}
		public boolean includeInitString(String existingInitString) {
			initializeUnqualifiedInitStrings();
 			// Check the intString against the existing ones
			for (int i = 0; i < fUnqualifiedInitStrings.length; i++) {
				if(fUnqualifiedInitStrings[i].equals(existingInitString)){
					return true;
				}
			}
			return false;			
		}
		public String getInitString(Object val) {
			initializeUnqualifiedInitStrings();
			// The argument is an IJavaInstance of an int.  Get the actual int value
			IIntegerBeanProxy integerBeanProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)val);
			int bitValue = integerBeanProxy.intValue();
			for (int i = 0; i < bitValues.length; i++) {
				if(bitValues[i].intValue() == bitValue){
					return fUnqualifiedInitStrings[i];
				}
			}			
			return "???";
		}
		private void initializeUnqualifiedInitStrings(){
			if(fUnqualifiedInitStrings == null){
				fUnqualifiedInitStrings = new String[fInitStrings.length];
				for (int i = 0; i < fInitStrings.length; i++) {
					if(fInitStrings[i].startsWith(SWT_TYPE_ID)){
						// Take out the leading "org.eclipse.swt."
						fUnqualifiedInitStrings[i] = fInitStrings[i].substring(20);
					} else {
						fUnqualifiedInitStrings[i] = fInitStrings[i];
					}
				}
			}			
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
			super(new StyleBitPropertyID(propertyName,values,initStrings),propertyName);
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
	Map styleDetails = getStyleDetails(beanDecor);
	while(styleDetails == null || styleDetails.size() == 0){
		eClass = ((JavaClass)eClass).getSupertype();
		if(eClass == null) return;
		beanDecor = Utilities.getBeanDecorator(eClass);
		styleDetails = getStyleDetails(beanDecor);		
	}
	
	Iterator sweetProperties = styleDetails.keySet().iterator();
	while(sweetProperties.hasNext()){
		// The properties are instances of BeanDecoratorImpl.Sweet
		// and the third is an Integer[] of actual values
		String propertyName = (String)sweetProperties.next();
		SweetStyleBits styleBits = (SweetStyleBits) styleDetails.get(propertyName);
		String[] names = styleBits.fNames;
		String[] initStrings = styleBits.fInitStrings;	
		Integer[] values = styleBits.fValues;
		
		// If there is just one value then change this to be ON and add another one with OFF
		// The propertyName is also changed to be the first name
		// This is for style bits like SWT.BORDER that become called "border" with "BORDER" and "UNSET";
		if(names.length == 1){
			names = new String[] { names[0] , "UNSET" };
			initStrings = new String[] { initStrings[0] , "-1" };
			values = new Integer[] { values[0] , new Integer(-1) };
		}
		IPropertyDescriptor property = new StyleBitPropertyDescriptor(propertyName,styleBits.fDisplayName,styleBits.fIsExpert,names,initStrings,values);
		propertyDescriptors.add(property);		
	}

}

public static class SweetStyleBits{
	public String fPropertyName;
	public String fDisplayName;
	public boolean fIsExpert;
	public String[] fNames;
	public String[] fInitStrings;
	public Integer[] fValues;
	public SweetStyleBits(String propertyName, String displayName, boolean isExpert, String[] names, String[] initStrings, Integer[] values){
		fPropertyName = propertyName;
		fDisplayName = displayName;
		fIsExpert = isExpert;
		fNames = names;
		fInitStrings = initStrings;
		fValues = values;
	}
}
public static Map getStyleDetails(BeanDecorator beanDecorator){

	FeatureAttributeValue value = (FeatureAttributeValue) beanDecorator.getAttributes().get(SWEET_STYLE_ID);
	// If the value has been previously calculated it is cached so return it
	if(value != null && value.isSetValueJava()) return (Map)value.getValueJava();
	// otherwise derive it from the target values
	if(value != null && value.isSetValueProxy()){
		// There are some style bits picked up by introspection
		// Turn them into a map of IDE style bits that we can use, and then cache these back onto the BeanDecorator so 
		// we don't have to re-do this target VM query each time
		IArrayBeanProxy outerArray  = (IArrayBeanProxy) value.getValueProxy();
		Map styleDetails = new HashMap();
		for (int i = 0; i < outerArray.getLength(); i++) {
			try{
				IArrayBeanProxy innerArray = (IArrayBeanProxy) outerArray.get(i);
				// The first element is a String for the internal canonnical name
				String propertyName = ((IStringBeanProxy)innerArray.get(0)).stringValue();
				// The second element is the user visible name
				String displayName = ((IStringBeanProxy)innerArray.get(1)).stringValue();
				// The third element is a Boolean value for whether the property is expert or not
				boolean expert = ((IBooleanBeanProxy)innerArray.get(2)).booleanValue();
				// The next is a three element array of name, initString, and actual value * n for the number of allowble values
				// Iterate over it to extract the names and strings and turn these into two separate String arrays
				IArrayBeanProxy triplicateArray = (IArrayBeanProxy)innerArray.get(3);
				int numberOfValues = triplicateArray.getLength()/3;
				String[] names = new String[numberOfValues];
				String[] initStrings = new String[numberOfValues];
				Integer[] values = new Integer[numberOfValues]; 
				for (int j = 0; j < triplicateArray.getLength(); j = j+3) {
					int index = j/3;
					names[index] = ((IStringBeanProxy)triplicateArray.get(j)).stringValue();
					initStrings[index] = ((IStringBeanProxy)triplicateArray.get(j+1)).stringValue();
					values[index] = new Integer(((IIntegerBeanProxy)triplicateArray.get(j+2)).intValue());
				}							
				styleDetails.put(propertyName, new SweetStyleBits(propertyName,displayName,expert,names,initStrings,values));
			} catch (ThrowableProxy exc){
				SwtPlugin.getDefault().getLogger().log(exc,Level.WARNING);
			}
		}
		value.setValueJava(styleDetails);
		return styleDetails;
	}
	return null;
}
private WidgetProxyAdapter widgetProxyAdapter;
private IJavaObjectInstance javaObjectInstance;
private IJavaObjectInstance getJavaObjectInstance(){
	if(javaObjectInstance == null){
		javaObjectInstance = (IJavaObjectInstance) getTarget();	
	}
	return javaObjectInstance;
}

private WidgetProxyAdapter getWidgetProxyAdapter(){
	if(widgetProxyAdapter == null){
		widgetProxyAdapter = (WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost(getJavaObjectInstance());
	}
	return widgetProxyAdapter;
}

public Object getPropertyValue(Object descriptorID) {
	if(descriptorID instanceof EStructuralFeature) {
		return super.getPropertyValue(descriptorID);
	} else if(descriptorID instanceof StyleBitPropertyID) {
 		int currentStyleValue = getWidgetProxyAdapter().getStyle();
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
	} else if (descriptorID instanceof StyleBitPropertyID) {
		int currentValue = ((Integer)getPropertyValue(descriptorID)).intValue();
		// If the current property value is -1 then this means it is the "UNSET" value from a single value'd property and by definition must be not set
		if(currentValue == -1) return false;
		// The explicit style is obtained by parsing the actual initString style bit to see which were set on
		int explicitStyle = getWidgetProxyAdapter().getExplicitStyle();
		// The current value is the one set on the actual target VM

		// If the explicit style bitAND the current value is 1 then it is set
		return (currentValue & explicitStyle) != 0;
	} else {
		return false;
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
	} else if (feature instanceof StyleBitPropertyID) {
		StyleBitPropertyID propertyID = (StyleBitPropertyID)feature;
		// The tree for creating the example for a single style bit is
		// <allocation xmi:type=ParseTreeAllocation>
		//   <expression xmi:type=PTClassInstanceCreation>
		//     <arguments xmi:type=PTInstanceReference>
		//     <arguments xmi:type=PTFieldAccess field="NONE>
		//       <receiver xmi:type=PTName name="org.eclipse.swt.SWT">
		// And for a set of style bits is
		// <allocation xmi:type=ParseTreeAllocation>
		//   <expression xmi:type=PTClassInstanceCreation>
		//     <arguments xmi:type=PTInstanceReference>
		//     <arguments xmi:type=PTInfixExpression operator=OR>
		//       <leftOperator xmi:type=PTName name="org.eclipse.swt.BORDER">
		//       <rightOperator xmi:type=PTName name="org.eclipse.swt.CHECK">
		ParseTreeAllocation parseTreeAllocation = (ParseTreeAllocation) getJavaObjectInstance().getAllocation();
		PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) parseTreeAllocation.getExpression();
		InstantiationFactory factory = InstantiationFactory.eINSTANCE;		
		// A value of -1 means that we are unsetting the property value
		int intValue = ((IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)val)).intValue();
		boolean unset = intValue == -1;
		int numberOfArguments = classInstanceCreation.getArguments().size();
		if(numberOfArguments == 2){
			Object secondConstructionArgument = classInstanceCreation.getArguments().get(1);
			// If just a single style bit is set this will be a PTFieldAccess 
			if(secondConstructionArgument instanceof PTFieldAccess){
				// See whether the style bit is one we should replace or whether we should add to it
				PTFieldAccess fieldAccess = (PTFieldAccess)secondConstructionArgument;
				String existingFieldName = fieldAccess.getField();
				if (isSameStyleFamily(existingFieldName,propertyID)){
					// The field name is in the same family as the one already set
					if(unset) {
						// When we are down to the last property replace it with the value of NONE
						fieldAccess.setField(NONE);
					} else {
						fieldAccess.setField(propertyID.getInitString(val));
					}
					// We need the BeanProxy and code gen to refresh itself so create a new parse tree allocation
					// and set the Java Object instance with this new allocation.
					ParseTreeAllocation newParseTreeAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
					newParseTreeAlloc.setExpression(parseTreeAllocation.getExpression());
					getJavaObjectInstance().setAllocation(newParseTreeAlloc);
				} else {
					if(unset){
						// We are unsetting a value, however it is not in the same family as an existing value that is set
						// This should not occur, but if it does there's nothing we can do to alter the code
					} else {
						// Add to the existing style bit.  This is done by taking the existing one that is a PTFieldAccess
						// 	and creating a new PTInfix expression for the argument, the left expression of which is a PTName for the bit mask flag
						// 	and the second is the new one being set
						PTInfixExpression inFixExpression =InstantiationFactory.eINSTANCE.createPTInfixExpression(
								factory.createPTFieldAccess(factory.createPTName("org.eclipse.swt.SWT"),existingFieldName),
								PTInfixOperator.OR_LITERAL,
								factory.createPTFieldAccess(factory.createPTName("org.eclipse.swt.SWT"),propertyID.getInitString(val)),
						null
						);
						classInstanceCreation.getArguments().remove(1);
						classInstanceCreation.getArguments().add(inFixExpression);
					}
					// We need the BeanProxy and code gen to refresh itself so create a new parse tree allocation
					// and set the Java Object instance with this new allocation.
					ParseTreeAllocation newParseTreeAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
					newParseTreeAlloc.setExpression(parseTreeAllocation.getExpression());
					getJavaObjectInstance().setAllocation(newParseTreeAlloc);	 // Touch the allocation so listeners (bean proxy/code gen) can deal accordingly 	
				}
			} else if (secondConstructionArgument instanceof PTInfixExpression){
				PTInfixExpression inFix = (PTInfixExpression)secondConstructionArgument;
				PTFieldAccess leftStyle = (PTFieldAccess)inFix.getLeftOperand();
				PTFieldAccess rightStyle = (PTFieldAccess)inFix.getRightOperand();
				if(unset){
					// Scan the arguments to find which one is the set value of the one we are unsetting
					String setInitString = propertyID.fInitStrings[0];
					if(setInitString.startsWith(SWT_TYPE_ID)) setInitString = setInitString.substring(20);
					if( leftStyle.getField().equals(setInitString)){
						inFix.setLeftOperand(null);
						classInstanceCreation.getArguments().remove(1);
						classInstanceCreation.getArguments().add(inFix.asCompressedExpression());
						// The RHS needs to be removed						
					} else if( rightStyle.getField().equals(setInitString)){
						inFix.setRightOperand(null);
						classInstanceCreation.getArguments().remove(1);
						classInstanceCreation.getArguments().add(inFix.asCompressedExpression());						
					} else {
						// The bit being unset must be an extended operand - Find it and remove it						
						int index = 0;
						Iterator iter = inFix.getExtendedOperands().iterator();
						while(iter.hasNext()){
							PTFieldAccess fieldAccess = (PTFieldAccess)iter.next();
							if(fieldAccess.getField().equals(setInitString)){
								inFix.getExtendedOperands().remove(index);
								break;
							}
							index++;
						}
					}
				} else {
					String initString = propertyID.getInitString(val);
					// Scan the arguments to see if any of them match the value we are setting
					if(isSameStyleFamily(leftStyle.getField(),propertyID)){
						// Replace the left operand with our new style bit as it is in the same family
						leftStyle.setField(initString);
					} else if(isSameStyleFamily(rightStyle.getField(),propertyID)){
						// Replace the right operand with our new style bit as it is in the same family
						rightStyle.setField(initString);
					} else {
						// Scan the existing expressionsm to see if we need to reset one
						boolean existingFound = false;
						Iterator iter = inFix.getExtendedOperands().iterator();
						while(iter.hasNext()){
							PTFieldAccess fieldAccess = (PTFieldAccess)iter.next();
							if(isSameStyleFamily(fieldAccess.getField(),propertyID)){
								fieldAccess.setField(propertyID.getInitString(val));
								existingFound = true;
								break;
							}
						}
						if(!existingFound) {
							// The expression we have does not match any of the expressions so create a new one and add it
							PTExpression newExpression = factory.createPTFieldAccess(factory.createPTName("org.eclipse.swt.SWT"),initString);
							inFix.getExtendedOperands().add(newExpression);
						}
					}
				}
				// We need the BeanProxy and code gen to refresh itself so create a new parse tree allocation
				// and set the Java Object instance with this new allocation.
				ParseTreeAllocation newParseTreeAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
				newParseTreeAlloc.setExpression(parseTreeAllocation.getExpression());
				getJavaObjectInstance().setAllocation(newParseTreeAlloc);	 				
			}
		}
	}
}
/**
 * @param existingInitString The name of an existing file on org.eclipse.swt.SWT such as NONE or BORDER
 * @param propertyID Instance of StyleBitPropertyID that we are testing to see whether it is part of the same family or not
 * @return
 * 
 * @since 1.0.0
 */
private boolean isSameStyleFamily(String existingInitString, StyleBitPropertyID propertyID){
	// Trim the field name
	if(existingInitString.startsWith(SWT_TYPE_ID)){
		// Copy everything after "org.eclipse.swt." which is 
		existingInitString = existingInitString.substring(20);
	}
	// NONE is always replaced
	if(existingInitString.equals(NONE)) return true;
	// Iterate over the style bits to see if any of them are in the same family
	return propertyID.includeInitString(existingInitString);
}
}
