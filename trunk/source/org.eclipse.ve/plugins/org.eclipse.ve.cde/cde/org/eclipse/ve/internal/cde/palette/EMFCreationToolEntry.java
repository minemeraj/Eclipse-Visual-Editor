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
 *  $RCSfile: EMFCreationToolEntry.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */


import java.lang.String;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMF Creation Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry#getCreationClassURI <em>Creation Class URI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEMFCreationToolEntry()
 * @model 
 * @generated
 */

public interface EMFCreationToolEntry extends CreationToolEntry {
	/**
	 * Returns the value of the '<em><b>Creation Class URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Creation Class URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Creation Class URI</em>' attribute.
	 * @see #setCreationClassURI(String)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEMFCreationToolEntry_CreationClassURI()
	 * @model 
	 * @generated
	 */
	String getCreationClassURI();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry#getCreationClassURI <em>Creation Class URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Creation Class URI</em>' attribute.
	 * @see #getCreationClassURI()
	 * @generated
	 */
	void setCreationClassURI(String value);

}
