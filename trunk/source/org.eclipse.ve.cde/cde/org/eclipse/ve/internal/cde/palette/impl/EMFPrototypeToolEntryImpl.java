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
 *  $RCSfile: EMFPrototypeToolEntryImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.emf.EMFPrototypeCreationFactory;
import org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY:
				return defaultEntry != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
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
