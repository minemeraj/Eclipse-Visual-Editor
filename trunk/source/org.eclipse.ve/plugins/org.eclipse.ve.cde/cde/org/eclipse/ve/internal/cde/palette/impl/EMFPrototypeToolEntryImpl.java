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
 *  $RCSfile: EMFPrototypeToolEntryImpl.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:50 $ 
 */
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.emf.EMFPrototypeCreationFactory;
import org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EMF Prototype Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EMFPrototypeToolEntryImpl#getPrototypeURI <em>Prototype URI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class EMFPrototypeToolEntryImpl extends CreationToolEntryImpl implements EMFPrototypeToolEntry {

	/**
	 * The default value of the '{@link #getPrototypeURI() <em>Prototype URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrototypeURI()
	 * @generated
	 * @ordered
	 */
	protected static final String PROTOTYPE_URI_EDEFAULT = null;

	

	/**
	 * The cached value of the '{@link #getPrototypeURI() <em>Prototype URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrototypeURI()
	 * @generated
	 * @ordered
	 */
	protected String prototypeURI = PROTOTYPE_URI_EDEFAULT;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected EMFPrototypeToolEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getEMFPrototypeToolEntry();
	}

	public CreationFactory createFactory() {
		return new EMFPrototypeCreationFactory(getPrototypeURI());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__STRING_PROPERTIES:
				return stringProperties != null && !stringProperties.isEmpty();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI:
				return PROTOTYPE_URI_EDEFAULT == null ? prototypeURI != null : !PROTOTYPE_URI_EDEFAULT.equals(prototypeURI);
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				getStringProperties().addAll((Collection)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI:
				setPrototypeURI((String)newValue);
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI:
				setPrototypeURI(PROTOTYPE_URI_EDEFAULT);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPrototypeURI() {
		return prototypeURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrototypeURI(String newPrototypeURI) {
		String oldPrototypeURI = prototypeURI;
		prototypeURI = newPrototypeURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI, oldPrototypeURI, prototypeURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__STRING_PROPERTIES:
					return ((InternalEList)getStringProperties()).basicRemove(otherEnd, msgs);
				case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ID:
				return getId();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__MODIFICATION:
				return getModification();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__STRING_PROPERTIES:
				return getStringProperties();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__KEYED_VALUES:
				return getKeyedValues();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI:
				return getPrototypeURI();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (prototypeURI: ");
		result.append(prototypeURI);
		result.append(')');
		return result.toString();
	}

}
