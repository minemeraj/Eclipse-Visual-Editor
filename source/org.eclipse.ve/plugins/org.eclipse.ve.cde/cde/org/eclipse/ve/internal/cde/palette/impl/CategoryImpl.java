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
 *  $RCSfile: CategoryImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2004-05-21 17:48:23 $ 
 */
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;

import org.eclipse.ve.internal.cde.palette.Category;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Category</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.CategoryImpl#getCategoryLabel <em>Category Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public abstract class CategoryImpl extends ContainerImpl implements Category {

	

	/**
	 * The cached value of the '{@link #getCategoryLabel() <em>Category Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategoryLabel()
	 * @generated
	 * @ordered
	 */
	protected AbstractString categoryLabel = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected CategoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getCategory();
	}

	public List getChildren() {
		// TODO Need to eventually restructure this. GEF no longer allows groups with the categories. Need
		// to restructure EMF model of palette to take this into account.
		// For now, delve into the groups, get their children and return those instead.
		List c = getCategoryGroups();
		ArrayList result = new ArrayList(c.size()*5);
		for (int i=0; i<c.size(); i++) {
			GroupImpl g = (GroupImpl) c.get(i);
			result.addAll(g.getChildren());
		}
		return result;
			
	}

	/**
	 * Return the groups
	 */
	public List getGroups() {
		return getCategoryGroups();
	}

	public String getLabel() {
		return getCategoryLabel() != null ? getCategoryLabel().getStringValue() : ""; //$NON-NLS-1$
	}

	protected List getCategoryGroups() {
		throw new IllegalStateException("Must be implemented by subclass."); //$NON-NLS-1$
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getCategoryLabel() {
		return categoryLabel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCategoryLabel(AbstractString newCategoryLabel, NotificationChain msgs) {
		AbstractString oldCategoryLabel = categoryLabel;
		categoryLabel = newCategoryLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PalettePackage.CATEGORY__CATEGORY_LABEL, oldCategoryLabel, newCategoryLabel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCategoryLabel(AbstractString newCategoryLabel) {
		if (newCategoryLabel != categoryLabel) {
			NotificationChain msgs = null;
			if (categoryLabel != null)
				msgs = ((InternalEObject)categoryLabel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PalettePackage.CATEGORY__CATEGORY_LABEL, null, msgs);
			if (newCategoryLabel != null)
				msgs = ((InternalEObject)newCategoryLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PalettePackage.CATEGORY__CATEGORY_LABEL, null, msgs);
			msgs = basicSetCategoryLabel(newCategoryLabel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.CATEGORY__CATEGORY_LABEL, newCategoryLabel, newCategoryLabel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.CATEGORY__CATEGORY_LABEL:
					return basicSetCategoryLabel(null, msgs);
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
			case PalettePackage.CATEGORY__CATEGORY_LABEL:
				return getCategoryLabel();
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
			case PalettePackage.CATEGORY__CATEGORY_LABEL:
				return categoryLabel != null;
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
			case PalettePackage.CATEGORY__CATEGORY_LABEL:
				setCategoryLabel((AbstractString)newValue);
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
			case PalettePackage.CATEGORY__CATEGORY_LABEL:
				setCategoryLabel((AbstractString)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * @see org.eclipse.ve.internal.cde.palette.impl.ContainerImpl#createPaletteContainer()
	 */
	protected PaletteContainer createPaletteContainer() {
		PaletteDrawer pd = new PaletteDrawer(getLabel());
		pd.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		return pd;
	}

}
