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
 *  $RCSfile: ListenerImpl.java,v $
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
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.jcm.AbstractEventInvocation;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.Listener;
import org.eclipse.ve.internal.jcm.ListenerType;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Listener</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.ListenerImpl#getListenedBy <em>Listened By</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.ListenerImpl#getListenerType <em>Listener Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ListenerImpl extends EObjectImpl implements Listener {
	/**
	 * The cached value of the '{@link #getListenedBy() <em>Listened By</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListenedBy()
	 * @generated
	 * @ordered
	 */
	protected EList listenedBy = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ListenerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getListener();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getListenedBy() {
		if (listenedBy == null) {
			listenedBy = new EObjectWithInverseResolvingEList(AbstractEventInvocation.class, this, JCMPackage.LISTENER__LISTENED_BY, JCMPackage.ABSTRACT_EVENT_INVOCATION__LISTENER);
		}
		return listenedBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ListenerType getListenerType() {
		if (eContainerFeatureID != JCMPackage.LISTENER__LISTENER_TYPE) return null;
		return (ListenerType)eContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setListenerType(ListenerType newListenerType) {
		if (newListenerType != eContainer || (eContainerFeatureID != JCMPackage.LISTENER__LISTENER_TYPE && newListenerType != null)) {
			if (EcoreUtil.isAncestor(this, newListenerType))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eContainer != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newListenerType != null)
				msgs = ((InternalEObject)newListenerType).eInverseAdd(this, JCMPackage.LISTENER_TYPE__LISTENERS, ListenerType.class, msgs);
			msgs = eBasicSetContainer((InternalEObject)newListenerType, JCMPackage.LISTENER__LISTENER_TYPE, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.LISTENER__LISTENER_TYPE, newListenerType, newListenerType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.LISTENER__LISTENED_BY:
					return ((InternalEList)getListenedBy()).basicAdd(otherEnd, msgs);
				case JCMPackage.LISTENER__LISTENER_TYPE:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, JCMPackage.LISTENER__LISTENER_TYPE, msgs);
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
				case JCMPackage.LISTENER__LISTENED_BY:
					return ((InternalEList)getListenedBy()).basicRemove(otherEnd, msgs);
				case JCMPackage.LISTENER__LISTENER_TYPE:
					return eBasicSetContainer(null, JCMPackage.LISTENER__LISTENER_TYPE, msgs);
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
	public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs) {
		if (eContainerFeatureID >= 0) {
			switch (eContainerFeatureID) {
				case JCMPackage.LISTENER__LISTENER_TYPE:
					return eContainer.eInverseRemove(this, JCMPackage.LISTENER_TYPE__LISTENERS, ListenerType.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return eContainer.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case JCMPackage.LISTENER__LISTENED_BY:
				return getListenedBy();
			case JCMPackage.LISTENER__LISTENER_TYPE:
				return getListenerType();
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
			case JCMPackage.LISTENER__LISTENED_BY:
				getListenedBy().clear();
				getListenedBy().addAll((Collection)newValue);
				return;
			case JCMPackage.LISTENER__LISTENER_TYPE:
				setListenerType((ListenerType)newValue);
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
			case JCMPackage.LISTENER__LISTENED_BY:
				getListenedBy().clear();
				return;
			case JCMPackage.LISTENER__LISTENER_TYPE:
				setListenerType((ListenerType)null);
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
			case JCMPackage.LISTENER__LISTENED_BY:
				return listenedBy != null && !listenedBy.isEmpty();
			case JCMPackage.LISTENER__LISTENER_TYPE:
				return getListenerType() != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //ListenerImpl
