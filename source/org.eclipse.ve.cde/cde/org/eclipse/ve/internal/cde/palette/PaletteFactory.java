package org.eclipse.ve.internal.cde.palette;
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
 *  $RCSfile: PaletteFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-20 23:54:40 $ 
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
public interface PaletteFactory extends EFactory{
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PaletteFactory eINSTANCE = new org.eclipse.ve.internal.cde.palette.impl.PaletteFactoryImpl();

	/**
	 * Returns a new object of class '<em>Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Root</em>'.
	 * @generated
	 */
	Root createRoot();

	/**
	 * Returns a new object of class '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group</em>'.
	 * @generated
	 */
	Group createGroup();

	/**
	 * Returns a new object of class '<em>Tool Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tool Entry</em>'.
	 * @generated
	 */
	ToolEntry createToolEntry();

	/**
	 * Returns a new object of class '<em>Cmp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cmp</em>'.
	 * @generated
	 */
	PaletteCmp createPaletteCmp();

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
	 * Returns a new object of class '<em>Drawer</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Drawer</em>'.
	 * @generated
	 */
	Drawer createDrawer();

	/**
	 * Returns a new object of class '<em>Stack</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Stack</em>'.
	 * @generated
	 */
	Stack createStack();

	/**
	 * Returns a new object of class '<em>Separator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Separator</em>'.
	 * @generated
	 */
	Separator createSeparator();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	PalettePackage getPalettePackage();

} //PaletteFactory
