package org.eclipse.ve.internal.cde.palette.impl;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ContainerImpl.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-20 23:54:40 $ 
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

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;

import org.eclipse.ve.internal.cde.palette.Container;
import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.ContainerImpl#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ContainerImpl extends EntryImpl implements Container {

	
	
	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList children = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected ContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList(Entry.class, this, PalettePackage.CONTAINER__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.CONTAINER__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.CONTAINER__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.CONTAINER__CHILDREN:
					return ((InternalEList)getChildren()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.CONTAINER__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.CONTAINER__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.CONTAINER__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CONTAINER__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.CONTAINER__ID:
				return getId();
			case PalettePackage.CONTAINER__MODIFICATION:
				return getModification();
			case PalettePackage.CONTAINER__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.CONTAINER__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.CONTAINER__CHILDREN:
				return getChildren();
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
			case PalettePackage.CONTAINER__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.CONTAINER__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.CONTAINER__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CONTAINER__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.CONTAINER__ID:
				setId((String)newValue);
				return;
			case PalettePackage.CONTAINER__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.CONTAINER__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.CONTAINER__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.CONTAINER__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
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
			case PalettePackage.CONTAINER__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.CONTAINER__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.CONTAINER__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.CONTAINER__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.CONTAINER__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.CONTAINER__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.CONTAINER__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.CONTAINER__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.CONTAINER__CHILDREN:
				getChildren().clear();
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
			case PalettePackage.CONTAINER__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.CONTAINER__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.CONTAINER__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.CONTAINER__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.CONTAINER__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.CONTAINER__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.CONTAINER__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.CONTAINER__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.CONTAINER__CHILDREN:
				return children != null && !children.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

	protected void configurePaletteEntry(PaletteEntry entry, Map entryToPaletteEntry) {
		super.configurePaletteEntry(entry, entryToPaletteEntry);
		PaletteContainer c = (PaletteContainer) entry;
		c.addAll(getChildrenEntries(entryToPaletteEntry));
	}
	
	
	/**
	 * Get the children as GEF Entries.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected List getChildrenEntries(Map entryToPaletteEntry) {
		List children = getChildren();
		if (children.isEmpty())
			return Collections.EMPTY_LIST;
		List entries = new ArrayList(children.size());
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			EntryImpl child = (EntryImpl) iter.next();
			entries.add(child.getEntry(entryToPaletteEntry));
		}
		return entries;
	}
}
