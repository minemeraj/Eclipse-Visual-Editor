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
 *  $RCSfile: CategoryCmp.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:17:52 $ 
 */


import org.eclipse.emf.common.util.EList;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Category Cmp</b></em>'.
 * This is a category where the groups is a composite aggregation. (i.e. it is contained within the  CategoryCmp)
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a category where the groups is a composite aggregation. (i.e. it is contained within the  CategoryCmp)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.CategoryCmp#getCmpGroups <em>Cmp Groups</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCategoryCmp()
 * @model 
 * @generated
 */
public interface CategoryCmp extends Category{


	/**
	 * Returns the value of the '<em><b>Cmp Groups</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cde.palette.Group}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cmp Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cmp Groups</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCategoryCmp_CmpGroups()
	 * @model type="org.eclipse.ve.internal.cde.palette.Group" containment="true"
	 * @generated
	 */
	EList getCmpGroups();

}
