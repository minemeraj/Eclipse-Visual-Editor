/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.palette.impl;
/*
 *  $RCSfile: EntryImpl.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:35 $ 
 */
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.ICDEToolEntry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#getIcon16Name <em>Icon16 Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#getIcon32Name <em>Icon32 Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#isDefaultEntry <em>Default Entry</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#getEntryLabel <em>Entry Label</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#getEntryShortDescription <em>Entry Short Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EntryImpl extends EObjectImpl implements Entry {

	/**
	 * The default value of the '{@link #getIcon16Name() <em>Icon16 Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIcon16Name()
	 * @generated
	 * @ordered
	 */
	protected static final String ICON16_NAME_EDEFAULT = null;

	
	protected ImageDescriptor fIcon16;
	protected ImageDescriptor fIcon32;

	/**
	 * The cached value of the '{@link #getIcon16Name() <em>Icon16 Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIcon16Name()
	 * @generated
	 * @ordered
	 */
	protected String icon16Name = ICON16_NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getIcon32Name() <em>Icon32 Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIcon32Name()
	 * @generated
	 * @ordered
	 */
	protected static final String ICON32_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIcon32Name() <em>Icon32 Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIcon32Name()
	 * @generated
	 * @ordered
	 */
	protected String icon32Name = ICON32_NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #isDefaultEntry() <em>Default Entry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDefaultEntry()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DEFAULT_ENTRY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDefaultEntry() <em>Default Entry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDefaultEntry()
	 * @generated
	 * @ordered
	 */
	protected boolean defaultEntry = DEFAULT_ENTRY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEntryLabel() <em>Entry Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryLabel()
	 * @generated
	 * @ordered
	 */
	protected AbstractString entryLabel = null;
	/**
	 * The cached value of the '{@link #getEntryShortDescription() <em>Entry Short Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryShortDescription()
	 * @generated
	 * @ordered
	 */
	protected AbstractString entryShortDescription = null;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected EntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getEntry();
	}

	/**
	 * Answer the 16x16 IIcon for this entry.
	 * @return IImage
	 */
	public ImageDescriptor getSmallIcon() {
		if (fIcon16 == null && getIcon16Name() != null) {
			try {
				fIcon16 = ImageDescriptor.createFromURL(new URL(getIcon16Name()));
			} catch (MalformedURLException e) {
				fIcon16 = ImageDescriptor.getMissingImageDescriptor();
			}
		}
		return fIcon16;
	}

	/**
	 * Answer the 32x32 IIcon for this entry.
	 * @return IImage
	 */
	public ImageDescriptor getLargeIcon() {
		if (fIcon32 == null && getIcon32Name() != null) {
			try {
				fIcon32 = ImageDescriptor.createFromURL(new URL(getIcon32Name()));
			} catch (MalformedURLException e) {
				fIcon32 = ImageDescriptor.getMissingImageDescriptor();				
			}
		}
		return fIcon32;
	}

	public String getLabel() {
		return getEntryLabel() != null ? getEntryLabel().getStringValue() : ""; //$NON-NLS-1$
	}

	public String getDescription() {
		return getEntryShortDescription() != null ? getEntryShortDescription().getStringValue() : null;
		// no short description is valid
	}

	public void setIcon16Name(String value) {
		fIcon16 = null;
		this.setIcon16NameGen(value);
	}
	public void setIcon32Name(String value) {
		fIcon32 = null;
		this.setIcon32NameGen(value);
	}

	public boolean isDefault() {
		return isDefaultEntry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIcon16Name() {
		return icon16Name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIcon32Name() {
		return icon32Name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getEntryLabel() {
		return entryLabel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEntryLabel(AbstractString newEntryLabel, NotificationChain msgs) {
		AbstractString oldEntryLabel = entryLabel;
		entryLabel = newEntryLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ENTRY_LABEL, oldEntryLabel, newEntryLabel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntryLabel(AbstractString newEntryLabel) {
		if (newEntryLabel != entryLabel) {
			NotificationChain msgs = null;
			if (entryLabel != null)
				msgs = ((InternalEObject)entryLabel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PalettePackage.ENTRY__ENTRY_LABEL, null, msgs);
			if (newEntryLabel != null)
				msgs = ((InternalEObject)newEntryLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PalettePackage.ENTRY__ENTRY_LABEL, null, msgs);
			msgs = basicSetEntryLabel(newEntryLabel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ENTRY_LABEL, newEntryLabel, newEntryLabel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractString getEntryShortDescription() {
		return entryShortDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEntryShortDescription(AbstractString newEntryShortDescription, NotificationChain msgs) {
		AbstractString oldEntryShortDescription = entryShortDescription;
		entryShortDescription = newEntryShortDescription;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION, oldEntryShortDescription, newEntryShortDescription);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntryShortDescription(AbstractString newEntryShortDescription) {
		if (newEntryShortDescription != entryShortDescription) {
			NotificationChain msgs = null;
			if (entryShortDescription != null)
				msgs = ((InternalEObject)entryShortDescription).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION, null, msgs);
			if (newEntryShortDescription != null)
				msgs = ((InternalEObject)newEntryShortDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION, null, msgs);
			msgs = basicSetEntryShortDescription(newEntryShortDescription, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION, newEntryShortDescription, newEntryShortDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.ENTRY__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
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
			case PalettePackage.ENTRY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.ENTRY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
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
			case PalettePackage.ENTRY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.ENTRY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.ENTRY__DEFAULT_ENTRY:
				return defaultEntry != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
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
			case PalettePackage.ENTRY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.ENTRY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
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
			case PalettePackage.ENTRY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.ENTRY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (icon16Name: ");
		result.append(icon16Name);
		result.append(", icon32Name: ");
		result.append(icon32Name);
		result.append(", defaultEntry: ");
		result.append(defaultEntry);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIcon16NameGen(String newIcon16Name) {
		String oldIcon16Name = icon16Name;
		icon16Name = newIcon16Name;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ICON16_NAME, oldIcon16Name, icon16Name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIcon32NameGen(String newIcon32Name) {
		String oldIcon32Name = icon32Name;
		icon32Name = newIcon32Name;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ICON32_NAME, oldIcon32Name, icon32Name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDefaultEntry() {
		return defaultEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultEntry(boolean newDefaultEntry) {
		boolean oldDefaultEntry = defaultEntry;
		defaultEntry = newDefaultEntry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__DEFAULT_ENTRY, oldDefaultEntry, defaultEntry));
	}

	/**
	 * @see org.eclipse.ve.internal.cde.palette.ICDEPaletteEntry#getEntry()
	 */
	public PaletteEntry getEntry() {
		PaletteEntry e = (PaletteEntry) createPaletteEntry();
		if (isDefaultEntry())
			((ICDEToolEntry) e).setDefaultEntry(true);
		e.setDescription(getDescription());
		e.setLabel(getLabel());
		e.setSmallIcon(getSmallIcon());
		ImageDescriptor large = getLargeIcon();
		e.setLargeIcon(large == null ? getSmallIcon() : large);
		return e;		
	}
	
	private static class CDEPaletteEntry extends PaletteEntry implements ICDEToolEntry {
		
		private boolean defaultEntry;
		/**
		 * Constructor for Entry.
		 * @param label
		 * @param shortDescription
		 */
		public CDEPaletteEntry() {
			super(null, null);
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

	}
	protected ICDEToolEntry createPaletteEntry() {
		return new CDEPaletteEntry();
	}

}
