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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMMethod;
import org.eclipse.ve.internal.jcm.JCMPackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bean Subclass Composition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanSubclassCompositionImpl#getThisPart <em>This Part</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanSubclassCompositionImpl#getMethods <em>Methods</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BeanSubclassCompositionImpl extends BeanCompositionImpl implements BeanSubclassComposition {
	/**
	 * The cached value of the '{@link #getThisPart() <em>This Part</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getThisPart()
	 * @generated
	 * @ordered
	 */
	protected IJavaObjectInstance thisPart = null;

	/**
	 * The cached value of the '{@link #getMethods() <em>Methods</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethods()
	 * @generated
	 * @ordered
	 */
	protected EList methods = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BeanSubclassCompositionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getBeanSubclassComposition();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IJavaObjectInstance getThisPart() {
		return thisPart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetThisPart(IJavaObjectInstance newThisPart, NotificationChain msgs) {
		IJavaObjectInstance oldThisPart = thisPart;
		thisPart = newThisPart;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART, oldThisPart, newThisPart);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setThisPart(IJavaObjectInstance newThisPart) {
		if (newThisPart != thisPart) {
			NotificationChain msgs = null;
			if (thisPart != null)
				msgs = ((InternalEObject)thisPart).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART, null, msgs);
			if (newThisPart != null)
				msgs = ((InternalEObject)newThisPart).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART, null, msgs);
			msgs = basicSetThisPart(newThisPart, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART, newThisPart, newThisPart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getMethods() {
		if (methods == null) {
			methods = new EObjectContainmentEList(JCMMethod.class, this, JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS);
		}
		return methods;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__DIAGRAMS:
					return ((InternalEList)getDiagrams()).basicAdd(otherEnd, msgs);
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
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__DIAGRAMS:
					return ((InternalEList)getDiagrams()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__ANNOTATIONS:
					return ((InternalEList)getAnnotations()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__PROPERTIES:
					return ((InternalEList)getProperties()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__MEMBERS:
					return ((InternalEList)getMembers()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__IMPLICITS:
					return ((InternalEList)getImplicits()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__LISTENER_TYPES:
					return ((InternalEList)getListenerTypes()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
					return basicSetThisPart(null, msgs);
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
					return ((InternalEList)getMethods()).basicRemove(otherEnd, msgs);
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
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__DIAGRAMS:
				return getDiagrams();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__ANNOTATIONS:
				return getAnnotations();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__PROPERTIES:
				return getProperties();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__MEMBERS:
				return getMembers();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__IMPLICITS:
				return getImplicits();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS:
				return getComponents();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__LISTENER_TYPES:
				return getListenerTypes();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				return getThisPart();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				return getMethods();
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
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__DIAGRAMS:
				getDiagrams().clear();
				getDiagrams().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__ANNOTATIONS:
				getAnnotations().clear();
				getAnnotations().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__MEMBERS:
				getMembers().clear();
				getMembers().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__IMPLICITS:
				getImplicits().clear();
				getImplicits().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS:
				getComponents().clear();
				getComponents().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__LISTENER_TYPES:
				getListenerTypes().clear();
				getListenerTypes().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				setThisPart((IJavaObjectInstance)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				getMethods().clear();
				getMethods().addAll((Collection)newValue);
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
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__DIAGRAMS:
				getDiagrams().clear();
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__ANNOTATIONS:
				getAnnotations().clear();
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__PROPERTIES:
				getProperties().clear();
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__MEMBERS:
				getMembers().clear();
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__IMPLICITS:
				getImplicits().clear();
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS:
				getComponents().clear();
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__LISTENER_TYPES:
				getListenerTypes().clear();
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				setThisPart((IJavaObjectInstance)null);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				getMethods().clear();
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
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__DIAGRAMS:
				return diagrams != null && !diagrams.isEmpty();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__ANNOTATIONS:
				return annotations != null && !annotations.isEmpty();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__MEMBERS:
				return members != null && !members.isEmpty();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__IMPLICITS:
				return implicits != null && !implicits.isEmpty();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS:
				return components != null && !components.isEmpty();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__LISTENER_TYPES:
				return listenerTypes != null && !listenerTypes.isEmpty();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				return thisPart != null;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				return methods != null && !methods.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //BeanSubclassCompositionImpl
