package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: BeanUtilities.java,v $
 *  $Revision: 1.14 $  $Date: 2004-05-26 08:44:17 $ 
 */

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.ILabelProvider;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.*;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;
import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.BeanComposition;

import org.eclipse.ve.internal.cdm.AnnotationEMF;
import org.eclipse.ve.internal.cdm.CDMFactory;
import org.eclipse.ve.internal.java.rules.IBeanNameProposalRule;

public class BeanUtilities {
	
	/**
	 * Create the object for the qualified class name.  The resourceSet is used only for reflection to look
	 * for the class, and the object returned is not added to any resource set - the caller must do that if required
	 * The class can be qualified class, e.g. java.lang.String
	 * The java object has not been added to a resource set, however the calls of this method
	 * may want to create a BeanProxyHost adaptor for it.  This is done as part of this method
	 * because if not then it cannot occur because no adaptor factory can be found
	 */
	public static IJavaInstance createJavaObject(
			String qualifiedClassName,
			ResourceSet aResourceSet,
			String initString) {
		return createJavaObject(JavaRefFactory.eINSTANCE.reflectType(qualifiedClassName, aResourceSet), aResourceSet, initString != null ? InstantiationFactory.eINSTANCE.createInitStringAllocation(initString) : null);
	}
	
	/**
	 * Create the object for the qualified class name.  The resourceSet is used only for reflection to look
	 * for the class, and the object returned is not added to any resource set - the caller must do that if required
	 * The class can be qualified class, e.g. java.lang.String
	 * The java object has not been added to a resource set, however the calls of this method
	 * may want to create a BeanProxyHost adaptor for it.  This is done as part of this method
	 * because if not then it cannot occur because no adaptor factory can be found
	 */
	public static IJavaInstance createJavaObject(
		String qualifiedClassName,
		ResourceSet aResourceSet,
		JavaAllocation allocation) {
		return createJavaObject(JavaRefFactory.eINSTANCE.reflectType(qualifiedClassName, aResourceSet), aResourceSet, allocation);
	}

	/**
	 * Create a java object given the class. If the resourceset is null, then it won't have
	 * the beanproxyhost created. However, if a beanproxy host is needed before this gets set into
	 * any resource, then the resource set must be supplied.
	 */
	public static IJavaInstance createJavaObject(
			JavaHelpers javaHelpers,
			ResourceSet aResourceSet,
			String initString) {
		return createJavaObject(javaHelpers, aResourceSet, initString != null ? InstantiationFactory.eINSTANCE.createInitStringAllocation(initString) : null);
	}
	
	/**
	 * Create a java object given the class. If the resourceset is null, then it won't have
	 * the beanproxyhost created. However, if a beanproxy host is needed before this gets set into
	 * any resource, then the resource set must be supplied.
	 */
	public static IJavaInstance createJavaObject(
		JavaHelpers javaHelpers,
		ResourceSet aResourceSet,
		JavaAllocation allocation) {
		IJavaInstance result = null;
		result = (IJavaInstance) javaHelpers.getEPackage().getEFactoryInstance().create(javaHelpers);
		result.setAllocation(allocation);

		if (aResourceSet != null) {
			// This method call on BeanProxyUtilities creates an adaptor in the argument resource set
			BeanProxyUtilities.getBeanProxyHost(result, aResourceSet);
		}

		return result;
	}

	public static IJavaObjectInstance createString(ResourceSet aResourceSet, String unquotedInitializationString) {
		return (IJavaObjectInstance) createJavaObject("java.lang.String", aResourceSet, createStringInitString(unquotedInitializationString)); //$NON-NLS-1$
	}

	/**
	 * Take a string and turn it into a init string for the String class. I.e. quote it and escape any imbedded quotes or escape chars.
	 */
	public static String createStringInitString(String value) {
		StringBuffer sb = new StringBuffer(value.length());
		sb.append('"');
		int sl = value.length();
		for (int i = 0; i < sl; i++) {
			char c = value.charAt(i);
			if (c == '"' || c == '\\')
				sb.append('\\'); // We need to escape it.
			sb.append(c);
		}
		sb.append('"');
		return sb.toString();
	}
	
	/** 
	 * @param newJavaBean   - A newly created JavaBean
	 * @param name		 	- The beanName which will become the field name
	 */
	public static void setBeanName(IJavaInstance newJavaBean, String name){
		
		CDMFactory fact = CDMFactory.eINSTANCE;
		AnnotationEMF an = fact.createAnnotationEMF();
		if(an!=null){
			EStringToStringMapEntryImpl sentry = (EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
			sentry.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			sentry.setValue(name);
			CDEUtilities.putEMapEntry(an.getKeyedValues(), sentry);
			an.setAnnotates(newJavaBean);
		}
	}

	/**
	 * Answer whether or not this is the class being composed
	 */
	public static boolean isThisPart(IJavaObjectInstance aBean) {

		EStructuralFeature containerSF = aBean.eContainmentFeature();
		return containerSF.getName().equals("thisPart"); //$NON-NLS-1$

	}

	/**
	 * @param javaObjectInstance
	 * @param featureName
	 * Return the feature value.  Either is is set in the EMF model so we get it
	 * or else we go to the target VM
	 * 
	 * @since 1.0.0
	 */
	public static IJavaInstance getFeatureValue(IJavaInstance javaObject, String featureName) {
		EStructuralFeature feature = javaObject.eClass().getEStructuralFeature(featureName);
		if(feature == null) {
			return null;
		} else {
			return getFeatureValue(javaObject,feature);
		}
	}
	/**
	 * @param javaObjectInstance
	 * @param featureName
	 * Return the feature value.  Either is is set in the EMF model so we get it
	 * or else we go to the target VM
	 * 
	 * @since 1.0.0
	 */	
	public static IJavaInstance getFeatureValue(IJavaInstance javaObject, EStructuralFeature feature){
		Object featureValue = javaObject.eGet(feature);
		if(featureValue != null){
			return (IJavaInstance)featureValue;
		} else {
			IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost(javaObject);
			return beanProxyHost.getBeanPropertyValue(feature);
		}
	}
	
	/**
	 * 
	 * @param classNameToCollect - The string of the class being searched for, e.g. "java.awt.Button" to find all buttons
	 * @param objects - the objects are added to this list (will be the IJavaInstance)
	 * @param labels - the labels that come from the label providers
	 * @param beanComposition - bean composition 
	 * @param editDomain - edit domain
	 * 
	 * @since 1.0.0
	 */
	public static void collectFreeFormParts(String classNameToCollect, List objects, List labels, BeanComposition beanComposition,EditDomain editDomain){
		
		// Having got the root object we can now ask it for all of its child objects that match the desired type we are supposed to list
		JavaClass javaClass = (JavaClass) Utilities.getJavaClass(classNameToCollect,beanComposition.eResource().getResourceSet());
		
		Iterator components = beanComposition.getMembers().iterator();
		while(components.hasNext()){
			Object component = components.next();
			if ( component instanceof IJavaObjectInstance ) { // We might have non java components, nor are we interested in primitives.	 
				// Test the class
				IJavaInstance javaComponent = (IJavaObjectInstance) component;
				JavaHelpers componentType= javaComponent.getJavaType();
				if ( javaClass.isAssignableFrom(componentType)) {
					objects.add(component);	
					// The label used for the icon is the same one used by the JavaBeans tree view
					ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(componentType);
					if ( labelProvider != null ) {
						labels.add(labelProvider.getText(component));		
					} else { 
						// If no label provider exists use the toString of the target VM JavaBean itself
						labels.add(BeanProxyUtilities.getBeanProxy(javaComponent).toBeanString()); 
					}
				}
			}
		}
		// Now we know the children set the items
		String[] items = new String[labels.size()];
		System.arraycopy(labels.toArray(),0,items,0,items.length);

		Arrays.sort(items);
		ArrayList a = new ArrayList() ;
		for (int i = 0; i < items.length; i++) {			
			for (int j = 0; j < labels.size(); j++) {
				if (items[i].equals(labels.get(j))) {
					a.add(objects.get(j));
					break;
				}
			}		
		}
	}	
}