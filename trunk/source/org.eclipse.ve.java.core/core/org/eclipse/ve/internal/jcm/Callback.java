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
/*
 *  $RCSfile: Callback.java,v $
 *  $Revision: 1.6 $  $Date: 2007-09-17 14:21:53 $ 
 */

import org.eclipse.emf.common.util.EList;

import org.eclipse.jem.java.Method;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jem.internal.instantiation.PTExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Callback</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.Callback#isSharedScope <em>Shared Scope</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.Callback#getMethod <em>Method</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.Callback#getStatements <em>Statements</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getCallback()
 * @model
 * @generated
 */
public interface Callback extends EObject{
	/**
	 * Returns the value of the '<em><b>Shared Scope</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shared Scope</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shared Scope</em>' attribute.
	 * @see #setSharedScope(boolean)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getCallback_SharedScope()
	 * @model
	 * @generated
	 */
	boolean isSharedScope();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.Callback#isSharedScope <em>Shared Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shared Scope</em>' attribute.
	 * @see #isSharedScope()
	 * @generated
	 */
	void setSharedScope(boolean value);

	/**
	 * Returns the value of the '<em><b>Method</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method</em>' reference.
	 * @see #setMethod(Method)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getCallback_Method()
	 * @model required="true"
	 * @generated
	 */
	Method getMethod();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.Callback#getMethod <em>Method</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method</em>' reference.
	 * @see #getMethod()
	 * @generated
	 */
	void setMethod(Method value);

	/**
	 * Returns the value of the '<em><b>Statements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jem.internal.instantiation.PTExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * List of modeled statements to be in this callback.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Statements</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getCallback_Statements()
	 * @model containment="true"
	 * @generated
	 */
	EList<PTExpression> getStatements();

} // Callback
