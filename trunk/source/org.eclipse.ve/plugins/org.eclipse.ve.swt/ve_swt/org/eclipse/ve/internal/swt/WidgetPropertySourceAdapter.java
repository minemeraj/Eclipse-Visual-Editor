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
 *  $Revision: 1.14 $  $Date: 2004-05-14 17:31:29 $ 
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
import org.eclipse.jem.java.JavaHelpers;

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

/**
 * Find existing style that is equivalent, and update it.
 * @param current  PTFieldAccess or PTInfixExpression
 * @param id
 * @param val
 * @return true, updated, false not updated.
 */
protected boolean updateStyleBit (PTExpression current, StyleBitPropertyID id, Object val) {
	if (current instanceof PTFieldAccess) {
    	// We have a single bit map
     	if (isSameStyleFamily(((PTFieldAccess)current).getField(),id)) {   
     		((PTFieldAccess)current).setField(id.getInitString(val));
     		return true;
     	}
     	return false ;
	}
	else if (current instanceof PTInfixExpression) {
		PTInfixExpression infix = (PTInfixExpression)current ;
		return updateStyleBit(infix.getLeftOperand(), id, val) ||
		       updateStyleBit(infix.getRightOperand(), id, val);
	}
	return false;
}

/**
 * Helper to add/update style bit to a style argument.
 * Assume the syle is constructed from Infix/Field OR expressions only 
 * 
 * @param current must be a PTFieldAccess, or PTInfixExpression
 * @param id, val the added style
 * @return updated or augmented with OR ('|')
 */
protected PTExpression  addStyleBit(PTExpression current, StyleBitPropertyID id, Object val) {	
	if (updateStyleBit(current, id, val))
		return current ;
	
	// Need to add a new element
	PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT");
	PTFieldAccess field = InstantiationFactory.eINSTANCE.createPTFieldAccess(name,id.getInitString(val));
	
     if (current instanceof PTFieldAccess) {
			PTInfixExpression inFixExpression =InstantiationFactory.eINSTANCE.createPTInfixExpression(
					current,
					PTInfixOperator.OR_LITERAL,
					field,
					null);
			return inFixExpression;
     }
     else {
     	// Assume it is already an inFix, add it to the right.     	     
		PTInfixExpression inFixExpression =InstantiationFactory.eINSTANCE.createPTInfixExpression(
				field,
				PTInfixOperator.OR_LITERAL,
				current,
				null);
		return inFixExpression;
     }
}
/**
 * called from removeStyleBit (PTExpression cur, StyleBitPropertyID id, Object val)
 * Keep looking to an Infix with one of its nodes that has to be removed - this will
 * remove the current infix all together.
 */
private PTExpression removeStyleBit (PTInfixExpression parent, PTExpression cur, StyleBitPropertyID id, Object val) {
	
	if (cur instanceof PTFieldAccess)
		return cur ; // Arrived to a leaf; stop
	
	PTInfixExpression current = (PTInfixExpression) cur;
	if (current.getLeftOperand() instanceof PTFieldAccess) {
		if (isSameStyleFamily(((PTFieldAccess)current.getLeftOperand()).getField(),id)) {
			// Need to remove the left operant from the list
			return current.getRightOperand();
		}
	}
	if (current.getRightOperand() instanceof PTFieldAccess) {
		if (isSameStyleFamily(((PTFieldAccess)current.getRightOperand()).getField(),id)) {
			// Need to remove the right operant from the list
			return current.getLeftOperand();
		}
	} 
	current.setLeftOperand(removeStyleBit(current, current.getLeftOperand(), id, val));
	current.setRightOperand(removeStyleBit(current, current.getRightOperand(), id, val));
	return current;
}

/**
 * This is the starting point of going through an Infix tree to remove a style bit
 * @param current root of the tree
 * @param id
 * @param val
 * @return
 */
protected PTExpression removeStyleBit (PTExpression cur, StyleBitPropertyID id, Object val) {
	if (cur instanceof PTFieldAccess) {
		// A single style bit
		if (isSameStyleFamily(((PTFieldAccess)cur).getField(),id)) {   

			((PTFieldAccess)cur).setField(NONE);     	
     	}
		return cur;
	}
	else {
		PTInfixExpression current = (PTInfixExpression) cur;
		if (current.getLeftOperand() instanceof PTFieldAccess) {
			if (isSameStyleFamily(((PTFieldAccess)current.getLeftOperand()).getField(),id)) {
				// Need to remove the left operant from the list
				return current.getRightOperand();
			}
		}
		if (current.getRightOperand() instanceof PTFieldAccess) {
			if (isSameStyleFamily(((PTFieldAccess)current.getRightOperand()).getField(),id)) {
				// Need to remove the right operant from the list
				return current.getLeftOperand();
			}
		} 
		current.setLeftOperand(removeStyleBit(current, current.getLeftOperand(), id, val));
		current.setRightOperand(removeStyleBit(current, current.getRightOperand(), id, val));
		return current;
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
		
		PTExpression flagsArgument = null;
		// TODO: We need to ensure that the argument is of the right type (int for style)
		if (numberOfArguments == 1 || numberOfArguments == 0) {
				JavaHelpers shell = Utilities.getJavaClass("org.eclipse.swt.widgets.Shell", ((IJavaObjectInstance) target).eResource().getResourceSet());
				if (shell.isAssignableFrom(((IJavaObjectInstance) target).getJavaType())) {
					if (numberOfArguments == 1) {
						// Shell that already have a style argument
						flagsArgument = (PTExpression) classInstanceCreation.getArguments().get(0);
					} else {
						// Shell with no arguments yet, create one
						//TODO: SHELL_TRIM is not valid for WinCE
						flagsArgument = factory.createPTFieldAccess(factory.createPTName("org.eclipse.swt.SWT"),"SHELL_TRIM");						
					}
				}
		}
		else if (numberOfArguments == 2)
			flagsArgument = (PTExpression) classInstanceCreation.getArguments().get(1);
		
		if(flagsArgument != null){
			PTExpression newArgument;
			// If just a single style bit is set this will be a PTFieldAccess 
			if (unset)
				newArgument = removeStyleBit(flagsArgument,propertyID, val);
			else
				newArgument = addStyleBit(flagsArgument, propertyID, val);
			
			int index = classInstanceCreation.getArguments().indexOf(flagsArgument);
			if (index<0) {
				classInstanceCreation.getArguments().add(newArgument);
			}
			else {
			  classInstanceCreation.getArguments().remove(flagsArgument);
			  classInstanceCreation.getArguments().add(index,newArgument);
			}
			ParseTreeAllocation newParseTreeAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
			newParseTreeAlloc.setExpression(parseTreeAllocation.getExpression());
			getJavaObjectInstance().setAllocation(newParseTreeAlloc);
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
