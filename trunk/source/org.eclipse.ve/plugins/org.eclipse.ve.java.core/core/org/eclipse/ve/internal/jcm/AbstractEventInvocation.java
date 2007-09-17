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
 *  $RCSfile: AbstractEventInvocation.java,v $
 *  $Revision: 1.3 $  $Date: 2007-09-17 14:21:53 $ 
 */

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Event Invocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.AbstractEventInvocation#getCallbacks <em>Callbacks</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.AbstractEventInvocation#getListener <em>Listener</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getAbstractEventInvocation()
 * @model abstract="true"
 * @generated
 */
public interface AbstractEventInvocation extends EObject{
	/**
	 * Returns the value of the '<em><b>Callbacks</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.jcm.Callback}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Callbacks</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Callbacks</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getAbstractEventInvocation_Callbacks()
	 * @model containment="true"
	 * @generated
	 */
	EList<Callback> getCallbacks();

	/**
	 * Returns the value of the '<em><b>Listener</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ve.internal.jcm.Listener#getListenedBy <em>Listened By</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Listener</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Listener</em>' reference.
	 * @see #setListener(Listener)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getAbstractEventInvocation_Listener()
	 * @see org.eclipse.ve.internal.jcm.Listener#getListenedBy
	 * @model opposite="listenedBy"
	 * @generated
	 */
	Listener getListener();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.AbstractEventInvocation#getListener <em>Listener</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Listener</em>' reference.
	 * @see #getListener()
	 * @generated
	 */
	void setListener(Listener value);

} // AbstractEventInvocation
