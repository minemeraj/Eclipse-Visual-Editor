package org.eclipse.ve.internal.cde.palette.impl;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PaletteRefImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:17:52 $ 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.PaletteRef;
import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ref</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.PaletteRefImpl#getRefControlGroup <em>Ref Control Group</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.PaletteRefImpl#getRefCategories <em>Ref Categories</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class PaletteRefImpl extends PaletteImpl implements PaletteRef {

	

	/**
	 * The cached value of the '{@link #getRefControlGroup() <em>Ref Control Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefControlGroup()
	 * @generated
	 * @ordered
	 */
	protected Group refControlGroup = null;
	/**
	 * The cached value of the '{@link #getRefCategories() <em>Ref Categories</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefCategories()
	 * @generated
	 * @ordered
	 */
	protected EList refCategories = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected PaletteRefImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getPaletteRef();
	}

	protected Group getPaletteControlGroup() {
		return getRefControlGroup();
	}

	protected List getPaletteCategories() {
		List cats = getRefCategories();
		ArrayList catList = new ArrayList(cats.size());
		// Slight possibility that one of the categories can't be found (i.e. the href was invalid).
		// In that case, a null is returned. We need to strip these out because GEF can't handle this.
		Iterator itr = cats.iterator();
		while (itr.hasNext()) {
			Object n = itr.next();
			if (n != null)
				catList.add(n);
		}
		return catList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Group getRefControlGroup() {
		if (refControlGroup != null && refControlGroup.eIsProxy()) {
			Group oldRefControlGroup = refControlGroup;
			refControlGroup = (Group)eResolveProxy((InternalEObject)refControlGroup);
			if (refControlGroup != oldRefControlGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PalettePackage.PALETTE_REF__REF_CONTROL_GROUP, oldRefControlGroup, refControlGroup));
			}
		}
		return refControlGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Group basicGetRefControlGroup() {
		return refControlGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRefControlGroup(Group newRefControlGroup) {
		Group oldRefControlGroup = refControlGroup;
		refControlGroup = newRefControlGroup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.PALETTE_REF__REF_CONTROL_GROUP, oldRefControlGroup, refControlGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getRefCategories() {
		if (refCategories == null) {
			refCategories = new EObjectResolvingEList(Category.class, this, PalettePackage.PALETTE_REF__REF_CATEGORIES);
		}
		return refCategories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.PALETTE_REF__PALETTE_LABEL:
					return basicSetPaletteLabel(null, msgs);
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
			case PalettePackage.PALETTE_REF__PALETTE_LABEL:
				return getPaletteLabel();
			case PalettePackage.PALETTE_REF__REF_CONTROL_GROUP:
				if (resolve) return getRefControlGroup();
				return basicGetRefControlGroup();
			case PalettePackage.PALETTE_REF__REF_CATEGORIES:
				return getRefCategories();
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
			case PalettePackage.PALETTE_REF__PALETTE_LABEL:
				return paletteLabel != null;
			case PalettePackage.PALETTE_REF__REF_CONTROL_GROUP:
				return refControlGroup != null;
			case PalettePackage.PALETTE_REF__REF_CATEGORIES:
				return refCategories != null && !refCategories.isEmpty();
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
			case PalettePackage.PALETTE_REF__PALETTE_LABEL:
				setPaletteLabel((AbstractString)newValue);
				return;
			case PalettePackage.PALETTE_REF__REF_CONTROL_GROUP:
				setRefControlGroup((Group)newValue);
				return;
			case PalettePackage.PALETTE_REF__REF_CATEGORIES:
				getRefCategories().clear();
				getRefCategories().addAll((Collection)newValue);
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
			case PalettePackage.PALETTE_REF__PALETTE_LABEL:
				setPaletteLabel((AbstractString)null);
				return;
			case PalettePackage.PALETTE_REF__REF_CONTROL_GROUP:
				setRefControlGroup((Group)null);
				return;
			case PalettePackage.PALETTE_REF__REF_CATEGORIES:
				getRefCategories().clear();
				return;
		}
		eDynamicUnset(eFeature);
	}

}
