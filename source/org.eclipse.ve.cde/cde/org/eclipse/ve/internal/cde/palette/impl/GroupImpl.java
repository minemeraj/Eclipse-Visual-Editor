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
 *  $RCSfile: GroupImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:18:00 $ 
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
import org.eclipse.gef.palette.PaletteGroup;

import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.Group;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
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

public abstract class GroupImpl extends ContainerImpl implements Group {

	

	/**
	 * The cached value of the '{@link #getGroupLabel() <em>Group Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupLabel()
	 * @generated
	 * @ordered
	 */
	protected AbstractString groupLabel = null;
	
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

	public String getLabel() {
		return getGroupLabel() != null ? getGroupLabel().getStringValue() : ""; //$NON-NLS-1$
	}

	public List getChildren() {
		List c = getGroupEntries();
		ArrayList result = new ArrayList(c.size());
		for (int i=0; i<c.size(); i++)
			result.add(((Entry) c.get(i)).getEntry());
			
		return result;
			
	}		

	/**
	 * Return the entries
	 */
	public List getEntries() {
		return getGroupEntries();
	}

	protected List getGroupEntries() {
		throw new IllegalStateException("Must be implemented by subclass."); //$NON-NLS-1$
	}

	public Object getType() {
		return PaletteGroup.PALETTE_TYPE_GROUP;
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getGroupLabel() {
		return groupLabel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupLabel(AbstractString newGroupLabel, NotificationChain msgs) {
		AbstractString oldGroupLabel = groupLabel;
		groupLabel = newGroupLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PalettePackage.GROUP__GROUP_LABEL, oldGroupLabel, newGroupLabel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupLabel(AbstractString newGroupLabel) {
		if (newGroupLabel != groupLabel) {
			NotificationChain msgs = null;
			if (groupLabel != null)
				msgs = ((InternalEObject)groupLabel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PalettePackage.GROUP__GROUP_LABEL, null, msgs);
			if (newGroupLabel != null)
				msgs = ((InternalEObject)newGroupLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PalettePackage.GROUP__GROUP_LABEL, null, msgs);
			msgs = basicSetGroupLabel(newGroupLabel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.GROUP__GROUP_LABEL, newGroupLabel, newGroupLabel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
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
			case PalettePackage.GROUP__GROUP_LABEL:
				return groupLabel != null;
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
			case PalettePackage.GROUP__GROUP_LABEL:
				setGroupLabel((AbstractString)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * @see org.eclipse.ve.internal.cde.palette.impl.ContainerImpl#createPaletteContainer()
	 */
	protected PaletteContainer createPaletteContainer() {
		return new PaletteGroup(getLabel());
	}

}
