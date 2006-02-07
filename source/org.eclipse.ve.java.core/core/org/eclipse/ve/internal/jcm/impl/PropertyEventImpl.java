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
 *  $RCSfile: PropertyEventImpl.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-07 17:21:37 $ 
 */

import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.PropertyEvent;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.PropertyEventImpl#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.PropertyEventImpl#isUseIfExpression <em>Use If Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PropertyEventImpl extends EObjectImpl implements PropertyEvent {
	/**
	 * The default value of the '{@link #getPropertyName() <em>Property Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertyName()
	 * @generated
	 * @ordered
	 */
	protected static final String PROPERTY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPropertyName() <em>Property Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertyName()
	 * @generated
	 * @ordered
	 */
	protected String propertyName = PROPERTY_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isUseIfExpression() <em>Use If Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseIfExpression()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_IF_EXPRESSION_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isUseIfExpression() <em>Use If Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseIfExpression()
	 * @generated
	 * @ordered
	 */
	protected static final int USE_IF_EXPRESSION_EFLAG = 1 << 8;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropertyEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.Literals.PROPERTY_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPropertyName(String newPropertyName) {
		String oldPropertyName = propertyName;
		propertyName = newPropertyName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.PROPERTY_EVENT__PROPERTY_NAME, oldPropertyName, propertyName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUseIfExpression() {
		return (eFlags & USE_IF_EXPRESSION_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUseIfExpression(boolean newUseIfExpression) {
		boolean oldUseIfExpression = (eFlags & USE_IF_EXPRESSION_EFLAG) != 0;
		if (newUseIfExpression) eFlags |= USE_IF_EXPRESSION_EFLAG; else eFlags &= ~USE_IF_EXPRESSION_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.PROPERTY_EVENT__USE_IF_EXPRESSION, oldUseIfExpression, newUseIfExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JCMPackage.PROPERTY_EVENT__PROPERTY_NAME:
				return getPropertyName();
			case JCMPackage.PROPERTY_EVENT__USE_IF_EXPRESSION:
				return isUseIfExpression() ? Boolean.TRUE : Boolean.FALSE;
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
			case JCMPackage.PROPERTY_EVENT__PROPERTY_NAME:
				setPropertyName((String)newValue);
				return;
			case JCMPackage.PROPERTY_EVENT__USE_IF_EXPRESSION:
				setUseIfExpression(((Boolean)newValue).booleanValue());
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
			case JCMPackage.PROPERTY_EVENT__PROPERTY_NAME:
				setPropertyName(PROPERTY_NAME_EDEFAULT);
				return;
			case JCMPackage.PROPERTY_EVENT__USE_IF_EXPRESSION:
				setUseIfExpression(USE_IF_EXPRESSION_EDEFAULT);
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
			case JCMPackage.PROPERTY_EVENT__PROPERTY_NAME:
				return PROPERTY_NAME_EDEFAULT == null ? propertyName != null : !PROPERTY_NAME_EDEFAULT.equals(propertyName);
			case JCMPackage.PROPERTY_EVENT__USE_IF_EXPRESSION:
				return ((eFlags & USE_IF_EXPRESSION_EFLAG) != 0) != USE_IF_EXPRESSION_EDEFAULT;
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
		result.append(" (propertyName: ");
		result.append(propertyName);
		result.append(", useIfExpression: ");
		result.append((eFlags & USE_IF_EXPRESSION_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //PropertyEventImpl
