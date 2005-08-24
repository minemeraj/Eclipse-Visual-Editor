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
package org.eclipse.ve.internal.cdm.impl;
/*
 *  $RCSfile: AnnotationGenericImpl.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cdm.AnnotationGeneric;
import org.eclipse.ve.internal.cdm.CDMPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotation Generic</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.AnnotationGenericImpl#getAnnotatesID <em>Annotates ID</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnnotationGenericImpl extends AnnotationImpl implements AnnotationGeneric {

	/**
	 * The default value of the '{@link #getAnnotatesID() <em>Annotates ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotatesID()
	 * @generated
	 * @ordered
	 */
	protected static final String ANNOTATES_ID_EDEFAULT = null;

	
	/**
	 * The cached value of the '{@link #getAnnotatesID() <em>Annotates ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotatesID()
	 * @generated
	 * @ordered
	 */
	protected String annotatesID = ANNOTATES_ID_EDEFAULT;
	
	/**
	 * This is true if the Annotates ID attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean annotatesIDESet = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected AnnotationGenericImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return CDMPackage.eINSTANCE.getAnnotationGeneric();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAnnotatesID() {
		return annotatesID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnnotatesID(String newAnnotatesID) {
		String oldAnnotatesID = annotatesID;
		annotatesID = newAnnotatesID;
		boolean oldAnnotatesIDESet = annotatesIDESet;
		annotatesIDESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID, oldAnnotatesID, annotatesID, !oldAnnotatesIDESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAnnotatesID() {
		String oldAnnotatesID = annotatesID;
		boolean oldAnnotatesIDESet = annotatesIDESet;
		annotatesID = ANNOTATES_ID_EDEFAULT;
		annotatesIDESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID, oldAnnotatesID, ANNOTATES_ID_EDEFAULT, oldAnnotatesIDESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAnnotatesID() {
		return annotatesIDESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case CDMPackage.ANNOTATION_GENERIC__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
				case CDMPackage.ANNOTATION_GENERIC__VISUAL_INFOS:
					return ((InternalEList)getVisualInfos()).basicRemove(otherEnd, msgs);
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
			case CDMPackage.ANNOTATION_GENERIC__KEYED_VALUES:
				return getKeyedValues();
			case CDMPackage.ANNOTATION_GENERIC__VISUAL_INFOS:
				return getVisualInfos();
			case CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID:
				return getAnnotatesID();
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
			case CDMPackage.ANNOTATION_GENERIC__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case CDMPackage.ANNOTATION_GENERIC__VISUAL_INFOS:
				return visualInfos != null && !visualInfos.isEmpty();
			case CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID:
				return isSetAnnotatesID();
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
			case CDMPackage.ANNOTATION_GENERIC__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case CDMPackage.ANNOTATION_GENERIC__VISUAL_INFOS:
				getVisualInfos().clear();
				getVisualInfos().addAll((Collection)newValue);
				return;
			case CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID:
				setAnnotatesID((String)newValue);
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
			case CDMPackage.ANNOTATION_GENERIC__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case CDMPackage.ANNOTATION_GENERIC__VISUAL_INFOS:
				getVisualInfos().clear();
				return;
			case CDMPackage.ANNOTATION_GENERIC__ANNOTATES_ID:
				unsetAnnotatesID();
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
		result.append(" (annotatesID: ");
		if (annotatesIDESet) result.append(annotatesID); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

}
