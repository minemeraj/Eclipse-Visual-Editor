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
package org.eclipse.ve.internal.cde.palette;
/*
 *  $RCSfile: PaletteCmp.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:35 $ 
 */


import org.eclipse.emf.common.util.EList;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cmp</b></em>'.
 * This is a palette where the categories and control group are composite aggregations. (i.e. they are contained within the  PaletteComp).
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a palette where the categories and control group are composite aggregations. (i.e. they are contained within the  PaletteComp).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpCategories <em>Cmp Categories</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpControlGroup <em>Cmp Control Group</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteCmp()
 * @model 
 * @generated
 */
public interface PaletteCmp extends Palette{


	/**
	 * Returns the value of the '<em><b>Cmp Categories</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cde.palette.Category}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cmp Categories</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cmp Categories</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteCmp_CmpCategories()
	 * @model type="org.eclipse.ve.internal.cde.palette.Category" containment="true"
	 * @generated
	 */
	EList getCmpCategories();

	/**
	 * Returns the value of the '<em><b>Cmp Control Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cmp Control Group</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cmp Control Group</em>' containment reference.
	 * @see #setCmpControlGroup(Group)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteCmp_CmpControlGroup()
	 * @model containment="true"
	 * @generated
	 */
	Group getCmpControlGroup();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpControlGroup <em>Cmp Control Group</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cmp Control Group</em>' containment reference.
	 * @see #getCmpControlGroup()
	 * @generated
	 */
	void setCmpControlGroup(Group value);

}
