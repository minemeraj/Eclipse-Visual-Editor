package org.eclipse.ve.internal.cde.palette.impl;
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
 *  $RCSfile: PalettePackageImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl;
import org.eclipse.ve.internal.cde.palette.AbstractToolEntry;
import org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry;
import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.CategoryCmp;
import org.eclipse.ve.internal.cde.palette.CategoryRef;
import org.eclipse.ve.internal.cde.palette.Container;
import org.eclipse.ve.internal.cde.palette.CreationToolEntry;
import org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry;
import org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry;
import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.GroupCmp;
import org.eclipse.ve.internal.cde.palette.GroupRef;
import org.eclipse.ve.internal.cde.palette.ICDEPaletteEntry;
import org.eclipse.ve.internal.cde.palette.Palette;
import org.eclipse.ve.internal.cde.palette.PaletteCmp;
import org.eclipse.ve.internal.cde.palette.PaletteFactory;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.PaletteRef;
import org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry;
import org.eclipse.ve.internal.cde.palette.ToolEntry;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;
import org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl;
import org.eclipse.ve.internal.cdm.impl.CDMPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PalettePackageImpl extends EPackageImpl implements PalettePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass paletteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass categoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass toolEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass creationToolEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractToolEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass paletteRefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass paletteCmpEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass categoryRefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass categoryCmpEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupCmpEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupRefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass containerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass emfCreationToolEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass emfPrototypeToolEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotatedCreationEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass selectionCreationToolEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass icdePaletteEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType creationFactoryEDataType = null;

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
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PalettePackageImpl() {
		super(eNS_URI, PaletteFactory.eINSTANCE);
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
	public static PalettePackage init() {
		if (isInited) return (PalettePackage)EPackage.Registry.INSTANCE.get(PalettePackage.eNS_URI);

		// Obtain or create and register package.
		PalettePackageImpl thePalettePackage = (PalettePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EPackage ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PalettePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CDMPackageImpl.init();
		EcorePackageImpl.init();

		// Obtain or create and register interdependencies
		DecoratorsPackageImpl theDecoratorsPackage = (DecoratorsPackageImpl)(EPackage.Registry.INSTANCE.get(DecoratorsPackage.eNS_URI) instanceof EPackage ? EPackage.Registry.INSTANCE.get(DecoratorsPackage.eNS_URI) : DecoratorsPackageImpl.eINSTANCE);
		UtilityPackageImpl theUtilityPackage = (UtilityPackageImpl)(EPackage.Registry.INSTANCE.get(UtilityPackage.eNS_URI) instanceof EPackage ? EPackage.Registry.INSTANCE.get(UtilityPackage.eNS_URI) : UtilityPackageImpl.eINSTANCE);

		// Step 1: create meta-model objects
		thePalettePackage.createPackageContents();
		theDecoratorsPackage.createPackageContents();
		theUtilityPackage.createPackageContents();

		// Step 2: complete initialization
		thePalettePackage.initializePackageContents();
		theDecoratorsPackage.initializePackageContents();
		theUtilityPackage.initializePackageContents();

		return thePalettePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPalette() {
		return paletteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPalette_PaletteLabel() {
		return (EReference)paletteEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCategory() {
		return categoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCategory_CategoryLabel() {
		return (EReference)categoryEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroup() {
		return groupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroup_GroupLabel() {
		return (EReference)groupEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntry() {
		return entryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntry_Icon16Name() {
		return (EAttribute)entryEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntry_Icon32Name() {
		return (EAttribute)entryEClass.getEAttributes().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntry_DefaultEntry() {
		return (EAttribute)entryEClass.getEAttributes().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntry_EntryLabel() {
		return (EReference)entryEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntry_EntryShortDescription() {
		return (EReference)entryEClass.getEReferences().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getToolEntry() {
		return toolEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getToolEntry_ToolClassName() {
		return (EAttribute)toolEntryEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCreationToolEntry() {
		return creationToolEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractToolEntry() {
		return abstractToolEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPaletteRef() {
		return paletteRefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPaletteRef_RefControlGroup() {
		return (EReference)paletteRefEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPaletteRef_RefCategories() {
		return (EReference)paletteRefEClass.getEReferences().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPaletteCmp() {
		return paletteCmpEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPaletteCmp_CmpCategories() {
		return (EReference)paletteCmpEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPaletteCmp_CmpControlGroup() {
		return (EReference)paletteCmpEClass.getEReferences().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCategoryRef() {
		return categoryRefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCategoryRef_RefGroups() {
		return (EReference)categoryRefEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCategoryCmp() {
		return categoryCmpEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCategoryCmp_CmpGroups() {
		return (EReference)categoryCmpEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroupCmp() {
		return groupCmpEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroupCmp_CmpEntries() {
		return (EReference)groupCmpEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroupRef() {
		return groupRefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroupRef_RefEntries() {
		return (EReference)groupRefEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getContainer() {
		return containerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEMFCreationToolEntry() {
		return emfCreationToolEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEMFCreationToolEntry_CreationClassURI() {
		return (EAttribute)emfCreationToolEntryEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEMFPrototypeToolEntry() {
		return emfPrototypeToolEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEMFPrototypeToolEntry_PrototypeURI() {
		return (EAttribute)emfPrototypeToolEntryEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotatedCreationEntry() {
		return annotatedCreationEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotatedCreationEntry_Values() {
		return (EReference)annotatedCreationEntryEClass.getEReferences().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotatedCreationEntry_ObjectCreationEntry() {
		return (EReference)annotatedCreationEntryEClass.getEReferences().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSelectionCreationToolEntry() {
		return selectionCreationToolEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSelectionCreationToolEntry_SelectorClassName() {
		return (EAttribute)selectionCreationToolEntryEClass.getEAttributes().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getICDEPaletteEntry() {
		return icdePaletteEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getCreationFactory() {
		return creationFactoryEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PaletteFactory getPaletteFactory() {
		return (PaletteFactory)getEFactoryInstance();
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
		paletteEClass = createEClass(PALETTE);
		createEReference(paletteEClass, PALETTE__PALETTE_LABEL);

		categoryEClass = createEClass(CATEGORY);
		createEReference(categoryEClass, CATEGORY__CATEGORY_LABEL);

		groupEClass = createEClass(GROUP);
		createEReference(groupEClass, GROUP__GROUP_LABEL);

		entryEClass = createEClass(ENTRY);
		createEAttribute(entryEClass, ENTRY__ICON16_NAME);
		createEAttribute(entryEClass, ENTRY__ICON32_NAME);
		createEAttribute(entryEClass, ENTRY__DEFAULT_ENTRY);
		createEReference(entryEClass, ENTRY__ENTRY_LABEL);
		createEReference(entryEClass, ENTRY__ENTRY_SHORT_DESCRIPTION);

		toolEntryEClass = createEClass(TOOL_ENTRY);
		createEAttribute(toolEntryEClass, TOOL_ENTRY__TOOL_CLASS_NAME);

		creationToolEntryEClass = createEClass(CREATION_TOOL_ENTRY);

		abstractToolEntryEClass = createEClass(ABSTRACT_TOOL_ENTRY);

		paletteRefEClass = createEClass(PALETTE_REF);
		createEReference(paletteRefEClass, PALETTE_REF__REF_CONTROL_GROUP);
		createEReference(paletteRefEClass, PALETTE_REF__REF_CATEGORIES);

		paletteCmpEClass = createEClass(PALETTE_CMP);
		createEReference(paletteCmpEClass, PALETTE_CMP__CMP_CATEGORIES);
		createEReference(paletteCmpEClass, PALETTE_CMP__CMP_CONTROL_GROUP);

		categoryRefEClass = createEClass(CATEGORY_REF);
		createEReference(categoryRefEClass, CATEGORY_REF__REF_GROUPS);

		categoryCmpEClass = createEClass(CATEGORY_CMP);
		createEReference(categoryCmpEClass, CATEGORY_CMP__CMP_GROUPS);

		groupCmpEClass = createEClass(GROUP_CMP);
		createEReference(groupCmpEClass, GROUP_CMP__CMP_ENTRIES);

		groupRefEClass = createEClass(GROUP_REF);
		createEReference(groupRefEClass, GROUP_REF__REF_ENTRIES);

		containerEClass = createEClass(CONTAINER);

		emfCreationToolEntryEClass = createEClass(EMF_CREATION_TOOL_ENTRY);
		createEAttribute(emfCreationToolEntryEClass, EMF_CREATION_TOOL_ENTRY__CREATION_CLASS_URI);

		emfPrototypeToolEntryEClass = createEClass(EMF_PROTOTYPE_TOOL_ENTRY);
		createEAttribute(emfPrototypeToolEntryEClass, EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI);

		annotatedCreationEntryEClass = createEClass(ANNOTATED_CREATION_ENTRY);
		createEReference(annotatedCreationEntryEClass, ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY);
		createEReference(annotatedCreationEntryEClass, ANNOTATED_CREATION_ENTRY__VALUES);

		selectionCreationToolEntryEClass = createEClass(SELECTION_CREATION_TOOL_ENTRY);
		createEAttribute(selectionCreationToolEntryEClass, SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME);

		icdePaletteEntryEClass = createEClass(ICDE_PALETTE_ENTRY);

		// Create data types
		creationFactoryEDataType = createEDataType(CREATION_FACTORY);
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
		UtilityPackageImpl theUtilityPackage = (UtilityPackageImpl)EPackage.Registry.INSTANCE.getEPackage(UtilityPackage.eNS_URI);
		EcorePackageImpl theEcorePackage = (EcorePackageImpl)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Add supertypes to classes
		paletteEClass.getESuperTypes().add(this.getContainer());
		categoryEClass.getESuperTypes().add(this.getContainer());
		groupEClass.getESuperTypes().add(this.getContainer());
		entryEClass.getESuperTypes().add(theEcorePackage.getEObject());
		entryEClass.getESuperTypes().add(this.getICDEPaletteEntry());
		toolEntryEClass.getESuperTypes().add(this.getAbstractToolEntry());
		creationToolEntryEClass.getESuperTypes().add(this.getAbstractToolEntry());
		abstractToolEntryEClass.getESuperTypes().add(this.getEntry());
		paletteRefEClass.getESuperTypes().add(this.getPalette());
		paletteCmpEClass.getESuperTypes().add(this.getPalette());
		categoryRefEClass.getESuperTypes().add(this.getCategory());
		categoryCmpEClass.getESuperTypes().add(this.getCategory());
		groupCmpEClass.getESuperTypes().add(this.getGroup());
		groupRefEClass.getESuperTypes().add(this.getGroup());
		containerEClass.getESuperTypes().add(theEcorePackage.getEObject());
		containerEClass.getESuperTypes().add(this.getICDEPaletteEntry());
		emfCreationToolEntryEClass.getESuperTypes().add(this.getCreationToolEntry());
		emfPrototypeToolEntryEClass.getESuperTypes().add(this.getCreationToolEntry());
		annotatedCreationEntryEClass.getESuperTypes().add(this.getAbstractToolEntry());
		selectionCreationToolEntryEClass.getESuperTypes().add(this.getCreationToolEntry());

		// Initialize classes and features; add operations and parameters
		initEClass(paletteEClass, Palette.class, "Palette", IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getPalette_PaletteLabel(), theUtilityPackage.getAbstractString(), null, "paletteLabel", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(categoryEClass, Category.class, "Category", IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getCategory_CategoryLabel(), theUtilityPackage.getAbstractString(), null, "categoryLabel", null, 1, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(groupEClass, Group.class, "Group", IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getGroup_GroupLabel(), theUtilityPackage.getAbstractString(), null, "groupLabel", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(entryEClass, Entry.class, "Entry", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getEntry_Icon16Name(), ecorePackage.getEString(), "icon16Name", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getEntry_Icon32Name(), ecorePackage.getEString(), "icon32Name", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEAttribute(getEntry_DefaultEntry(), ecorePackage.getEBoolean(), "defaultEntry", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);
		initEReference(getEntry_EntryLabel(), theUtilityPackage.getAbstractString(), null, "entryLabel", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);
		initEReference(getEntry_EntryShortDescription(), theUtilityPackage.getAbstractString(), null, "entryShortDescription", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(toolEntryEClass, ToolEntry.class, "ToolEntry", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getToolEntry_ToolClassName(), ecorePackage.getEString(), "toolClassName", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(creationToolEntryEClass, CreationToolEntry.class, "CreationToolEntry", IS_ABSTRACT, !IS_INTERFACE);

		addEOperation(creationToolEntryEClass, this.getCreationFactory(), "createFactory");

		EOperation op = addEOperation(creationToolEntryEClass, null, "setFactory");
		addEParameter(op, this.getCreationFactory(), "factory");

		initEClass(abstractToolEntryEClass, AbstractToolEntry.class, "AbstractToolEntry", IS_ABSTRACT, !IS_INTERFACE);

		initEClass(paletteRefEClass, PaletteRef.class, "PaletteRef", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getPaletteRef_RefControlGroup(), this.getGroup(), null, "refControlGroup", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);
		initEReference(getPaletteRef_RefCategories(), this.getCategory(), null, "refCategories", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(paletteCmpEClass, PaletteCmp.class, "PaletteCmp", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getPaletteCmp_CmpCategories(), this.getCategory(), null, "cmpCategories", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);
		initEReference(getPaletteCmp_CmpControlGroup(), this.getGroup(), null, "cmpControlGroup", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(categoryRefEClass, CategoryRef.class, "CategoryRef", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getCategoryRef_RefGroups(), this.getGroup(), null, "refGroups", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(categoryCmpEClass, CategoryCmp.class, "CategoryCmp", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getCategoryCmp_CmpGroups(), this.getGroup(), null, "cmpGroups", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(groupCmpEClass, GroupCmp.class, "GroupCmp", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getGroupCmp_CmpEntries(), this.getEntry(), null, "cmpEntries", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(groupRefEClass, GroupRef.class, "GroupRef", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getGroupRef_RefEntries(), this.getEntry(), null, "refEntries", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(containerEClass, Container.class, "Container", IS_ABSTRACT, !IS_INTERFACE);

		initEClass(emfCreationToolEntryEClass, EMFCreationToolEntry.class, "EMFCreationToolEntry", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getEMFCreationToolEntry_CreationClassURI(), ecorePackage.getEString(), "creationClassURI", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(emfPrototypeToolEntryEClass, EMFPrototypeToolEntry.class, "EMFPrototypeToolEntry", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getEMFPrototypeToolEntry_PrototypeURI(), ecorePackage.getEString(), "prototypeURI", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(annotatedCreationEntryEClass, AnnotatedCreationEntry.class, "AnnotatedCreationEntry", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getAnnotatedCreationEntry_ObjectCreationEntry(), this.getCreationToolEntry(), null, "objectCreationEntry", null, 1, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);
		initEReference(getAnnotatedCreationEntry_Values(), theEcorePackage.getEObject(), null, "values", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE);

		initEClass(selectionCreationToolEntryEClass, SelectionCreationToolEntry.class, "SelectionCreationToolEntry", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getSelectionCreationToolEntry_SelectorClassName(), ecorePackage.getEString(), "selectorClassName", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE);

		initEClass(icdePaletteEntryEClass, ICDEPaletteEntry.class, "ICDEPaletteEntry", IS_ABSTRACT, IS_INTERFACE);

		// Initialize data types
		initEDataType(creationFactoryEDataType, CreationFactory.class, "CreationFactory", IS_SERIALIZABLE);

		// Create resource
		createResource(eNS_URI);
	}
} //PalettePackageImpl
