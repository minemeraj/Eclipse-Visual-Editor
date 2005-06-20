/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: EMFPrototypeToolEntry.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-20 23:54:40 $ 
 */

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMF Prototype Tool Entry</b></em>'.
 * This is like an EMFCreationToolEntry, but instead of creating a new instance based upon a classtype, a copy of the supplied prototype is returned instead.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is like an EMFCreationToolEntry, but instead of creating a new instance based upon a classtype, a copy of the supplied prototype is returned instead.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry#getPrototypeURI <em>Prototype URI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEMFPrototypeToolEntry()
 * @model
 * @generated
 */
public interface EMFPrototypeToolEntry extends CreationToolEntry{
	/**
	 * Returns the value of the '<em><b>Prototype URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prototype URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is the string containing the prototype URI for the prototype. 
	 * <p>
	 * The URI is to the object protoype that is being created. The URI must point to an EObject in a resource. That EObject must be contained directly by the Resource it is in (i.e. it mustn't be contained by another EObject). Also in the resource can be Annotations for any of the EObjects that are part of the template. These will automatically be added in.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Prototype URI</em>' attribute.
	 * @see #setPrototypeURI(String)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEMFPrototypeToolEntry_PrototypeURI()
	 * @model
	 * @generated
	 */
	String getPrototypeURI();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry#getPrototypeURI <em>Prototype URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prototype URI</em>' attribute.
	 * @see #getPrototypeURI()
	 * @generated
	 */
	void setPrototypeURI(String value);

}
