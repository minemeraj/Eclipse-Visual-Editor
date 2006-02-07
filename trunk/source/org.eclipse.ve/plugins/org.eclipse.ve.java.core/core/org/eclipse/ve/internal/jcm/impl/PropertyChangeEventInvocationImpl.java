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
 *  $Revision: 1.5 $  $Date: 2006-02-07 17:21:37 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.jem.java.Method;

import org.eclipse.ve.internal.jcm.JCMPackage;
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
		return JCMPackage.Literals.PROPERTY_CHANGE_EVENT_INVOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Method getAddMethod() {
		if (addMethod != null && addMethod.eIsProxy()) {
			InternalEObject oldAddMethod = (InternalEObject)addMethod;
			addMethod = (Method)eResolveProxy(oldAddMethod);
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				return ((InternalEList)getProperties()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD:
				if (resolve) return getAddMethod();
				return basicGetAddMethod();
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				return getProperties();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD:
				setAddMethod((Method)newValue);
				return;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD:
				setAddMethod((Method)null);
				return;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				getProperties().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD:
				return addMethod != null;
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES:
				return properties != null && !properties.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PropertyChangeEventInvocationImpl
