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
 *  $RCSfile: EventInvocationImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:16:38 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.jem.internal.beaninfo.BeanEvent;
import org.eclipse.ve.internal.jcm.EventInvocation;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.Listener;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.EventInvocationImpl#getEvent <em>Event</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EventInvocationImpl extends AbstractEventInvocationImpl implements EventInvocation {
	/**
	 * The cached value of the '{@link #getEvent() <em>Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvent()
	 * @generated
	 * @ordered
	 */
	protected BeanEvent event = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EventInvocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getEventInvocation();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BeanEvent getEvent() {
		if (event != null && event.eIsProxy()) {
			BeanEvent oldEvent = event;
			event = (BeanEvent)eResolveProxy((InternalEObject)event);
			if (event != oldEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JCMPackage.EVENT_INVOCATION__EVENT, oldEvent, event));
			}
		}
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BeanEvent basicGetEvent() {
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEvent(BeanEvent newEvent) {
		BeanEvent oldEvent = event;
		event = newEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.EVENT_INVOCATION__EVENT, oldEvent, event));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.EVENT_INVOCATION__LISTENER:
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
				case JCMPackage.EVENT_INVOCATION__CALLBACKS:
					return ((InternalEList)getCallbacks()).basicRemove(otherEnd, msgs);
				case JCMPackage.EVENT_INVOCATION__LISTENER:
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
			case JCMPackage.EVENT_INVOCATION__CALLBACKS:
				return getCallbacks();
			case JCMPackage.EVENT_INVOCATION__LISTENER:
				if (resolve) return getListener();
				return basicGetListener();
			case JCMPackage.EVENT_INVOCATION__EVENT:
				if (resolve) return getEvent();
				return basicGetEvent();
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
			case JCMPackage.EVENT_INVOCATION__CALLBACKS:
				getCallbacks().clear();
				getCallbacks().addAll((Collection)newValue);
				return;
			case JCMPackage.EVENT_INVOCATION__LISTENER:
				setListener((Listener)newValue);
				return;
			case JCMPackage.EVENT_INVOCATION__EVENT:
				setEvent((BeanEvent)newValue);
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
			case JCMPackage.EVENT_INVOCATION__CALLBACKS:
				getCallbacks().clear();
				return;
			case JCMPackage.EVENT_INVOCATION__LISTENER:
				setListener((Listener)null);
				return;
			case JCMPackage.EVENT_INVOCATION__EVENT:
				setEvent((BeanEvent)null);
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
			case JCMPackage.EVENT_INVOCATION__CALLBACKS:
				return callbacks != null && !callbacks.isEmpty();
			case JCMPackage.EVENT_INVOCATION__LISTENER:
				return listener != null;
			case JCMPackage.EVENT_INVOCATION__EVENT:
				return event != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //EventInvocationImpl
