/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:18:01 $ 
 */


import java.util.List;

import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Category</b></em>'.
 * This is the abstract base category. There are two standard implementations of it:
 * 
 * CategoryCmp: This is a category where the groups is a composite aggregation. (i.e. it is contained within the  CategoryCmp).
 * 
 * CategoryRef: This is a category where the group is a shared aggregation. (i.e. is is actually contained somewhere else and only referenced here). This type of category allows references to pieces of other palettes to form together a new category.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is the abstract base category. There are two standard implementations of it:
 * 
 * CategoryCmp: This is a category where the groups is a composite aggregation. (i.e. it is contained within the  CategoryCmp).
 * 
 * CategoryRef: This is a category where the group is a shared aggregation. (i.e. is is actually contained somewhere else and only referenced here). This type of category allows references to pieces of other palettes to form together a new category.
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
public interface Category extends Container{


	/**
	 * Returns the value of the '<em><b>Category Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category Label</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category Label</em>' containment reference.
	 * @see #setCategoryLabel(AbstractString)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCategory_CategoryLabel()
	 * @model containment="true" required="true"
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

	/**
	 * Return the groups
	 */
	public List getGroups();	
}
