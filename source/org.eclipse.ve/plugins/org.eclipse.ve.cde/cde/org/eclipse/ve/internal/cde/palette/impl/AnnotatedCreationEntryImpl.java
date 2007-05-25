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
 *  $RCSfile: AnnotatedCreationEntryImpl.java,v $
 *  $Revision: 1.8 $  $Date: 2007-05-25 04:09:36 $ 
 */
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry;
import org.eclipse.ve.internal.cde.palette.CreationToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

import org.eclipse.ve.internal.cde.core.AnnotationCreationFactory;


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
	protected EList<EObject> values = null;
	
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
	@Override
	protected EClass eStaticClass() {
		return PalettePackage.Literals.ANNOTATED_CREATION_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getValues() {
		if (values == null) {
			values = new EObjectContainmentEList<EObject>(EObject.class, this, PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES);
		}
		return values;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				return basicSetObjectCreationEntry(null, msgs);
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				return ((InternalEList<?>)getValues()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				return getObjectCreationEntry();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				return getValues();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
		@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				setObjectCreationEntry((CreationToolEntry)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				getValues().clear();
				getValues().addAll((Collection<? extends EObject>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				setObjectCreationEntry((CreationToolEntry)null);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				getValues().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PalettePackage.ANNOTATED_CREATION_ENTRY__OBJECT_CREATION_ENTRY:
				return objectCreationEntry != null;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__VALUES:
				return values != null && !values.isEmpty();
		}
		return super.eIsSet(featureID);
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
