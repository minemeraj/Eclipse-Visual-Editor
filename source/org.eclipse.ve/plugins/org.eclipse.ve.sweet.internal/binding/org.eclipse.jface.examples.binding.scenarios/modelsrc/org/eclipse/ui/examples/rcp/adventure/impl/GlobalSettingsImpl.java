/**
 * <copyright>
 * </copyright>
 *
 * $Id: GlobalSettingsImpl.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.GlobalSettings;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Global Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.GlobalSettingsImpl#isDisconnectedMode <em>Disconnected Mode</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.GlobalSettingsImpl#isCacheCatalog <em>Cache Catalog</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.GlobalSettingsImpl#isServerAddressModifiable <em>Server Address Modifiable</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.GlobalSettingsImpl#isClearCache <em>Clear Cache</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GlobalSettingsImpl extends EObjectImpl implements GlobalSettings {
	/**
	 * The default value of the '{@link #isDisconnectedMode() <em>Disconnected Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDisconnectedMode()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DISCONNECTED_MODE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDisconnectedMode() <em>Disconnected Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDisconnectedMode()
	 * @generated
	 * @ordered
	 */
	protected boolean disconnectedMode = DISCONNECTED_MODE_EDEFAULT;

	/**
	 * The default value of the '{@link #isCacheCatalog() <em>Cache Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCacheCatalog()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CACHE_CATALOG_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isCacheCatalog() <em>Cache Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCacheCatalog()
	 * @generated
	 * @ordered
	 */
	protected boolean cacheCatalog = CACHE_CATALOG_EDEFAULT;

	/**
	 * The default value of the '{@link #isServerAddressModifiable() <em>Server Address Modifiable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isServerAddressModifiable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SERVER_ADDRESS_MODIFIABLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isServerAddressModifiable() <em>Server Address Modifiable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isServerAddressModifiable()
	 * @generated
	 * @ordered
	 */
	protected boolean serverAddressModifiable = SERVER_ADDRESS_MODIFIABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isClearCache() <em>Clear Cache</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isClearCache()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CLEAR_CACHE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isClearCache() <em>Clear Cache</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isClearCache()
	 * @generated
	 * @ordered
	 */
	protected boolean clearCache = CLEAR_CACHE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GlobalSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return AdventurePackage.eINSTANCE.getGlobalSettings();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDisconnectedMode() {
		return disconnectedMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDisconnectedMode(boolean newDisconnectedMode) {
		boolean oldDisconnectedMode = disconnectedMode;
		disconnectedMode = newDisconnectedMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.GLOBAL_SETTINGS__DISCONNECTED_MODE, oldDisconnectedMode, disconnectedMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isCacheCatalog() {
		return cacheCatalog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCacheCatalog(boolean newCacheCatalog) {
		boolean oldCacheCatalog = cacheCatalog;
		cacheCatalog = newCacheCatalog;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.GLOBAL_SETTINGS__CACHE_CATALOG, oldCacheCatalog, cacheCatalog));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isServerAddressModifiable() {
		return serverAddressModifiable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServerAddressModifiable(boolean newServerAddressModifiable) {
		boolean oldServerAddressModifiable = serverAddressModifiable;
		serverAddressModifiable = newServerAddressModifiable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.GLOBAL_SETTINGS__SERVER_ADDRESS_MODIFIABLE, oldServerAddressModifiable, serverAddressModifiable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isClearCache() {
		return clearCache;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setClearCache(boolean newClearCache) {
		boolean oldClearCache = clearCache;
		clearCache = newClearCache;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.GLOBAL_SETTINGS__CLEAR_CACHE, oldClearCache, clearCache));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case AdventurePackage.GLOBAL_SETTINGS__DISCONNECTED_MODE:
				return isDisconnectedMode() ? Boolean.TRUE : Boolean.FALSE;
			case AdventurePackage.GLOBAL_SETTINGS__CACHE_CATALOG:
				return isCacheCatalog() ? Boolean.TRUE : Boolean.FALSE;
			case AdventurePackage.GLOBAL_SETTINGS__SERVER_ADDRESS_MODIFIABLE:
				return isServerAddressModifiable() ? Boolean.TRUE : Boolean.FALSE;
			case AdventurePackage.GLOBAL_SETTINGS__CLEAR_CACHE:
				return isClearCache() ? Boolean.TRUE : Boolean.FALSE;
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
			case AdventurePackage.GLOBAL_SETTINGS__DISCONNECTED_MODE:
				setDisconnectedMode(((Boolean)newValue).booleanValue());
				return;
			case AdventurePackage.GLOBAL_SETTINGS__CACHE_CATALOG:
				setCacheCatalog(((Boolean)newValue).booleanValue());
				return;
			case AdventurePackage.GLOBAL_SETTINGS__SERVER_ADDRESS_MODIFIABLE:
				setServerAddressModifiable(((Boolean)newValue).booleanValue());
				return;
			case AdventurePackage.GLOBAL_SETTINGS__CLEAR_CACHE:
				setClearCache(((Boolean)newValue).booleanValue());
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
			case AdventurePackage.GLOBAL_SETTINGS__DISCONNECTED_MODE:
				setDisconnectedMode(DISCONNECTED_MODE_EDEFAULT);
				return;
			case AdventurePackage.GLOBAL_SETTINGS__CACHE_CATALOG:
				setCacheCatalog(CACHE_CATALOG_EDEFAULT);
				return;
			case AdventurePackage.GLOBAL_SETTINGS__SERVER_ADDRESS_MODIFIABLE:
				setServerAddressModifiable(SERVER_ADDRESS_MODIFIABLE_EDEFAULT);
				return;
			case AdventurePackage.GLOBAL_SETTINGS__CLEAR_CACHE:
				setClearCache(CLEAR_CACHE_EDEFAULT);
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
			case AdventurePackage.GLOBAL_SETTINGS__DISCONNECTED_MODE:
				return disconnectedMode != DISCONNECTED_MODE_EDEFAULT;
			case AdventurePackage.GLOBAL_SETTINGS__CACHE_CATALOG:
				return cacheCatalog != CACHE_CATALOG_EDEFAULT;
			case AdventurePackage.GLOBAL_SETTINGS__SERVER_ADDRESS_MODIFIABLE:
				return serverAddressModifiable != SERVER_ADDRESS_MODIFIABLE_EDEFAULT;
			case AdventurePackage.GLOBAL_SETTINGS__CLEAR_CACHE:
				return clearCache != CLEAR_CACHE_EDEFAULT;
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
		result.append(" (disconnectedMode: ");
		result.append(disconnectedMode);
		result.append(", cacheCatalog: ");
		result.append(cacheCatalog);
		result.append(", serverAddressModifiable: ");
		result.append(serverAddressModifiable);
		result.append(", clearCache: ");
		result.append(clearCache);
		result.append(')');
		return result.toString();
	}

} //GlobalSettingsImpl
