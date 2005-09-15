/**
 * <copyright>
 * </copyright>
 *
 * $Id: SeparatorImpl.java,v 1.2 2005-09-15 21:27:15 rkulp Exp $
 */
package org.eclipse.ve.internal.cde.palette.impl;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;

import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;
import org.eclipse.ve.internal.cde.palette.Separator;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Separator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class SeparatorImpl extends EntryImpl implements Separator {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SeparatorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getSeparator();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.SEPARATOR__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.SEPARATOR__ENTRY_SHORT_DESCRIPTION:
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
			case PalettePackage.SEPARATOR__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.SEPARATOR__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.SEPARATOR__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.SEPARATOR__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.SEPARATOR__ID:
				return getId();
			case PalettePackage.SEPARATOR__MODIFICATION:
				return getModification();
			case PalettePackage.SEPARATOR__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.SEPARATOR__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
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
			case PalettePackage.SEPARATOR__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.SEPARATOR__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.SEPARATOR__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.SEPARATOR__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.SEPARATOR__ID:
				setId((String)newValue);
				return;
			case PalettePackage.SEPARATOR__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.SEPARATOR__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.SEPARATOR__ENTRY_SHORT_DESCRIPTION:
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
			case PalettePackage.SEPARATOR__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.SEPARATOR__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.SEPARATOR__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.SEPARATOR__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.SEPARATOR__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.SEPARATOR__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.SEPARATOR__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.SEPARATOR__ENTRY_SHORT_DESCRIPTION:
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
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case PalettePackage.SEPARATOR__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.SEPARATOR__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.SEPARATOR__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
			case PalettePackage.SEPARATOR__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.SEPARATOR__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.SEPARATOR__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.SEPARATOR__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.SEPARATOR__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
		}
		return eDynamicIsSet(eFeature);
	}

	protected PaletteEntry createPaletteEntry() {
		return new PaletteSeparator();
	}

} //SeparatorImpl
