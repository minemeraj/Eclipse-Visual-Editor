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
 *  $RCSfile: Listener.java,v $
 *  $Revision: 1.4 $  $Date: 2007-09-17 14:21:53 $ 
 */

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Listener</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.Listener#getListenedBy <em>Listened By</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.Listener#getListenerType <em>Listener Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListener()
 * @model
 * @generated
 */
public interface Listener extends EObject{
	/**
	 * Returns the value of the '<em><b>Listened By</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.jcm.AbstractEventInvocation}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ve.internal.jcm.AbstractEventInvocation#getListener <em>Listener</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Listened By</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Listened By</em>' reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListener_ListenedBy()
	 * @see org.eclipse.ve.internal.jcm.AbstractEventInvocation#getListener
	 * @model opposite="listener"
	 * @generated
	 */
	EList<AbstractEventInvocation> getListenedBy();

	/**
	 * Returns the value of the '<em><b>Listener Type</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ve.internal.jcm.ListenerType#getListeners <em>Listeners</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Listener Type</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Listener Type</em>' container reference.
	 * @see #setListenerType(ListenerType)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListener_ListenerType()
	 * @see org.eclipse.ve.internal.jcm.ListenerType#getListeners
	 * @model opposite="listeners"
	 * @generated
	 */
	ListenerType getListenerType();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.Listener#getListenerType <em>Listener Type</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Listener Type</em>' container reference.
	 * @see #getListenerType()
	 * @generated
	 */
	void setListenerType(ListenerType value);

} // Listener
