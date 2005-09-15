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
 *  $RCSfile: MemberContainer.java,v $
 *  $Revision: 1.3 $  $Date: 2005-09-15 21:33:50 $ 
 */

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Member Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.MemberContainer#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.MemberContainer#getMembers <em>Members</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getMemberContainer()
 * @model
 * @generated
 */
public interface MemberContainer extends EObject{
	/**
	 * Returns the value of the '<em><b>Members</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Members</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Members</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getMemberContainer_Members()
	 * @model type="org.eclipse.emf.ecore.EObject" containment="true"
	 * @generated
	 */
	EList getMembers();

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getMemberContainer_Properties()
	 * @model type="org.eclipse.emf.ecore.EObject" containment="true"
	 * @generated
	 */
	EList getProperties();

} // MemberContainer
