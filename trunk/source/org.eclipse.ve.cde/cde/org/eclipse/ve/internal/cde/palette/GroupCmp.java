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
 *  $RCSfile: GroupCmp.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:17:52 $ 
 */


import org.eclipse.emf.common.util.EList;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group Cmp</b></em>'.
 * This is a group where the entries are a composite aggregation. (i.e. it is contained within the  GroupCmp).
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a group where the entries are a composite aggregation. (i.e. it is contained within the  GroupCmp).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.GroupCmp#getCmpEntries <em>Cmp Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getGroupCmp()
 * @model 
 * @generated
 */
public interface GroupCmp extends Group{


	/**
	 * Returns the value of the '<em><b>Cmp Entries</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cde.palette.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cmp Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cmp Entries</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getGroupCmp_CmpEntries()
	 * @model type="org.eclipse.ve.internal.cde.palette.Entry" containment="true"
	 * @generated
	 */
	EList getCmpEntries();

}
