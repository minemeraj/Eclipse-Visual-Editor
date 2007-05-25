/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.utility.impl;
/*
 *  $RCSfile: UtilityFactoryImpl.java,v $
 *  $Revision: 1.9 $  $Date: 2007-05-25 04:09:36 $ 
 */
import org.eclipse.ve.internal.cde.utility.*;

import java.util.Arrays;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UtilityFactoryImpl extends EFactoryImpl implements UtilityFactory {

	
	
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static UtilityFactory init() {
		try {
			UtilityFactory theUtilityFactory = (UtilityFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/ve/internal/cde/utility.ecore"); 
			if (theUtilityFactory != null) {
				return theUtilityFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new UtilityFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	public UtilityFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case UtilityPackage.CONSTANT_STRING: return createConstantString();
			case UtilityPackage.URL_RESOURCE_BUNDLE: return createURLResourceBundle();
			case UtilityPackage.GIF_FILE_GRAPHIC: return createGIFFileGraphic();
			case UtilityPackage.TRANSLATABLE_STRING: return createTranslatableString();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * Some helper methods for creating the string objects.
	 */
	public ConstantString createConstantString(String aString) {
		ConstantString o = createConstantString();
		o.setString(aString);
		return o;
	}

	public TranslatableString createTranslatableString(ResourceBundle bundle, String key) {
		TranslatableString o = createTranslatableString();
		o.setBundle(bundle);
		o.setKey(key);
		return o;
	}

	public ResourceBundle createURLResourceBundle(String[] urls, String bundleName) {
		URLResourceBundle o = createURLResourceBundle();
		o.getBundleURLs().addAll(Arrays.asList(urls));
		o.setBundleName(bundleName);
		return o;
	}


	public ResourceBundle createURLResourceBundle(String url, String bundleName) {
		URLResourceBundle o = createURLResourceBundle();
		o.getBundleURLs().add(url);
		o.setBundleName(bundleName);
		return o;
	}

	public GIFFileGraphic createGIFFileGraphic(String resourceName) {
		GIFFileGraphic o = createGIFFileGraphic();
		o.setResourceName(resourceName);
		return o;
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GIFFileGraphic createGIFFileGraphic() {
		GIFFileGraphicImpl gifFileGraphic = new GIFFileGraphicImpl();
		return gifFileGraphic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstantString createConstantString() {
		ConstantStringImpl constantString = new ConstantStringImpl();
		return constantString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TranslatableString createTranslatableString() {
		TranslatableStringImpl translatableString = new TranslatableStringImpl();
		return translatableString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public URLResourceBundle createURLResourceBundle() {
		URLResourceBundleImpl urlResourceBundle = new URLResourceBundleImpl();
		return urlResourceBundle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UtilityPackage getUtilityPackage() {
		return (UtilityPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static UtilityPackage getPackage() {
		return UtilityPackage.eINSTANCE;
	}

}
