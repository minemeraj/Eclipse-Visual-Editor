/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $RCSfile: UtilityPackageImpl.java,v $
 *  $Revision: 1.6 $  $Date: 2005-09-15 21:27:16 $ 
 */
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;

import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl;
import org.eclipse.ve.internal.cde.emf.IGraphic;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl;
import org.eclipse.ve.internal.cde.utility.AbstractString;
import org.eclipse.ve.internal.cde.utility.ConstantString;
import org.eclipse.ve.internal.cde.utility.GIFFileGraphic;
import org.eclipse.ve.internal.cde.utility.Graphic;
import org.eclipse.ve.internal.cde.utility.ResourceBundle;
import org.eclipse.ve.internal.cde.utility.TranslatableString;
import org.eclipse.ve.internal.cde.utility.URLResourceBundle;
import org.eclipse.ve.internal.cde.utility.UtilityFactory;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;
import org.eclipse.ve.internal.cdm.impl.CDMPackageImpl;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UtilityPackageImpl extends EPackageImpl implements UtilityPackage {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractStringEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constantStringEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resourceBundleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass urlResourceBundleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass graphicEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass gifFileGraphicEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass translatableStringEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iGraphicEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private UtilityPackageImpl() {
		super(eNS_URI, UtilityFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static UtilityPackage init() {
		if (isInited) return (UtilityPackage)EPackage.Registry.INSTANCE.getEPackage(UtilityPackage.eNS_URI);

		// Obtain or create and register package
		UtilityPackageImpl theUtilityPackage = (UtilityPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof UtilityPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new UtilityPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CDMPackageImpl.init();
		EcorePackageImpl.init();

		// Obtain or create and register interdependencies
		PalettePackageImpl thePalettePackage = (PalettePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PalettePackage.eNS_URI) instanceof PalettePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PalettePackage.eNS_URI) : PalettePackage.eINSTANCE);
		DecoratorsPackageImpl theDecoratorsPackage = (DecoratorsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DecoratorsPackage.eNS_URI) instanceof DecoratorsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DecoratorsPackage.eNS_URI) : DecoratorsPackage.eINSTANCE);

		// Create package meta-data objects
		theUtilityPackage.createPackageContents();
		thePalettePackage.createPackageContents();
		theDecoratorsPackage.createPackageContents();

		// Initialize created meta-data
		theUtilityPackage.initializePackageContents();
		thePalettePackage.initializePackageContents();
		theDecoratorsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUtilityPackage.freeze();

		return theUtilityPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractString() {
		return abstractStringEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGraphic() {
		return graphicEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGIFFileGraphic() {
		return gifFileGraphicEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGIFFileGraphic_ResourceName() {
		return (EAttribute)gifFileGraphicEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConstantString() {
		return constantStringEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstantString_String() {
		return (EAttribute)constantStringEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTranslatableString() {
		return translatableStringEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTranslatableString_Key() {
		return (EAttribute)translatableStringEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTranslatableString_Bundle() {
		return (EReference)translatableStringEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIGraphic() {
		return iGraphicEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResourceBundle() {
		return resourceBundleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getURLResourceBundle() {
		return urlResourceBundleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getURLResourceBundle_BundleName() {
		return (EAttribute)urlResourceBundleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getURLResourceBundle_BundleURLs() {
		return (EAttribute)urlResourceBundleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UtilityFactory getUtilityFactory() {
		return (UtilityFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		abstractStringEClass = createEClass(ABSTRACT_STRING);

		constantStringEClass = createEClass(CONSTANT_STRING);
		createEAttribute(constantStringEClass, CONSTANT_STRING__STRING);

		resourceBundleEClass = createEClass(RESOURCE_BUNDLE);

		urlResourceBundleEClass = createEClass(URL_RESOURCE_BUNDLE);
		createEAttribute(urlResourceBundleEClass, URL_RESOURCE_BUNDLE__BUNDLE_NAME);
		createEAttribute(urlResourceBundleEClass, URL_RESOURCE_BUNDLE__BUNDLE_UR_LS);

		graphicEClass = createEClass(GRAPHIC);

		gifFileGraphicEClass = createEClass(GIF_FILE_GRAPHIC);
		createEAttribute(gifFileGraphicEClass, GIF_FILE_GRAPHIC__RESOURCE_NAME);

		translatableStringEClass = createEClass(TRANSLATABLE_STRING);
		createEAttribute(translatableStringEClass, TRANSLATABLE_STRING__KEY);
		createEReference(translatableStringEClass, TRANSLATABLE_STRING__BUNDLE);

		iGraphicEClass = createEClass(IGRAPHIC);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EcorePackageImpl theEcorePackage = (EcorePackageImpl)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Add supertypes to classes
		constantStringEClass.getESuperTypes().add(this.getAbstractString());
		urlResourceBundleEClass.getESuperTypes().add(this.getResourceBundle());
		graphicEClass.getESuperTypes().add(theEcorePackage.getEObject());
		graphicEClass.getESuperTypes().add(this.getIGraphic());
		gifFileGraphicEClass.getESuperTypes().add(this.getGraphic());
		translatableStringEClass.getESuperTypes().add(this.getAbstractString());

		// Initialize classes and features; add operations and parameters
		initEClass(abstractStringEClass, AbstractString.class, "AbstractString", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(constantStringEClass, ConstantString.class, "ConstantString", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConstantString_String(), ecorePackage.getEString(), "string", " ", 0, 1, ConstantString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resourceBundleEClass, ResourceBundle.class, "ResourceBundle", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(urlResourceBundleEClass, URLResourceBundle.class, "URLResourceBundle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getURLResourceBundle_BundleName(), ecorePackage.getEString(), "bundleName", null, 0, 1, URLResourceBundle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getURLResourceBundle_BundleURLs(), ecorePackage.getEString(), "bundleURLs", null, 0, -1, URLResourceBundle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(graphicEClass, Graphic.class, "Graphic", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(gifFileGraphicEClass, GIFFileGraphic.class, "GIFFileGraphic", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGIFFileGraphic_ResourceName(), ecorePackage.getEString(), "resourceName", null, 0, 1, GIFFileGraphic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(translatableStringEClass, TranslatableString.class, "TranslatableString", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTranslatableString_Key(), ecorePackage.getEString(), "key", null, 0, 1, TranslatableString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTranslatableString_Bundle(), this.getResourceBundle(), null, "bundle", null, 0, 1, TranslatableString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iGraphicEClass, IGraphic.class, "IGraphic", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

}
