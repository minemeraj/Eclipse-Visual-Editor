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
 *  $RCSfile: PaletteFactory.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
 
import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage
 * @generated
 */
public interface PaletteFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PaletteFactory eINSTANCE = new org.eclipse.ve.internal.cde.palette.impl.PaletteFactoryImpl();

	/**
	 * Returns a new object of class '<em>Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entry</em>'.
	 * @generated
	 */
	Entry createEntry();

	/**
	 * Returns a new object of class '<em>Tool Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tool Entry</em>'.
	 * @generated
	 */
	ToolEntry createToolEntry();

	/**
	 * Returns a new object of class '<em>Ref</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ref</em>'.
	 * @generated
	 */
	PaletteRef createPaletteRef();

	/**
	 * Returns a new object of class '<em>Cmp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cmp</em>'.
	 * @generated
	 */
	PaletteCmp createPaletteCmp();

	/**
	 * Returns a new object of class '<em>Category Ref</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Category Ref</em>'.
	 * @generated
	 */
	CategoryRef createCategoryRef();

	/**
	 * Returns a new object of class '<em>Category Cmp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Category Cmp</em>'.
	 * @generated
	 */
	CategoryCmp createCategoryCmp();

	/**
	 * Returns a new object of class '<em>Group Cmp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group Cmp</em>'.
	 * @generated
	 */
	GroupCmp createGroupCmp();

	/**
	 * Returns a new object of class '<em>Group Ref</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group Ref</em>'.
	 * @generated
	 */
	GroupRef createGroupRef();

	/**
	 * Returns a new object of class '<em>EMF Creation Tool Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EMF Creation Tool Entry</em>'.
	 * @generated
	 */
	EMFCreationToolEntry createEMFCreationToolEntry();

	/**
	 * Returns a new object of class '<em>EMF Prototype Tool Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EMF Prototype Tool Entry</em>'.
	 * @generated
	 */
	EMFPrototypeToolEntry createEMFPrototypeToolEntry();

	/**
	 * Returns a new object of class '<em>Annotated Creation Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotated Creation Entry</em>'.
	 * @generated
	 */
	AnnotatedCreationEntry createAnnotatedCreationEntry();

	/**
	 * Returns a new object of class '<em>Selection Creation Tool Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Selection Creation Tool Entry</em>'.
	 * @generated
	 */
	SelectionCreationToolEntry createSelectionCreationToolEntry();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	PalettePackage getPalettePackage();

} //PaletteFactory
