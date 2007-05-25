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

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bean Subclass Composition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanSubclassComposition#getThisPart <em>This Part</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanSubclassComposition#getMethods <em>Methods</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanSubclassComposition()
 * @model
 * @generated
 */
public interface BeanSubclassComposition extends BeanComposition{
	/**
	 * Returns the value of the '<em><b>This Part</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>This Part</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>This Part</em>' containment reference.
	 * @see #setThisPart(IJavaObjectInstance)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanSubclassComposition_ThisPart()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IJavaObjectInstance getThisPart();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.BeanSubclassComposition#getThisPart <em>This Part</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>This Part</em>' containment reference.
	 * @see #getThisPart()
	 * @generated
	 */
	void setThisPart(IJavaObjectInstance value);

	/**
	 * Returns the value of the '<em><b>Methods</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.jcm.JCMMethod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Methods</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Methods</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanSubclassComposition_Methods()
	 * @model type="org.eclipse.ve.internal.jcm.JCMMethod" containment="true"
	 * @generated
	 */
	EList<JCMMethod> getMethods();

} // BeanSubclassComposition
