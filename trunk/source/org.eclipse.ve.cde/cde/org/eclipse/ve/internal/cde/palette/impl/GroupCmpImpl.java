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
 *  $RCSfile: GroupCmpImpl.java,v $
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

import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.GroupCmp;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group Cmp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.GroupCmpImpl#getCmpEntries <em>Cmp Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class GroupCmpImpl extends GroupImpl implements GroupCmp {

	

	/**
	 * The cached value of the '{@link #getCmpEntries() <em>Cmp Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCmpEntries()
	 * @generated
	 * @ordered
	 */
	protected EList cmpEntries = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected GroupCmpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getGroupCmp();
	}

	protected List getGroupEntries() {
		return getCmpEntries();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getCmpEntries() {
		if (cmpEntries == null) {
			cmpEntries = new EObjectContainmentEList(Entry.class, this, PalettePackage.GROUP_CMP__CMP_ENTRIES);
		}
		return cmpEntries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.GROUP_CMP__GROUP_LABEL:
					return basicSetGroupLabel(null, msgs);
				case PalettePackage.GROUP_CMP__CMP_ENTRIES:
					return ((InternalEList)getCmpEntries()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.GROUP_CMP__GROUP_LABEL:
				return getGroupLabel();
			case PalettePackage.GROUP_CMP__CMP_ENTRIES:
				return getCmpEntries();
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
			case PalettePackage.GROUP_CMP__GROUP_LABEL:
				setGroupLabel((AbstractString)newValue);
				return;
			case PalettePackage.GROUP_CMP__CMP_ENTRIES:
				getCmpEntries().clear();
				getCmpEntries().addAll((Collection)newValue);
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
			case PalettePackage.GROUP_CMP__GROUP_LABEL:
				setGroupLabel((AbstractString)null);
				return;
			case PalettePackage.GROUP_CMP__CMP_ENTRIES:
				getCmpEntries().clear();
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
			case PalettePackage.GROUP_CMP__GROUP_LABEL:
				return groupLabel != null;
			case PalettePackage.GROUP_CMP__CMP_ENTRIES:
				return cmpEntries != null && !cmpEntries.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

}
