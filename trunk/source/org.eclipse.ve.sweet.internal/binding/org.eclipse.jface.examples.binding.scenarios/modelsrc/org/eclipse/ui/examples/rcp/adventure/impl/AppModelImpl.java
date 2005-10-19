/**
 * <copyright>
 * </copyright>
 *
 * $Id: AppModelImpl.java,v 1.1 2005-10-19 18:35:45 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.AppModel;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.GlobalSettings;
import org.eclipse.ui.examples.rcp.adventure.Order;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>App Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AppModelImpl#getGlobalSettings <em>Global Settings</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AppModelImpl#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AppModelImpl#getOrders <em>Orders</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AppModelImpl extends EObjectImpl implements AppModel {
	/**
	 * The cached value of the '{@link #getGlobalSettings() <em>Global Settings</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGlobalSettings()
	 * @generated
	 * @ordered
	 */
	protected GlobalSettings globalSettings = null;

	/**
	 * The cached value of the '{@link #getCatalog() <em>Catalog</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCatalog()
	 * @generated
	 * @ordered
	 */
	protected Catalog catalog = null;

	/**
	 * The cached value of the '{@link #getOrders() <em>Orders</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrders()
	 * @generated
	 * @ordered
	 */
	protected EList orders = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AppModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return AdventurePackage.eINSTANCE.getAppModel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GlobalSettings getGlobalSettings() {
		if (globalSettings != null && globalSettings.eIsProxy()) {
			GlobalSettings oldGlobalSettings = globalSettings;
			globalSettings = (GlobalSettings)eResolveProxy((InternalEObject)globalSettings);
			if (globalSettings != oldGlobalSettings) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.APP_MODEL__GLOBAL_SETTINGS, oldGlobalSettings, globalSettings));
			}
		}
		return globalSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GlobalSettings basicGetGlobalSettings() {
		return globalSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGlobalSettings(GlobalSettings newGlobalSettings) {
		GlobalSettings oldGlobalSettings = globalSettings;
		globalSettings = newGlobalSettings;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.APP_MODEL__GLOBAL_SETTINGS, oldGlobalSettings, globalSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Catalog getCatalog() {
		if (catalog != null && catalog.eIsProxy()) {
			Catalog oldCatalog = catalog;
			catalog = (Catalog)eResolveProxy((InternalEObject)catalog);
			if (catalog != oldCatalog) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.APP_MODEL__CATALOG, oldCatalog, catalog));
			}
		}
		return catalog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Catalog basicGetCatalog() {
		return catalog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCatalog(Catalog newCatalog) {
		Catalog oldCatalog = catalog;
		catalog = newCatalog;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.APP_MODEL__CATALOG, oldCatalog, catalog));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getOrders() {
		if (orders == null) {
			orders = new EObjectResolvingEList(Order.class, this, AdventurePackage.APP_MODEL__ORDERS);
		}
		return orders;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case AdventurePackage.APP_MODEL__GLOBAL_SETTINGS:
				if (resolve) return getGlobalSettings();
				return basicGetGlobalSettings();
			case AdventurePackage.APP_MODEL__CATALOG:
				if (resolve) return getCatalog();
				return basicGetCatalog();
			case AdventurePackage.APP_MODEL__ORDERS:
				return getOrders();
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
			case AdventurePackage.APP_MODEL__GLOBAL_SETTINGS:
				setGlobalSettings((GlobalSettings)newValue);
				return;
			case AdventurePackage.APP_MODEL__CATALOG:
				setCatalog((Catalog)newValue);
				return;
			case AdventurePackage.APP_MODEL__ORDERS:
				getOrders().clear();
				getOrders().addAll((Collection)newValue);
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
			case AdventurePackage.APP_MODEL__GLOBAL_SETTINGS:
				setGlobalSettings((GlobalSettings)null);
				return;
			case AdventurePackage.APP_MODEL__CATALOG:
				setCatalog((Catalog)null);
				return;
			case AdventurePackage.APP_MODEL__ORDERS:
				getOrders().clear();
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
			case AdventurePackage.APP_MODEL__GLOBAL_SETTINGS:
				return globalSettings != null;
			case AdventurePackage.APP_MODEL__CATALOG:
				return catalog != null;
			case AdventurePackage.APP_MODEL__ORDERS:
				return orders != null && !orders.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //AppModelImpl
