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
 *  $RCSfile: EventInvocation.java,v $
 *  $Revision: 1.3 $  $Date: 2005-09-15 21:33:50 $ 
 */

import org.eclipse.jem.internal.beaninfo.BeanEvent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event Invocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.EventInvocation#getEvent <em>Event</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getEventInvocation()
 * @model
 * @generated
 */
public interface EventInvocation extends AbstractEventInvocation{
	/**
	 * Returns the value of the '<em><b>Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event</em>' reference.
	 * @see #setEvent(BeanEvent)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getEventInvocation_Event()
	 * @model
	 * @generated
	 */
	BeanEvent getEvent();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.EventInvocation#getEvent <em>Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event</em>' reference.
	 * @see #getEvent()
	 * @generated
	 */
	void setEvent(BeanEvent value);

} // EventInvocation
