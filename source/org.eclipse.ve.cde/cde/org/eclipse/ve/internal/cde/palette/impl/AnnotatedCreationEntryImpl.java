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
 *  $RCSfile: AnnotatedCreationEntryImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.core.AnnotationCreationFactory;
import org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry;
import org.eclipse.ve.internal.cde.palette.CreationToolEntry;
import org.eclipse.ve.internal.cde.palette.ICDEToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
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

	public Tool getTool() {
		if (getObjectCreationEntry() != null) {
			CreationFactory factory = getObjectCreationEntry().createFactory();
			factory = new AnnotationCreationFactory(getValues(), factory);
			getObjectCreationEntry().setFactory(factory); // Put the wrappered factory back
			return getObjectCreationEntry().getTool(); // Now get the tool with the new factory.
		}
		return null;
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
			case PalettePackage.ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
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
			case PalettePackage.ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY:
				return defaultEntry != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
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
			case PalettePackage.ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
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
			case PalettePackage.ANNOTATED_CREATION_ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.ANNOTATED_CREATION_ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
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

	private class CreationEntry extends org.eclipse.gef.palette.CreationToolEntry implements ICDEToolEntry {
		
		private boolean defaultEntry;
		
		public CreationEntry() {
			super(null, null, null, null, null);
		}

		/**
		 * @see org.eclipse.ve.internal.cde.palette.ICDEToolEntry#isDefaultEntry()
		 */
		public boolean isDefaultEntry() {
			return defaultEntry;
		}

		/**
		 * @see org.eclipse.ve.internal.cde.palette.ICDEToolEntry#setDefaultEntry(boolean)
		 */
		public void setDefaultEntry(boolean defaultEntry) {
			this.defaultEntry = defaultEntry;
		}

		/**
		 * @see org.eclipse.gef.palette.ToolEntry#createTool()
		 */
		public Tool createTool() {
			return getTool();
		}

	}

	/**
	 * @see org.eclipse.ve.internal.cde.palette.impl.EntryImpl#createPaletteEntry()
	 */
	protected ICDEToolEntry createPaletteEntry() {
		return new CreationEntry();
	}

}
