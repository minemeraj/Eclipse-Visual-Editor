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
 *  $RCSfile: PaletteCmpImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.PaletteCmp;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cmp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.PaletteCmpImpl#getCmpCategories <em>Cmp Categories</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.PaletteCmpImpl#getCmpControlGroup <em>Cmp Control Group</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class PaletteCmpImpl extends PaletteImpl implements PaletteCmp {

	

	/**
	 * The cached value of the '{@link #getCmpCategories() <em>Cmp Categories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCmpCategories()
	 * @generated
	 * @ordered
	 */
	protected EList cmpCategories = null;
	/**
	 * The cached value of the '{@link #getCmpControlGroup() <em>Cmp Control Group</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCmpControlGroup()
	 * @generated
	 * @ordered
	 */
	protected Group cmpControlGroup = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected PaletteCmpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getPaletteCmp();
	}

	protected Group getPaletteControlGroup() {
		return getCmpControlGroup();
	}

	protected List getPaletteCategories() {
		return getCmpCategories();
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getCmpCategories() {
		if (cmpCategories == null) {
			cmpCategories = new EObjectContainmentEList(Category.class, this, PalettePackage.PALETTE_CMP__CMP_CATEGORIES);
		}
		return cmpCategories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Group getCmpControlGroup() {
		return cmpControlGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCmpControlGroup(Group newCmpControlGroup, NotificationChain msgs) {
		Group oldCmpControlGroup = cmpControlGroup;
		cmpControlGroup = newCmpControlGroup;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP, oldCmpControlGroup, newCmpControlGroup);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCmpControlGroup(Group newCmpControlGroup) {
		if (newCmpControlGroup != cmpControlGroup) {
			NotificationChain msgs = null;
			if (cmpControlGroup != null)
				msgs = ((InternalEObject)cmpControlGroup).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP, null, msgs);
			if (newCmpControlGroup != null)
				msgs = ((InternalEObject)newCmpControlGroup).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP, null, msgs);
			msgs = basicSetCmpControlGroup(newCmpControlGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP, newCmpControlGroup, newCmpControlGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
					return basicSetPaletteLabel(null, msgs);
				case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
					return ((InternalEList)getCmpCategories()).basicRemove(otherEnd, msgs);
				case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
					return basicSetCmpControlGroup(null, msgs);
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
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				return getPaletteLabel();
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				return getCmpCategories();
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				return getCmpControlGroup();
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
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				return paletteLabel != null;
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				return cmpCategories != null && !cmpCategories.isEmpty();
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				return cmpControlGroup != null;
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
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				setPaletteLabel((AbstractString)newValue);
				return;
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				getCmpCategories().clear();
				getCmpCategories().addAll((Collection)newValue);
				return;
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				setCmpControlGroup((Group)newValue);
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
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				setPaletteLabel((AbstractString)null);
				return;
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				getCmpCategories().clear();
				return;
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				setCmpControlGroup((Group)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

}
