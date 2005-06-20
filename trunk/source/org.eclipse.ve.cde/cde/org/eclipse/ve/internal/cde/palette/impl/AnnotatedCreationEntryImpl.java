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
 *  $RCSfile: AnnotatedCreationEntryImpl.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-20 23:54:40 $ 
 */
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

import org.eclipse.ve.internal.cde.core.AnnotationCreationFactory;
import org.eclipse.ve.internal.cde.palette.*;
import org.eclipse.ve.internal.cde.utility.AbstractString;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotated Creation Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.AnnotatedCreationEntryImpl#getObjectCreationEntry <em>Object Creation Entry</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.AnnotatedCreationEntryImpl#getValues <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class AnnotatedCreationEntryImpl extends AbstractToolEntryImpl implements AnnotatedCreationEntry {

	

	/**
	 * The cached value of the '{@link #getObjectCreationEntry() <em>Object Creation Entry</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectCreationEntry()
	 * @generated
	 * @ordered
	 */
	protected CreationToolEntry objectCreationEntry = null;
	/**
	 * The cached value of the '{@link #getValues() <em>Values</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValues()
	 * @generated
	 * @ordered
	 */
	protected EList values = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected AnnotatedCreationEntryImpl() {
		super();
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CreationToolEntry getObjectCreationEntry() {
		return objectCreationEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetObjectCreationEntry(CreationToolEntry newObjectCreationEntry, NotificationChain msgs) {
		CreationToolEntry oldObjectCreationEntry = objectCreationEntry;
		objectCreationEntry = newObjectCreationEntry;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY, oldObjectCreationEntry, newObjectCreationEntry);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setObjectCreationEntry(CreationToolEntry newObjectCreationEntry) {
		if (newObjectCreationEntry != objectCreationEntry) {
			NotificationChain msgs = null;
			if (objectCreationEntry != null)
				msgs = ((InternalEObject)objectCreationEntry).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY, null, msgs);
			if (newObjectCreationEntry != null)
				msgs = ((InternalEObject)newObjectCreationEntry).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY, null, msgs);
			msgs = basicSetObjectCreationEntry(newObjectCreationEntry, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY, newObjectCreationEntry, newObjectCreationEntry));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.ANNOTATED_CREATION_ENTRY__STRING_PROPERTIES:
					return ((InternalEList)getStringProperties()).basicRemove(otherEnd, msgs);
				case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
					return basicSetObjectCreationEntry(null, msgs);
				case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
					return ((InternalEList)getValues()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ID:
				return getId();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__MODIFICATION:
				return getModification();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__STRING_PROPERTIES:
				return getStringProperties();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				return getObjectCreationEntry();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				return getValues();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getAnnotatedCreationEntry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getValues() {
		if (values == null) {
			values = new EObjectContainmentEList(EObject.class, this, PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES);
		}
		return values;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.ANNOTATED_CREATION_ENTRY__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__STRING_PROPERTIES:
				return stringProperties != null && !stringProperties.isEmpty();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				return objectCreationEntry != null;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				return values != null && !values.isEmpty();
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
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				getStringProperties().addAll((Collection)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				setObjectCreationEntry((CreationToolEntry)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				getValues().clear();
				getValues().addAll((Collection)newValue);
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
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__STRING_PROPERTIES:
				getStringProperties().clear();
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				setObjectCreationEntry((CreationToolEntry)null);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				getValues().clear();
				return;
		}
		eDynamicUnset(eFeature);
	}
	
	protected PaletteEntry createPaletteEntry() {
		return getObjectCreationEntry().getEntry();
	}
	
	protected void configurePaletteEntry(PaletteEntry entry, Map entryToPaletteEntry) {
		super.configurePaletteEntry(entry, entryToPaletteEntry);
		// Now change the factory to include the annotations we have.
		org.eclipse.gef.palette.CreationToolEntry ce = (org.eclipse.gef.palette.CreationToolEntry) entry;
		CreationFactory cf = (CreationFactory) ce.getToolProperty(CreationTool.PROPERTY_CREATION_FACTORY);
		ce.setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, new AnnotationCreationFactory(getValues(), cf));
	}
}
