/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jcm;
/*
 *  $RCSfile: BeanComposition.java,v $
 *  $Revision: 1.4 $  $Date: 2005-09-15 21:33:50 $ 
 */

import org.eclipse.emf.common.util.EList;

import org.eclipse.ve.internal.cdm.DiagramData;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bean Composition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanComposition#getComponents <em>Components</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanComposition#getListenerTypes <em>Listener Types</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanComposition()
 * @model
 * @generated
 */
public interface BeanComposition extends DiagramData, MemberContainer{
	/**
	 * Returns the value of the '<em><b>Components</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Components</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Components</em>' reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanComposition_Components()
	 * @model type="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EList getComponents();

	/**
	 * Returns the value of the '<em><b>Listener Types</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.jcm.ListenerType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Listener Types</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Listener Types</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanComposition_ListenerTypes()
	 * @model type="org.eclipse.ve.internal.jcm.ListenerType" containment="true"
	 * @generated
	 */
	EList getListenerTypes();

} // BeanComposition
