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
 *  $RCSfile: PropertyChangeEventInvocationImpl.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-03 19:20:57 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.jem.java.Method;

import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.Listener;
import org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation;
import org.eclipse.ve.internal.jcm.PropertyEvent;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Change Event Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.PropertyChangeEventInvocationImpl#getAddMethod <em>Add Method</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.PropertyChangeEventInvocationImpl#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PropertyChangeEventInvocationImpl extends AbstractEventInvocationImpl implements PropertyChangeEventInvocation {
	/**
	 * The cached value of the '{@link #getAddMethod() <em>Add Method</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddMethod()
	 * @generated
	 * @ordered
	 */
	protected Method addMethod = null;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList properties = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropertyChangeEventInvocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getPropertyChangeEventInvocation();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Method getAddMethod() {
		if (addMethod != null && addMethod.eIsProxy()) {
			Method oldAddMethod = addMethod;
			addMethod = (Method)eResolveProxy((InternalEObject)addMethod);
			if (addMethod != oldAddMethod) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD, oldAddMethod, addMethod));
			}
		}
		return addMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Method basicGetAddMethod() {
		return addMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAddMethod(Method newAddMethod) {
		Method oldAddMethod = addMethod;
		addMethod = newAddMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD, oldAddMethod, addMethod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList(PropertyEvent.class, this, JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__LISTENER:
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
				case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__CALLBACKS:
					return ((InternalEList)getCallbacks()).basicRemove(otherEnd, msgs);
				case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__LISTENER:
					return basicSetListener(null, msgs);
				case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
					return ((InternalEList)getProperties()).basicRemove(otherEnd, msgs);
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
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__CALLBACKS:
				return getCallbacks();
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__LISTENER:
				if (resolve) return getListener();
				return basicGetListener();
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD:
				if (resolve) return getAddMethod();
				return basicGetAddMethod();
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				return getProperties();
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
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__CALLBACKS:
				getCallbacks().clear();
				getCallbacks().addAll((Collection)newValue);
				return;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__LISTENER:
				setListener((Listener)newValue);
				return;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD:
				setAddMethod((Method)newValue);
				return;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection)newValue);
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
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__CALLBACKS:
				getCallbacks().clear();
				return;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__LISTENER:
				setListener((Listener)null);
				return;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD:
				setAddMethod((Method)null);
				return;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				getProperties().clear();
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
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__CALLBACKS:
				return callbacks != null && !callbacks.isEmpty();
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__LISTENER:
				return listener != null;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD:
				return addMethod != null;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				return properties != null && !properties.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //PropertyChangeEventInvocationImpl
