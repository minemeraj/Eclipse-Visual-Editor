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
 *  $RCSfile: AbstractEventInvocationImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2007-09-17 14:21:53 $ 
 */

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.ve.internal.jcm.AbstractEventInvocation;
import org.eclipse.ve.internal.jcm.Callback;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.Listener;
import java.util.Collection;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Event Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.AbstractEventInvocationImpl#getCallbacks <em>Callbacks</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.AbstractEventInvocationImpl#getListener <em>Listener</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractEventInvocationImpl extends EObjectImpl implements AbstractEventInvocation {
	/**
	 * The cached value of the '{@link #getCallbacks() <em>Callbacks</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallbacks()
	 * @generated
	 * @ordered
	 */
	protected EList<Callback> callbacks;

	/**
	 * The cached value of the '{@link #getListener() <em>Listener</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListener()
	 * @generated
	 * @ordered
	 */
	protected Listener listener;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractEventInvocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JCMPackage.Literals.ABSTRACT_EVENT_INVOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Callback> getCallbacks() {
		if (callbacks == null) {
			callbacks = new EObjectContainmentEList<Callback>(Callback.class, this, JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS);
		}
		return callbacks;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Listener getListener() {
		if (listener != null && listener.eIsProxy()) {
			InternalEObject oldListener = (InternalEObject)listener;
			listener = (Listener)eResolveProxy(oldListener);
			if (listener != oldListener) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER, oldListener, listener));
			}
		}
		return listener;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Listener basicGetListener() {
		return listener;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetListener(Listener newListener, NotificationChain msgs) {
		Listener oldListener = listener;
		listener = newListener;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER, oldListener, newListener);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setListener(Listener newListener) {
		if (newListener != listener) {
			NotificationChain msgs = null;
			if (listener != null)
				msgs = ((InternalEObject)listener).eInverseRemove(this, JCMPackage.LISTENER__LISTENED_BY, Listener.class, msgs);
			if (newListener != null)
				msgs = ((InternalEObject)newListener).eInverseAdd(this, JCMPackage.LISTENER__LISTENED_BY, Listener.class, msgs);
			msgs = basicSetListener(newListener, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER, newListener, newListener));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				if (listener != null)
					msgs = ((InternalEObject)listener).eInverseRemove(this, JCMPackage.LISTENER__LISTENED_BY, Listener.class, msgs);
				return basicSetListener((Listener)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				return ((InternalEList<?>)getCallbacks()).basicRemove(otherEnd, msgs);
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				return basicSetListener(null, msgs);
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
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				return getCallbacks();
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				if (resolve) return getListener();
				return basicGetListener();
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
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				getCallbacks().clear();
				getCallbacks().addAll((Collection<? extends Callback>)newValue);
				return;
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				setListener((Listener)newValue);
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
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				getCallbacks().clear();
				return;
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				setListener((Listener)null);
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
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				return callbacks != null && !callbacks.isEmpty();
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				return listener != null;
		}
		return super.eIsSet(featureID);
	}

} //AbstractEventInvocationImpl
