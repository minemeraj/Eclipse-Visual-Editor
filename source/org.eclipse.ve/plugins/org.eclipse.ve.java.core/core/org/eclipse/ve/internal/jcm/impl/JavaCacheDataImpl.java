/**
 * <copyright>
 * </copyright>
 *
 * $Id: JavaCacheDataImpl.java,v 1.1 2005-01-05 18:41:43 gmendel Exp $
 */
package org.eclipse.ve.internal.jcm.impl;

import java.util.Collection;

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
		return JCMPackage.eINSTANCE.getJavaCacheData();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap getNamesToBeans() {
		if (namesToBeans == null) {
			namesToBeans = new EcoreEMap(JCMPackage.eINSTANCE.getNamesToBeans(), NamesToBeansImpl.class, this, JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS);
		}
		return namesToBeans;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
					return ((InternalEList)getNamesToBeans()).basicRemove(otherEnd, msgs);
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
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				return getNamesToBeans();
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
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				getNamesToBeans().clear();
				getNamesToBeans().addAll((Collection)newValue);
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
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				getNamesToBeans().clear();
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
			case JCMPackage.JAVA_CACHE_DATA__NAMES_TO_BEANS:
				return namesToBeans != null && !namesToBeans.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //JavaCacheDataImpl
