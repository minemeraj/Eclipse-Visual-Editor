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
 *  $RCSfile: PaletteRef.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:17:52 $ 
 */


import org.eclipse.emf.common.util.EList;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ref</b></em>'.
 * This is a palette where the categories and control group are shared aggregations. (i.e. they are actually contained somewhere else and only referenced here). This type of palette allows references to pieces of other palettes to form together a new palette.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a palette where the categories and control group are shared aggregations. (i.e. they are actually contained somewhere else and only referenced here). This type of palette allows references to pieces of other palettes to form together a new palette.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.PaletteRef#getRefControlGroup <em>Ref Control Group</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.PaletteRef#getRefCategories <em>Ref Categories</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteRef()
 * @model 
 * @generated
 */
public interface PaletteRef extends Palette{


	/**
	 * Returns the value of the '<em><b>Ref Control Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref Control Group</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Control Group</em>' reference.
	 * @see #setRefControlGroup(Group)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteRef_RefControlGroup()
	 * @model 
	 * @generated
	 */
	Group getRefControlGroup();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.PaletteRef#getRefControlGroup <em>Ref Control Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Control Group</em>' reference.
	 * @see #getRefControlGroup()
	 * @generated
	 */
	void setRefControlGroup(Group value);

	/**
	 * Returns the value of the '<em><b>Ref Categories</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cde.palette.Category}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref Categories</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Categories</em>' reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteRef_RefCategories()
	 * @model type="org.eclipse.ve.internal.cde.palette.Category"
	 * @generated
	 */
	EList getRefCategories();

}
