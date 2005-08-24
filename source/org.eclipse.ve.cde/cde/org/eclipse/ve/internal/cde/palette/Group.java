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
 *  $RCSfile: Group.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:51 $ 
 */


import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a group. 
 * <p>
 * A group is a container that cannot be collapsed. It can accept any entry type.
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
 * @model
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
	 * @deprecated Use {@link Entry#getEntryLabel()} instead.
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is obsolete. Use Entry.entryLabel instead.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Group Label</em>' containment reference.
	 * @see #setGroupLabel(AbstractString)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getGroup_GroupLabel()
	 * @model containment="true" transient="true" volatile="true"
	 * @generated
	 */
	AbstractString getGroupLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Group#getGroupLabel <em>Group Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @deprecated Use {@link Entry#setEntryLabel(AbstractString)} instead.
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Label</em>' containment reference.
	 * @see #getGroupLabel()
	 * @generated
	 */
	void setGroupLabel(AbstractString value);

}
