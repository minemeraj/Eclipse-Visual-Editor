/**
 * <copyright>
 * </copyright>
 *
 * $Id: JavaCacheDataImpl.java,v 1.3 2006-02-07 17:21:37 rkulp Exp $
 */
package org.eclipse.ve.internal.jcm.impl;
/*******************************************************************************
 * Copyright (c)  2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.JavaCacheData;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Java Cache Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.JavaCacheDataImpl#getNamesToBeans <em>Names To Beans</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JavaCacheDataImpl extends EObjectImpl implements JavaCacheData {
	/**
	 * The cached value of the '{@link #getNamesToBeans() <em>Names To Beans</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamesToBeans()
	 * @generated
	 * @ordered
	 */
	protected EMap namesToBeans = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JavaCacheDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.Literals.JAVA_CACHE_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap getNamesToBeans() {
		if (namesToBeans == null) {
			namesToBeans = new EcoreEMap(JCMPackage.Literals.NAMES_TO_BEANS, NamesToBeansImpl.class, this, JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS);
		}
		return namesToBeans;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				return ((InternalEList)getNamesToBeans()).basicRemove(otherEnd, msgs);
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
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				if (coreType) return getNamesToBeans();
				else return getNamesToBeans().map();
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
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				((EStructuralFeature.Setting)getNamesToBeans()).set(newValue);
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
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				getNamesToBeans().clear();
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
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				return namesToBeans != null && !namesToBeans.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //JavaCacheDataImpl
