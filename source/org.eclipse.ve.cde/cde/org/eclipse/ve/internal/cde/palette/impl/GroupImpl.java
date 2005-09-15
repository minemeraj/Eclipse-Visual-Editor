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
 *  $RCSfile: GroupImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-09-15 21:27:15 $ 
 */
import java.util.Collection;


import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.gef.palette.*;

import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.GroupImpl#getGroupLabel <em>Group Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class GroupImpl extends ContainerImpl implements Group {

	

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected GroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getGroup();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public AbstractString getGroupLabel() {
		return getEntryLabel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupLabel(AbstractString newGroupLabel, NotificationChain msgs) {
		// TODO: implement this method to set the contained 'Group Label' containment reference
		// -> this method is automatically invoked to keep the containment relationship in synch
		// -> do not modify other features
		// -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void setGroupLabel(AbstractString newGroupLabel) {
		setEntryLabel(newGroupLabel);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.GROUP__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.GROUP__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.GROUP__CHILDREN:
					return ((InternalEList)getChildren()).basicRemove(otherEnd, msgs);
				case PalettePackage.GROUP__GROUP_LABEL:
					return basicSetGroupLabel(null, msgs);
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
			case PalettePackage.GROUP__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.GROUP__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.GROUP__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.GROUP__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.GROUP__ID:
				return getId();
			case PalettePackage.GROUP__MODIFICATION:
				return getModification();
			case PalettePackage.GROUP__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.GROUP__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.GROUP__CHILDREN:
				return getChildren();
			case PalettePackage.GROUP__GROUP_LABEL:
				return getGroupLabel();
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
			case PalettePackage.GROUP__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.GROUP__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.GROUP__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
			case PalettePackage.GROUP__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.GROUP__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.GROUP__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.GROUP__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.GROUP__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.GROUP__CHILDREN:
				return children != null && !children.isEmpty();
			case PalettePackage.GROUP__GROUP_LABEL:
				return getGroupLabel() != null;
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
			case PalettePackage.GROUP__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.GROUP__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.GROUP__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.GROUP__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.GROUP__ID:
				setId((String)newValue);
				return;
			case PalettePackage.GROUP__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.GROUP__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.GROUP__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.GROUP__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
				return;
			case PalettePackage.GROUP__GROUP_LABEL:
				setGroupLabel((AbstractString)newValue);
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
			case PalettePackage.GROUP__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.GROUP__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.GROUP__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.GROUP__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.GROUP__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.GROUP__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.GROUP__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.GROUP__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.GROUP__CHILDREN:
				getChildren().clear();
				return;
			case PalettePackage.GROUP__GROUP_LABEL:
				setGroupLabel((AbstractString)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	protected PaletteEntry createPaletteEntry() {
		return new PaletteGroup(getLabel());
	}

}
