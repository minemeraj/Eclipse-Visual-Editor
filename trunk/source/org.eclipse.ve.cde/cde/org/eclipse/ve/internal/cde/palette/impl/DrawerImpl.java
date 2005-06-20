/**
 * <copyright>
 * </copyright>
 *
 * $Id: DrawerImpl.java,v 1.1 2005-06-20 23:54:40 rkulp Exp $
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

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;

import org.eclipse.ve.internal.cde.palette.Drawer;
import org.eclipse.ve.internal.cde.palette.InitialState;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Permissions;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Drawer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.impl.DrawerImpl#getInitialState <em>Initial State</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DrawerImpl extends ContainerImpl implements Drawer {
	/**
	 * The default value of the '{@link #getInitialState() <em>Initial State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialState()
	 * @generated
	 * @ordered
	 */
	protected static final InitialState INITIAL_STATE_EDEFAULT = InitialState.CLOSED_LITERAL;

	/**
	 * The cached value of the '{@link #getInitialState() <em>Initial State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialState()
	 * @generated
	 * @ordered
	 */
	protected InitialState initialState = INITIAL_STATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DrawerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getDrawer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InitialState getInitialState() {
		return initialState;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitialState(InitialState newInitialState) {
		InitialState oldInitialState = initialState;
		initialState = newInitialState == null ? INITIAL_STATE_EDEFAULT : newInitialState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.DRAWER__INITIAL_STATE, oldInitialState, initialState));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case PalettePackage.DRAWER__ENTRY_LABEL:
					return basicSetEntryLabel(null, msgs);
				case PalettePackage.DRAWER__ENTRY_SHORT_DESCRIPTION:
					return basicSetEntryShortDescription(null, msgs);
				case PalettePackage.DRAWER__CHILDREN:
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
			case PalettePackage.DRAWER__ICON16_NAME:
				return getIcon16Name();
			case PalettePackage.DRAWER__ICON32_NAME:
				return getIcon32Name();
			case PalettePackage.DRAWER__VISIBLE:
				return isVisible() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.DRAWER__DEFAULT_ENTRY:
				return isDefaultEntry() ? Boolean.TRUE : Boolean.FALSE;
			case PalettePackage.DRAWER__ID:
				return getId();
			case PalettePackage.DRAWER__MODIFICATION:
				return getModification();
			case PalettePackage.DRAWER__ENTRY_LABEL:
				return getEntryLabel();
			case PalettePackage.DRAWER__ENTRY_SHORT_DESCRIPTION:
				return getEntryShortDescription();
			case PalettePackage.DRAWER__CHILDREN:
				return getChildren();
			case PalettePackage.DRAWER__INITIAL_STATE:
				return getInitialState();
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
			case PalettePackage.DRAWER__ICON16_NAME:
				setIcon16Name((String)newValue);
				return;
			case PalettePackage.DRAWER__ICON32_NAME:
				setIcon32Name((String)newValue);
				return;
			case PalettePackage.DRAWER__VISIBLE:
				setVisible(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.DRAWER__DEFAULT_ENTRY:
				setDefaultEntry(((Boolean)newValue).booleanValue());
				return;
			case PalettePackage.DRAWER__ID:
				setId((String)newValue);
				return;
			case PalettePackage.DRAWER__MODIFICATION:
				setModification((Permissions)newValue);
				return;
			case PalettePackage.DRAWER__ENTRY_LABEL:
				setEntryLabel((AbstractString)newValue);
				return;
			case PalettePackage.DRAWER__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)newValue);
				return;
			case PalettePackage.DRAWER__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection)newValue);
				return;
			case PalettePackage.DRAWER__INITIAL_STATE:
				setInitialState((InitialState)newValue);
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
			case PalettePackage.DRAWER__ICON16_NAME:
				setIcon16Name(ICON16_NAME_EDEFAULT);
				return;
			case PalettePackage.DRAWER__ICON32_NAME:
				setIcon32Name(ICON32_NAME_EDEFAULT);
				return;
			case PalettePackage.DRAWER__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.DRAWER__DEFAULT_ENTRY:
				setDefaultEntry(DEFAULT_ENTRY_EDEFAULT);
				return;
			case PalettePackage.DRAWER__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.DRAWER__MODIFICATION:
				setModification(MODIFICATION_EDEFAULT);
				return;
			case PalettePackage.DRAWER__ENTRY_LABEL:
				setEntryLabel((AbstractString)null);
				return;
			case PalettePackage.DRAWER__ENTRY_SHORT_DESCRIPTION:
				setEntryShortDescription((AbstractString)null);
				return;
			case PalettePackage.DRAWER__CHILDREN:
				getChildren().clear();
				return;
			case PalettePackage.DRAWER__INITIAL_STATE:
				setInitialState(INITIAL_STATE_EDEFAULT);
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
			case PalettePackage.DRAWER__ICON16_NAME:
				return ICON16_NAME_EDEFAULT == null ? icon16Name != null : !ICON16_NAME_EDEFAULT.equals(icon16Name);
			case PalettePackage.DRAWER__ICON32_NAME:
				return ICON32_NAME_EDEFAULT == null ? icon32Name != null : !ICON32_NAME_EDEFAULT.equals(icon32Name);
			case PalettePackage.DRAWER__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.DRAWER__DEFAULT_ENTRY:
				return isDefaultEntry() != DEFAULT_ENTRY_EDEFAULT;
			case PalettePackage.DRAWER__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.DRAWER__MODIFICATION:
				return modification != MODIFICATION_EDEFAULT;
			case PalettePackage.DRAWER__ENTRY_LABEL:
				return entryLabel != null;
			case PalettePackage.DRAWER__ENTRY_SHORT_DESCRIPTION:
				return entryShortDescription != null;
			case PalettePackage.DRAWER__CHILDREN:
				return children != null && !children.isEmpty();
			case PalettePackage.DRAWER__INITIAL_STATE:
				return initialState != INITIAL_STATE_EDEFAULT;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (initialState: ");
		result.append(initialState);
		result.append(')');
		return result.toString();
	}

	protected PaletteEntry createPaletteEntry() {
		return new PaletteDrawer(getLabel());
	}
	
	protected void configurePaletteEntry(PaletteEntry entry, Map entryToPaletteEntry) {
		super.configurePaletteEntry(entry, entryToPaletteEntry);
		PaletteDrawer d = (PaletteDrawer) entry;
		switch (getInitialState().getValue()) {
			case InitialState.CLOSED:
				d.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
				break;
			case InitialState.OPEN:
				d.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
				break;
			case InitialState.PINNED_OPEN:
				d.setInitialState(PaletteDrawer.INITIAL_STATE_PINNED_OPEN);
				break;
		}
	}


} //DrawerImpl
