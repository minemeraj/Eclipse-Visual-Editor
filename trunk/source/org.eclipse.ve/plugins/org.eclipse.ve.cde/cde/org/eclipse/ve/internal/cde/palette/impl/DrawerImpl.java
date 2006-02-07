/**
 * <copyright>
 * </copyright>
 *
 * $Id: DrawerImpl.java,v 1.4 2006-02-07 17:21:33 rkulp Exp $
 */
package org.eclipse.ve.internal.cde.palette.impl;

import java.util.Map;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;

import org.eclipse.ve.internal.cde.palette.Drawer;
import org.eclipse.ve.internal.cde.palette.InitialState;
import org.eclipse.ve.internal.cde.palette.PalettePackage;


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
		return PalettePackage.Literals.DRAWER;
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.DRAWER__INITIAL_STATE:
				return getInitialState();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PalettePackage.DRAWER__INITIAL_STATE:
				setInitialState((InitialState)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case PalettePackage.DRAWER__INITIAL_STATE:
				setInitialState(INITIAL_STATE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PalettePackage.DRAWER__INITIAL_STATE:
				return initialState != INITIAL_STATE_EDEFAULT;
		}
		return super.eIsSet(featureID);
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
		return new PaletteDrawer(getLabel(), getSmallIcon());
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
