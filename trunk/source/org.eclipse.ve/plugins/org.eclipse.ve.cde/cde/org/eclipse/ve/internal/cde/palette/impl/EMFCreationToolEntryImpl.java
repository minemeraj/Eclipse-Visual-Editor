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
 *  $RCSfile: EMFCreationToolEntryImpl.java,v $
 *  $Revision: 1.6 $  $Date: 2006-02-07 17:21:33 $ 
 */

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.emf.EMFClassCreationFactory;
import org.eclipse.ve.internal.cde.palette.EMFCreationToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EMF Creation Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EMFCreationToolEntryImpl#getCreationClassURI <em>Creation Class URI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class EMFCreationToolEntryImpl extends CreationToolEntryImpl implements EMFCreationToolEntry {

	/**
	 * The default value of the '{@link #getCreationClassURI() <em>Creation Class URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreationClassURI()
	 * @generated
	 * @ordered
	 */
	protected static final String CREATION_CLASS_URI_EDEFAULT = null;

	

	/**
	 * The cached value of the '{@link #getCreationClassURI() <em>Creation Class URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreationClassURI()
	 * @generated
	 * @ordered
	 */
	protected String creationClassURI = CREATION_CLASS_URI_EDEFAULT;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected EMFCreationToolEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.Literals.EMF_CREATION_TOOL_ENTRY;
	}

	public CreationFactory createFactory() {
		return new EMFClassCreationFactory(getCreationClassURI());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCreationClassURI() {
		return creationClassURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCreationClassURI(String newCreationClassURI) {
		String oldCreationClassURI = creationClassURI;
		creationClassURI = newCreationClassURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.EMF_CREATION_TOOL_ENTRY__CREATION_CLASS_URI, oldCreationClassURI, creationClassURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.EMF_CREATION_TOOL_ENTRY__CREATION_CLASS_URI:
				return getCreationClassURI();
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
			case PalettePackage.EMF_CREATION_TOOL_ENTRY__CREATION_CLASS_URI:
				setCreationClassURI((String)newValue);
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
			case PalettePackage.EMF_CREATION_TOOL_ENTRY__CREATION_CLASS_URI:
				setCreationClassURI(CREATION_CLASS_URI_EDEFAULT);
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
			case PalettePackage.EMF_CREATION_TOOL_ENTRY__CREATION_CLASS_URI:
				return CREATION_CLASS_URI_EDEFAULT == null ? creationClassURI != null : !CREATION_CLASS_URI_EDEFAULT.equals(creationClassURI);
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
		result.append(" (creationClassURI: ");
		result.append(creationClassURI);
		result.append(')');
		return result.toString();
	}

}
