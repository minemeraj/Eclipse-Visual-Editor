/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.utility.impl;
/*
 *  $RCSfile: TranslatableStringImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:51 $ 
 */
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.ve.internal.cde.utility.ResourceBundle;
import org.eclipse.ve.internal.cde.utility.TranslatableString;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Translatable String</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.impl.TranslatableStringImpl#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.impl.TranslatableStringImpl#getBundle <em>Bundle</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TranslatableStringImpl extends AbstractStringImpl implements TranslatableString {

	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;
	/**
	 * The cached value of the '{@link #getBundle() <em>Bundle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBundle()
	 * @generated
	 * @ordered
	 */
	protected ResourceBundle bundle = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected TranslatableStringImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return UtilityPackage.eINSTANCE.getTranslatableString();
	}

	public String getStringValue() {
		if (getKey() != null && getBundle() != null) {
			java.util.ResourceBundle bundle = getBundle().getBundle();
			if (bundle != null)
				try {
					return bundle.getString(getKey());
				} catch (java.util.MissingResourceException e) {
				}
		}
		return ""; // Default value //$NON-NLS-1$
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilityPackage.TRANSLATABLE_STRING__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceBundle getBundle() {
		if (bundle != null && bundle.eIsProxy()) {
			ResourceBundle oldBundle = bundle;
			bundle = (ResourceBundle)eResolveProxy((InternalEObject)bundle);
			if (bundle != oldBundle) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UtilityPackage.TRANSLATABLE_STRING__BUNDLE, oldBundle, bundle));
			}
		}
		return bundle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceBundle basicGetBundle() {
		return bundle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBundle(ResourceBundle newBundle) {
		ResourceBundle oldBundle = bundle;
		bundle = newBundle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilityPackage.TRANSLATABLE_STRING__BUNDLE, oldBundle, bundle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UtilityPackage.TRANSLATABLE_STRING__KEY:
				return getKey();
			case UtilityPackage.TRANSLATABLE_STRING__BUNDLE:
				if (resolve) return getBundle();
				return basicGetBundle();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UtilityPackage.TRANSLATABLE_STRING__KEY:
				return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
			case UtilityPackage.TRANSLATABLE_STRING__BUNDLE:
				return bundle != null;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UtilityPackage.TRANSLATABLE_STRING__KEY:
				setKey((String)newValue);
				return;
			case UtilityPackage.TRANSLATABLE_STRING__BUNDLE:
				setBundle((ResourceBundle)newValue);
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
			case UtilityPackage.TRANSLATABLE_STRING__KEY:
				setKey(KEY_EDEFAULT);
				return;
			case UtilityPackage.TRANSLATABLE_STRING__BUNDLE:
				setBundle((ResourceBundle)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (key: ");
		result.append(key);
		result.append(')');
		return result.toString();
	}

}
