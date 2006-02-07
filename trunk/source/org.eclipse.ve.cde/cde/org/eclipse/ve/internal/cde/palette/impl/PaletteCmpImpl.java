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
package org.eclipse.ve.internal.cde.palette.impl;
/*
 *  $RCSfile: PaletteCmpImpl.java,v $
 *  $Revision: 1.6 $  $Date: 2006-02-07 17:21:33 $ 
 */
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.common.util.*;
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
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.PaletteCmpImpl#getPaletteLabel <em>Palette Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class PaletteCmpImpl extends RootImpl implements PaletteCmp {

	

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
		return PalettePackage.Literals.PALETTE_CMP;
	}

	public EList getChildren() {
		// We have to override so that it returns ControlGroup,Categories. This list cannot be modified.
		EList cats = getCmpCategories();
		Group controlGroup = getCmpControlGroup();
		EList children = new BasicEList(((controlGroup != null) ? 1 : 0)+cats.size());
		if (controlGroup != null)
			children.add(controlGroup);
		children.addAll(cats);
		return ECollections.unmodifiableEList(children);
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
	 */
	public AbstractString getPaletteLabel() {
		return getEntryLabel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public NotificationChain basicSetPaletteLabel(AbstractString newPaletteLabel, NotificationChain msgs) {
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void setPaletteLabel(AbstractString newPaletteLabel) {
		setEntryLabel(newPaletteLabel);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				return ((InternalEList)getCmpCategories()).basicRemove(otherEnd, msgs);
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				return basicSetCmpControlGroup(null, msgs);
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				return basicSetPaletteLabel(null, msgs);
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
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				return getCmpCategories();
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				return getCmpControlGroup();
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				return getPaletteLabel();
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
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				getCmpCategories().clear();
				getCmpCategories().addAll((Collection)newValue);
				return;
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				setCmpControlGroup((Group)newValue);
				return;
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				setPaletteLabel((AbstractString)newValue);
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
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				getCmpCategories().clear();
				return;
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				setCmpControlGroup((Group)null);
				return;
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				setPaletteLabel((AbstractString)null);
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
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				return cmpCategories != null && !cmpCategories.isEmpty();
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				return cmpControlGroup != null;
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				return getPaletteLabel() != null;
		}
		return super.eIsSet(featureID);
	}

}
