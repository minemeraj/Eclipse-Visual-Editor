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
 *  $RCSfile: CategoryRef.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:17:52 $ 
 */


import org.eclipse.emf.common.util.EList;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Category Ref</b></em>'.
 * This is a catefory where the group is a shared aggregation. (i.e. is is actually contained somewhere else and only referenced here). This type of category allows references to pieces of other palettes to form together a new category.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a catefory where the group is a shared aggregation. (i.e. is is actually contained somewhere else and only referenced here). This type of category allows references to pieces of other palettes to form together a new category.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.CategoryRef#getRefGroups <em>Ref Groups</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCategoryRef()
 * @model 
 * @generated
 */
public interface CategoryRef extends Category{


	/**
	 * Returns the value of the '<em><b>Ref Groups</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cde.palette.Group}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref Groups</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Groups</em>' reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCategoryRef_RefGroups()
	 * @model type="org.eclipse.ve.internal.cde.palette.Group"
	 * @generated
	 */
	EList getRefGroups();

}
