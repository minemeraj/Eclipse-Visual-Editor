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
package org.eclipse.ve.internal.cde.palette.impl;
/*
 *  $RCSfile: PalettePackageImpl.java,v $
 *  $Revision: 1.9 $  $Date: 2007-09-17 14:17:13 $ 
 */
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.impl.DecoratorsPackageImpl;
import org.eclipse.ve.internal.cde.palette.AbstractToolEntry;
import org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry;
import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.CategoryCmp;
import org.eclipse.ve.internal.cde.palette.Container;
import org.eclipse.ve.internal.cde.palette.CreationToolEntry;
import org.eclipse.ve.internal.cde.palette.Drawer;
import org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry;
import org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry;
import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.GroupCmp;
import org.eclipse.ve.internal.cde.palette.InitialState;
import org.eclipse.ve.internal.cde.palette.PaletteCmp;
import org.eclipse.ve.internal.cde.palette.PaletteFactory;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;
import org.eclipse.ve.internal.cde.palette.Root;
import org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry;
import org.eclipse.ve.internal.cde.palette.Separator;
import org.eclipse.ve.internal.cde.palette.Stack;
import org.eclipse.ve.internal.cde.palette.ToolEntry;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;
import org.eclipse.ve.internal.cde.utility.impl.UtilityPackageImpl;
import org.eclipse.ve.internal.cdm.CDMPackage;


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
	private EClass rootEClass = null;

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
	private EClass paletteCmpEClass = null;

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
	private EClass drawerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stackEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass separatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum permissionsEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum initialStateEEnum = null;

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
		if (isInited) return (PalettePackage)EPackage.Registry.INSTANCE.getEPackage(PalettePackage.eNS_URI);

		// Obtain or create and register package
		PalettePackageImpl thePalettePackage = (PalettePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof PalettePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new PalettePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CDMPackage.eINSTANCE.eClass();
		EcorePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		UtilityPackageImpl theUtilityPackage = (UtilityPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(UtilityPackage.eNS_URI) instanceof UtilityPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(UtilityPackage.eNS_URI) : UtilityPackage.eINSTANCE);
		DecoratorsPackageImpl theDecoratorsPackage = (DecoratorsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DecoratorsPackage.eNS_URI) instanceof DecoratorsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DecoratorsPackage.eNS_URI) : DecoratorsPackage.eINSTANCE);

		// Create package meta-data objects
		thePalettePackage.createPackageContents();
		theUtilityPackage.createPackageContents();
		theDecoratorsPackage.createPackageContents();

		// Initialize created meta-data
		thePalettePackage.initializePackageContents();
		theUtilityPackage.initializePackageContents();
		theDecoratorsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePalettePackage.freeze();

		return thePalettePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRoot() {
		return rootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRoot_DefEntry() {
		return (EReference)rootEClass.getEStructuralFeatures().get(0);
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
		return (EReference)categoryEClass.getEStructuralFeatures().get(0);
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
		return (EReference)groupEClass.getEStructuralFeatures().get(0);
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
		return (EAttribute)entryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntry_Icon32Name() {
		return (EAttribute)entryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntry_Visible() {
		return (EAttribute)entryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntry_DefaultEntry() {
		return (EAttribute)entryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntry_Id() {
		return (EAttribute)entryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntry_Modification() {
		return (EAttribute)entryEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntry_EntryLabel() {
		return (EReference)entryEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntry_EntryShortDescription() {
		return (EReference)entryEClass.getEStructuralFeatures().get(7);
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
		return (EAttribute)toolEntryEClass.getEStructuralFeatures().get(0);
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
	public EReference getAbstractToolEntry_StringProperties() {
		return (EReference)abstractToolEntryEClass.getEStructuralFeatures().get(0);
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
		return (EReference)paletteCmpEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPaletteCmp_CmpControlGroup() {
		return (EReference)paletteCmpEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPaletteCmp_PaletteLabel() {
		return (EReference)paletteCmpEClass.getEStructuralFeatures().get(2);
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
		return (EReference)categoryCmpEClass.getEStructuralFeatures().get(0);
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
		return (EReference)groupCmpEClass.getEStructuralFeatures().get(0);
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
	public EReference getContainer_Children() {
		return (EReference)containerEClass.getEStructuralFeatures().get(0);
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
		return (EAttribute)emfCreationToolEntryEClass.getEStructuralFeatures().get(0);
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
		return (EAttribute)emfPrototypeToolEntryEClass.getEStructuralFeatures().get(0);
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
		return (EReference)annotatedCreationEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotatedCreationEntry_ObjectCreationEntry() {
		return (EReference)annotatedCreationEntryEClass.getEStructuralFeatures().get(0);
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
		return (EAttribute)selectionCreationToolEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDrawer() {
		return drawerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDrawer_InitialState() {
		return (EAttribute)drawerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStack() {
		return stackEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStack_ActiveEntry() {
		return (EReference)stackEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSeparator() {
		return separatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getPermissions() {
		return permissionsEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getInitialState() {
		return initialStateEEnum;
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
		rootEClass = createEClass(ROOT);
		createEReference(rootEClass, ROOT__DEF_ENTRY);

		categoryEClass = createEClass(CATEGORY);
		createEReference(categoryEClass, CATEGORY__CATEGORY_LABEL);

		groupEClass = createEClass(GROUP);
		createEReference(groupEClass, GROUP__GROUP_LABEL);

		entryEClass = createEClass(ENTRY);
		createEAttribute(entryEClass, ENTRY__ICON16_NAME);
		createEAttribute(entryEClass, ENTRY__ICON32_NAME);
		createEAttribute(entryEClass, ENTRY__VISIBLE);
		createEAttribute(entryEClass, ENTRY__DEFAULT_ENTRY);
		createEAttribute(entryEClass, ENTRY__ID);
		createEAttribute(entryEClass, ENTRY__MODIFICATION);
		createEReference(entryEClass, ENTRY__ENTRY_LABEL);
		createEReference(entryEClass, ENTRY__ENTRY_SHORT_DESCRIPTION);

		toolEntryEClass = createEClass(TOOL_ENTRY);
		createEAttribute(toolEntryEClass, TOOL_ENTRY__TOOL_CLASS_NAME);

		creationToolEntryEClass = createEClass(CREATION_TOOL_ENTRY);

		abstractToolEntryEClass = createEClass(ABSTRACT_TOOL_ENTRY);
		createEReference(abstractToolEntryEClass, ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES);

		paletteCmpEClass = createEClass(PALETTE_CMP);
		createEReference(paletteCmpEClass, PALETTE_CMP__CMP_CATEGORIES);
		createEReference(paletteCmpEClass, PALETTE_CMP__CMP_CONTROL_GROUP);
		createEReference(paletteCmpEClass, PALETTE_CMP__PALETTE_LABEL);

		categoryCmpEClass = createEClass(CATEGORY_CMP);
		createEReference(categoryCmpEClass, CATEGORY_CMP__CMP_GROUPS);

		groupCmpEClass = createEClass(GROUP_CMP);
		createEReference(groupCmpEClass, GROUP_CMP__CMP_ENTRIES);

		containerEClass = createEClass(CONTAINER);
		createEReference(containerEClass, CONTAINER__CHILDREN);

		emfCreationToolEntryEClass = createEClass(EMF_CREATION_TOOL_ENTRY);
		createEAttribute(emfCreationToolEntryEClass, EMF_CREATION_TOOL_ENTRY__CREATION_CLASS_URI);

		emfPrototypeToolEntryEClass = createEClass(EMF_PROTOTYPE_TOOL_ENTRY);
		createEAttribute(emfPrototypeToolEntryEClass, EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI);

		annotatedCreationEntryEClass = createEClass(ANNOTATED_CREATION_ENTRY);
		createEReference(annotatedCreationEntryEClass, ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY);
		createEReference(annotatedCreationEntryEClass, ANNOTATED_CREATION_ENTRY__VALUES);

		selectionCreationToolEntryEClass = createEClass(SELECTION_CREATION_TOOL_ENTRY);
		createEAttribute(selectionCreationToolEntryEClass, SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME);

		drawerEClass = createEClass(DRAWER);
		createEAttribute(drawerEClass, DRAWER__INITIAL_STATE);

		stackEClass = createEClass(STACK);
		createEReference(stackEClass, STACK__ACTIVE_ENTRY);

		separatorEClass = createEClass(SEPARATOR);

		// Create enums
		permissionsEEnum = createEEnum(PERMISSIONS);
		initialStateEEnum = createEEnum(INITIAL_STATE);

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
		UtilityPackage theUtilityPackage = (UtilityPackage)EPackage.Registry.INSTANCE.getEPackage(UtilityPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		CDMPackage theCDMPackage = (CDMPackage)EPackage.Registry.INSTANCE.getEPackage(CDMPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		rootEClass.getESuperTypes().add(this.getContainer());
		categoryEClass.getESuperTypes().add(this.getDrawer());
		groupEClass.getESuperTypes().add(this.getContainer());
		entryEClass.getESuperTypes().add(theEcorePackage.getEObject());
		toolEntryEClass.getESuperTypes().add(this.getAbstractToolEntry());
		creationToolEntryEClass.getESuperTypes().add(this.getAbstractToolEntry());
		creationToolEntryEClass.getESuperTypes().add(theCDMPackage.getKeyedValueHolder());
		abstractToolEntryEClass.getESuperTypes().add(this.getEntry());
		paletteCmpEClass.getESuperTypes().add(this.getRoot());
		categoryCmpEClass.getESuperTypes().add(this.getCategory());
		groupCmpEClass.getESuperTypes().add(this.getGroup());
		containerEClass.getESuperTypes().add(this.getEntry());
		emfCreationToolEntryEClass.getESuperTypes().add(this.getCreationToolEntry());
		emfPrototypeToolEntryEClass.getESuperTypes().add(this.getCreationToolEntry());
		annotatedCreationEntryEClass.getESuperTypes().add(this.getAbstractToolEntry());
		selectionCreationToolEntryEClass.getESuperTypes().add(this.getCreationToolEntry());
		drawerEClass.getESuperTypes().add(this.getContainer());
		stackEClass.getESuperTypes().add(this.getContainer());
		separatorEClass.getESuperTypes().add(this.getEntry());

		// Initialize classes and features; add operations and parameters
		initEClass(rootEClass, Root.class, "Root", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRoot_DefEntry(), this.getAbstractToolEntry(), null, "defEntry", null, 0, 1, Root.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(categoryEClass, Category.class, "Category", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCategory_CategoryLabel(), theUtilityPackage.getAbstractString(), null, "categoryLabel", null, 1, 1, Category.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(groupEClass, Group.class, "Group", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGroup_GroupLabel(), theUtilityPackage.getAbstractString(), null, "groupLabel", null, 0, 1, Group.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entryEClass, Entry.class, "Entry", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntry_Icon16Name(), ecorePackage.getEString(), "icon16Name", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Icon32Name(), ecorePackage.getEString(), "icon32Name", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Visible(), ecorePackage.getEBoolean(), "visible", "true", 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_DefaultEntry(), ecorePackage.getEBoolean(), "defaultEntry", null, 0, 1, Entry.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Id(), ecorePackage.getEString(), "id", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Modification(), this.getPermissions(), "modification", "Default", 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntry_EntryLabel(), theUtilityPackage.getAbstractString(), null, "entryLabel", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntry_EntryShortDescription(), theUtilityPackage.getAbstractString(), null, "entryShortDescription", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(toolEntryEClass, ToolEntry.class, "ToolEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getToolEntry_ToolClassName(), ecorePackage.getEString(), "toolClassName", null, 0, 1, ToolEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(creationToolEntryEClass, CreationToolEntry.class, "CreationToolEntry", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(abstractToolEntryEClass, AbstractToolEntry.class, "AbstractToolEntry", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAbstractToolEntry_StringProperties(), theEcorePackage.getEStringToStringMapEntry(), null, "stringProperties", null, 0, -1, AbstractToolEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(paletteCmpEClass, PaletteCmp.class, "PaletteCmp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPaletteCmp_CmpCategories(), this.getCategory(), null, "cmpCategories", null, 0, -1, PaletteCmp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPaletteCmp_CmpControlGroup(), this.getGroup(), null, "cmpControlGroup", null, 0, 1, PaletteCmp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPaletteCmp_PaletteLabel(), theUtilityPackage.getAbstractString(), null, "paletteLabel", null, 0, 1, PaletteCmp.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(categoryCmpEClass, CategoryCmp.class, "CategoryCmp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCategoryCmp_CmpGroups(), this.getGroup(), null, "cmpGroups", null, 0, -1, CategoryCmp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(groupCmpEClass, GroupCmp.class, "GroupCmp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGroupCmp_CmpEntries(), this.getEntry(), null, "cmpEntries", null, 0, -1, GroupCmp.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(containerEClass, Container.class, "Container", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContainer_Children(), this.getEntry(), null, "children", null, 0, -1, Container.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(emfCreationToolEntryEClass, EMFCreationToolEntry.class, "EMFCreationToolEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEMFCreationToolEntry_CreationClassURI(), ecorePackage.getEString(), "creationClassURI", null, 0, 1, EMFCreationToolEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(emfPrototypeToolEntryEClass, EMFPrototypeToolEntry.class, "EMFPrototypeToolEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEMFPrototypeToolEntry_PrototypeURI(), ecorePackage.getEString(), "prototypeURI", null, 0, 1, EMFPrototypeToolEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(annotatedCreationEntryEClass, AnnotatedCreationEntry.class, "AnnotatedCreationEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnnotatedCreationEntry_ObjectCreationEntry(), this.getCreationToolEntry(), null, "objectCreationEntry", null, 1, 1, AnnotatedCreationEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnnotatedCreationEntry_Values(), theEcorePackage.getEObject(), null, "values", null, 0, -1, AnnotatedCreationEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(selectionCreationToolEntryEClass, SelectionCreationToolEntry.class, "SelectionCreationToolEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSelectionCreationToolEntry_SelectorClassName(), ecorePackage.getEString(), "selectorClassName", null, 0, 1, SelectionCreationToolEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(drawerEClass, Drawer.class, "Drawer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDrawer_InitialState(), this.getInitialState(), "initialState", "Closed", 0, 1, Drawer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stackEClass, Stack.class, "Stack", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStack_ActiveEntry(), this.getEntry(), null, "activeEntry", null, 0, 1, Stack.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(separatorEClass, Separator.class, "Separator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(permissionsEEnum, Permissions.class, "Permissions");
		addEEnumLiteral(permissionsEEnum, Permissions.DEFAULT_LITERAL);
		addEEnumLiteral(permissionsEEnum, Permissions.FULL_LITERAL);
		addEEnumLiteral(permissionsEEnum, Permissions.HIDE_ONLY_LITERAL);
		addEEnumLiteral(permissionsEEnum, Permissions.LIMITED_LITERAL);
		addEEnumLiteral(permissionsEEnum, Permissions.NONE_LITERAL);

		initEEnum(initialStateEEnum, InitialState.class, "InitialState");
		addEEnumLiteral(initialStateEEnum, InitialState.OPEN_LITERAL);
		addEEnumLiteral(initialStateEEnum, InitialState.CLOSED_LITERAL);
		addEEnumLiteral(initialStateEEnum, InitialState.PINNED_OPEN_LITERAL);

		// Initialize data types
		initEDataType(creationFactoryEDataType, CreationFactory.class, "CreationFactory", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

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
		  (rootEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is the palette root.\r\n<p>\r\nIts children can be anything except a Tool or a Stack."
		   });		
		addAnnotation
		  (getRoot_DefEntry(), 
		   source, 
		   new String[] {
			 "documentation", "Default Tool Entry\r\n<p>\r\nThe entry selected when no entry is explicitly selected."
		   });		
		addAnnotation
		  (categoryEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Use Drawer instead."
		   });		
		addAnnotation
		  (getCategory_CategoryLabel(), 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Use Entry.entryLabel instead."
		   });		
		addAnnotation
		  (groupEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is a group. \r\n<p>\r\nA group is a container that cannot be collapsed. It can accept any entry type."
		   });		
		addAnnotation
		  (getGroup_GroupLabel(), 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Use Entry.entryLabel instead."
		   });		
		addAnnotation
		  (entryEClass, 
		   source, 
		   new String[] {
			 "documentation", "The root of the palette hierarchy.\r\n<p>\r\nIt is not abstract, but it is treated as basically abstract."
		   });		
		addAnnotation
		  (getEntry_Icon16Name(), 
		   source, 
		   new String[] {
			 "documentation", "The URL for the 16x16 (small) icon for the entry. If not set, then a default will be used. Some entries do not have an image as a default, others do. For example ToolEntries do not have an image while Drawers do."
		   });		
		addAnnotation
		  (getEntry_Icon32Name(), 
		   source, 
		   new String[] {
			 "documentation", "The URL for the 32x32 (large) icon. If not specified then the small icon will be used."
		   });		
		addAnnotation
		  (getEntry_Visible(), 
		   source, 
		   new String[] {
			 "documentation", "Is entry visible? Default is true. false is useful for letting palette modifications occur and user can then turn it on."
		   });		
		addAnnotation
		  (getEntry_DefaultEntry(), 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Only here for compatibility. It is ignored. Use Palette.defaultEntry instead."
		   });		
		addAnnotation
		  (getEntry_Id(), 
		   source, 
		   new String[] {
			 "documentation", "Entry ID. (optional)\r\n<p>\r\nOnly used for programatically inserting other palette entries. It provides a reference point for palette contributors to find and add into the section that contains this palette entry."
		   });		
		addAnnotation
		  (getEntry_EntryLabel(), 
		   source, 
		   new String[] {
			 "documentation", "Label for the entry"
		   });		
		addAnnotation
		  (getEntry_EntryShortDescription(), 
		   source, 
		   new String[] {
			 "documentation", "Short description for the entry (optional)"
		   });		
		addAnnotation
		  (toolEntryEClass, 
		   source, 
		   new String[] {
			 "documentation", "Generic ToolEntry\r\n<p>\r\nThis is a generic tool entry. You will need to supply the classname and any of the properties for it.\r\n"
		   });		
		addAnnotation
		  (getToolEntry_ToolClassName(), 
		   source, 
		   new String[] {
			 "documentation", "Tool class name.\r\n<p>\r\nThe classname of the tool entry. Because it is not known the namespace for the class, you need to use a special format:\r\n<p>\r\n<ul>\r\n<li><b>packagename.classname</b>: This means it must be available from the default class loader. (In Eclipse, this will be the org.eclipse.ve.cde plugin. It must be visible to this to be found).\r\n<li><b>namespace/packagename.classname</b>: This means it will be found  in the namespace. (In Eclipse the namespace is the name of a bundle. It will look within that bundle to find the class).\r\n</ul>"
		   });		
		addAnnotation
		  (creationToolEntryEClass, 
		   source, 
		   new String[] {
			 "documentation", "Creation Tool\r\n<p>\r\nThis is abstract.\r\n<p>\r\nIf there are any annotation values, then an annotation will be created for the object that the factory returns. The annotation values come from the mixin KeyedValueHolder.keyedValues."
		   });		
		addAnnotation
		  (abstractToolEntryEClass, 
		   source, 
		   new String[] {
			 "documentation", "Base ToolEntry."
		   });		
		addAnnotation
		  (getAbstractToolEntry_StringProperties(), 
		   source, 
		   new String[] {
			 "documentation", "Key/Value properties.\r\n<p>\r\nThis is a map of property name to string property values. These will be applied to the GEF ToolEntry using the key/value setProperty method.\r\n<p>\r\nIf string keys and string values is not sufficient, you will need to create your own AbstractToolEntry subclass instead to supply those."
		   });		
		addAnnotation
		  (paletteCmpEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Use Root instead."
		   });		
		addAnnotation
		  (getPaletteCmp_CmpCategories(), 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Add to Container.children instead."
		   });		
		addAnnotation
		  (getPaletteCmp_CmpControlGroup(), 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Use Container.children instead."
		   });		
		addAnnotation
		  (categoryCmpEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Use Drawer instead."
		   });		
		addAnnotation
		  (getCategoryCmp_CmpGroups(), 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Categories can no longer contain groups."
		   });		
		addAnnotation
		  (groupCmpEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Use Group instead."
		   });		
		addAnnotation
		  (getGroupCmp_CmpEntries(), 
		   source, 
		   new String[] {
			 "documentation", "This is obsolete. Use Container.children instead."
		   });		
		addAnnotation
		  (containerEClass, 
		   source, 
		   new String[] {
			 "documentation", "Basic Palette Container.\r\n<p>\r\nIt contains other entries."
		   });		
		addAnnotation
		  (getContainer_Children(), 
		   source, 
		   new String[] {
			 "documentation", "This is the children of the container. Each container subclass can limit the type of children allowed."
		   });		
		addAnnotation
		  (emfCreationToolEntryEClass, 
		   source, 
		   new String[] {
			 "documentation", "Creation Tool for creating EMF instances given just the EMF class.\r\n<p>\r\nA default empty instance with no features set will be created."
		   });		
		addAnnotation
		  (getEMFCreationToolEntry_CreationClassURI(), 
		   source, 
		   new String[] {
			 "documentation", "The URI of the class for which an instance is to be created. "
		   });		
		addAnnotation
		  (emfPrototypeToolEntryEClass, 
		   source, 
		   new String[] {
			 "documentation", "This is like an EMFCreationToolEntry, but instead of creating a new instance based upon a classtype, a copy of the supplied prototype is returned instead."
		   });		
		addAnnotation
		  (getEMFPrototypeToolEntry_PrototypeURI(), 
		   source, 
		   new String[] {
			 "documentation", "This is the string containing the prototype URI for the prototype. \r\n<p>\r\nThe URI is to the object protoype that is being created. The URI must point to an EObject in a resource. That EObject must be contained directly by the Resource it is in (i.e. it mustn\'t be contained by another EObject). Also in the resource can be Annotations for any of the EObjects that are part of the template. These will automatically be added in."
		   });		
		addAnnotation
		  (annotatedCreationEntryEClass, 
		   source, 
		   new String[] {
			 "documentation", "This will wrapper a CreationToolEntry and provide for creating an annotation too. This is the entry that should be in the palette when an annotation entry is desired.\r\n<p>\r\nThis is obsolete. The CreationToolEntry now can handle this itself."
		   });		
		addAnnotation
		  (selectionCreationToolEntryEClass, 
		   source, 
		   new String[] {
			 "documentation", "A creation tool entry where the actual object created is determined by the selectionCreation class. This allows the actual object created to be determined at selection time rather than statically defined in the palette entry itself."
		   });		
		addAnnotation
		  (getSelectionCreationToolEntry_SelectorClassName(), 
		   source, 
		   new String[] {
			 "documentation", "This is the classname of the selector class. It must implement the ISelector interface.  Because it is not known the namespace for the class, you need to use a special format:\r\n<p>\r\n<ul>\r\n<li><b>packagename.classname</b>: This means it must be available from the default class loader. (In Eclipse, this will be the org.eclipse.ve.cde plugin. It must be visible to this to be found).\r\n<li><b>namespace/packagename.classname</b>: This means it will be found  in the namespace. (In Eclipse the namespace is the name of a bundle. It will look within that bundle to find the class).\r\n</ul>"
		   });		
		addAnnotation
		  (drawerEClass, 
		   source, 
		   new String[] {
			 "documentation", "Palette Drawer.\r\n<p>\r\nThis is a collapsable collection of entries. It accepts anything except a Group or a Drawer."
		   });		
		addAnnotation
		  (permissionsEEnum, 
		   source, 
		   new String[] {
			 "documentation", "The valid modification permissions for this entry. Used in the palette customizer."
		   });		
		addAnnotation
		  (permissionsEEnum.getELiterals().get(0), 
		   source, 
		   new String[] {
			 "documentation", "This is the default permission for the entry. Each entry type has a default permission. It may different depending on the entry type."
		   });		
		addAnnotation
		  (permissionsEEnum.getELiterals().get(1), 
		   source, 
		   new String[] {
			 "documentation", "Full modification by the customizer is allowed."
		   });		
		addAnnotation
		  (permissionsEEnum.getELiterals().get(2), 
		   source, 
		   new String[] {
			 "documentation", "The customizer can only hide/show the entry."
		   });		
		addAnnotation
		  (permissionsEEnum.getELiterals().get(3), 
		   source, 
		   new String[] {
			 "documentation", "Limited modification is allowed. It is not allowed to move to another container or to be deleted."
		   });		
		addAnnotation
		  (permissionsEEnum.getELiterals().get(4), 
		   source, 
		   new String[] {
			 "documentation", "No modification is allowed."
		   });		
		addAnnotation
		  (initialStateEEnum, 
		   source, 
		   new String[] {
			 "documentation", "Drawer initial state."
		   });		
		addAnnotation
		  (initialStateEEnum.getELiterals().get(0), 
		   source, 
		   new String[] {
			 "documentation", "Initially open."
		   });		
		addAnnotation
		  (initialStateEEnum.getELiterals().get(1), 
		   source, 
		   new String[] {
			 "documentation", "Initially closed."
		   });		
		addAnnotation
		  (initialStateEEnum.getELiterals().get(2), 
		   source, 
		   new String[] {
			 "documentation", "Initially pinned open."
		   });		
		addAnnotation
		  (stackEClass, 
		   source, 
		   new String[] {
			 "documentation", "Stack Entry.\r\n<p>\r\nA grouping of ToolEntries. They are represented by one entry on the palette and there can be a dropdown list to select from and a current active entry.\r\n<p>\r\nIt can only accept ToolEntries."
		   });		
		addAnnotation
		  (getStack_ActiveEntry(), 
		   source, 
		   new String[] {
			 "documentation", "The current active entry, if any. This is the entry that would be selected if the stack was selected."
		   });		
		addAnnotation
		  (separatorEClass, 
		   source, 
		   new String[] {
			 "documentation", "Palette Separator\r\n<p>\r\nIt is a spacer. But in addition it can be used a section marker for letting other entries be added into the section. The id of the separator can be used for this."
		   });
	}

} //PalettePackageImpl
