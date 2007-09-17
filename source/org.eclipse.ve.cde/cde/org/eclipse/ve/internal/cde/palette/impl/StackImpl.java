/**
 * <copyright>
 * </copyright>
 *
 * $Id: StackImpl.java,v 1.5 2007-09-17 14:17:13 srobenalt Exp $
 */
package org.eclipse.ve.internal.cde.palette.impl;

import java.util.Map;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteStack;

import org.eclipse.ve.internal.cde.palette.Entry;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Stack;


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
	protected Entry activeEntry;

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
	@Override
	protected EClass eStaticClass() {
		return PalettePackage.Literals.STACK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entry getActiveEntry() {
		if (activeEntry != null && activeEntry.eIsProxy()) {
			InternalEObject oldActiveEntry = (InternalEObject)activeEntry;
			activeEntry = (Entry)eResolveProxy(oldActiveEntry);
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.STACK__ACTIVE_ENTRY:
				if (resolve) return getActiveEntry();
				return basicGetActiveEntry();
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
			case PalettePackage.STACK__ACTIVE_ENTRY:
				setActiveEntry((Entry)newValue);
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
			case PalettePackage.STACK__ACTIVE_ENTRY:
				setActiveEntry((Entry)null);
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
			case PalettePackage.STACK__ACTIVE_ENTRY:
				return activeEntry != null;
		}
		return super.eIsSet(featureID);
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
