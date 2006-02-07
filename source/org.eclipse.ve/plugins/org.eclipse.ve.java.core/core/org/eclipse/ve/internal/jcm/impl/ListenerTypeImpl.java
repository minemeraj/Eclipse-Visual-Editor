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
 *  $RCSfile: ListenerTypeImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2006-02-07 17:21:37 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.Listener;
import org.eclipse.ve.internal.jcm.ListenerType;
import org.eclipse.jem.java.JavaClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Listener Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.ListenerTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.ListenerTypeImpl#isThisPart <em>This Part</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.ListenerTypeImpl#getExtends <em>Extends</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.ListenerTypeImpl#getImplements <em>Implements</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.ListenerTypeImpl#getIs <em>Is</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.ListenerTypeImpl#getListeners <em>Listeners</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ListenerTypeImpl extends EObjectImpl implements ListenerType {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isThisPart() <em>This Part</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isThisPart()
	 * @generated
	 * @ordered
	 */
	protected static final boolean THIS_PART_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isThisPart() <em>This Part</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isThisPart()
	 * @generated
	 * @ordered
	 */
	protected static final int THIS_PART_EFLAG = 1 << 8;

	/**
	 * The cached value of the '{@link #getExtends() <em>Extends</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtends()
	 * @generated
	 * @ordered
	 */
	protected JavaClass extends_ = null;

	/**
	 * The cached value of the '{@link #getImplements() <em>Implements</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImplements()
	 * @generated
	 * @ordered
	 */
	protected EList implements_ = null;

	/**
	 * The cached value of the '{@link #getIs() <em>Is</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIs()
	 * @generated
	 * @ordered
	 */
	protected JavaClass is = null;

	/**
	 * The cached value of the '{@link #getListeners() <em>Listeners</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListeners()
	 * @generated
	 * @ordered
	 */
	protected EList listeners = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ListenerTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.Literals.LISTENER_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.LISTENER_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isThisPart() {
		return (eFlags & THIS_PART_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setThisPart(boolean newThisPart) {
		boolean oldThisPart = (eFlags & THIS_PART_EFLAG) != 0;
		if (newThisPart) eFlags |= THIS_PART_EFLAG; else eFlags &= ~THIS_PART_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.LISTENER_TYPE__THIS_PART, oldThisPart, newThisPart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaClass getExtends() {
		if (extends_ != null && extends_.eIsProxy()) {
			InternalEObject oldExtends = (InternalEObject)extends_;
			extends_ = (JavaClass)eResolveProxy(oldExtends);
			if (extends_ != oldExtends) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JCMPackage.LISTENER_TYPE__EXTENDS, oldExtends, extends_));
			}
		}
		return extends_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaClass basicGetExtends() {
		return extends_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtends(JavaClass newExtends) {
		JavaClass oldExtends = extends_;
		extends_ = newExtends;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.LISTENER_TYPE__EXTENDS, oldExtends, extends_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getImplements() {
		if (implements_ == null) {
			implements_ = new EObjectResolvingEList(JavaClass.class, this, JCMPackage.LISTENER_TYPE__IMPLEMENTS);
		}
		return implements_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaClass getIs() {
		if (is != null && is.eIsProxy()) {
			InternalEObject oldIs = (InternalEObject)is;
			is = (JavaClass)eResolveProxy(oldIs);
			if (is != oldIs) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JCMPackage.LISTENER_TYPE__IS, oldIs, is));
			}
		}
		return is;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaClass basicGetIs() {
		return is;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIs(JavaClass newIs) {
		JavaClass oldIs = is;
		is = newIs;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.LISTENER_TYPE__IS, oldIs, is));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getListeners() {
		if (listeners == null) {
			listeners = new EObjectContainmentWithInverseEList(Listener.class, this, JCMPackage.LISTENER_TYPE__LISTENERS, JCMPackage.LISTENER__LISTENER_TYPE);
		}
		return listeners;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.LISTENER_TYPE__LISTENERS:
				return ((InternalEList)getListeners()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.LISTENER_TYPE__LISTENERS:
				return ((InternalEList)getListeners()).basicRemove(otherEnd, msgs);
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
			case JCMPackage.LISTENER_TYPE__NAME:
				return getName();
			case JCMPackage.LISTENER_TYPE__THIS_PART:
				return isThisPart() ? Boolean.TRUE : Boolean.FALSE;
			case JCMPackage.LISTENER_TYPE__EXTENDS:
				if (resolve) return getExtends();
				return basicGetExtends();
			case JCMPackage.LISTENER_TYPE__IMPLEMENTS:
				return getImplements();
			case JCMPackage.LISTENER_TYPE__IS:
				if (resolve) return getIs();
				return basicGetIs();
			case JCMPackage.LISTENER_TYPE__LISTENERS:
				return getListeners();
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
			case JCMPackage.LISTENER_TYPE__NAME:
				setName((String)newValue);
				return;
			case JCMPackage.LISTENER_TYPE__THIS_PART:
				setThisPart(((Boolean)newValue).booleanValue());
				return;
			case JCMPackage.LISTENER_TYPE__EXTENDS:
				setExtends((JavaClass)newValue);
				return;
			case JCMPackage.LISTENER_TYPE__IMPLEMENTS:
				getImplements().clear();
				getImplements().addAll((Collection)newValue);
				return;
			case JCMPackage.LISTENER_TYPE__IS:
				setIs((JavaClass)newValue);
				return;
			case JCMPackage.LISTENER_TYPE__LISTENERS:
				getListeners().clear();
				getListeners().addAll((Collection)newValue);
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
			case JCMPackage.LISTENER_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case JCMPackage.LISTENER_TYPE__THIS_PART:
				setThisPart(THIS_PART_EDEFAULT);
				return;
			case JCMPackage.LISTENER_TYPE__EXTENDS:
				setExtends((JavaClass)null);
				return;
			case JCMPackage.LISTENER_TYPE__IMPLEMENTS:
				getImplements().clear();
				return;
			case JCMPackage.LISTENER_TYPE__IS:
				setIs((JavaClass)null);
				return;
			case JCMPackage.LISTENER_TYPE__LISTENERS:
				getListeners().clear();
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
			case JCMPackage.LISTENER_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case JCMPackage.LISTENER_TYPE__THIS_PART:
				return ((eFlags & THIS_PART_EFLAG) != 0) != THIS_PART_EDEFAULT;
			case JCMPackage.LISTENER_TYPE__EXTENDS:
				return extends_ != null;
			case JCMPackage.LISTENER_TYPE__IMPLEMENTS:
				return implements_ != null && !implements_.isEmpty();
			case JCMPackage.LISTENER_TYPE__IS:
				return is != null;
			case JCMPackage.LISTENER_TYPE__LISTENERS:
				return listeners != null && !listeners.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", thisPart: ");
		result.append((eFlags & THIS_PART_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //ListenerTypeImpl
