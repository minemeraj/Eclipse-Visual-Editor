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
 *  $RCSfile: AbstractToolEntryImpl.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:50 $ 
 */
import java.util.Collection;

import java.util.*;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;

import org.eclipse.ve.internal.cde.palette.AbstractToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.AbstractToolEntryImpl#getStringProperties <em>String Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public abstract class AbstractToolEntryImpl extends EntryImpl implements AbstractToolEntry {

	
	
	/**
	 * The cached value of the '{@link #getStringProperties() <em>String Properties</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStringProperties()
	 * @generated
	 * @ordered
	 */
	protected EMap stringProperties = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected AbstractToolEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getAbstractToolEntry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap getStringProperties() {
		if (stringProperties == null) {
			stringProperties = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, PalettePackage.ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES);
		}
		return stringProperties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES:
					return ((InternalEList)getStringProperties()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.ABSTRACT_TOOL_ENTRY__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ID:
				return getId();
			case PalettePackage.ABSTRACT_TOOL_ENTRY__MODIFICATION:
				return getModification();
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES:
				return getStringProperties();
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
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				getStringProperties().addAll((Collection)newValue);
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
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
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
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.ABSTRACT_TOOL_ENTRY__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.ABSTRACT_TOOL_ENTRY__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.ABSTRACT_TOOL_ENTRY__STRING_PROPERTIES:
				return stringProperties != null && !stringProperties.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

	protected void configurePaletteEntry(PaletteEntry entry, Map entryToPaletteEntry) {
		super.configurePaletteEntry(entry, entryToPaletteEntry);
		if (eIsSet(PalettePackage.eINSTANCE.getAbstractToolEntry_StringProperties())) {
			ToolEntry toolEntry = (ToolEntry) entry;
			for (Iterator iter = getStringProperties().entrySet().iterator(); iter.hasNext();) {
				Map.Entry properties = (Map.Entry) iter.next();
				toolEntry.setToolProperty(properties.getKey(), properties.getValue());
			}
		}
	}
}
