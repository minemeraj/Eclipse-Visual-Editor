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
 *  $RCSfile: CategoryCmpImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cde.palette.CategoryCmp;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Category Cmp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.CategoryCmpImpl#getCmpGroups <em>Cmp Groups</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class CategoryCmpImpl extends CategoryImpl implements CategoryCmp {

	

	/**
	 * The cached value of the '{@link #getCmpGroups() <em>Cmp Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCmpGroups()
	 * @generated
	 * @ordered
	 */
	protected EList cmpGroups = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected CategoryCmpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getCategoryCmp();
	}

	protected List getCategoryGroups() {
		return getCmpGroups();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getCmpGroups() {
		if (cmpGroups == null) {
			cmpGroups = new EObjectContainmentEList(Group.class, this, PalettePackage.CATEGORY_CMP__CMP_GROUPS);
		}
		return cmpGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.CATEGORY_CMP__CATEGORY_LABEL:
					return basicSetCategoryLabel(null, msgs);
				case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
					return ((InternalEList)getCmpGroups()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.CATEGORY_CMP__CATEGORY_LABEL:
				return getCategoryLabel();
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				return getCmpGroups();
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
			case PalettePackage.CATEGORY_CMP__CATEGORY_LABEL:
				setCategoryLabel((AbstractString)newValue);
				return;
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				getCmpGroups().clear();
				getCmpGroups().addAll((Collection)newValue);
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
			case PalettePackage.CATEGORY_CMP__CATEGORY_LABEL:
				setCategoryLabel((AbstractString)null);
				return;
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				getCmpGroups().clear();
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
			case PalettePackage.CATEGORY_CMP__CATEGORY_LABEL:
				return categoryLabel != null;
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				return cmpGroups != null && !cmpGroups.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

}
