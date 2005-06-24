/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: ControlPropertySourceAdapter.java,v $
 *  $Revision: 1.13 $  $Date: 2005-06-24 14:31:24 $ 
 */
import java.util.*;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.beaninfo.vm.BaseBeanInfo;
import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.common.FeatureAttributeValue;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.INumberBeanProxy;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;

import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * Default PropertySourceAdapter for org.eclipse.swt.widgets.Control
 * 
 * If the composite's layout manager is null then bounds/size and location are shown,
 * otherwise they are read only
 */

public class ControlPropertySourceAdapter extends WidgetPropertySourceAdapter {
	
	private static final Integer NO_FACTORY = new Integer(-1);
	private Map factoryArguments = new HashMap(2); // Map for factory arguments, keyed by property name and value is argument number of method invocation
	
	public boolean isPropertySet(Object descriptorID) {
		
		// If we have already looked up this property and found it part of an argument then return
		Number argumentNumber = (Number)factoryArguments.get(descriptorID);
		if(argumentNumber == null) {
			// This is first time lookup
			factoryArguments.put(descriptorID,NO_FACTORY);
			try{
				EStructuralFeature eFeature = null;
				boolean isStyleBit = false;
				if(descriptorID instanceof StyleBitPropertyID){
					eFeature = getBean().getJavaType().getEStructuralFeature("style");
					// Check for null as only BeanInfo with explicit "style" PropertyDescriptors have features, introspected ones do not
					if(eFeature == null) return super.isPropertySet(descriptorID);				
					isStyleBit = true;
				} else {
					eFeature = (EStructuralFeature)descriptorID;
				}
				ParseTreeAllocation parseTreeAllocation = (ParseTreeAllocation) getBean().getAllocation();			
				// 	See whether the property value can come from a factory method, e.g. FormToolkit
				PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator(eFeature);
				// 	The pattern for how a factory is descriptor is on the bean info is:
				// key of "FACTORY_CREATION",
				// value is an array of factory methods each of which is a four arg array:
				// 1 = name of factory receiver, 2 = name of method 3 = index of where in method args property is and 4 = further array of strings of arg types
				// for example
				// FACTORY_CREATION = new Object[] {
				//   new Object[] { "org.eclipse.ui.forms.widgets.FormToolkit" , "createText" , new Integer(1) , 
				//   	new String[] { "org.eclipse.swt.Composite" , "java.lang.String" , "int"} }
				//   new Object[] { "org.eclipse.ui.forms.widgets.FormToolkit" , "createText" , new Integer(1) ,
				//   	new String[] { "org.eclipse.swt.Composite" , "java.lang.String" } }				
				EMap attributes = propertyDecorator.getAttributes();
				Object object = attributes.get(BaseBeanInfo.FACTORY_CREATION);
				if(object != null){
					// The object is a multi arg array for each possible factory method
					Object[] factories = (Object[]) ((FeatureAttributeValue)object).getValue();
					factoryLoop: for (int i = 0; i < factories.length; i++) {
						// The object is a 4 arg array
						Object[] factoryArgs = (Object[]) factories[i];
						// We are going to walk the allocation and compare it against the factory arguments to see if the property value is contained there
						PTMethodInvocation methodInvocation = (PTMethodInvocation)parseTreeAllocation.getExpression();
						PTInstanceReference receiver = (PTInstanceReference)methodInvocation.getReceiver();
						// 	See if the method name is the same as the factory one
						if(methodInvocation.getName().equals(factoryArgs[1])){				
							// 	See if this is a call to the factory object itself as the receiver
							if(receiver.getObject().getJavaType().getQualifiedName().equals(factoryArgs[0])){
								// Match the argument types
								Object[] argTypes = (Object[])factoryArgs[3];
								if (methodInvocation.getArguments().size() == argTypes.length){	
									// 	Walk the parse tree arguments and match their name to the requested type
									for (int j = 0; j < argTypes.length; j++) {
										PTExpression arg = (PTExpression)methodInvocation.getArguments().get(j);
										// Get the JavaClass of the arg name being passed in and the argument to see if they are compatible
										if(!matches(arg,(String)argTypes[j])){
											continue factoryLoop;
										}
									}
									// If we are here then the property is part of the parse tree allocation that matches a factory method
									argumentNumber = (Number)factoryArgs[2];
									factoryArguments.put(descriptorID,argumentNumber);	// Record the argument number that matches the property for re-retrieval
									// Style bits always come from the live object whereas other arguments come from the allocation arg itself
									// The reason style bits still walk into this code is so that the code above that determines the argument number can run
									// and allow the style bit to be set
									if(isStyleBit){
										return super.isPropertySet(descriptorID);
									} else {
										Object parseTreeArgument = methodInvocation.getArguments().get(argumentNumber.intValue());
										return !(parseTreeArgument instanceof PTNullLiteral);
									}
								}
							}
						}
					}
				}
			} catch (ClassCastException exc){
				
			}
		} else if(argumentNumber.intValue() != -1) {
			// The property is part of a factory argument
			PTExpression argument = getFactoryArgument(argumentNumber);
			if(descriptorID instanceof StyleBitPropertyID){
				 // For style bits we need to get the target VM value and compare it against the style bit we're querying
				int currentValue = ((Number) getPropertyValue(descriptorID)).intValue();
				// If the current property value is -1 then this means it is the "UNSET" value from a single value'd property and by definition must be
				// not set
				if (currentValue == STYLE_NOT_SET)
					return false;
				// The current value is the one set on the actual target VM
				// If the explicit style bitAND the current value is 1 then it is set		
			} else {
				// For non style bits just see whether the argument is null or not - TODO need to think about prim values having default values maybe ??
				return !(argument == null || argument instanceof PTNullLiteral);
			}
		}
		return super.isPropertySet(descriptorID);		
	}
	
	protected PTExpression getStyleExpression(PTExpression allocationExp) {
		if (allocationExp instanceof PTMethodInvocation) {
			// Find the argument number of the "style" feature in the factory method call
		}
		return null; // Not found or not of what we expect.
	}	
		
	private PTExpression getFactoryArgument(Number argumentNumber){
		ParseTreeAllocation parseTreeAllocation = (ParseTreeAllocation) getBean().getAllocation();
		PTMethodInvocation methodInvocation = (PTMethodInvocation)parseTreeAllocation.getExpression();
		return (PTExpression) methodInvocation.getArguments().get(argumentNumber.intValue());		
	}
	
	public void resetPropertyValue(Object descriptorID) {
		
		Number argumentNumber = (Number)factoryArguments.get(descriptorID);
		if(argumentNumber != null && argumentNumber.intValue() != -1){
			// If we are part of a factory method then a reset means we null out the argument
			if(descriptorID instanceof StyleBitPropertyID){
				
			} else {
				// Non style bits just get nulled out
				ParseTreeAllocation parseTreeAllocation = (ParseTreeAllocation) getBean().getAllocation();
				PTMethodInvocation methodInvocation = (PTMethodInvocation)parseTreeAllocation.getExpression();
				PTNullLiteral nullLiteral = InstantiationFactory.eINSTANCE.createPTNullLiteral();
				methodInvocation.getArguments().set(argumentNumber.intValue(),nullLiteral);
				getBean().setAllocation(parseTreeAllocation);  // Touch the allocation to cause a target VM refresh
			}
		} else {
			super.resetPropertyValue(descriptorID);
		}
	}
	
	public void setPropertyValue(Object descriptorID, Object val) {
		// If the property descriptor comes from a factory argument then we need to do parse tree manipulation
		Number argumentNumber = (Number)factoryArguments.get(descriptorID);
		if(argumentNumber == null || argumentNumber == NO_FACTORY){
			super.setPropertyValue(descriptorID, val);			
		} else {
			// Change the allocation so that the argument is replaced
			if(descriptorID instanceof StyleBitPropertyID){
				// Change the style bit
				int intValue = val != null ? ((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) val)).intValue() : STYLE_NOT_SET;
				// See if we are changing it. If not, then don't do anything. Don't want to signal an unneeded change.	
				if (((Number) getPropertyValue(descriptorID)).intValue() == intValue) {
					return;	// The property has not changed. Don't do anything.			
				}
				ParseTreeAllocation allocation = (ParseTreeAllocation) getBean().getAllocation();
				PTMethodInvocation methodInvocation = (PTMethodInvocation)allocation.getExpression();
				PTExpression existingStyleBitExpression = (PTExpression)methodInvocation.getArguments().get(argumentNumber.intValue());				
				PTExpression changedStyleExpression = getChangedStyleExpression(existingStyleBitExpression,(StyleBitPropertyID)descriptorID,intValue);
				// It is possible that the existing style bit is still an argument in which case just set it back at the same position
				// but if not then add it at the end (this occurs if you do something like go from SWT.ARROW to SWT.ARROW | SWT.UP
				if(methodInvocation.getArguments().size() == argumentNumber.intValue()){
					methodInvocation.getArguments().add(changedStyleExpression);					
				} else {
					methodInvocation.getArguments().set(argumentNumber.intValue(),changedStyleExpression);
				}
				getBean().setAllocation(allocation);  // Set the allocation back into the bean to trigger notification
			} else {
				// Change a regular property		
				ParseTreeAllocation allocation = (ParseTreeAllocation) getBean().getAllocation();
				PTMethodInvocation methodInvocation = (PTMethodInvocation)allocation.getExpression();	
				PTExpression expression = getExpression((IJavaInstance)val);
				methodInvocation.getArguments().set(argumentNumber.intValue(),expression);
				getBean().setAllocation(allocation);  // Set the allocation back into the bean to trigger notification				
			} 
		}
	}
	
	private PTExpression getExpression(IJavaInstance val){
		
		PTInstanceReference reference = InstantiationFactory.eINSTANCE.createPTInstanceReference();
		reference.setObject((IJavaObjectInstance)val);
		return reference;				
		
	}

	private boolean matches(PTExpression anExpression, String expectedArgument){
		// null is compatible with any argument
		if(anExpression instanceof PTNullLiteral){
			return true;
		}
		JavaHelpers expressionClass = getJavaClass(anExpression);
		if(expressionClass == null) return false;
		JavaHelpers expectedClass = Utilities.getJavaType(expectedArgument,getBean().eResource().getResourceSet());	
		return expectedClass.isAssignableFrom(expressionClass);
	}
	
	private JavaHelpers getJavaClass(PTExpression anExpression){
		if(anExpression instanceof PTName){
			return getJavaClass((PTName)anExpression);
		} else if (anExpression instanceof PTInstanceReference){
			return getJavaClass((PTInstanceReference)anExpression);			
		} else if (anExpression instanceof PTFieldAccess) {
			PTFieldAccess fieldAccess = (PTFieldAccess)anExpression;
			JavaClass receiverClass = (JavaClass)getJavaClass(fieldAccess.getReceiver());
			Field field = receiverClass.getFieldExtended(fieldAccess.getField());
			return (JavaHelpers)field.getEType();
		} else if (anExpression instanceof PTStringLiteral){
			return getJavaClass((PTStringLiteral)anExpression);
		} else {
			return null;
		}
	}
	
	private JavaClass getJavaClass(PTStringLiteral aStringLiteral){
		return Utilities.getJavaClass("java.lang.String",getBean().eResource().getResourceSet());		
	}
	
	private JavaClass getJavaClass(PTName aName){
		return Utilities.getJavaClass(aName.getName(),getBean().eResource().getResourceSet());		
	}
	private JavaClass getJavaClass(PTInstanceReference anExpression){
		return (JavaClass) anExpression.getObject().getJavaType();		
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		IPropertyDescriptor[] descriptors = super.getPropertyDescriptors();

		IJavaObjectInstance compositeJavaObjectInstance = null;
		if(getEObject().eResource() != null) {
			compositeJavaObjectInstance = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(
					getEObject(),
					JavaInstantiation.getReference(
							Utilities.getJavaClass("org.eclipse.swt.widgets.Composite",getEObject().eResource().getResourceSet()), //$NON-NLS-1$
							SWTConstants.SF_COMPOSITE_CONTROLS));
		}
		
		boolean explicitUserSizing = false;
		// Top level things like Shells don't have a parent
		if (compositeJavaObjectInstance == null) {
			explicitUserSizing = true;
		} else {
			CompositeProxyAdapter compositeProxyAdapter = (CompositeProxyAdapter) BeanProxyUtilities.getBeanProxyHost(compositeJavaObjectInstance);
			IBeanProxy layoutBeanProxy = BeanSWTUtilities.invoke_getLayout(compositeProxyAdapter.getBeanProxy());
			// null layout and FillLayout don't have layout data
			if (layoutBeanProxy == null) 
				explicitUserSizing = true;
		}
		
		List descriptorList = new ArrayList(descriptors.length);			
		loop: for (int i = 0; i<descriptors.length; i++) {
			IPropertyDescriptor pd = descriptors[i];
			if (pd.getId() instanceof EStructuralFeature) {
				EStructuralFeature sf = (EStructuralFeature)pd.getId();
				String fn = sf.getName();				
				if(explicitUserSizing) {
					// exclude the layoutData property
					if ("layoutData".equals(fn)) //$NON-NLS-1$
						continue loop;
				} else {
					// exclude bounds/size/location because we have a layout manager
					if ("bounds".equals(fn) || "size".equals(fn) || "location".equals(fn)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						continue loop;
				}

				// LayoutData is wrappered so it is treated differently to allow values on un-set layoutData instances to be set
				if("layoutData".equals(fn)){ //$NON-NLS-1$
					// We need the class of the layoutData to set.  This comes from looking at the policyFactory
					// for our container's layoutManager
					CompositeProxyAdapter compositeProxyAdapter = (CompositeProxyAdapter)BeanProxyUtilities.getBeanProxyHost(compositeJavaObjectInstance);					
					EditDomain domain = compositeProxyAdapter.getBeanProxyDomain().getEditDomain();
					ILayoutPolicyFactory factory = BeanSWTUtilities.getLayoutPolicyFactory(compositeProxyAdapter.getBeanProxy(), domain);
					IPropertyDescriptor layoutPD = factory.getConstraintPropertyDescriptor(sf);
					if (layoutPD != null) {
					    if (layoutPD instanceof INeedData) 
					        ((INeedData)layoutPD).setData(domain);
						descriptorList.add(layoutPD);
					}
				} else {
					descriptorList.add(pd);
				}
			} else {
				descriptorList.add(pd);  // Without a structural feature it is included as required by constructor style bits
			}
		}
		return (IPropertyDescriptor[]) descriptorList.toArray(new IPropertyDescriptor[descriptorList.size()]);
	}
}