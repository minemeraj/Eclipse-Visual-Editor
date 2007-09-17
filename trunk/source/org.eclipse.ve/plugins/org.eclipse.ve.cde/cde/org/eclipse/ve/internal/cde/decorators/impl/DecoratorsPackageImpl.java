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
package org.eclipse.ve.internal.cde.decorators.impl;
/*
 *  $RCSfile: DecoratorsPackageImpl.java,v $
 *  $Revision: 1.10 $  $Date: 2007-09-17 14:17:13 $ 
 */

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsFactory;
import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator;
import org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator;
import org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation;
import org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;
import org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl;
import org.eclipse.ve.internal.cdm.CDMPackage;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DecoratorsPackageImpl extends EPackageImpl implements DecoratorsPackage {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass basePropertyDecoratorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertySourceAdapterInformationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertyDescriptorDecoratorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertyDescriptorInformationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featureDescriptorDecoratorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass classDescriptorDecoratorEClass = null;

	
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
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DecoratorsPackageImpl() {
		super(eNS_URI, DecoratorsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	public static DecoratorsPackage init() {
		DecoratorsPackage p = initGen();
		if (EcorePlugin.IS_ECLIPSE_RUNNING) {
			// Normally this should not need to be tested for eclipse running because CDE is not
			// meant to run outside of Eclipse. But at one point we needed a standalone migration 
			// utility for the palette (VE 1.0 -> 1.1) that ran outside of Eclipse, so we need the test for it.
			// Register some default property editors for things like String, Boolean, etc...
			org.eclipse.ve.internal.cde.properties.PropertyEditorRegistry.registerDefaultEditors();
		}
		return p;
	}
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
	public static DecoratorsPackage initGen() {
		if (isInited) return (DecoratorsPackage)EPackage.Registry.INSTANCE.getEPackage(DecoratorsPackage.eNS_URI);

		// Obtain or create and register package
		DecoratorsPackageImpl theDecoratorsPackage = (DecoratorsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DecoratorsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new DecoratorsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CDMPackage.eINSTANCE.eClass();
		EcorePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		PalettePackageImpl thePalettePackage = (PalettePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PalettePackage.eNS_URI) instanceof PalettePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PalettePackage.eNS_URI) : PalettePackage.eINSTANCE);
		UtilityPackageImpl theUtilityPackage = (UtilityPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(UtilityPackage.eNS_URI) instanceof UtilityPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(UtilityPackage.eNS_URI) : UtilityPackage.eINSTANCE);

		// Create package meta-data objects
		theDecoratorsPackage.createPackageContents();
		thePalettePackage.createPackageContents();
		theUtilityPackage.createPackageContents();

		// Initialize created meta-data
		theDecoratorsPackage.initializePackageContents();
		thePalettePackage.initializePackageContents();
		theUtilityPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDecoratorsPackage.freeze();

		return theDecoratorsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBasePropertyDecorator() {
		return basePropertyDecoratorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasePropertyDecorator_CellEditorValidatorClassnames() {
		return (EAttribute)basePropertyDecoratorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasePropertyDecorator_LabelProviderClassname() {
		return (EAttribute)basePropertyDecoratorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasePropertyDecorator_CellEditorClassname() {
		return (EAttribute)basePropertyDecoratorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasePropertyDecorator_NullInvalid() {
		return (EAttribute)basePropertyDecoratorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasePropertyDecorator_EntryExpandable() {
		return (EAttribute)basePropertyDecoratorEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getClassDescriptorDecorator() {
		return classDescriptorDecoratorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassDescriptorDecorator_CustomizerClassname() {
		return (EAttribute)classDescriptorDecoratorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassDescriptorDecorator_TreeViewClassname() {
		return (EAttribute)classDescriptorDecoratorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassDescriptorDecorator_GraphViewClassname() {
		return (EAttribute)classDescriptorDecoratorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassDescriptorDecorator_ModelAdapterClassname() {
		return (EAttribute)classDescriptorDecoratorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassDescriptorDecorator_DefaultPalette() {
		return (EAttribute)classDescriptorDecoratorEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getClassDescriptorDecorator_Graphic() {
		return (EReference)classDescriptorDecoratorEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassDescriptorDecorator_LabelProviderClassname() {
		return (EAttribute)classDescriptorDecoratorEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeatureDescriptorDecorator() {
		return featureDescriptorDecoratorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureDescriptorDecorator_DisplayNameString() {
		return (EReference)featureDescriptorDecoratorEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureDescriptorDecorator_DescriptionString() {
		return (EReference)featureDescriptorDecoratorEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureDescriptorDecorator_Hidden() {
		return (EAttribute)featureDescriptorDecoratorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureDescriptorDecorator_HelpContextIdsString() {
		return (EAttribute)featureDescriptorDecoratorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureDescriptorDecorator_Preferred() {
		return (EAttribute)featureDescriptorDecoratorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureDescriptorDecorator_CategoryString() {
		return (EReference)featureDescriptorDecoratorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureDescriptorDecorator_FilterFlagStrings() {
		return (EReference)featureDescriptorDecoratorEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertyDescriptorDecorator() {
		return propertyDescriptorDecoratorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyDescriptorDecorator_DesigntimeProperty() {
		return (EAttribute)propertyDescriptorDecoratorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyDescriptorDecorator_AlwaysIncompatible() {
		return (EAttribute)propertyDescriptorDecoratorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertySourceAdapterInformation() {
		return propertySourceAdapterInformationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertySourceAdapterInformation_PropertySourceAdapterClassname() {
		return (EAttribute)propertySourceAdapterInformationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertyDescriptorInformation() {
		return propertyDescriptorInformationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyDescriptorInformation_Adapter() {
		return (EAttribute)propertyDescriptorInformationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyDescriptorInformation_PropertyDescriptorClassname() {
		return (EAttribute)propertyDescriptorInformationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DecoratorsFactory getDecoratorsFactory() {
		return (DecoratorsFactory)getEFactoryInstance();
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
		basePropertyDecoratorEClass = createEClass(BASE_PROPERTY_DECORATOR);
		createEAttribute(basePropertyDecoratorEClass, BASE_PROPERTY_DECORATOR__CELL_EDITOR_VALIDATOR_CLASSNAMES);
		createEAttribute(basePropertyDecoratorEClass, BASE_PROPERTY_DECORATOR__LABEL_PROVIDER_CLASSNAME);
		createEAttribute(basePropertyDecoratorEClass, BASE_PROPERTY_DECORATOR__CELL_EDITOR_CLASSNAME);
		createEAttribute(basePropertyDecoratorEClass, BASE_PROPERTY_DECORATOR__NULL_INVALID);
		createEAttribute(basePropertyDecoratorEClass, BASE_PROPERTY_DECORATOR__ENTRY_EXPANDABLE);

		propertySourceAdapterInformationEClass = createEClass(PROPERTY_SOURCE_ADAPTER_INFORMATION);
		createEAttribute(propertySourceAdapterInformationEClass, PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME);

		propertyDescriptorDecoratorEClass = createEClass(PROPERTY_DESCRIPTOR_DECORATOR);
		createEAttribute(propertyDescriptorDecoratorEClass, PROPERTY_DESCRIPTOR_DECORATOR__DESIGNTIME_PROPERTY);
		createEAttribute(propertyDescriptorDecoratorEClass, PROPERTY_DESCRIPTOR_DECORATOR__ALWAYS_INCOMPATIBLE);

		propertyDescriptorInformationEClass = createEClass(PROPERTY_DESCRIPTOR_INFORMATION);
		createEAttribute(propertyDescriptorInformationEClass, PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER);
		createEAttribute(propertyDescriptorInformationEClass, PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME);

		featureDescriptorDecoratorEClass = createEClass(FEATURE_DESCRIPTOR_DECORATOR);
		createEAttribute(featureDescriptorDecoratorEClass, FEATURE_DESCRIPTOR_DECORATOR__HIDDEN);
		createEAttribute(featureDescriptorDecoratorEClass, FEATURE_DESCRIPTOR_DECORATOR__HELP_CONTEXT_IDS_STRING);
		createEAttribute(featureDescriptorDecoratorEClass, FEATURE_DESCRIPTOR_DECORATOR__PREFERRED);
		createEReference(featureDescriptorDecoratorEClass, FEATURE_DESCRIPTOR_DECORATOR__CATEGORY_STRING);
		createEReference(featureDescriptorDecoratorEClass, FEATURE_DESCRIPTOR_DECORATOR__FILTER_FLAG_STRINGS);
		createEReference(featureDescriptorDecoratorEClass, FEATURE_DESCRIPTOR_DECORATOR__DISPLAY_NAME_STRING);
		createEReference(featureDescriptorDecoratorEClass, FEATURE_DESCRIPTOR_DECORATOR__DESCRIPTION_STRING);

		classDescriptorDecoratorEClass = createEClass(CLASS_DESCRIPTOR_DECORATOR);
		createEAttribute(classDescriptorDecoratorEClass, CLASS_DESCRIPTOR_DECORATOR__CUSTOMIZER_CLASSNAME);
		createEAttribute(classDescriptorDecoratorEClass, CLASS_DESCRIPTOR_DECORATOR__TREE_VIEW_CLASSNAME);
		createEAttribute(classDescriptorDecoratorEClass, CLASS_DESCRIPTOR_DECORATOR__GRAPH_VIEW_CLASSNAME);
		createEAttribute(classDescriptorDecoratorEClass, CLASS_DESCRIPTOR_DECORATOR__MODEL_ADAPTER_CLASSNAME);
		createEAttribute(classDescriptorDecoratorEClass, CLASS_DESCRIPTOR_DECORATOR__DEFAULT_PALETTE);
		createEAttribute(classDescriptorDecoratorEClass, CLASS_DESCRIPTOR_DECORATOR__LABEL_PROVIDER_CLASSNAME);
		createEReference(classDescriptorDecoratorEClass, CLASS_DESCRIPTOR_DECORATOR__GRAPHIC);
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
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		UtilityPackage theUtilityPackage = (UtilityPackage)EPackage.Registry.INSTANCE.getEPackage(UtilityPackage.eNS_URI);
		CDMPackage theCDMPackage = (CDMPackage)EPackage.Registry.INSTANCE.getEPackage(CDMPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		basePropertyDecoratorEClass.getESuperTypes().add(theEcorePackage.getEAnnotation());
		propertySourceAdapterInformationEClass.getESuperTypes().add(theEcorePackage.getEAnnotation());
		propertyDescriptorDecoratorEClass.getESuperTypes().add(this.getFeatureDescriptorDecorator());
		propertyDescriptorDecoratorEClass.getESuperTypes().add(this.getBasePropertyDecorator());
		propertyDescriptorInformationEClass.getESuperTypes().add(theEcorePackage.getEAnnotation());
		featureDescriptorDecoratorEClass.getESuperTypes().add(theEcorePackage.getEAnnotation());
		classDescriptorDecoratorEClass.getESuperTypes().add(this.getFeatureDescriptorDecorator());
		classDescriptorDecoratorEClass.getESuperTypes().add(theCDMPackage.getKeyedValueHolder());

		// Initialize classes and features; add operations and parameters
		initEClass(basePropertyDecoratorEClass, BasePropertyDecorator.class, "BasePropertyDecorator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBasePropertyDecorator_CellEditorValidatorClassnames(), ecorePackage.getEString(), "cellEditorValidatorClassnames", null, 0, -1, BasePropertyDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasePropertyDecorator_LabelProviderClassname(), ecorePackage.getEString(), "labelProviderClassname", null, 0, 1, BasePropertyDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasePropertyDecorator_CellEditorClassname(), ecorePackage.getEString(), "cellEditorClassname", null, 0, 1, BasePropertyDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasePropertyDecorator_NullInvalid(), ecorePackage.getEBoolean(), "nullInvalid", null, 0, 1, BasePropertyDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasePropertyDecorator_EntryExpandable(), ecorePackage.getEBoolean(), "entryExpandable", null, 0, 1, BasePropertyDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertySourceAdapterInformationEClass, PropertySourceAdapterInformation.class, "PropertySourceAdapterInformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPropertySourceAdapterInformation_PropertySourceAdapterClassname(), ecorePackage.getEString(), "propertySourceAdapterClassname", null, 0, 1, PropertySourceAdapterInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertyDescriptorDecoratorEClass, PropertyDescriptorDecorator.class, "PropertyDescriptorDecorator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPropertyDescriptorDecorator_DesigntimeProperty(), ecorePackage.getEBoolean(), "designtimeProperty", null, 0, 1, PropertyDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPropertyDescriptorDecorator_AlwaysIncompatible(), ecorePackage.getEBoolean(), "alwaysIncompatible", null, 0, 1, PropertyDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertyDescriptorInformationEClass, PropertyDescriptorInformation.class, "PropertyDescriptorInformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPropertyDescriptorInformation_Adapter(), ecorePackage.getEBoolean(), "adapter", "true", 0, 1, PropertyDescriptorInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPropertyDescriptorInformation_PropertyDescriptorClassname(), ecorePackage.getEString(), "propertyDescriptorClassname", null, 0, 1, PropertyDescriptorInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(featureDescriptorDecoratorEClass, FeatureDescriptorDecorator.class, "FeatureDescriptorDecorator", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFeatureDescriptorDecorator_Hidden(), ecorePackage.getEBoolean(), "hidden", null, 0, 1, FeatureDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeatureDescriptorDecorator_HelpContextIdsString(), ecorePackage.getEString(), "helpContextIdsString", null, 0, -1, FeatureDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeatureDescriptorDecorator_Preferred(), ecorePackage.getEBoolean(), "preferred", null, 0, 1, FeatureDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeatureDescriptorDecorator_CategoryString(), theUtilityPackage.getAbstractString(), null, "categoryString", null, 0, 1, FeatureDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeatureDescriptorDecorator_FilterFlagStrings(), theUtilityPackage.getAbstractString(), null, "filterFlagStrings", null, 0, -1, FeatureDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeatureDescriptorDecorator_DisplayNameString(), theUtilityPackage.getAbstractString(), null, "displayNameString", null, 0, 1, FeatureDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeatureDescriptorDecorator_DescriptionString(), theUtilityPackage.getAbstractString(), null, "descriptionString", null, 0, 1, FeatureDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(featureDescriptorDecoratorEClass, ecorePackage.getEBoolean(), "isFiltered", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "flag", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(classDescriptorDecoratorEClass, ClassDescriptorDecorator.class, "ClassDescriptorDecorator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getClassDescriptorDecorator_CustomizerClassname(), ecorePackage.getEString(), "customizerClassname", null, 0, 1, ClassDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClassDescriptorDecorator_TreeViewClassname(), ecorePackage.getEString(), "treeViewClassname", null, 0, 1, ClassDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClassDescriptorDecorator_GraphViewClassname(), ecorePackage.getEString(), "graphViewClassname", null, 0, 1, ClassDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClassDescriptorDecorator_ModelAdapterClassname(), ecorePackage.getEString(), "modelAdapterClassname", null, 0, 1, ClassDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClassDescriptorDecorator_DefaultPalette(), ecorePackage.getEString(), "defaultPalette", null, 0, 1, ClassDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClassDescriptorDecorator_LabelProviderClassname(), ecorePackage.getEString(), "labelProviderClassname", null, 0, 1, ClassDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getClassDescriptorDecorator_Graphic(), theUtilityPackage.getGraphic(), null, "graphic", null, 0, 1, ClassDescriptorDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/emf/2002/GenModel
		createGenModelAnnotations();
	}
	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/GenModel</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createGenModelAnnotations() {
		String source = "http://www.eclipse.org/emf/2002/GenModel";		
		addAnnotation
		  (basePropertyDecoratorEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is base information for properties. This will be on an EClassifier for how to edit a property of the type of the EClassifier."
		   });		
		addAnnotation
		  (getBasePropertyDecorator_LabelProviderClassname(), 
		   source, 
		   new String[] {
			 "documentation", "Label Provider Classname. If set, but is null, then that means there explicitly isn\'t one and don\'t use default."
		   });		
		addAnnotation
		  (getBasePropertyDecorator_CellEditorClassname(), 
		   source, 
		   new String[] {
			 "documentation", "Cell Editor Classname. If set, but is null, then that means there explicitly isn\'t one and don\'t use default."
		   });		
		addAnnotation
		  (getBasePropertyDecorator_NullInvalid(), 
		   source, 
		   new String[] {
			 "documentation", "Are null settings invalid. If this is true, then they will be prevented. If this is false, then the cell editor and validator will be used to determine if null is valid or not.\r\n\r\nThe search order will be find it explicitly set on:\r\n  1) The BasePropertyDescriptor on the feature, then if not set,\r\n  2) The BasePropertyDescriptor on the class, then if not set,\r\n  3) use false as the value."
		   });		
		addAnnotation
		  (getBasePropertyDecorator_EntryExpandable(), 
		   source, 
		   new String[] {
			 "documentation", "If this returns true, then the entry will expand in the property sheet if \r\nthere are child entries. If this returns false then it won\'t expand. This is useful when the cell editor handles the complete customization\r\nand it is not desired to allow sub-property customization.\r\n\r\nThe search order will be find it explicitly set on:\r\n  1) The BasePropertyDescriptor on the feature, then if not set,\r\n  2) The BasePropertyDescriptor on the class, then if not set,\r\n  3) use true as the value."
		   });		
		addAnnotation
		  (propertySourceAdapterInformationEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is the information required to establish a PropertySourceAdapter.\r\n\r\nThis will be decorating a EClassifier to indicate what  PropertySourceAdapter to use for this EClassifier."
		   });		
		addAnnotation
		  (getPropertySourceAdapterInformation_PropertySourceAdapterClassname(), 
		   source, 
		   new String[] {
			 "documentation", "Adapter classname. If explicitly set to null (versus not set, which is a default of null, isSet will return true, but value will be null), then this means there is no PropertySourceAdapter for this class, ever."
		   });		
		addAnnotation
		  (propertyDescriptorDecoratorEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is the decorator for a StructuralFeature to supply the information for editing this property."
		   });		
		addAnnotation
		  (getPropertyDescriptorDecorator_DesigntimeProperty(), 
		   source, 
		   new String[] {
			 "documentation", "Whether this property is designtime. If not explicitly set, then ignore this setting. If true then it is shown only on property sheet and not available for connections. If false, then it is available only for connections and not on property sheet."
		   });		
		addAnnotation
		  (getPropertyDescriptorDecorator_AlwaysIncompatible(), 
		   source, 
		   new String[] {
			 "documentation", "If true, then when multiple selections of this property is made, they will be considered incompatible."
		   });		
		addAnnotation
		  (propertyDescriptorInformationEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is the information required to establish a PropertyDescriptorAdapter or a PropertyDescriptor.\r\n\r\nThis will be decorating a RefStructuralFeature to indicate what  PropertyDescriptorAdapter or PropertyDescscriptor  to use for this feature.\r\n\r\nIt depends upon whether the type flag is Adapter is true. If the flag is true, then an adapter will be created once for the feature.\r\n\r\nIf the flag is false, then it will create a descriptor each time a source is asked for descriptors. It will assume to have either a constructor that takes a RefObject and a RefStructuralFeature or one that takes just a RefObject. The RefObject is the source of the feature. This is useful when the descriptor needs to know the source to be able to answer its questions correctly, such as the celleditor, which may vary depending upon the source."
		   });		
		addAnnotation
		  (featureDescriptorDecoratorEClass, 
		   source, 
		   new String[] {
			 "documentation", "This descriptor decorator for any kind of feature. It contains info that is generalized to features."
		   });		
		addAnnotation
		  (featureDescriptorDecoratorEClass.getEOperations().get(0), 
		   source, 
		   new String[] {
			 "documentation", "Answer whether this filter flag passed in is in this features filterFlags."
		   });		
		addAnnotation
		  (classDescriptorDecoratorEClass, 
		   source, 
		   new String[] {
			 "documentation", "The descriptor decorator for a class. Currently it adds nothing over FeatureDescriptorDecortor."
		   });		
		addAnnotation
		  (getClassDescriptorDecorator_CustomizerClassname(), 
		   source, 
		   new String[] {
			 "documentation", "Classname string of the Customizer class.\r\n\r\nThe format of the string is \"classname:initdata\" or \"plugin/classname:initdata\""
		   });		
		addAnnotation
		  (getClassDescriptorDecorator_TreeViewClassname(), 
		   source, 
		   new String[] {
			 "documentation", "Classname of the TreeView EditPart.\r\n\r\nThe format of the string is \"classname:initdata\" or \"plugin/classname:initdata\""
		   });		
		addAnnotation
		  (getClassDescriptorDecorator_GraphViewClassname(), 
		   source, 
		   new String[] {
			 "documentation", "Classname of the GraphView EditPart.\r\n\r\nThe format of the string is \"classname:initdata\" or \"plugin/classname:initdata\""
		   });		
		addAnnotation
		  (getClassDescriptorDecorator_ModelAdapterClassname(), 
		   source, 
		   new String[] {
			 "documentation", "Classname of the Model Adapter. This is the class that either implements all of the interfaces required by the model adapter, or it should be an IAdaptable that will return such adapters. See IModelAdapterFactory.\r\n\r\nThe format of the string is \"classname:initdata\" or \"plugin/classname:initdata\""
		   });		
		addAnnotation
		  (getClassDescriptorDecorator_DefaultPalette(), 
		   source, 
		   new String[] {
			 "documentation", "The default palette to use for this class if not specified by other means. It is in the form a MOF reference, i.e. \"filename#id of palette\"."
		   });		
		addAnnotation
		  (getClassDescriptorDecorator_LabelProviderClassname(), 
		   source, 
		   new String[] {
			 "documentation", "The classname for the label provider to be used for objects of the class that this decorator decorates. This label provider will be used in cases other than property sheet. In that case the labelProvider from the BasePropertyDecorator on this class will be used. (Though the default if there is no BasePropertyDecorator will be to use the string from this label provider, but not the image).\r\n\r\nThe format of the string is \"classname:initdata\" or \"plugin/classname:initdata\""
		   });
	}

}
