/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ve.internal.cde.palette.impl;
/*
 *  $RCSfile: PaletteFactoryImpl.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:35 $ 
 */
import org.eclipse.ve.internal.cde.palette.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.gef.requests.CreationFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PaletteFactoryImpl extends EFactoryImpl implements PaletteFactory {
	/**
	 * Creates and instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PaletteFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case PalettePackage.ENTRY: return createEntry();
			case PalettePackage.TOOL_ENTRY: return createToolEntry();
			case PalettePackage.PALETTE_REF: return createPaletteRef();
			case PalettePackage.PALETTE_CMP: return createPaletteCmp();
			case PalettePackage.CATEGORY_REF: return createCategoryRef();
			case PalettePackage.CATEGORY_CMP: return createCategoryCmp();
			case PalettePackage.GROUP_CMP: return createGroupCmp();
			case PalettePackage.GROUP_REF: return createGroupRef();
			case PalettePackage.EMF_CREATION_TOOL_ENTRY: return createEMFCreationToolEntry();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY: return createEMFPrototypeToolEntry();
			case PalettePackage.ANNOTATED_CREATION_ENTRY: return createAnnotatedCreationEntry();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY: return createSelectionCreationToolEntry();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case PalettePackage.CREATION_FACTORY:
				return createCreationFactoryFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case PalettePackage.CREATION_FACTORY:
				return convertCreationFactoryToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entry createEntry() {
		EntryImpl entry = new EntryImpl();
		return entry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ToolEntry createToolEntry() {
		ToolEntryImpl toolEntry = new ToolEntryImpl();
		return toolEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PaletteRef createPaletteRef() {
		PaletteRefImpl paletteRef = new PaletteRefImpl();
		return paletteRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PaletteCmp createPaletteCmp() {
		PaletteCmpImpl paletteCmp = new PaletteCmpImpl();
		return paletteCmp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CategoryRef createCategoryRef() {
		CategoryRefImpl categoryRef = new CategoryRefImpl();
		return categoryRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CategoryCmp createCategoryCmp() {
		CategoryCmpImpl categoryCmp = new CategoryCmpImpl();
		return categoryCmp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupCmp createGroupCmp() {
		GroupCmpImpl groupCmp = new GroupCmpImpl();
		return groupCmp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupRef createGroupRef() {
		GroupRefImpl groupRef = new GroupRefImpl();
		return groupRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMFCreationToolEntry createEMFCreationToolEntry() {
		EMFCreationToolEntryImpl emfCreationToolEntry = new EMFCreationToolEntryImpl();
		return emfCreationToolEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMFPrototypeToolEntry createEMFPrototypeToolEntry() {
		EMFPrototypeToolEntryImpl emfPrototypeToolEntry = new EMFPrototypeToolEntryImpl();
		return emfPrototypeToolEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotatedCreationEntry createAnnotatedCreationEntry() {
		AnnotatedCreationEntryImpl annotatedCreationEntry = new AnnotatedCreationEntryImpl();
		return annotatedCreationEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SelectionCreationToolEntry createSelectionCreationToolEntry() {
		SelectionCreationToolEntryImpl selectionCreationToolEntry = new SelectionCreationToolEntryImpl();
		return selectionCreationToolEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CreationFactory createCreationFactoryFromString(EDataType eDataType, String initialValue) {
		return (CreationFactory)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCreationFactoryToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PalettePackage getPalettePackage() {
		return (PalettePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static PalettePackage getPackage() {
		return PalettePackage.eINSTANCE;
	}
} //PaletteFactoryImpl
