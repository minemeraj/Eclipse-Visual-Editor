/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.ve.internal.jcm;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>JCMMethod</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.JCMMethod#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.JCMMethod#getInitializes <em>Initializes</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.JCMMethod#getReturn <em>Return</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getJCMMethod()
 * @model
 * @generated
 */
public interface JCMMethod extends MemberContainer{
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getJCMMethod_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.JCMMethod#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Initializes</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initializes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initializes</em>' reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getJCMMethod_Initializes()
	 * @model type="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EList getInitializes();

	/**
	 * Returns the value of the '<em><b>Return</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return</em>' reference.
	 * @see #setReturn(EObject)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getJCMMethod_Return()
	 * @model
	 * @generated
	 */
	EObject getReturn();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.JCMMethod#getReturn <em>Return</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return</em>' reference.
	 * @see #getReturn()
	 * @generated
	 */
	void setReturn(EObject value);

} // JCMMethod
