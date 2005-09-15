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
package org.eclipse.ve.internal.cde.utility;
/*
 *  $RCSfile: TranslatableString.java,v $
 *  $Revision: 1.4 $  $Date: 2005-09-15 21:27:15 $ 
 */

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Translatable String</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.TranslatableString#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.TranslatableString#getBundle <em>Bundle</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getTranslatableString()
 * @model
 * @generated
 */
public interface TranslatableString extends AbstractString{


	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getTranslatableString_Key()
	 * @model
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.utility.TranslatableString#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Bundle</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bundle</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bundle</em>' reference.
	 * @see #setBundle(ResourceBundle)
	 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getTranslatableString_Bundle()
	 * @model
	 * @generated
	 */
	ResourceBundle getBundle();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.utility.TranslatableString#getBundle <em>Bundle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bundle</em>' reference.
	 * @see #getBundle()
	 * @generated
	 */
	void setBundle(ResourceBundle value);

}
