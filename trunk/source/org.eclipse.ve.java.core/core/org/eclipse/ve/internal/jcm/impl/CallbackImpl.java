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
 *  $Revision: 1.8 $  $Date: 2007-09-17 14:21:53 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.jem.internal.instantiation.PTExpression;
import org.eclipse.jem.java.Method;

import org.eclipse.ve.internal.jcm.Callback;
import org.eclipse.ve.internal.jcm.JCMPackage;

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
	protected Method method;

	/**
	 * The cached value of the '{@link #getStatements() <em>Statements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatements()
	 * @generated
	 * @ordered
	 */
	protected EList<PTExpression> statements;

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
	@Override
	protected EClass eStaticClass() {
		return JCMPackage.Literals.CALLBACK;
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
			InternalEObject oldMethod = (InternalEObject)method;
			method = (Method)eResolveProxy(oldMethod);
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
	public EList<PTExpression> getStatements() {
		if (statements == null) {
			statements = new EObjectContainmentEList<PTExpression>(PTExpression.class, this, JCMPackage.CALLBACK__STATEMENTS);
		}
		return statements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.CALLBACK__STATEMENTS:
				return ((InternalEList<?>)getStatements()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JCMPackage.CALLBACK__SHARED_SCOPE:
				return isSharedScope() ? Boolean.TRUE : Boolean.FALSE;
			case JCMPackage.CALLBACK__METHOD:
				if (resolve) return getMethod();
				return basicGetMethod();
			case JCMPackage.CALLBACK__STATEMENTS:
				return getStatements();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
		@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case JCMPackage.CALLBACK__SHARED_SCOPE:
				setSharedScope(((Boolean)newValue).booleanValue());
				return;
			case JCMPackage.CALLBACK__METHOD:
				setMethod((Method)newValue);
				return;
			case JCMPackage.CALLBACK__STATEMENTS:
				getStatements().clear();
				getStatements().addAll((Collection<? extends PTExpression>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
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
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case JCMPackage.CALLBACK__SHARED_SCOPE:
				return ((eFlags & SHARED_SCOPE_EFLAG) != 0) != SHARED_SCOPE_EDEFAULT;
			case JCMPackage.CALLBACK__METHOD:
				return method != null;
			case JCMPackage.CALLBACK__STATEMENTS:
				return statements != null && !statements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (sharedScope: ");
		result.append((eFlags & SHARED_SCOPE_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //CallbackImpl
