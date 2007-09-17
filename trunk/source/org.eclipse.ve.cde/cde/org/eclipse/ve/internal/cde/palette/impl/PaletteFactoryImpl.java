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
 *  $RCSfile: PaletteFactoryImpl.java,v $
 *  $Revision: 1.9 $  $Date: 2007-09-17 14:17:13 $ 
 */
import org.eclipse.ve.internal.cde.palette.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.gef.requests.CreationFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PaletteFactoryImpl extends EFactoryImpl implements PaletteFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PaletteFactory init() {
		try {
			PaletteFactory thePaletteFactory = (PaletteFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/ve/internal/cde/palette.ecore"); 
			if (thePaletteFactory != null) {
				return thePaletteFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new PaletteFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
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
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case PalettePackage.ROOT: return createRoot();
			case PalettePackage.GROUP: return createGroup();
			case PalettePackage.TOOL_ENTRY: return createToolEntry();
			case PalettePackage.PALETTE_CMP: return createPaletteCmp();
			case PalettePackage.CATEGORY_CMP: return createCategoryCmp();
			case PalettePackage.GROUP_CMP: return createGroupCmp();
			case PalettePackage.EMF_CREATION_TOOL_ENTRY: return createEMFCreationToolEntry();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY: return createEMFPrototypeToolEntry();
			case PalettePackage.ANNOTATED_CREATION_ENTRY: return createAnnotatedCreationEntry();
			case PalettePackage.SELECTION_CREATION_TOOL_ENTRY: return createSelectionCreationToolEntry();
			case PalettePackage.DRAWER: return createDrawer();
			case PalettePackage.STACK: return createStack();
			case PalettePackage.SEPARATOR: return createSeparator();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case PalettePackage.PERMISSIONS:
				return createPermissionsFromString(eDataType, initialValue);
			case PalettePackage.INITIAL_STATE:
				return createInitialStateFromString(eDataType, initialValue);
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
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case PalettePackage.PERMISSIONS:
				return convertPermissionsToString(eDataType, instanceValue);
			case PalettePackage.INITIAL_STATE:
				return convertInitialStateToString(eDataType, instanceValue);
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
	public Root createRoot() {
		RootImpl root = new RootImpl();
		return root;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Group createGroup() {
		GroupImpl group = new GroupImpl();
		return group;
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
	public PaletteCmp createPaletteCmp() {
		PaletteCmpImpl paletteCmp = new PaletteCmpImpl();
		return paletteCmp;
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
	public Drawer createDrawer() {
		DrawerImpl drawer = new DrawerImpl();
		return drawer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Stack createStack() {
		StackImpl stack = new StackImpl();
		return stack;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Separator createSeparator() {
		SeparatorImpl separator = new SeparatorImpl();
		return separator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Permissions createPermissionsFromString(EDataType eDataType, String initialValue) {
		Permissions result = Permissions.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPermissionsToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InitialState createInitialStateFromString(EDataType eDataType, String initialValue) {
		InitialState result = InitialState.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInitialStateToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
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
	@Deprecated
	public static PalettePackage getPackage() {
		return PalettePackage.eINSTANCE;
	}

} //PaletteFactoryImpl
