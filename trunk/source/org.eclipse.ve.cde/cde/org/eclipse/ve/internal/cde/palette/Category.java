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
 *  $RCSfile: Category.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:51 $ 
 */


import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Category</b></em>'.
 * @deprecated Use {@link org.eclipse.ve.internal.cde.palette.Drawer} instead.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is obsolete. Use Drawer instead.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Category#getCategoryLabel <em>Category Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCategory()
 * @model abstract="true"
 * @generated
 */
public interface Category extends Drawer{


	/**
	 * Returns the value of the '<em><b>Category Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category Label</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is obsolete. Use Entry.entryLabel instead.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Category Label</em>' containment reference.
	 * @see #setCategoryLabel(AbstractString)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCategory_CategoryLabel()
	 * @model containment="true" required="true" transient="true" volatile="true"
	 * @generated
	 */
	AbstractString getCategoryLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Category#getCategoryLabel <em>Category Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category Label</em>' containment reference.
	 * @see #getCategoryLabel()
	 * @generated
	 */
	void setCategoryLabel(AbstractString value);

}
