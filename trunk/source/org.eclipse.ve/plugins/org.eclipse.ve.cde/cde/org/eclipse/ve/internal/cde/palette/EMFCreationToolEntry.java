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
 *  $RCSfile: EMFCreationToolEntry.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:35 $ 
 */


import java.lang.String;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMF Creation Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Creation for creating EMF instances.
 * <!-- end-model-doc -->
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

public interface EMFCreationToolEntry extends CreationToolEntry{
	/**
	 * Returns the value of the '<em><b>Creation Class URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Creation Class URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The URI of the class for which an instance is to be created.
	 * <!-- end-model-doc -->
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
