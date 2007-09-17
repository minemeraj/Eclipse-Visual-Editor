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
 *  $RCSfile: PropertyChangeEventInvocation.java,v $
 *  $Revision: 1.5 $  $Date: 2007-09-17 14:21:53 $ 
 */

import org.eclipse.jem.java.Method;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Change Event Invocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation#getAddMethod <em>Add Method</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getPropertyChangeEventInvocation()
 * @model
 * @generated
 */
public interface PropertyChangeEventInvocation extends AbstractEventInvocation{
	/**
	 * Returns the value of the '<em><b>Add Method</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Add Method</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Add Method</em>' reference.
	 * @see #setAddMethod(Method)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getPropertyChangeEventInvocation_AddMethod()
	 * @model required="true"
	 * @generated
	 */
	Method getAddMethod();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation#getAddMethod <em>Add Method</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Add Method</em>' reference.
	 * @see #getAddMethod()
	 * @generated
	 */
	void setAddMethod(Method value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.jcm.PropertyEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getPropertyChangeEventInvocation_Properties()
	 * @model containment="true"
	 * @generated
	 */
	EList<PropertyEvent> getProperties();

} // PropertyChangeEventInvocation
