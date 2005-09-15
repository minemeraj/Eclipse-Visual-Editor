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
package org.eclipse.ve.internal.cde.palette;
/*
 *  $RCSfile: PalettePackage.java,v $
 *  $Revision: 1.5 $  $Date: 2005-09-15 21:27:16 $ 
 */
 
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
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
 * @model kind="package"
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
	int CONTAINER = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.RootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.RootImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getRoot()
	 * @generated
	 */
	int ROOT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.DrawerImpl <em>Drawer</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.DrawerImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getDrawer()
	 * @generated
	 */
	int DRAWER = 15;

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
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.GroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.GroupImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getGroup()
	 * @generated
	 */
	int GROUP = 2;

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
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__VISIBLE = EcorePackage.EOBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__DEFAULT_ENTRY = EcorePackage.EOBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__ID = EcorePackage.EOBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__MODIFICATION = EcorePackage.EOBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__ENTRY_LABEL = EcorePackage.EOBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__ENTRY_SHORT_DESCRIPTION = EcorePackage.EOBJECT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the the '<em>Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_FEATURE_COUNT = EcorePackage.EOBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ICON16_NAME = ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ICON32_NAME = ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__VISIBLE = ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__DEFAULT_ENTRY = ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ID = ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__MODIFICATION = ENTRY__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ENTRY_LABEL = ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ENTRY_SHORT_DESCRIPTION = ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__CHILDREN = ENTRY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_FEATURE_COUNT = ENTRY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__ICON16_NAME = CONTAINER__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__ICON32_NAME = CONTAINER__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__VISIBLE = CONTAINER__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__DEFAULT_ENTRY = CONTAINER__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__ID = CONTAINER__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__MODIFICATION = CONTAINER__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__ENTRY_LABEL = CONTAINER__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__ENTRY_SHORT_DESCRIPTION = CONTAINER__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__CHILDREN = CONTAINER__CHILDREN;

	/**
	 * The feature id for the '<em><b>Def Entry</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__DEF_ENTRY = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__ICON16_NAME = CONTAINER__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__ICON32_NAME = CONTAINER__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__VISIBLE = CONTAINER__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__DEFAULT_ENTRY = CONTAINER__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__ID = CONTAINER__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__MODIFICATION = CONTAINER__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__ENTRY_LABEL = CONTAINER__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__ENTRY_SHORT_DESCRIPTION = CONTAINER__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__CHILDREN = CONTAINER__CHILDREN;

	/**
	 * The feature id for the '<em><b>Initial State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER__INITIAL_STATE = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Drawer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRAWER_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ICON16_NAME = DRAWER__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ICON32_NAME = DRAWER__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__VISIBLE = DRAWER__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__DEFAULT_ENTRY = DRAWER__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ID = DRAWER__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__MODIFICATION = DRAWER__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ENTRY_LABEL = DRAWER__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ENTRY_SHORT_DESCRIPTION = DRAWER__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__CHILDREN = DRAWER__CHILDREN;

	/**
	 * The feature id for the '<em><b>Initial State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__INITIAL_STATE = DRAWER__INITIAL_STATE;

	/**
	 * The feature id for the '<em><b>Category Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__CATEGORY_LABEL = DRAWER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Category</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_FEATURE_COUNT = DRAWER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__ICON16_NAME = CONTAINER__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__ICON32_NAME = CONTAINER__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__VISIBLE = CONTAINER__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__DEFAULT_ENTRY = CONTAINER__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__ID = CONTAINER__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__MODIFICATION = CONTAINER__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__ENTRY_LABEL = CONTAINER__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__ENTRY_SHORT_DESCRIPTION = CONTAINER__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__CHILDREN = CONTAINER__CHILDREN;

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
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__VISIBLE = ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY = ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__ID = ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__MODIFICATION = ENTRY__MODIFICATION;

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
	 * The feature id for the '<em><b>String Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES = ENTRY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Abstract Tool Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_TOOL_ENTRY_FEATURE_COUNT = ENTRY_FEATURE_COUNT + 1;

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
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__VISIBLE = ABSTRACT_TOOL_ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__DEFAULT_ENTRY = ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__ID = ABSTRACT_TOOL_ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__MODIFICATION = ABSTRACT_TOOL_ENTRY__MODIFICATION;

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
	 * The feature id for the '<em><b>String Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOL_ENTRY__STRING_PROPERTIES = ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES;

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
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__VISIBLE = ABSTRACT_TOOL_ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__DEFAULT_ENTRY = ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__ID = ABSTRACT_TOOL_ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__MODIFICATION = ABSTRACT_TOOL_ENTRY__MODIFICATION;

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
	 * The feature id for the '<em><b>String Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__STRING_PROPERTIES = ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY__KEYED_VALUES = ABSTRACT_TOOL_ENTRY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Creation Tool Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CREATION_TOOL_ENTRY_FEATURE_COUNT = ABSTRACT_TOOL_ENTRY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.PaletteCmpImpl <em>Cmp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.PaletteCmpImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getPaletteCmp()
	 * @generated
	 */
	int PALETTE_CMP = 7;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__ICON16_NAME = ROOT__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__ICON32_NAME = ROOT__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__VISIBLE = ROOT__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__DEFAULT_ENTRY = ROOT__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__ID = ROOT__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__MODIFICATION = ROOT__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__ENTRY_LABEL = ROOT__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__ENTRY_SHORT_DESCRIPTION = ROOT__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__CHILDREN = ROOT__CHILDREN;

	/**
	 * The feature id for the '<em><b>Def Entry</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__DEF_ENTRY = ROOT__DEF_ENTRY;

	/**
	 * The feature id for the '<em><b>Cmp Categories</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__CMP_CATEGORIES = ROOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cmp Control Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__CMP_CONTROL_GROUP = ROOT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Palette Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP__PALETTE_LABEL = ROOT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the the '<em>Cmp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_CMP_FEATURE_COUNT = ROOT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.CategoryCmpImpl <em>Category Cmp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.CategoryCmpImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getCategoryCmp()
	 * @generated
	 */
	int CATEGORY_CMP = 8;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__ICON16_NAME = CATEGORY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__ICON32_NAME = CATEGORY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__VISIBLE = CATEGORY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__DEFAULT_ENTRY = CATEGORY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__ID = CATEGORY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__MODIFICATION = CATEGORY__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__ENTRY_LABEL = CATEGORY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__ENTRY_SHORT_DESCRIPTION = CATEGORY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__CHILDREN = CATEGORY__CHILDREN;

	/**
	 * The feature id for the '<em><b>Initial State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_CMP__INITIAL_STATE = CATEGORY__INITIAL_STATE;

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
	int GROUP_CMP = 9;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__ICON16_NAME = GROUP__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__ICON32_NAME = GROUP__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__VISIBLE = GROUP__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__DEFAULT_ENTRY = GROUP__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__ID = GROUP__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__MODIFICATION = GROUP__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__ENTRY_LABEL = GROUP__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__ENTRY_SHORT_DESCRIPTION = GROUP__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_CMP__CHILDREN = GROUP__CHILDREN;

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
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.EMFCreationToolEntryImpl <em>EMF Creation Tool Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.EMFCreationToolEntryImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getEMFCreationToolEntry()
	 * @generated
	 */
	int EMF_CREATION_TOOL_ENTRY = 11;

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
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__VISIBLE = CREATION_TOOL_ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__DEFAULT_ENTRY = CREATION_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__ID = CREATION_TOOL_ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__MODIFICATION = CREATION_TOOL_ENTRY__MODIFICATION;

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
	 * The feature id for the '<em><b>String Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__STRING_PROPERTIES = CREATION_TOOL_ENTRY__STRING_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_CREATION_TOOL_ENTRY__KEYED_VALUES = CREATION_TOOL_ENTRY__KEYED_VALUES;

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
	int EMF_PROTOTYPE_TOOL_ENTRY = 12;

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
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__VISIBLE = CREATION_TOOL_ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY = CREATION_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__ID = CREATION_TOOL_ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__MODIFICATION = CREATION_TOOL_ENTRY__MODIFICATION;

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
	 * The feature id for the '<em><b>String Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__STRING_PROPERTIES = CREATION_TOOL_ENTRY__STRING_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_PROTOTYPE_TOOL_ENTRY__KEYED_VALUES = CREATION_TOOL_ENTRY__KEYED_VALUES;

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
	int ANNOTATED_CREATION_ENTRY = 13;

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
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__VISIBLE = ABSTRACT_TOOL_ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY = ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__ID = ABSTRACT_TOOL_ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__MODIFICATION = ABSTRACT_TOOL_ENTRY__MODIFICATION;

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
	 * The feature id for the '<em><b>String Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CREATION_ENTRY__STRING_PROPERTIES = ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES;

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
	int SELECTION_CREATION_TOOL_ENTRY = 14;

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
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__VISIBLE = CREATION_TOOL_ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__DEFAULT_ENTRY = CREATION_TOOL_ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__ID = CREATION_TOOL_ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__MODIFICATION = CREATION_TOOL_ENTRY__MODIFICATION;

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
	 * The feature id for the '<em><b>String Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__STRING_PROPERTIES = CREATION_TOOL_ENTRY__STRING_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELECTION_CREATION_TOOL_ENTRY__KEYED_VALUES = CREATION_TOOL_ENTRY__KEYED_VALUES;

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
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.StackImpl <em>Stack</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.StackImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getStack()
	 * @generated
	 */
	int STACK = 16;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__ICON16_NAME = CONTAINER__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__ICON32_NAME = CONTAINER__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__VISIBLE = CONTAINER__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__DEFAULT_ENTRY = CONTAINER__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__ID = CONTAINER__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__MODIFICATION = CONTAINER__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__ENTRY_LABEL = CONTAINER__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__ENTRY_SHORT_DESCRIPTION = CONTAINER__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__CHILDREN = CONTAINER__CHILDREN;

	/**
	 * The feature id for the '<em><b>Active Entry</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK__ACTIVE_ENTRY = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Stack</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STACK_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.impl.SeparatorImpl <em>Separator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.impl.SeparatorImpl
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getSeparator()
	 * @generated
	 */
	int SEPARATOR = 17;

	/**
	 * The feature id for the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__ICON16_NAME = ENTRY__ICON16_NAME;

	/**
	 * The feature id for the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__ICON32_NAME = ENTRY__ICON32_NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__VISIBLE = ENTRY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__DEFAULT_ENTRY = ENTRY__DEFAULT_ENTRY;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__ID = ENTRY__ID;

	/**
	 * The feature id for the '<em><b>Modification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__MODIFICATION = ENTRY__MODIFICATION;

	/**
	 * The feature id for the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__ENTRY_LABEL = ENTRY__ENTRY_LABEL;

	/**
	 * The feature id for the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR__ENTRY_SHORT_DESCRIPTION = ENTRY__ENTRY_SHORT_DESCRIPTION;

	/**
	 * The number of structural features of the the '<em>Separator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEPARATOR_FEATURE_COUNT = ENTRY_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.Permissions <em>Permissions</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.Permissions
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getPermissions()
	 * @generated
	 */
	int PERMISSIONS = 18;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.cde.palette.InitialState <em>Initial State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.cde.palette.InitialState
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getInitialState()
	 * @generated
	 */
	int INITIAL_STATE = 19;

	/**
	 * The meta object id for the '<em>Creation Factory</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.requests.CreationFactory
	 * @see org.eclipse.ve.internal.cde.palette.impl.PalettePackageImpl#getCreationFactory()
	 * @generated
	 */
	int CREATION_FACTORY = 20;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Root <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Root
	 * @generated
	 */
	EClass getRoot();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.cde.palette.Root#getDefEntry <em>Def Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Def Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Root#getDefEntry()
	 * @see #getRoot()
	 * @generated
	 */
	EReference getRoot_DefEntry();

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
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.Entry#isVisible <em>Visible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry#isVisible()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Visible();

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
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.Entry#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry#getId()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.Entry#getModification <em>Modification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Modification</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Entry#getModification()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Modification();

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
	 * Returns the meta object for the map '{@link org.eclipse.ve.internal.cde.palette.AbstractToolEntry#getStringProperties <em>String Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>String Properties</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.AbstractToolEntry#getStringProperties()
	 * @see #getAbstractToolEntry()
	 * @generated
	 */
	EReference getAbstractToolEntry_StringProperties();

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
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getPaletteLabel <em>Palette Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Palette Label</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.PaletteCmp#getPaletteLabel()
	 * @see #getPaletteCmp()
	 * @generated
	 */
	EReference getPaletteCmp_PaletteLabel();

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
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Container <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Container
	 * @generated
	 */
	EClass getContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.cde.palette.Container#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Container#getChildren()
	 * @see #getContainer()
	 * @generated
	 */
	EReference getContainer_Children();

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
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Drawer <em>Drawer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Drawer</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Drawer
	 * @generated
	 */
	EClass getDrawer();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.cde.palette.Drawer#getInitialState <em>Initial State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initial State</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Drawer#getInitialState()
	 * @see #getDrawer()
	 * @generated
	 */
	EAttribute getDrawer_InitialState();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Stack <em>Stack</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stack</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Stack
	 * @generated
	 */
	EClass getStack();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.cde.palette.Stack#getActiveEntry <em>Active Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Active Entry</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Stack#getActiveEntry()
	 * @see #getStack()
	 * @generated
	 */
	EReference getStack_ActiveEntry();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.cde.palette.Separator <em>Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Separator</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Separator
	 * @generated
	 */
	EClass getSeparator();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.ve.internal.cde.palette.Permissions <em>Permissions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Permissions</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.Permissions
	 * @generated
	 */
	EEnum getPermissions();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.ve.internal.cde.palette.InitialState <em>Initial State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Initial State</em>'.
	 * @see org.eclipse.ve.internal.cde.palette.InitialState
	 * @generated
	 */
	EEnum getInitialState();

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
