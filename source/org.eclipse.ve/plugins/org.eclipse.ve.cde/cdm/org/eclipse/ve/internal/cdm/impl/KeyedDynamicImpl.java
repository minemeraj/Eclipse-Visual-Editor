/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.ve.internal.cdm.impl;
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

import java.text.MessageFormat;

import org.eclipse.ve.internal.cdm.CDMPackage;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Keyed Dynamic</b></em>'.
 * The dynamic subclasses (created within an ecore file and read into a resource, not code-generated)
 * must have a containment feature (either attribute or reference) that is named "typedValue". This 
 * feature should be typed to the kind of value the subclass should have for the value. When getValue
 * or setValue is called, it will be forwarded over to an eGet or eSet on the feature "typedValue".
 * 
 * The dynamic subclass needs an attribute setting of type String named "keyString". This will be used
 * as the key. The attribute may have a default value applied to it so that the key is not needed to
 * be set when used. This is convienent for writing xmi files when it should always be a specific key.  
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.KeyedDynamicImpl#getTypedKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.KeyedDynamicImpl#getTypedValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class KeyedDynamicImpl extends EObjectImpl implements BasicEMap.Entry<String,Object> {
	/**
	 * The default value of the '{@link #getTypedKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getTypedValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object VALUE_EDEFAULT = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected KeyedDynamicImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CDMPackage.Literals.KEYED_DYNAMIC;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public String getTypedKey() {
		return (String) eGet(getKeyStringSF());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void setTypedKey(String newKey) {
		eSet(getKeyStringSF(), newKey);
	}

	private static final String TYPED_VALUE = "typedValue";	// Feature of subclass that contains the typed value.
	private EStructuralFeature getTypedValueSF() {
		EStructuralFeature typedValue = eClass().getEStructuralFeature(TYPED_VALUE);
		if (typedValue == null)
			throw new IllegalArgumentException(MessageFormat.format(CDMImplMessages.KeyedDynamicImpl_ClassDidNotImplementFeature_EXC_, new Object[] {eClass().getName(), TYPED_VALUE })); 
		return typedValue;
	}
	
	private static final String KEY_STRING = "keyString";	// Feature of subclass that contains the key string.
	private EStructuralFeature getKeyStringSF() {
		EStructuralFeature typedValue = eClass().getEStructuralFeature(KEY_STRING);
		if (typedValue == null)
			throw new IllegalArgumentException(MessageFormat.format(CDMImplMessages.KeyedDynamicImpl_ClassDidNotImplementFeature_EXC_, new Object[] {eClass().getName(), KEY_STRING })); 
		return typedValue;
	}	
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public Object getTypedValue() {
		return eGet(getTypedValueSF());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void setTypedValue(Object newValue) {
		eSet(getTypedValueSF(), newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void unsetTypedValue() {
		eUnset(getTypedValueSF());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean isSetTypedValue() {
		return eIsSet(getTypedValueSF());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CDMPackage.KEYED_DYNAMIC__KEY:
				return getTypedKey();
			case CDMPackage.KEYED_DYNAMIC__VALUE:
				return getTypedValue();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CDMPackage.KEYED_DYNAMIC__KEY:
				setTypedKey((String)newValue);
				return;
			case CDMPackage.KEYED_DYNAMIC__VALUE:
				setTypedValue(newValue);
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
			case CDMPackage.KEYED_DYNAMIC__KEY:
				setTypedKey(KEY_EDEFAULT);
				return;
			case CDMPackage.KEYED_DYNAMIC__VALUE:
				unsetTypedValue();
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
			case CDMPackage.KEYED_DYNAMIC__KEY:
				return KEY_EDEFAULT == null ? getTypedKey() != null : !KEY_EDEFAULT.equals(getTypedKey());
			case CDMPackage.KEYED_DYNAMIC__VALUE:
				return isSetTypedValue();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected int hash = -1;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHash() {
		if (hash == -1) {
			Object theKey = getKey();
			hash = (theKey == null ? 0 : theKey.hashCode());
		}
		return hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHash(int hash) {
		this.hash = hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKey() {
		return getTypedKey();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey(String key) {
		setTypedKey(key);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getValue() {
		return getTypedValue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object setValue(Object value) {
		Object oldValue = getValue();
		setTypedValue(value);
		return oldValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EMap<String, Object> getEMap() {
		EObject container = eContainer();
		return container == null ? null : (EMap<String, Object>)container.eGet(eContainmentFeature());
	}

} //KeyedDynamicImpl
