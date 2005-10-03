/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.ve.internal.jcm.impl;
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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.jcm.JCMMethod;
import org.eclipse.ve.internal.jcm.JCMPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.JCMMethodImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.JCMMethodImpl#getInitializes <em>Initializes</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.JCMMethodImpl#getReturn <em>Return</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JCMMethodImpl extends MemberContainerImpl implements JCMMethod {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInitializes() <em>Initializes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitializes()
	 * @generated
	 * @ordered
	 */
	protected EList initializes = null;

	/**
	 * The cached value of the '{@link #getReturn() <em>Return</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturn()
	 * @generated
	 * @ordered
	 */
	protected EObject return_ = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JCMMethodImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getJCMMethod();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.JCM_METHOD__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getInitializes() {
		if (initializes == null) {
			initializes = new EObjectResolvingEList(EObject.class, this, JCMPackage.JCM_METHOD__INITIALIZES);
		}
		return initializes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getReturn() {
		if (return_ != null && return_.eIsProxy()) {
			EObject oldReturn = return_;
			return_ = eResolveProxy((InternalEObject)return_);
			if (return_ != oldReturn) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JCMPackage.JCM_METHOD__RETURN, oldReturn, return_));
			}
		}
		return return_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetReturn() {
		return return_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReturn(EObject newReturn) {
		EObject oldReturn = return_;
		return_ = newReturn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.JCM_METHOD__RETURN, oldReturn, return_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.JCM_METHOD__PROPERTIES:
					return ((InternalEList)getProperties()).basicRemove(otherEnd, msgs);
				case JCMPackage.JCM_METHOD__MEMBERS:
					return ((InternalEList)getMembers()).basicRemove(otherEnd, msgs);
				case JCMPackage.JCM_METHOD__IMPLICITS:
					return ((InternalEList)getImplicits()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case JCMPackage.JCM_METHOD__PROPERTIES:
				return getProperties();
			case JCMPackage.JCM_METHOD__MEMBERS:
				return getMembers();
			case JCMPackage.JCM_METHOD__IMPLICITS:
				return getImplicits();
			case JCMPackage.JCM_METHOD__NAME:
				return getName();
			case JCMPackage.JCM_METHOD__INITIALIZES:
				return getInitializes();
			case JCMPackage.JCM_METHOD__RETURN:
				if (resolve) return getReturn();
				return basicGetReturn();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case JCMPackage.JCM_METHOD__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection)newValue);
				return;
			case JCMPackage.JCM_METHOD__MEMBERS:
				getMembers().clear();
				getMembers().addAll((Collection)newValue);
				return;
			case JCMPackage.JCM_METHOD__IMPLICITS:
				getImplicits().clear();
				getImplicits().addAll((Collection)newValue);
				return;
			case JCMPackage.JCM_METHOD__NAME:
				setName((String)newValue);
				return;
			case JCMPackage.JCM_METHOD__INITIALIZES:
				getInitializes().clear();
				getInitializes().addAll((Collection)newValue);
				return;
			case JCMPackage.JCM_METHOD__RETURN:
				setReturn((EObject)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case JCMPackage.JCM_METHOD__PROPERTIES:
				getProperties().clear();
				return;
			case JCMPackage.JCM_METHOD__MEMBERS:
				getMembers().clear();
				return;
			case JCMPackage.JCM_METHOD__IMPLICITS:
				getImplicits().clear();
				return;
			case JCMPackage.JCM_METHOD__NAME:
				setName(NAME_EDEFAULT);
				return;
			case JCMPackage.JCM_METHOD__INITIALIZES:
				getInitializes().clear();
				return;
			case JCMPackage.JCM_METHOD__RETURN:
				setReturn((EObject)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case JCMPackage.JCM_METHOD__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case JCMPackage.JCM_METHOD__MEMBERS:
				return members != null && !members.isEmpty();
			case JCMPackage.JCM_METHOD__IMPLICITS:
				return implicits != null && !implicits.isEmpty();
			case JCMPackage.JCM_METHOD__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case JCMPackage.JCM_METHOD__INITIALIZES:
				return initializes != null && !initializes.isEmpty();
			case JCMPackage.JCM_METHOD__RETURN:
				return return_ != null;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //JCMMethodImpl
