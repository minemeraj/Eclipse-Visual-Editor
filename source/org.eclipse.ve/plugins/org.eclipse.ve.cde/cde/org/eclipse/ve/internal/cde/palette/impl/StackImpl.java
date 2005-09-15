/**
 * <copyright>
 * </copyright>
 *
 * $Id: StackImpl.java,v 1.2 2005-09-15 21:27:15 rkulp Exp $
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
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteStack;

import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;
import org.eclipse.ve.internal.cde.palette.Stack;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Stack</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.StackImpl#getActiveEntry <em>Active Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StackImpl extends ContainerImpl implements Stack {
	/**
	 * The cached value of the '{@link #getActiveEntry() <em>Active Entry</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActiveEntry()
	 * @generated
	 * @ordered
	 */
	protected Entry activeEntry = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StackImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getStack();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entry getActiveEntry() {
		if (activeEntry != null && activeEntry.eIsProxy()) {
			Entry oldActiveEntry = activeEntry;
			activeEntry = (Entry)eResolveProxy((InternalEObject)activeEntry);
			if (activeEntry != oldActiveEntry) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PalettePackage.STACK__ACTIVE_ENTRY, oldActiveEntry, activeEntry));
			}
		}
		return activeEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entry basicGetActiveEntry() {
		return activeEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActiveEntry(Entry newActiveEntry) {
		Entry oldActiveEntry = activeEntry;
		activeEntry = newActiveEntry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.STACK__ACTIVE_ENTRY, oldActiveEntry, activeEntry));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.STACK__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.STACK__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.STACK__CHILDREN:
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
			case PalettePackage.STACK__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.STACK__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.STACK__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.STACK__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.STACK__ID:
				return getId();
			case PalettePackage.STACK__MODIFICATION:
				return getModification();
			case PalettePackage.STACK__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.STACK__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.STACK__CHILDREN:
				return getChildren();
			case PalettePackage.STACK__ACTIVE_ENTRY:
				if (resolve) return getActiveEntry();
				return basicGetActiveEntry();
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
			case PalettePackage.STACK__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.STACK__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.STACK__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.STACK__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.STACK__ID:
				setId((String)newValue);
				return;
			case PalettePackage.STACK__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.STACK__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.STACK__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.STACK__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
				return;
			case PalettePackage.STACK__ACTIVE_ENTRY:
				setActiveEntry((Entry)newValue);
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
			case PalettePackage.STACK__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.STACK__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.STACK__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.STACK__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.STACK__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.STACK__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.STACK__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.STACK__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.STACK__CHILDREN:
				getChildren().clear();
				return;
			case PalettePackage.STACK__ACTIVE_ENTRY:
				setActiveEntry((Entry)null);
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
			case PalettePackage.STACK__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.STACK__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.STACK__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
			case PalettePackage.STACK__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.STACK__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.STACK__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.STACK__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.STACK__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.STACK__CHILDREN:
				return children != null && !children.isEmpty();
			case PalettePackage.STACK__ACTIVE_ENTRY:
				return activeEntry != null;
		}
		return eDynamicIsSet(eFeature);
	}

	protected PaletteEntry createPaletteEntry() {
		return new PaletteStack(getLabel(), getDescription(), getSmallIcon());
	}
	
	protected void configurePaletteEntry(PaletteEntry entry, Map entryToPaletteEntry) {
		super.configurePaletteEntry(entry, entryToPaletteEntry);
		// super.configure would of gotton all of the children, so active entry will be in the map.
		if (getActiveEntry() != null) {
			PaletteEntry ae = (PaletteEntry) entryToPaletteEntry.get(getActiveEntry());
			if (ae != null)
				((PaletteStack) ae).setActiveEntry(ae);
		}
	}

} //StackImpl
