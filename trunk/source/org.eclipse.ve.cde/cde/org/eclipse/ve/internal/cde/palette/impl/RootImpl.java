/**
 * <copyright>
 * </copyright>
 *
 * $Id: RootImpl.java,v 1.1 2005-06-20 23:54:40 rkulp Exp $
 */
package org.eclipse.ve.internal.cde.palette.impl;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cde.palette.AbstractToolEntry;
import org.eclipse.gef.palette.*;

import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;
import org.eclipse.ve.internal.cde.palette.Root;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.RootImpl#getDefEntry <em>Def Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RootImpl extends ContainerImpl implements Root {
	/**
	 * The cached value of the '{@link #getDefEntry() <em>Def Entry</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefEntry()
	 * @generated
	 * @ordered
	 */
	protected AbstractToolEntry defEntry = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getRoot();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractToolEntry getDefEntry() {
		if (defEntry != null && defEntry.eIsProxy()) {
			AbstractToolEntry oldDefEntry = defEntry;
			defEntry = (AbstractToolEntry)eResolveProxy((InternalEObject)defEntry);
			if (defEntry != oldDefEntry) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PalettePackage.ROOT__DEF_ENTRY, oldDefEntry, defEntry));
			}
		}
		return defEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractToolEntry basicGetDefEntry() {
		return defEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefEntry(AbstractToolEntry newDefEntry) {
		AbstractToolEntry oldDefEntry = defEntry;
		defEntry = newDefEntry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ROOT__DEF_ENTRY, oldDefEntry, defEntry));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.ROOT__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.ROOT__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.ROOT__CHILDREN:
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
			case PalettePackage.ROOT__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.ROOT__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.ROOT__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ROOT__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.ROOT__ID:
				return getId();
			case PalettePackage.ROOT__MODIFICATION:
				return getModification();
			case PalettePackage.ROOT__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.ROOT__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.ROOT__CHILDREN:
				return getChildren();
			case PalettePackage.ROOT__DEF_ENTRY:
				if (resolve) return getDefEntry();
				return basicGetDefEntry();
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
			case PalettePackage.ROOT__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.ROOT__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.ROOT__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ROOT__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.ROOT__ID:
				setId((String)newValue);
				return;
			case PalettePackage.ROOT__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.ROOT__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.ROOT__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.ROOT__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
				return;
			case PalettePackage.ROOT__DEF_ENTRY:
				setDefEntry((AbstractToolEntry)newValue);
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
			case PalettePackage.ROOT__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.ROOT__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.ROOT__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.ROOT__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.ROOT__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.ROOT__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.ROOT__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.ROOT__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.ROOT__CHILDREN:
				getChildren().clear();
				return;
			case PalettePackage.ROOT__DEF_ENTRY:
				setDefEntry((AbstractToolEntry)null);
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
			case PalettePackage.ROOT__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.ROOT__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.ROOT__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.ROOT__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.ROOT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.ROOT__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.ROOT__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.ROOT__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.ROOT__CHILDREN:
				return children != null && !children.isEmpty();
			case PalettePackage.ROOT__DEF_ENTRY:
				return defEntry != null;
		}
		return eDynamicIsSet(eFeature);
	}

	protected PaletteEntry createPaletteEntry() {
		return new PaletteRoot();
	}
	
	protected void configurePaletteEntry(PaletteEntry entry, Map entryToPaletteEntry) {
		super.configurePaletteEntry(entry, entryToPaletteEntry);
		// super configure is container, and container has already got all of the children. So the entire tree is built
		// at this point. The map will have the default entry key in it.
		if (getDefEntry() != null) {
			ToolEntry tentry = (ToolEntry) entryToPaletteEntry.get(getDefEntry());
			if (tentry != null)
				((PaletteRoot) entry).setDefaultEntry(tentry);
		}
	}

} //RootImpl
