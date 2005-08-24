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
 *  $RCSfile: AnnotationImpl.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.VisualInfo;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.AnnotationImpl#getVisualInfos <em>Visual Infos</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AnnotationImpl extends KeyedValueHolderImpl implements Annotation {

	

	/**
	 * The cached value of the '{@link #getVisualInfos() <em>Visual Infos</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVisualInfos()
	 * @generated
	 * @ordered
	 */
	protected EList visualInfos = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected AnnotationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return CDMPackage.eINSTANCE.getAnnotation();
	}

	public VisualInfo getVisualInfo(Diagram aDiagram) {
		Iterator itr = getVisualInfos().iterator();
		while (itr.hasNext()) {
			VisualInfo vi = (VisualInfo) itr.next();
			if (vi.getDiagram() == aDiagram)
				return vi;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getVisualInfos() {
		if (visualInfos == null) {
			visualInfos = new EObjectContainmentEList(VisualInfo.class, this, CDMPackage.ANNOTATION__VISUAL_INFOS);
		}
		return visualInfos;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case CDMPackage.ANNOTATION__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
				case CDMPackage.ANNOTATION__VISUAL_INFOS:
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
			case CDMPackage.ANNOTATION__KEYED_VALUES:
				return getKeyedValues();
			case CDMPackage.ANNOTATION__VISUAL_INFOS:
				return getVisualInfos();
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
			case CDMPackage.ANNOTATION__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case CDMPackage.ANNOTATION__VISUAL_INFOS:
				getVisualInfos().clear();
				getVisualInfos().addAll((Collection)newValue);
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
			case CDMPackage.ANNOTATION__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case CDMPackage.ANNOTATION__VISUAL_INFOS:
				getVisualInfos().clear();
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
			case CDMPackage.ANNOTATION__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case CDMPackage.ANNOTATION__VISUAL_INFOS:
				return visualInfos != null && !visualInfos.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

}
