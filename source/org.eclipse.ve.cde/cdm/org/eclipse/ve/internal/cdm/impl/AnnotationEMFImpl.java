/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: AnnotationEMFImpl.java,v $
 *  $Revision: 1.8 $  $Date: 2005-05-11 22:41:15 $ 
 */
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.notify.impl.NotificationChainImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cdm.AnnotationEMF;
import org.eclipse.ve.internal.cdm.CDMPackage;

import org.eclipse.ve.internal.cdm.*;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotation EMF</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.impl.AnnotationEMFImpl#getAnnotates <em>Annotates</em>}</li>
 * </ul>
 * </p>
 *
 */
public final class AnnotationEMFImpl extends AnnotationImpl implements AnnotationEMF {

	
	
	public static final class ParentAdapterImpl extends AdapterImpl implements ParentAdapter {
		protected Annotation parentAnnotation;
		
		/**
		 * @see org.eclipse.ve.internal.cdm.AnnotationEMF.ParentAdapter#getParentAnnotation()
		 */
		public Annotation getParentAnnotation() {
			return parentAnnotation;
		}
		
		protected void setParentAnnotation(Annotation parentAnnotation) {
			this.parentAnnotation = parentAnnotation;
		}

		/**
		 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(Object)
		 */
		public boolean isAdapterForType(Object type) {
			return type == PARENT_ANNOTATION_ADAPTER_KEY;
		}
	}
	
	/**
	 * The cached value of the '{@link #getAnnotates() <em>Annotates</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotates()
	 * @generated
	 * @ordered
	 */
	protected EObject annotates = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnnotationEMFImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return CDMPackage.eINSTANCE.getAnnotationEMF();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getAnnotates() {
		if (annotates != null && annotates.eIsProxy()) {
			EObject oldAnnotates = annotates;
			annotates = eResolveProxy((InternalEObject)annotates);
			if (annotates != oldAnnotates) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CDMPackage.ANNOTATION_EMF__ANNOTATES, oldAnnotates, annotates));
			}
		}
		return annotates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetAnnotates() {
		return annotates;
	}

	public void setAnnotates(EObject newAnnotates) {
		NotificationChain msgs = null;
		if (newAnnotates != annotates) {
			// Since this isn't a real bi-dir reference, need to simulate it.
			if (annotates != null) msgs = inverseRemoveAnnotates(msgs);
			if (newAnnotates != null) msgs = inverseAddAnnotates(newAnnotates, msgs);
			msgs = basicSetAnnotates(newAnnotates, msgs);
			if (msgs != null) {
				// We need to change the parent annotation notifications to be our custom one.
				List mList = (List) msgs;	// We know it is a NotificationChainImpl, which is a List implementation.
				int mSize = mList.size();
				for (int i = 0; i < mSize; i++) {
					ENotificationImpl m = (ENotificationImpl) mList.get(i);
					if (m.getFeature() == CDMPackage.eINSTANCE.getEObject_ParentAnnotation())
						mList.set(i, new ENotificationImpl((InternalEObject) m.getNotifier(), ParentAdapter.PARENT_ANNOTATION_NOTIFICATION_TYPE, (EStructuralFeature) m.getFeature(), m.getOldValue(), m.getNewValue(), m.getEventType()) {
							/**
							 * @see org.eclipse.emf.common.notify.impl.NotificationImpl#isTouch()
							 */
							public boolean isTouch() {
								return getPosition() == Notification.SET && getOldValue() == getNewValue();
							}
						});	
				}
				msgs.dispatch();
			}
		} else 
			setAnnotatesGen(newAnnotates);
	}
	
	
	private NotificationChain inverseRemoveAnnotates(NotificationChain msgs) {
		// Remove annotation from parent annotation setting on old annotates object.
		ParentAdapterImpl a = (ParentAdapterImpl) EcoreUtil.getExistingAdapter(annotates, ParentAdapter.PARENT_ANNOTATION_ADAPTER_KEY);
		if (a == null)
			return null;	// This shouldn't occur
		Annotation oldAnnotation = a.getParentAnnotation();
		a.setParentAnnotation(null);
		if (((InternalEObject) annotates).eNotificationRequired()) {
			if (msgs == null) msgs = new NotificationChainImpl(4);
			msgs.add(new ENotificationImpl((InternalEObject) annotates, Notification.SET, CDMPackage.eINSTANCE.getEObject_ParentAnnotation(), oldAnnotation, null));
		}
		return msgs;
	}
	
	private NotificationChain inverseAddAnnotates(EObject newAnnotates, NotificationChain msgs) {
		ParentAdapterImpl a = (ParentAdapterImpl) EcoreUtil.getExistingAdapter(newAnnotates, ParentAdapter.PARENT_ANNOTATION_ADAPTER_KEY);
		if (a == null) {
			// Need to create the adapter
			a = new ParentAdapterImpl();
			newAnnotates.eAdapters().add(a);
		}
		
		Annotation oldAnnotation = a.getParentAnnotation();			
		if (oldAnnotation != null)
			msgs = ((AnnotationEMFImpl)oldAnnotation).basicSetAnnotates(null, msgs);
		
		a.setParentAnnotation(this);
		if (((InternalEObject) newAnnotates).eNotificationRequired()) {
			if (msgs == null) msgs = new NotificationChainImpl(4);
			msgs.add(new ENotificationImpl((InternalEObject) newAnnotates, Notification.SET, CDMPackage.eINSTANCE.getEObject_ParentAnnotation(), oldAnnotation, this));
		}		
		return msgs;	
	}
	
	NotificationChain basicSetAnnotates(EObject newAnnotates, NotificationChain msgs) {
		EObject oldAnnotates = annotates;
		annotates = newAnnotates;
		if (eNotificationRequired()) {
			if (msgs == null) msgs = new NotificationChainImpl(4);
			msgs.add(new ENotificationImpl(this, Notification.SET, CDMPackage.ANNOTATION_EMF__ANNOTATES, oldAnnotates, annotates));
		}
		return msgs;
	}	
					
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnnotatesGen(EObject newAnnotates) {
		EObject oldAnnotates = annotates;
		annotates = newAnnotates;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CDMPackage.ANNOTATION_EMF__ANNOTATES, oldAnnotates, annotates));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case CDMPackage.ANNOTATION_EMF__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
				case CDMPackage.ANNOTATION_EMF__VISUAL_INFOS:
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
			case CDMPackage.ANNOTATION_EMF__KEYED_VALUES:
				return getKeyedValues();
			case CDMPackage.ANNOTATION_EMF__VISUAL_INFOS:
				return getVisualInfos();
			case CDMPackage.ANNOTATION_EMF__ANNOTATES:
				if (resolve) return getAnnotates();
				return basicGetAnnotates();
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
			case CDMPackage.ANNOTATION_EMF__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case CDMPackage.ANNOTATION_EMF__VISUAL_INFOS:
				return visualInfos != null && !visualInfos.isEmpty();
			case CDMPackage.ANNOTATION_EMF__ANNOTATES:
				return annotates != null;
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
			case CDMPackage.ANNOTATION_EMF__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case CDMPackage.ANNOTATION_EMF__VISUAL_INFOS:
				getVisualInfos().clear();
				getVisualInfos().addAll((Collection)newValue);
				return;
			case CDMPackage.ANNOTATION_EMF__ANNOTATES:
				setAnnotates((EObject)newValue);
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
			case CDMPackage.ANNOTATION_EMF__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case CDMPackage.ANNOTATION_EMF__VISUAL_INFOS:
				getVisualInfos().clear();
				return;
			case CDMPackage.ANNOTATION_EMF__ANNOTATES:
				setAnnotates((EObject)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

}
