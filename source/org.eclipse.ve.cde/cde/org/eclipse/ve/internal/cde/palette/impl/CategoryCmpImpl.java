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
 *  $RCSfile: CategoryCmpImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-09-15 21:27:15 $ 
 */
import java.util.Collection;

import java.util.*;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.palette.PaletteGroup;

import org.eclipse.ve.internal.cde.palette.CategoryCmp;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.InitialState;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

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

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EList getCmpGroups() {
		return getChildren();
	}
	
	protected List getChildrenEntries(Map entryToPaletteEntry) {
		// New GEF Palette doesn't allow group to be child of Category (drawer), but
		// old GEF required it. Since this class is for transition to new format
		// we need to strip the group out and get the children of the group instead.
		List children = getChildren();
		if (children.isEmpty())
			return Collections.EMPTY_LIST;
		List entries = new ArrayList(children.size());
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			EntryImpl child = (EntryImpl) iter.next();
			if (child instanceof Group) {
				PaletteGroup pg = (PaletteGroup) child.getEntry(entryToPaletteEntry);
				entries.addAll(pg.getChildren());
			} else 
				entries.add(child.getEntry(entryToPaletteEntry));
		}
		return entries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.CATEGORY_CMP__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.CATEGORY_CMP__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.CATEGORY_CMP__CHILDREN:
					return ((InternalEList)getChildren()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.CATEGORY_CMP__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.CATEGORY_CMP__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.CATEGORY_CMP__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CATEGORY_CMP__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CATEGORY_CMP__ID:
				return getId();
			case PalettePackage.CATEGORY_CMP__MODIFICATION:
				return getModification();
			case PalettePackage.CATEGORY_CMP__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.CATEGORY_CMP__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.CATEGORY_CMP__CHILDREN:
				return getChildren();
			case PalettePackage.CATEGORY_CMP__INITIAL_STATE:
				return getInitialState();
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
			case PalettePackage.CATEGORY_CMP__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.CATEGORY_CMP__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.CATEGORY_CMP__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CATEGORY_CMP__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CATEGORY_CMP__ID:
				setId((String)newValue);
				return;
			case PalettePackage.CATEGORY_CMP__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.CATEGORY_CMP__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.CATEGORY_CMP__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.CATEGORY_CMP__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
				return;
			case PalettePackage.CATEGORY_CMP__INITIAL_STATE:
				setInitialState((InitialState)newValue);
				return;
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
			case PalettePackage.CATEGORY_CMP__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.CATEGORY_CMP__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.CATEGORY_CMP__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.CATEGORY_CMP__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.CATEGORY_CMP__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.CATEGORY_CMP__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.CATEGORY_CMP__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.CATEGORY_CMP__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.CATEGORY_CMP__CHILDREN:
				getChildren().clear();
				return;
			case PalettePackage.CATEGORY_CMP__INITIAL_STATE:
				setInitialState(INITIAL_STATE_EDEFAULT);
				return;
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
			case PalettePackage.CATEGORY_CMP__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.CATEGORY_CMP__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.CATEGORY_CMP__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
			case PalettePackage.CATEGORY_CMP__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.CATEGORY_CMP__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.CATEGORY_CMP__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.CATEGORY_CMP__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.CATEGORY_CMP__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.CATEGORY_CMP__CHILDREN:
				return children != null && !children.isEmpty();
			case PalettePackage.CATEGORY_CMP__INITIAL_STATE:
				return initialState != INITIAL_STATE_EDEFAULT;
			case PalettePackage.CATEGORY_CMP__CATEGORY_LABEL:
				return getCategoryLabel() != null;
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				return cmpGroups != null && !cmpGroups.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

	/*
	 * This method is here because EMF keeps adding some imports that are never referenced and
	 * this causes compile errors.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void neverCall() {
		if (EObjectContainmentEList.class != null)
			if (Group.class != null)
				throw new UnsupportedOperationException("This method should not be called.");
	}
}
