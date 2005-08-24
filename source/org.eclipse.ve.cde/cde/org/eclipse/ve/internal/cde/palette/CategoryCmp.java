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
 *  $RCSfile: CategoryCmp.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:51 $ 
 */


import org.eclipse.emf.common.util.EList;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Category Cmp</b></em>'.
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
	 * <!-- begin-model-doc -->
	 * This is obsolete. Categories can no longer contain groups.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Cmp Groups</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCategoryCmp_CmpGroups()
	 * @model type="org.eclipse.ve.internal.cde.palette.Group" containment="true"
	 * @generated
	 */
	EList getCmpGroups();

}
