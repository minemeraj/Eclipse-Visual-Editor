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
 *  $Revision: 1.8 $  $Date: 2004-04-01 18:52:24 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

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
	public static IJavaInstance getFeatureValue(IJavaObjectInstance javaObject, String featureName) {
		EStructuralFeature feature = javaObject.eClass().getEStructuralFeature(featureName);
		Object featureValue = javaObject.eGet(feature);
		if(featureValue != null){
			return (IJavaInstance)featureValue;
		} else {
			IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost(javaObject);
			return beanProxyHost.getBeanPropertyValue(feature);
		}
	}
}