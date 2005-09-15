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
 *  $Revision: 1.5 $  $Date: 2005-09-15 21:27:15 $ 
 */
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cde.palette.AbstractToolEntry;
import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.PaletteCmp;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

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
		return PalettePackage.eINSTANCE.getPaletteCmp();
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.PALETTE_CMP__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.PALETTE_CMP__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.PALETTE_CMP__CHILDREN:
					return ((InternalEList)getChildren()).basicRemove(otherEnd, msgs);
				case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
					return ((InternalEList)getCmpCategories()).basicRemove(otherEnd, msgs);
				case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
					return basicSetCmpControlGroup(null, msgs);
				case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
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
			case PalettePackage.PALETTE_CMP__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.PALETTE_CMP__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.PALETTE_CMP__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.PALETTE_CMP__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.PALETTE_CMP__ID:
				return getId();
			case PalettePackage.PALETTE_CMP__MODIFICATION:
				return getModification();
			case PalettePackage.PALETTE_CMP__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.PALETTE_CMP__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.PALETTE_CMP__CHILDREN:
				return getChildren();
			case PalettePackage.PALETTE_CMP__DEF_ENTRY:
				if (resolve) return getDefEntry();
				return basicGetDefEntry();
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				return getCmpCategories();
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				return getCmpControlGroup();
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				return getPaletteLabel();
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
			case PalettePackage.PALETTE_CMP__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.PALETTE_CMP__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.PALETTE_CMP__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
			case PalettePackage.PALETTE_CMP__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.PALETTE_CMP__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.PALETTE_CMP__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.PALETTE_CMP__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.PALETTE_CMP__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.PALETTE_CMP__CHILDREN:
				return children != null && !children.isEmpty();
			case PalettePackage.PALETTE_CMP__DEF_ENTRY:
				return defEntry != null;
			case PalettePackage.PALETTE_CMP__CMP_CATEGORIES:
				return cmpCategories != null && !cmpCategories.isEmpty();
			case PalettePackage.PALETTE_CMP__CMP_CONTROL_GROUP:
				return cmpControlGroup != null;
			case PalettePackage.PALETTE_CMP__PALETTE_LABEL:
				return getPaletteLabel() != null;
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
			case PalettePackage.PALETTE_CMP__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.PALETTE_CMP__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.PALETTE_CMP__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.PALETTE_CMP__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.PALETTE_CMP__ID:
				setId((String)newValue);
				return;
			case PalettePackage.PALETTE_CMP__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.PALETTE_CMP__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.PALETTE_CMP__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.PALETTE_CMP__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
				return;
			case PalettePackage.PALETTE_CMP__DEF_ENTRY:
				setDefEntry((AbstractToolEntry)newValue);
				return;
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
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.PALETTE_CMP__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.PALETTE_CMP__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.PALETTE_CMP__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.PALETTE_CMP__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.PALETTE_CMP__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.PALETTE_CMP__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.PALETTE_CMP__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.PALETTE_CMP__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.PALETTE_CMP__CHILDREN:
				getChildren().clear();
				return;
			case PalettePackage.PALETTE_CMP__DEF_ENTRY:
				setDefEntry((AbstractToolEntry)null);
				return;
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
		eDynamicUnset(eFeature);
	}

}
