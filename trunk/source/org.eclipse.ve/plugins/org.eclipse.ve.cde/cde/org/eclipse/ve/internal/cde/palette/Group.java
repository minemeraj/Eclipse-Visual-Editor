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
 *  $RCSfile: Group.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:35 $ 
 */


import java.util.List;

import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * This is the abstract base group. There are two standard implementations of it:
 * 
 * GroupCmp: This is a group where the entries are a composite aggregation. (i.e. it is contained within the  GroupCmp).
 * 
 * GroupRef: This is a group where the entries is a shared aggregation. (i.e. is is actually contained somewhere else and only referenced here). This type of group allows references to pieces of other palettes to form together a new group.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is the abstract base group. There are two standard implementations of it:
 * 
 * GroupCmp: This is a group where the entries are a composite aggregation. (i.e. it is contained within the  GroupCmp).
 * 
 * GroupRef: This is a group where the entries is a shared aggregation. (i.e. is is actually contained somewhere else and only referenced here). This type of group allows references to pieces of other palettes to form together a new group.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Group#getGroupLabel <em>Group Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getGroup()
 * @model abstract="true"
 * @generated
 */
public interface Group extends Container{


	/**
	 * Returns the value of the '<em><b>Group Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Label</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Label</em>' containment reference.
	 * @see #setGroupLabel(AbstractString)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getGroup_GroupLabel()
	 * @model containment="true"
	 * @generated
	 */
	AbstractString getGroupLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Group#getGroupLabel <em>Group Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Label</em>' containment reference.
	 * @see #getGroupLabel()
	 * @generated
	 */
	void setGroupLabel(AbstractString value);

	/**
	 * Return the entries
	 */
	public List getEntries();
	
}
