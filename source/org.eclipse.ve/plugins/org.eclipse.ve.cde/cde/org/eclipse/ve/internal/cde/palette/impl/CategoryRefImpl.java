package org.eclipse.ve.internal.cde.palette.impl;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CategoryRefImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:18:00 $ 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.ve.internal.cde.palette.CategoryRef;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Category Ref</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.CategoryRefImpl#getRefGroups <em>Ref Groups</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class CategoryRefImpl extends CategoryImpl implements CategoryRef {

	

	/**
	 * The cached value of the '{@link #getRefGroups() <em>Ref Groups</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefGroups()
	 * @generated
	 * @ordered
	 */
	protected EList refGroups = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected CategoryRefImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getCategoryRef();
	}

	protected List getCategoryGroups() {
		List groups = getRefGroups();
		ArrayList groupList = new ArrayList(groups.size());
		// Slight possibility that one of the groups can't be found (i.e. the href was invalid).
		// In that case, a null is returned. We need to strip these out because GEF can't handle this.
		Iterator itr = groups.iterator();
		while (itr.hasNext()) {
			Object n = itr.next();
			if (n != null)
				groupList.add(n);
		}
		return groupList;
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getRefGroups() {
		if (refGroups == null) {
			refGroups = new EObjectResolvingEList(Group.class, this, PalettePackage.CATEGORY_REF__REF_GROUPS);
		}
		return refGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.CATEGORY_REF__CATEGORY_LABEL:
					return basicSetCategoryLabel(null, msgs);
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
			case PalettePackage.CATEGORY_REF__CATEGORY_LABEL:
				return getCategoryLabel();
			case PalettePackage.CATEGORY_REF__REF_GROUPS:
				return getRefGroups();
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
			case PalettePackage.CATEGORY_REF__CATEGORY_LABEL:
				setCategoryLabel((AbstractString)newValue);
				return;
			case PalettePackage.CATEGORY_REF__REF_GROUPS:
				getRefGroups().clear();
				getRefGroups().addAll((Collection)newValue);
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
			case PalettePackage.CATEGORY_REF__CATEGORY_LABEL:
				setCategoryLabel((AbstractString)null);
				return;
			case PalettePackage.CATEGORY_REF__REF_GROUPS:
				getRefGroups().clear();
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
			case PalettePackage.CATEGORY_REF__CATEGORY_LABEL:
				return categoryLabel != null;
			case PalettePackage.CATEGORY_REF__REF_GROUPS:
				return refGroups != null && !refGroups.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

}
