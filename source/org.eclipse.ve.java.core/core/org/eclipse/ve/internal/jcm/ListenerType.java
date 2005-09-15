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
 *  $RCSfile: ListenerType.java,v $
 *  $Revision: 1.4 $  $Date: 2005-09-15 21:33:50 $ 
 */

import org.eclipse.jem.java.JavaClass;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Listener Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.ListenerType#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.ListenerType#isThisPart <em>This Part</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.ListenerType#getExtends <em>Extends</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.ListenerType#getImplements <em>Implements</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.ListenerType#getIs <em>Is</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.ListenerType#getListeners <em>Listeners</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListenerType()
 * @model
 * @generated
 */
public interface ListenerType extends EObject{
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of the inner class when it is a non-anonymous class.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListenerType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.ListenerType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>This Part</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>This Part</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether the listener is the this part. This is for VAJ style 1 where the this object itself implements all of the necessary listener interfaces.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>This Part</em>' attribute.
	 * @see #setThisPart(boolean)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListenerType_ThisPart()
	 * @model
	 * @generated
	 */
	boolean isThisPart();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.ListenerType#isThisPart <em>This Part</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>This Part</em>' attribute.
	 * @see #isThisPart()
	 * @generated
	 */
	void setThisPart(boolean value);

	/**
	 * Returns the value of the '<em><b>Extends</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extends</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extends</em>' reference.
	 * @see #setExtends(JavaClass)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListenerType_Extends()
	 * @model
	 * @generated
	 */
	JavaClass getExtends();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.ListenerType#getExtends <em>Extends</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extends</em>' reference.
	 * @see #getExtends()
	 * @generated
	 */
	void setExtends(JavaClass value);

	/**
	 * Returns the value of the '<em><b>Implements</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.jem.java.JavaClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implements</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implements</em>' reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListenerType_Implements()
	 * @model type="org.eclipse.jem.java.JavaClass"
	 * @generated
	 */
	EList getImplements();

	/**
	 * Returns the value of the '<em><b>Is</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is</em>' reference.
	 * @see #setIs(JavaClass)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListenerType_Is()
	 * @model
	 * @generated
	 */
	JavaClass getIs();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.ListenerType#getIs <em>Is</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is</em>' reference.
	 * @see #getIs()
	 * @generated
	 */
	void setIs(JavaClass value);

	/**
	 * Returns the value of the '<em><b>Listeners</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.jcm.Listener}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ve.internal.jcm.Listener#getListenerType <em>Listener Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Listeners</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Listeners</em>' containment reference list.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getListenerType_Listeners()
	 * @see org.eclipse.ve.internal.jcm.Listener#getListenerType
	 * @model type="org.eclipse.ve.internal.jcm.Listener" opposite="listenerType" containment="true"
	 * @generated
	 */
	EList getListeners();

} // ListenerType
