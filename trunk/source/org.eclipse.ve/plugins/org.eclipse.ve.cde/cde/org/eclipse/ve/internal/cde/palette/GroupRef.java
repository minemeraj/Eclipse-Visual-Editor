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
 *  $RCSfile: GroupRef.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:35 $ 
 */


import org.eclipse.emf.common.util.EList;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group Ref</b></em>'.
 * This is a group where the entries is a shared aggregation. (i.e. is is actually contained somewhere else and only referenced here). This type of group allows references to pieces of other palettes to form together a new group.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a group where the entries is a shared aggregation. (i.e. is is actually contained somewhere else and only referenced here). This type of group allows references to pieces of other palettes to form together a new group.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.GroupRef#getRefEntries <em>Ref Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getGroupRef()
 * @model 
 * @generated
 */
public interface GroupRef extends Group{


	/**
	 * Returns the value of the '<em><b>Ref Entries</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cde.palette.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Entries</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getGroupRef_RefEntries()
	 * @model type="org.eclipse.ve.internal.cde.palette.Entry" containment="true"
	 * @generated
	 */
	EList getRefEntries();

}
