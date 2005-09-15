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
/*
 *  $RCSfile: CallbackImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-09-15 21:33:49 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.jem.internal.instantiation.PTExpression;

import org.eclipse.ve.internal.jcm.Callback;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.jem.java.Method;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Callback</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.CallbackImpl#isSharedScope <em>Shared Scope</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.CallbackImpl#getMethod <em>Method</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.CallbackImpl#getStatements <em>Statements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CallbackImpl extends EObjectImpl implements Callback {
	/**
	 * The default value of the '{@link #isSharedScope() <em>Shared Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSharedScope()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHARED_SCOPE_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isSharedScope() <em>Shared Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSharedScope()
	 * @generated
	 * @ordered
	 */
	protected static final int SHARED_SCOPE_EFLAG = 1 << 8;

	/**
	 * The cached value of the '{@link #getMethod() <em>Method</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethod()
	 * @generated
	 * @ordered
	 */
	protected Method method = null;

	/**
	 * The cached value of the '{@link #getStatements() <em>Statements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatements()
	 * @generated
	 * @ordered
	 */
	protected EList statements = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CallbackImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getCallback();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSharedScope() {
		return (eFlags & SHARED_SCOPE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSharedScope(boolean newSharedScope) {
		boolean oldSharedScope = (eFlags & SHARED_SCOPE_EFLAG) != 0;
		if (newSharedScope) eFlags |= SHARED_SCOPE_EFLAG; else eFlags &= ~SHARED_SCOPE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.CALLBACK__SHARED_SCOPE, oldSharedScope, newSharedScope));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Method getMethod() {
		if (method != null && method.eIsProxy()) {
			Method oldMethod = method;
			method = (Method)eResolveProxy((InternalEObject)method);
			if (method != oldMethod) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JCMPackage.CALLBACK__METHOD, oldMethod, method));
			}
		}
		return method;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Method basicGetMethod() {
		return method;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMethod(Method newMethod) {
		Method oldMethod = method;
		method = newMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.CALLBACK__METHOD, oldMethod, method));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getStatements() {
		if (statements == null) {
			statements = new EObjectContainmentEList(PTExpression.class, this, JCMPackage.CALLBACK__STATEMENTS);
		}
		return statements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.CALLBACK__STATEMENTS:
					return ((InternalEList)getStatements()).basicRemove(otherEnd, msgs);
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
			case JCMPackage.CALLBACK__SHARED_SCOPE:
				return isSharedScope() ? Boolean.TRUE : Boolean.FALSE;
			case JCMPackage.CALLBACK__METHOD:
				if (resolve) return getMethod();
				return basicGetMethod();
			case JCMPackage.CALLBACK__STATEMENTS:
				return getStatements();
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
			case JCMPackage.CALLBACK__SHARED_SCOPE:
				setSharedScope(((Boolean)newValue).booleanValue());
				return;
			case JCMPackage.CALLBACK__METHOD:
				setMethod((Method)newValue);
				return;
			case JCMPackage.CALLBACK__STATEMENTS:
				getStatements().clear();
				getStatements().addAll((Collection)newValue);
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
			case JCMPackage.CALLBACK__SHARED_SCOPE:
				setSharedScope(SHARED_SCOPE_EDEFAULT);
				return;
			case JCMPackage.CALLBACK__METHOD:
				setMethod((Method)null);
				return;
			case JCMPackage.CALLBACK__STATEMENTS:
				getStatements().clear();
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
			case JCMPackage.CALLBACK__SHARED_SCOPE:
				return ((eFlags & SHARED_SCOPE_EFLAG) != 0) != SHARED_SCOPE_EDEFAULT;
			case JCMPackage.CALLBACK__METHOD:
				return method != null;
			case JCMPackage.CALLBACK__STATEMENTS:
				return statements != null && !statements.isEmpty();
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
		result.append(" (sharedScope: ");
		result.append((eFlags & SHARED_SCOPE_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //CallbackImpl
