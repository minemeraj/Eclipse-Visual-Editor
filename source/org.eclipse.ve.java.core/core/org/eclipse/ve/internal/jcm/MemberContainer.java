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
 *  $Revision: 1.5 $  $Date: 2007-09-17 14:21:53 $ 
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
 *   <li>{@link org.eclipse.ve.internal.jcm.MemberContainer#getImplicits <em>Implicits</em>}</li>
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
	 * <!-- begin-model-doc -->
	 * This is the collection of members. Members are values that are located in this container AND have either a declaration or property settings.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Members</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getMemberContainer_Members()
	 * @model containment="true"
	 * @generated
	 */
	EList<EObject> getMembers();

	/**
	 * Returns the value of the '<em><b>Implicits</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This contains implicit settings. These are settings that are properties of members but they are not explicitly set. They are the value of a setting that has not been set. It is a default value. Unlike properties, implicit may have further settings on them.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Implicits</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getMemberContainer_Implicits()
	 * @model containment="true"
	 * @generated
	 */
	EList<EObject> getImplicits();

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Properties are values that are settings of properties on members, but do not have any settings on themselves. If the value has set property settings then it should be in members instead.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getMemberContainer_Properties()
	 * @model containment="true"
	 * @generated
	 */
	EList<EObject> getProperties();

} // MemberContainer
