package org.eclipse.ve.internal.cde.utility;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: UtilityPackage.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.ve.internal.cde.utility.UtilityFactory
 * @generated
 */
public interface UtilityPackage extends EPackage{


	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "utility"; //$NON-NLS-1$

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.utility.impl.AbstractStringImpl <em>Abstract String</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.utility.impl.AbstractStringImpl
	 * @see org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl#getAbstractString()
	 * @generated
	 */
	int ABSTRACT_STRING = 0;
	/**
	 * The number of structural features of the the '<em>Abstract String</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_STRING_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.utility.impl.GraphicImpl <em>Graphic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.utility.impl.GraphicImpl
	 * @see org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl#getGraphic()
	 * @generated
	 */
	int GRAPHIC = 4;
	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.utility.impl.GIFFileGraphicImpl <em>GIF File Graphic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.utility.impl.GIFFileGraphicImpl
	 * @see org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl#getGIFFileGraphic()
	 * @generated
	 */
	int GIF_FILE_GRAPHIC = 5;
	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.utility.impl.ConstantStringImpl <em>Constant String</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.utility.impl.ConstantStringImpl
	 * @see org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl#getConstantString()
	 * @generated
	 */
	int CONSTANT_STRING = 1;
	/**
	 * The feature id for the '<em><b>String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT_STRING__STRING = ABSTRACT_STRING_FEATURE_COUNT + 0;
	/**
	 * The number of structural features of the the '<em>Constant String</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT_STRING_FEATURE_COUNT = ABSTRACT_STRING_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.utility.impl.TranslatableStringImpl <em>Translatable String</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.utility.impl.TranslatableStringImpl
	 * @see org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl#getTranslatableString()
	 * @generated
	 */
	int TRANSLATABLE_STRING = 6;
	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.utility.impl.ResourceBundleImpl <em>Resource Bundle</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.utility.impl.ResourceBundleImpl
	 * @see org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl#getResourceBundle()
	 * @generated
	 */
	int RESOURCE_BUNDLE = 2;
	/**
	 * The number of structural features of the the '<em>Resource Bundle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_BUNDLE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.utility.impl.URLResourceBundleImpl <em>URL Resource Bundle</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.utility.impl.URLResourceBundleImpl
	 * @see org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl#getURLResourceBundle()
	 * @generated
	 */
	int URL_RESOURCE_BUNDLE = 3;
	/**
	 * The feature id for the '<em><b>Bundle Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int URL_RESOURCE_BUNDLE__BUNDLE_NAME = RESOURCE_BUNDLE_FEATURE_COUNT + 0;
	/**
	 * The feature id for the '<em><b>Bundle UR Ls</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int URL_RESOURCE_BUNDLE__BUNDLE_UR_LS = RESOURCE_BUNDLE_FEATURE_COUNT + 1;
	/**
	 * The number of structural features of the the '<em>URL Resource Bundle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int URL_RESOURCE_BUNDLE_FEATURE_COUNT = RESOURCE_BUNDLE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the the '<em>Graphic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHIC_FEATURE_COUNT = EcorePackage.EOBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Resource Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GIF_FILE_GRAPHIC__RESOURCE_NAME = GRAPHIC_FEATURE_COUNT + 0;
	/**
	 * The number of structural features of the the '<em>GIF File Graphic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GIF_FILE_GRAPHIC_FEATURE_COUNT = GRAPHIC_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSLATABLE_STRING__KEY = ABSTRACT_STRING_FEATURE_COUNT + 0;
	/**
	 * The feature id for the '<em><b>Bundle</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSLATABLE_STRING__BUNDLE = ABSTRACT_STRING_FEATURE_COUNT + 1;
	/**
	 * The number of structural features of the the '<em>Translatable String</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSLATABLE_STRING_FEATURE_COUNT = ABSTRACT_STRING_FEATURE_COUNT + 2;


	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.emf.IGraphic <em>IGraphic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.emf.IGraphic
	 * @see org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl#getIGraphic()
	 * @generated
	 */
	int IGRAPHIC = 7;

	/**
	 * The number of structural features of the the '<em>IGraphic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IGRAPHIC_FEATURE_COUNT = 0;


	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/ve/internal/cde/utility.ecore"; //$NON-NLS-1$
	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.ve.internal.cde.utility"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UtilityPackage eINSTANCE = org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl.init();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.utility.AbstractString <em>Abstract String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract String</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.AbstractString
	 * @generated
	 */
	EClass getAbstractString();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.utility.Graphic <em>Graphic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Graphic</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.Graphic
	 * @generated
	 */
	EClass getGraphic();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.utility.GIFFileGraphic <em>GIF File Graphic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GIF File Graphic</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.GIFFileGraphic
	 * @generated
	 */
	EClass getGIFFileGraphic();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.utility.GIFFileGraphic#getResourceName <em>Resource Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Resource Name</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.GIFFileGraphic#getResourceName()
	 * @see #getGIFFileGraphic()
	 * @generated
	 */
	EAttribute getGIFFileGraphic_ResourceName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.utility.ConstantString <em>Constant String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constant String</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.ConstantString
	 * @generated
	 */
	EClass getConstantString();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.utility.ConstantString#getString <em>String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>String</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.ConstantString#getString()
	 * @see #getConstantString()
	 * @generated
	 */
	EAttribute getConstantString_String();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.utility.TranslatableString <em>Translatable String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Translatable String</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.TranslatableString
	 * @generated
	 */
	EClass getTranslatableString();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.utility.TranslatableString#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.TranslatableString#getKey()
	 * @see #getTranslatableString()
	 * @generated
	 */
	EAttribute getTranslatableString_Key();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.cde.utility.TranslatableString#getBundle <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Bundle</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.TranslatableString#getBundle()
	 * @see #getTranslatableString()
	 * @generated
	 */
	EReference getTranslatableString_Bundle();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.emf.IGraphic <em>IGraphic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IGraphic</em>'.
	 * @see org.eclipse.ve.internal.cde.emf.IGraphic
	 * @model instanceClass="org.eclipse.ve.internal.cde.emf.IGraphic" 
	 * @generated
	 */
	EClass getIGraphic();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.utility.ResourceBundle <em>Resource Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Bundle</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.ResourceBundle
	 * @generated
	 */
	EClass getResourceBundle();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.utility.URLResourceBundle <em>URL Resource Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>URL Resource Bundle</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.URLResourceBundle
	 * @generated
	 */
	EClass getURLResourceBundle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.utility.URLResourceBundle#getBundleName <em>Bundle Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bundle Name</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.URLResourceBundle#getBundleName()
	 * @see #getURLResourceBundle()
	 * @generated
	 */
	EAttribute getURLResourceBundle_BundleName();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.ve.internal.cde.utility.URLResourceBundle#getBundleURLs <em>Bundle UR Ls</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Bundle UR Ls</em>'.
	 * @see org.eclipse.ve.internal.cde.utility.URLResourceBundle#getBundleURLs()
	 * @see #getURLResourceBundle()
	 * @generated
	 */
	EAttribute getURLResourceBundle_BundleURLs();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	UtilityFactory getUtilityFactory();

}
