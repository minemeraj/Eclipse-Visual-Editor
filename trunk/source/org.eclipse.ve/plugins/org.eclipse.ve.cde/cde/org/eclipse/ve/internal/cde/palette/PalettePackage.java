package org.eclipse.ve.internal.cde.palette;
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
 *  $RCSfile: PalettePackage.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
 
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see org.eclipse.ve.internal.cde.palette.PaletteFactory
 * @generated
 */
public interface PalettePackage extends EPackage{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "palette"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/ve/internal/cde/palette.ecore"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.ve.internal.cde.palette"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PalettePackage eINSTANCE = org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.ContainerImpl <em>Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.ContainerImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getContainer()
	 * @generated
	 */
	int CONTAINER = 13;

	/**
	 * The number of structural features of the the '<em>Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_FEATURE_COUNT = EcorePackage.EOBJECT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.PaletteImpl <em>Palette</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.PaletteImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getPalette()
	 * @generated
	 */
	int PALETTE = 0;

	/**
	 * The feature id for the '<em><b>Palette Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE__PALETTE_LABEL = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Palette</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.CategoryImpl <em>Category</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.CategoryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getCategory()
	 * @generated
	 */
	int CATEGORY = 1;

	/**
	 * The feature id for the '<em><b>Category Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__CATEGORY_LABEL = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Category</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.GroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.GroupImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getGroup()
	 * @generated
	 */
	int GROUP = 2;

	/**
	 * The feature id for the '<em><b>Group Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__GROUP_LABEL = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl <em>Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.EntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getEntry()
	 * @generated
	 */
	int ENTRY = 3;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__ICON16_NAME = EcorePackage.EOBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__ICON32_NAME = EcorePackage.EOBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__DEFAULT_ENTRY = EcorePackage.EOBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__ENTRY_LABEL = EcorePackage.EOBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__ENTRY_SHORT_DESCRIPTION = EcorePackage.EOBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the the '<em>Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_FEATURE_COUNT = EcorePackage.EOBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.AbstractToolEntryImpl <em>Abstract Tool Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.AbstractToolEntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getAbstractToolEntry()
	 * @generated
	 */
	int ABSTRACT_TOOL_ENTRY = 6;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__ICON16_NAME = ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__ICON32_NAME = ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY = ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__ENTRY_LABEL = ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION = ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The number of structural features of the the '<em>Abstract Tool Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY_FEATURE_COUNT = ENTRY_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.ToolEntryImpl <em>Tool Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.ToolEntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getToolEntry()
	 * @generated
	 */
	int TOOL_ENTRY = 4;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__ICON16_NAME = ABSTRACT_TOOL_ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__ICON32_NAME = ABSTRACT_TOOL_ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__DEFAULT_ENTRY = ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__ENTRY_LABEL = ABSTRACT_TOOL_ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION = ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Tool Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__TOOL_CLASS_NAME = ABSTRACT_TOOL_ENTRY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Tool Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY_FEATURE_COUNT = ABSTRACT_TOOL_ENTRY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.CreationToolEntryImpl <em>Creation Tool Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.CreationToolEntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getCreationToolEntry()
	 * @generated
	 */
	int CREATION_TOOL_ENTRY = 5;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__ICON16_NAME = ABSTRACT_TOOL_ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__ICON32_NAME = ABSTRACT_TOOL_ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__DEFAULT_ENTRY = ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__ENTRY_LABEL = ABSTRACT_TOOL_ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION = ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The number of structural features of the the '<em>Creation Tool Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY_FEATURE_COUNT = ABSTRACT_TOOL_ENTRY_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.PaletteRefImpl <em>Ref</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.PaletteRefImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getPaletteRef()
	 * @generated
	 */
	int PALETTE_REF = 7;

	/**
	 * The feature id for the '<em><b>Palette Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_REF__PALETTE_LABEL = PALETTE__PALETTE_LABEL;

	/**
	 * The feature id for the '<em><b>Ref Control Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_REF__REF_CONTROL_GROUP = PALETTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ref Categories</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_REF__REF_CATEGORIES = PALETTE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Ref</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_REF_FEATURE_COUNT = PALETTE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.PaletteCmpImpl <em>Cmp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.PaletteCmpImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getPaletteCmp()
	 * @generated
	 */
	int PALETTE_CMP = 8;

	/**
	 * The feature id for the '<em><b>Palette Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__PALETTE_LABEL = PALETTE__PALETTE_LABEL;

	/**
	 * The feature id for the '<em><b>Cmp Categories</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__CMP_CATEGORIES = PALETTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cmp Control Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__CMP_CONTROL_GROUP = PALETTE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Cmp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP_FEATURE_COUNT = PALETTE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.CategoryRefImpl <em>Category Ref</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.CategoryRefImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getCategoryRef()
	 * @generated
	 */
	int CATEGORY_REF = 9;

	/**
	 * The feature id for the '<em><b>Category Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_REF__CATEGORY_LABEL = CATEGORY__CATEGORY_LABEL;

	/**
	 * The feature id for the '<em><b>Ref Groups</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_REF__REF_GROUPS = CATEGORY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Category Ref</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_REF_FEATURE_COUNT = CATEGORY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.CategoryCmpImpl <em>Category Cmp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.CategoryCmpImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getCategoryCmp()
	 * @generated
	 */
	int CATEGORY_CMP = 10;

	/**
	 * The feature id for the '<em><b>Category Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__CATEGORY_LABEL = CATEGORY__CATEGORY_LABEL;

	/**
	 * The feature id for the '<em><b>Cmp Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__CMP_GROUPS = CATEGORY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Category Cmp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP_FEATURE_COUNT = CATEGORY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.GroupCmpImpl <em>Group Cmp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.GroupCmpImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getGroupCmp()
	 * @generated
	 */
	int GROUP_CMP = 11;

	/**
	 * The feature id for the '<em><b>Group Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__GROUP_LABEL = GROUP__GROUP_LABEL;

	/**
	 * The feature id for the '<em><b>Cmp Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__CMP_ENTRIES = GROUP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Group Cmp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP_FEATURE_COUNT = GROUP_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.GroupRefImpl <em>Group Ref</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.GroupRefImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getGroupRef()
	 * @generated
	 */
	int GROUP_REF = 12;

	/**
	 * The feature id for the '<em><b>Group Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_REF__GROUP_LABEL = GROUP__GROUP_LABEL;

	/**
	 * The feature id for the '<em><b>Ref Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_REF__REF_ENTRIES = GROUP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Group Ref</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_REF_FEATURE_COUNT = GROUP_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.EMFCreationToolEntryImpl <em>EMF Creation Tool Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.EMFCreationToolEntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getEMFCreationToolEntry()
	 * @generated
	 */
	int EMF_CREATION_TOOL_ENTRY = 14;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__ICON16_NAME = CREATION_TOOL_ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__ICON32_NAME = CREATION_TOOL_ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__DEFAULT_ENTRY = CREATION_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__ENTRY_LABEL = CREATION_TOOL_ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION = CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Creation Class URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__CREATION_CLASS_URI = CREATION_TOOL_ENTRY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>EMF Creation Tool Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY_FEATURE_COUNT = CREATION_TOOL_ENTRY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.EMFPrototypeToolEntryImpl <em>EMF Prototype Tool Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.EMFPrototypeToolEntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getEMFPrototypeToolEntry()
	 * @generated
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY = 15;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__ICON16_NAME = CREATION_TOOL_ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__ICON32_NAME = CREATION_TOOL_ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY = CREATION_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL = CREATION_TOOL_ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION = CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Prototype URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI = CREATION_TOOL_ENTRY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>EMF Prototype Tool Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY_FEATURE_COUNT = CREATION_TOOL_ENTRY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.AnnotatedCreationEntryImpl <em>Annotated Creation Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.AnnotatedCreationEntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getAnnotatedCreationEntry()
	 * @generated
	 */
	int ANNOTATED_CREATION_ENTRY = 16;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__ICON16_NAME = ABSTRACT_TOOL_ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__ICON32_NAME = ABSTRACT_TOOL_ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY = ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__ENTRY_LABEL = ABSTRACT_TOOL_ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION = ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Object Creation Entry</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY = ABSTRACT_TOOL_ENTRY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Values</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__VALUES = ABSTRACT_TOOL_ENTRY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Annotated Creation Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY_FEATURE_COUNT = ABSTRACT_TOOL_ENTRY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.SelectionCreationToolEntryImpl <em>Selection Creation Tool Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.SelectionCreationToolEntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getSelectionCreationToolEntry()
	 * @generated
	 */
	int SELECTION_CREATION_TOOL_ENTRY = 17;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__ICON16_NAME = CREATION_TOOL_ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__ICON32_NAME = CREATION_TOOL_ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__DEFAULT_ENTRY = CREATION_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__ENTRY_LABEL = CREATION_TOOL_ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION = CREATION_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Selector Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__SELECTOR_CLASS_NAME = CREATION_TOOL_ENTRY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Selection Creation Tool Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY_FEATURE_COUNT = CREATION_TOOL_ENTRY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.ICDEPaletteEntry <em>ICDE Palette Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.ICDEPaletteEntry
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getICDEPaletteEntry()
	 * @generated
	 */
	int ICDE_PALETTE_ENTRY = 18;

	/**
	 * The number of structural features of the the '<em>ICDE Palette Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICDE_PALETTE_ENTRY_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '<em>Creation Factory</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.requests.CreationFactory
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getCreationFactory()
	 * @generated
	 */
	int CREATION_FACTORY = 19;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Palette <em>Palette</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Palette</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Palette
	 * @generated
	 */
	EClass getPalette();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.palette.Palette#getPaletteLabel <em>Palette Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Palette Label</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Palette#getPaletteLabel()
	 * @see #getPalette()
	 * @generated
	 */
	EReference getPalette_PaletteLabel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Category <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Category</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Category
	 * @generated
	 */
	EClass getCategory();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.palette.Category#getCategoryLabel <em>Category Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Category Label</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Category#getCategoryLabel()
	 * @see #getCategory()
	 * @generated
	 */
	EReference getCategory_CategoryLabel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Group <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Group
	 * @generated
	 */
	EClass getGroup();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.palette.Group#getGroupLabel <em>Group Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Group Label</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Group#getGroupLabel()
	 * @see #getGroup()
	 * @generated
	 */
	EReference getGroup_GroupLabel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Entry <em>Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry
	 * @generated
	 */
	EClass getEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.Entry#getIcon16Name <em>Icon16 Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Icon16 Name</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry#getIcon16Name()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Icon16Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.Entry#getIcon32Name <em>Icon32 Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Icon32 Name</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry#getIcon32Name()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Icon32Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.Entry#isDefaultEntry <em>Default Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry#isDefaultEntry()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_DefaultEntry();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.palette.Entry#getEntryLabel <em>Entry Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Entry Label</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry#getEntryLabel()
	 * @see #getEntry()
	 * @generated
	 */
	EReference getEntry_EntryLabel();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.palette.Entry#getEntryShortDescription <em>Entry Short Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Entry Short Description</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry#getEntryShortDescription()
	 * @see #getEntry()
	 * @generated
	 */
	EReference getEntry_EntryShortDescription();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.ToolEntry <em>Tool Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tool Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.ToolEntry
	 * @generated
	 */
	EClass getToolEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.ToolEntry#getToolClassName <em>Tool Class Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tool Class Name</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.ToolEntry#getToolClassName()
	 * @see #getToolEntry()
	 * @generated
	 */
	EAttribute getToolEntry_ToolClassName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.CreationToolEntry <em>Creation Tool Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Creation Tool Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.CreationToolEntry
	 * @generated
	 */
	EClass getCreationToolEntry();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.AbstractToolEntry <em>Abstract Tool Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Tool Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.AbstractToolEntry
	 * @generated
	 */
	EClass getAbstractToolEntry();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.PaletteRef <em>Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ref</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.PaletteRef
	 * @generated
	 */
	EClass getPaletteRef();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.cde.palette.PaletteRef#getRefControlGroup <em>Ref Control Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Ref Control Group</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.PaletteRef#getRefControlGroup()
	 * @see #getPaletteRef()
	 * @generated
	 */
	EReference getPaletteRef_RefControlGroup();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ve.internal.cde.palette.PaletteRef#getRefCategories <em>Ref Categories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ref Categories</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.PaletteRef#getRefCategories()
	 * @see #getPaletteRef()
	 * @generated
	 */
	EReference getPaletteRef_RefCategories();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.PaletteCmp <em>Cmp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cmp</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.PaletteCmp
	 * @generated
	 */
	EClass getPaletteCmp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpCategories <em>Cmp Categories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cmp Categories</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpCategories()
	 * @see #getPaletteCmp()
	 * @generated
	 */
	EReference getPaletteCmp_CmpCategories();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpControlGroup <em>Cmp Control Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Cmp Control Group</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpControlGroup()
	 * @see #getPaletteCmp()
	 * @generated
	 */
	EReference getPaletteCmp_CmpControlGroup();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.CategoryRef <em>Category Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Category Ref</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.CategoryRef
	 * @generated
	 */
	EClass getCategoryRef();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ve.internal.cde.palette.CategoryRef#getRefGroups <em>Ref Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ref Groups</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.CategoryRef#getRefGroups()
	 * @see #getCategoryRef()
	 * @generated
	 */
	EReference getCategoryRef_RefGroups();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.CategoryCmp <em>Category Cmp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Category Cmp</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.CategoryCmp
	 * @generated
	 */
	EClass getCategoryCmp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cde.palette.CategoryCmp#getCmpGroups <em>Cmp Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cmp Groups</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.CategoryCmp#getCmpGroups()
	 * @see #getCategoryCmp()
	 * @generated
	 */
	EReference getCategoryCmp_CmpGroups();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.GroupCmp <em>Group Cmp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group Cmp</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.GroupCmp
	 * @generated
	 */
	EClass getGroupCmp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cde.palette.GroupCmp#getCmpEntries <em>Cmp Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cmp Entries</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.GroupCmp#getCmpEntries()
	 * @see #getGroupCmp()
	 * @generated
	 */
	EReference getGroupCmp_CmpEntries();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.GroupRef <em>Group Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group Ref</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.GroupRef
	 * @generated
	 */
	EClass getGroupRef();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cde.palette.GroupRef#getRefEntries <em>Ref Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ref Entries</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.GroupRef#getRefEntries()
	 * @see #getGroupRef()
	 * @generated
	 */
	EReference getGroupRef_RefEntries();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Container <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Container
	 * @generated
	 */
	EClass getContainer();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry <em>EMF Creation Tool Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EMF Creation Tool Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry
	 * @generated
	 */
	EClass getEMFCreationToolEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry#getCreationClassURI <em>Creation Class URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Creation Class URI</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry#getCreationClassURI()
	 * @see #getEMFCreationToolEntry()
	 * @generated
	 */
	EAttribute getEMFCreationToolEntry_CreationClassURI();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry <em>EMF Prototype Tool Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EMF Prototype Tool Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry
	 * @generated
	 */
	EClass getEMFPrototypeToolEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry#getPrototypeURI <em>Prototype URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prototype URI</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry#getPrototypeURI()
	 * @see #getEMFPrototypeToolEntry()
	 * @generated
	 */
	EAttribute getEMFPrototypeToolEntry_PrototypeURI();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry <em>Annotated Creation Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotated Creation Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry
	 * @generated
	 */
	EClass getAnnotatedCreationEntry();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry#getValues <em>Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Values</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry#getValues()
	 * @see #getAnnotatedCreationEntry()
	 * @generated
	 */
	EReference getAnnotatedCreationEntry_Values();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry#getObjectCreationEntry <em>Object Creation Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Object Creation Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry#getObjectCreationEntry()
	 * @see #getAnnotatedCreationEntry()
	 * @generated
	 */
	EReference getAnnotatedCreationEntry_ObjectCreationEntry();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry <em>Selection Creation Tool Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Selection Creation Tool Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry
	 * @generated
	 */
	EClass getSelectionCreationToolEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry#getSelectorClassName <em>Selector Class Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Selector Class Name</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry#getSelectorClassName()
	 * @see #getSelectionCreationToolEntry()
	 * @generated
	 */
	EAttribute getSelectionCreationToolEntry_SelectorClassName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.ICDEPaletteEntry <em>ICDE Palette Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ICDE Palette Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.ICDEPaletteEntry
	 * @model instanceClass="org.eclipse.ve.internal.cde.palette.ICDEPaletteEntry" 
	 * @generated
	 */
	EClass getICDEPaletteEntry();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.gef.requests.CreationFactory <em>Creation Factory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Creation Factory</em>'.
	 * @see org.eclipse.gef.requests.CreationFactory
	 * @model instanceClass="org.eclipse.gef.requests.CreationFactory"
	 * @generated
	 */
	EDataType getCreationFactory();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PaletteFactory getPaletteFactory();

} //PalettePackage
