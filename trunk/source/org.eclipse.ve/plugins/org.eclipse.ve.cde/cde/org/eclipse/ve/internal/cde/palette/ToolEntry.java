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
 *  $RCSfile: ToolEntry.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.ToolEntry#getToolClassName <em>Tool Class Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getToolEntry()
 * @model 
 * @generated
 */

public interface ToolEntry extends AbstractToolEntry {


	/**
	 * Returns the value of the '<em><b>Tool Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tool Class Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tool Class Name</em>' attribute.
	 * @see #setToolClassName(String)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getToolEntry_ToolClassName()
	 * @model 
	 * @generated
	 */
	String getToolClassName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.ToolEntry#getToolClassName <em>Tool Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tool Class Name</em>' attribute.
	 * @see #getToolClassName()
	 * @generated
	 */
	void setToolClassName(String value);

}
