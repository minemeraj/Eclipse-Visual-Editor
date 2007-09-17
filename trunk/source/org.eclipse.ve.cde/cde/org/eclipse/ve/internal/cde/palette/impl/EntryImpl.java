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
 *  $RCSfile: EntryImpl.java,v $
 *  $Revision: 1.12 $  $Date: 2007-09-17 14:17:13 $ 
 */
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ve.internal.cde.palette.*;

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
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#isVisible <em>Visible</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#isDefaultEntry <em>Default Entry</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#getModification <em>Modification</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#getEntryLabel <em>Entry Label</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.EntryImpl#getEntryShortDescription <em>Entry Short Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class EntryImpl extends EObjectImpl implements Entry {

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
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = true;

	/**
	 * The flag representing the value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final int VISIBLE_EFLAG = 1 << 8;

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
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getModification() <em>Modification</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModification()
	 * @generated
	 * @ordered
	 */
	protected static final Permissions MODIFICATION_EDEFAULT = Permissions.DEFAULT_LITERAL;

	/**
	 * The cached value of the '{@link #getModification() <em>Modification</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModification()
	 * @generated
	 * @ordered
	 */
	protected Permissions modification = MODIFICATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEntryLabel() <em>Entry Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryLabel()
	 * @generated
	 * @ordered
	 */
	protected AbstractString entryLabel;
	/**
	 * The cached value of the '{@link #getEntryShortDescription() <em>Entry Short Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryShortDescription()
	 * @generated
	 * @ordered
	 */
	protected AbstractString entryShortDescription;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected EntryImpl() {
		super();
		eFlags |= VISIBLE_EFLAG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PalettePackage.Literals.ENTRY;
	}

	/**
	 * Get the small icon as an ImageDescriptor.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ImageDescriptor getSmallIcon() {
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
	 * Get the large icon as an ImageDescriptor
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ImageDescriptor getLargeIcon() {
		if (fIcon32 == null && getIcon32Name() != null) {
			try {
				fIcon32 = ImageDescriptor.createFromURL(new URL(getIcon32Name()));
			} catch (MalformedURLException e) {
				fIcon32 = ImageDescriptor.getMissingImageDescriptor();				
			}
		}
		return fIcon32;
	}

	/**
	 * The resolved label.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected String getLabel() {
		return getEntryLabel() != null ? getEntryLabel().getStringValue() : ""; //$NON-NLS-1$
	}

	/**
	 * The resolved description.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected String getDescription() {
		// no short description is valid
		return getEntryShortDescription() != null ? getEntryShortDescription().getStringValue() : null;
	}

	public void setIcon16Name(String value) {
		fIcon16 = null;
		this.setIcon16NameGen(value);
	}
	public void setIcon32Name(String value) {
		fIcon32 = null;
		this.setIcon32NameGen(value);
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PalettePackage.ENTRY__ENTRY_LABEL:
				return basicSetEntryLabel(null, msgs);
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				return basicSetEntryShortDescription(null, msgs);
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
			case PalettePackage.ENTRY__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.ENTRY__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.ENTRY__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ENTRY__ID:
				return getId();
			case PalettePackage.ENTRY__MODIFICATION:
				return getModification();
			case PalettePackage.ENTRY__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PalettePackage.ENTRY__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.ENTRY__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.ENTRY__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ENTRY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.ENTRY__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
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
			case PalettePackage.ENTRY__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.ENTRY__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.ENTRY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.ENTRY__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.ENTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.ENTRY__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.ENTRY__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
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
			case PalettePackage.ENTRY__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.ENTRY__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.ENTRY__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
			case PalettePackage.ENTRY__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.ENTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.ENTRY__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.ENTRY__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.ENTRY__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (icon16Name: ");
		result.append(icon16Name);
		result.append(", icon32Name: ");
		result.append(icon32Name);
		result.append(", visible: ");
		result.append((eFlags & VISIBLE_EFLAG) != 0);
		result.append(", id: ");
		result.append(id);
		result.append(", modification: ");
		result.append(modification);
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
	public boolean isVisible() {
		return (eFlags & VISIBLE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVisible(boolean newVisible) {
		boolean oldVisible = (eFlags & VISIBLE_EFLAG) != 0;
		if (newVisible) eFlags |= VISIBLE_EFLAG; else eFlags &= ~VISIBLE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__VISIBLE, oldVisible, newVisible));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean isDefaultEntry() {
		return defaultEntry;
	}

	private boolean defaultEntry;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void setDefaultEntry(boolean newDefaultEntry) {
		defaultEntry = newDefaultEntry;
		
		// TODO when this gets deleted, delete eBasicSetContainer too.
	}
	
	protected void eBasicSetContainer(InternalEObject newContainer, int newContainerFeatureID) {
		super.eBasicSetContainer(newContainer, newContainerFeatureID);
		
		// This only makes sense when we are in  a palette directly (and not 
		// through a cat). This is old code that will go away eventually.
		// We will find the root and try to set it appropriately.
		if (!defaultEntry || !(this instanceof AbstractToolEntry))
			return;	// Shouldn't happen, but new root can only take an abstract tool entry as default.
		EObject container = eContainer();
		while (container != null) {
			if (container instanceof Root) {
				Root root = (Root) container;
				if (defaultEntry)
					root.setDefEntry((AbstractToolEntry) this);
				break;
			}
			container = container.eContainer();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Permissions getModification() {
		return modification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModification(Permissions newModification) {
		Permissions oldModification = modification;
		modification = newModification == null ? MODIFICATION_EDEFAULT : newModification;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__MODIFICATION, oldModification, modification));
	}

	public final PaletteEntry getEntry() {
		return getEntry(new HashMap());
	}
	
	/**
	 * Get the entry.
	 * @param entryToPaletteEntry The map of cde.Entry->PaletteEntry. This is used only during construction to find the created palette entry for a given CDE EMF Entry.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final PaletteEntry getEntry(Map entryToPaletteEntry) {
		PaletteEntry e = createPaletteEntry();
		entryToPaletteEntry.put(this, e);
		configurePaletteEntry(e, entryToPaletteEntry);
		return e;				
	}
	
	/**
	 * Subclasses should return the palette entry. After this configureEntry will be called.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract PaletteEntry createPaletteEntry();
	
	/**
	 * Now configure the palette entry. Subclasses should call super.configfurePaletteEntry.
	 * 
	 * @param entry
	 * @param entryToPaletteEntry TODO
	 * 
	 * @since 1.1.0
	 */
	protected void configurePaletteEntry(PaletteEntry entry, Map entryToPaletteEntry) {
		entry.setDescription(getDescription());
		entry.setLabel(getLabel());
		entry.setSmallIcon(getSmallIcon());
		ImageDescriptor large = getLargeIcon();
		entry.setLargeIcon(large == null ? getSmallIcon() : large);	
		entry.setVisible(isVisible());
		entry.setId(getId());
		if (getModification() != Permissions.DEFAULT_LITERAL) {
			switch (getModification().getValue()) {
				case Permissions.FULL:
					entry.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
					break;
				case Permissions.HIDE_ONLY:
					entry.setUserModificationPermission(PaletteEntry.PERMISSION_HIDE_ONLY);
					break;
				case Permissions.LIMITED:
					entry.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);
					break;
				case Permissions.NONE:
					entry.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
					break;
			}
		}
	}

}
