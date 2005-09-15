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
 *  $RCSfile: GroupCmpImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-09-15 21:27:15 $ 
 */
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cde.palette.GroupCmp;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

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

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EList getCmpEntries() {
		return getChildren();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.GROUP_CMP__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.GROUP_CMP__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.GROUP_CMP__CHILDREN:
					return ((InternalEList)getChildren()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.GROUP_CMP__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.GROUP_CMP__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.GROUP_CMP__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.GROUP_CMP__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.GROUP_CMP__ID:
				return getId();
			case PalettePackage.GROUP_CMP__MODIFICATION:
				return getModification();
			case PalettePackage.GROUP_CMP__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.GROUP_CMP__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.GROUP_CMP__CHILDREN:
				return getChildren();
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
			case PalettePackage.GROUP_CMP__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.GROUP_CMP__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.GROUP_CMP__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.GROUP_CMP__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.GROUP_CMP__ID:
				setId((String)newValue);
				return;
			case PalettePackage.GROUP_CMP__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.GROUP_CMP__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.GROUP_CMP__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.GROUP_CMP__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
				return;
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
			case PalettePackage.GROUP_CMP__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.GROUP_CMP__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.GROUP_CMP__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.GROUP_CMP__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.GROUP_CMP__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.GROUP_CMP__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.GROUP_CMP__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.GROUP_CMP__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.GROUP_CMP__CHILDREN:
				getChildren().clear();
				return;
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
			case PalettePackage.GROUP_CMP__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.GROUP_CMP__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.GROUP_CMP__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
			case PalettePackage.GROUP_CMP__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.GROUP_CMP__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.GROUP_CMP__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.GROUP_CMP__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.GROUP_CMP__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.GROUP_CMP__CHILDREN:
				return children != null && !children.isEmpty();
			case PalettePackage.GROUP_CMP__GROUP_LABEL:
				return getGroupLabel() != null;
			case PalettePackage.GROUP_CMP__CMP_ENTRIES:
				return !getCmpEntries().isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

}
