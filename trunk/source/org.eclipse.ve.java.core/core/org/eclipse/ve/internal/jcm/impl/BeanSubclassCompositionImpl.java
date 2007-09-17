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
	protected IJavaObjectInstance thisPart;

	/**
	 * The cached value of the '{@link #getMethods() <em>Methods</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethods()
	 * @generated
	 * @ordered
	 */
	protected EList<JCMMethod> methods;

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
	@Override
	protected EClass eStaticClass() {
		return JCMPackage.Literals.BEAN_SUBCLASS_COMPOSITION;
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
	public EList<JCMMethod> getMethods() {
		if (methods == null) {
			methods = new EObjectContainmentEList<JCMMethod>(JCMMethod.class, this, JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS);
		}
		return methods;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				return basicSetThisPart(null, msgs);
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				return ((InternalEList<?>)getMethods()).basicRemove(otherEnd, msgs);
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
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				return getThisPart();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				return getMethods();
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
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				setThisPart((IJavaObjectInstance)newValue);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				getMethods().clear();
				getMethods().addAll((Collection<? extends JCMMethod>)newValue);
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
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				setThisPart((IJavaObjectInstance)null);
				return;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				getMethods().clear();
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
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				return thisPart != null;
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS:
				return methods != null && !methods.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //BeanSubclassCompositionImpl
