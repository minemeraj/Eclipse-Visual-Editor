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
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AbstractEventInvocationImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:16:38 $ 
 */

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
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
	protected EList callbacks = null;

	/**
	 * The cached value of the '{@link #getListener() <em>Listener</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListener()
	 * @generated
	 * @ordered
	 */
	protected Listener listener = null;

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
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getAbstractEventInvocation();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getCallbacks() {
		if (callbacks == null) {
			callbacks = new EObjectContainmentEList(Callback.class, this, JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS);
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
			Listener oldListener = listener;
			listener = (Listener)eResolveProxy((InternalEObject)listener);
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
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
					if (listener != null)
						msgs = ((InternalEObject)listener).eInverseRemove(this, JCMPackage.LISTENER__LISTENED_BY, Listener.class, msgs);
					return basicSetListener((Listener)otherEnd, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
					return ((InternalEList)getCallbacks()).basicRemove(otherEnd, msgs);
				case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
					return basicSetListener(null, msgs);
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
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				return getCallbacks();
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				if (resolve) return getListener();
				return basicGetListener();
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
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				getCallbacks().clear();
				getCallbacks().addAll((Collection)newValue);
				return;
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				setListener((Listener)newValue);
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
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				getCallbacks().clear();
				return;
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				setListener((Listener)null);
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
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS:
				return callbacks != null && !callbacks.isEmpty();
			case JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER:
				return listener != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //AbstractEventInvocationImpl
