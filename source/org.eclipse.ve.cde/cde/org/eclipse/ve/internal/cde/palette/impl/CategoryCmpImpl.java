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
 *  $Revision: 1.6 $  $Date: 2006-02-07 17:21:33 $ 
 */
import java.util.Collection;

import java.util.*;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.palette.PaletteGroup;

import org.eclipse.ve.internal.cde.palette.CategoryCmp;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.PalettePackage;

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
		return PalettePackage.Literals.CATEGORY_CMP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EList getCmpGroups() {
		return getChildren();
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				return ((InternalEList)getCmpGroups()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				return getCmpGroups();
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
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				getCmpGroups().clear();
				getCmpGroups().addAll((Collection)newValue);
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
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				getCmpGroups().clear();
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
			case PalettePackage.CATEGORY_CMP__CMP_GROUPS:
				return cmpGroups != null && !cmpGroups.isEmpty();
		}
		return super.eIsSet(featureID);
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
