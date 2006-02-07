/**
 * <copyright>
 * </copyright>
 *
 * $Id: RootImpl.java,v 1.3 2006-02-07 17:21:33 rkulp Exp $
 */
package org.eclipse.ve.internal.cde.palette.impl;

import java.util.Map;

import org.eclipse.emf.common.notify.Notification;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;


import org.eclipse.ve.internal.cde.palette.AbstractToolEntry;
import org.eclipse.gef.palette.*;

import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Root;


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
		return PalettePackage.Literals.ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractToolEntry getDefEntry() {
		if (defEntry != null && defEntry.eIsProxy()) {
			InternalEObject oldDefEntry = (InternalEObject)defEntry;
			defEntry = (AbstractToolEntry)eResolveProxy(oldDefEntry);
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.ROOT__DEF_ENTRY:
				if (resolve) return getDefEntry();
				return basicGetDefEntry();
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
			case PalettePackage.ROOT__DEF_ENTRY:
				setDefEntry((AbstractToolEntry)newValue);
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
			case PalettePackage.ROOT__DEF_ENTRY:
				setDefEntry((AbstractToolEntry)null);
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
			case PalettePackage.ROOT__DEF_ENTRY:
				return defEntry != null;
		}
		return super.eIsSet(featureID);
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
