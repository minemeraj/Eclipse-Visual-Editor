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
 *  $RCSfile: CategoryImpl.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:50 $ 
 */
import java.util.Collection;


import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.InitialState;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Category</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.CategoryImpl#getCategoryLabel <em>Category Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public abstract class CategoryImpl extends DrawerImpl implements Category {

	

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected CategoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getCategory();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public AbstractString getCategoryLabel() {
		return getEntryLabel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public NotificationChain basicSetCategoryLabel(AbstractString newCategoryLabel, NotificationChain msgs) {
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void setCategoryLabel(AbstractString newCategoryLabel) {
		setEntryLabel(newCategoryLabel);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.CATEGORY__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.CATEGORY__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.CATEGORY__CHILDREN:
					return ((InternalEList)getChildren()).basicRemove(otherEnd, msgs);
				case PalettePackage.CATEGORY__CATEGORY_LABEL:
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
			case PalettePackage.CATEGORY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.CATEGORY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.CATEGORY__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CATEGORY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CATEGORY__ID:
				return getId();
			case PalettePackage.CATEGORY__MODIFICATION:
				return getModification();
			case PalettePackage.CATEGORY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.CATEGORY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.CATEGORY__CHILDREN:
				return getChildren();
			case PalettePackage.CATEGORY__INITIAL_STATE:
				return getInitialState();
			case PalettePackage.CATEGORY__CATEGORY_LABEL:
				return getCategoryLabel();
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
			case PalettePackage.CATEGORY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.CATEGORY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.CATEGORY__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.CATEGORY__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.CATEGORY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.CATEGORY__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.CATEGORY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.CATEGORY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.CATEGORY__CHILDREN:
				return children != null && !children.isEmpty();
			case PalettePackage.CATEGORY__INITIAL_STATE:
				return initialState != INITIAL_STATE_EDEFAULT;
			case PalettePackage.CATEGORY__CATEGORY_LABEL:
				return getCategoryLabel() != null;
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
			case PalettePackage.CATEGORY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.CATEGORY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.CATEGORY__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CATEGORY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CATEGORY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.CATEGORY__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.CATEGORY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.CATEGORY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.CATEGORY__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
				return;
			case PalettePackage.CATEGORY__INITIAL_STATE:
				setInitialState((InitialState)newValue);
				return;
			case PalettePackage.CATEGORY__CATEGORY_LABEL:
				setCategoryLabel((AbstractString)newValue);
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
			case PalettePackage.CATEGORY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.CATEGORY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.CATEGORY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.CATEGORY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.CATEGORY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.CATEGORY__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.CATEGORY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.CATEGORY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.CATEGORY__CHILDREN:
				getChildren().clear();
				return;
			case PalettePackage.CATEGORY__INITIAL_STATE:
				setInitialState(INITIAL_STATE_EDEFAULT);
				return;
			case PalettePackage.CATEGORY__CATEGORY_LABEL:
				setCategoryLabel((AbstractString)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

}
