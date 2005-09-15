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
 *  $RCSfile: PropertyEvent.java,v $
 *  $Revision: 1.3 $  $Date: 2005-09-15 21:33:50 $ 
 */

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.PropertyEvent#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.PropertyEvent#isUseIfExpression <em>Use If Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getPropertyEvent()
 * @model
 * @generated
 */
public interface PropertyEvent extends EObject{
	/**
	 * Returns the value of the '<em><b>Property Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property Name</em>' attribute.
	 * @see #setPropertyName(String)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getPropertyEvent_PropertyName()
	 * @model
	 * @generated
	 */
	String getPropertyName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.PropertyEvent#getPropertyName <em>Property Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property Name</em>' attribute.
	 * @see #getPropertyName()
	 * @generated
	 */
	void setPropertyName(String value);

	/**
	 * Returns the value of the '<em><b>Use If Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use If Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use If Expression</em>' attribute.
	 * @see #setUseIfExpression(boolean)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getPropertyEvent_UseIfExpression()
	 * @model
	 * @generated
	 */
	boolean isUseIfExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.PropertyEvent#isUseIfExpression <em>Use If Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use If Expression</em>' attribute.
	 * @see #isUseIfExpression()
	 * @generated
	 */
	void setUseIfExpression(boolean value);

} // PropertyEvent
