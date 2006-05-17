/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
 *  $Revision: 1.7 $  $Date: 2006-05-17 20:13:52 $ 
 */

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.emf.EMFPrototypeCreationFactory;
import org.eclipse.ve.internal.cde.palette.EMFPrototypeToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;


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
		return PalettePackage.Literals.EMF_PROTOTYPE_TOOL_ENTRY;
	}

	public CreationFactory createFactory() {
		return new EMFPrototypeCreationFactory(getPrototypeURI());
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI:
				return getPrototypeURI();
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI:
				setPrototypeURI((String)newValue);
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI:
				setPrototypeURI(PROTOTYPE_URI_EDEFAULT);
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
			case PalettePackage.EMF_PROTOTYPE_TOOL_ENTRY__PROTOTYPE_URI:
				return PROTOTYPE_URI_EDEFAULT == null ? prototypeURI != null : !PROTOTYPE_URI_EDEFAULT.equals(prototypeURI);
		}
		return super.eIsSet(featureID);
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
