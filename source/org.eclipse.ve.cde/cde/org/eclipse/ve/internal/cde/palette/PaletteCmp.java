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
 *  $RCSfile: PaletteCmp.java,v $
 *  $Revision: 1.8 $  $Date: 2007-09-17 14:17:14 $ 
 */


import org.eclipse.emf.common.util.EList;
import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cmp</b></em>'.
 * This is a palette where the categories and control group are composite aggregations. (i.e. they are contained within the  PaletteComp).
 * @deprecated Use {@link org.eclipse.ve.internal.cde.palette.Root} instead.
 * <p>
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is obsolete. Use Root instead.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpCategories <em>Cmp Categories</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getCmpControlGroup <em>Cmp Control Group</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getPaletteLabel <em>Palette Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteCmp()
 * @model
 * @generated
 */
public interface PaletteCmp extends Root{


	/**
	 * Returns the value of the '<em><b>Cmp Categories</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cde.palette.Category}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cmp Categories</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is obsolete. Add to Container.children instead.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Cmp Categories</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteCmp_CmpCategories()
	 * @model containment="true"
	 * @generated
	 */
	EList<Category> getCmpCategories();

	/**
	 * Returns the value of the '<em><b>Cmp Control Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cmp Control Group</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is obsolete. Use Container.children instead.
	 * <!-- end-model-doc -->
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

	/**
	 * Returns the value of the '<em><b>Palette Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Palette Label</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Palette Label</em>' containment reference.
	 * @see #setPaletteLabel(AbstractString)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPaletteCmp_PaletteLabel()
	 * @model containment="true" transient="true" volatile="true"
	 * @generated
	 */
	AbstractString getPaletteLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.PaletteCmp#getPaletteLabel <em>Palette Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Palette Label</em>' containment reference.
	 * @see #getPaletteLabel()
	 * @generated
	 */
	void setPaletteLabel(AbstractString value);

}
