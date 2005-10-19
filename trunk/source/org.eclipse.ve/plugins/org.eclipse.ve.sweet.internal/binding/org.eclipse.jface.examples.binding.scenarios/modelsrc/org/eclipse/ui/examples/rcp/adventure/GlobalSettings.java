/**
 * <copyright>
 * </copyright>
 *
 * $Id: GlobalSettings.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Global Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isDisconnectedMode <em>Disconnected Mode</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isCacheCatalog <em>Cache Catalog</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isServerAddressModifiable <em>Server Address Modifiable</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isClearCache <em>Clear Cache</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getGlobalSettings()
 * @model
 * @generated
 */
public interface GlobalSettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Disconnected Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Disconnected Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Disconnected Mode</em>' attribute.
	 * @see #setDisconnectedMode(boolean)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getGlobalSettings_DisconnectedMode()
	 * @model
	 * @generated
	 */
	boolean isDisconnectedMode();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isDisconnectedMode <em>Disconnected Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Disconnected Mode</em>' attribute.
	 * @see #isDisconnectedMode()
	 * @generated
	 */
	void setDisconnectedMode(boolean value);

	/**
	 * Returns the value of the '<em><b>Cache Catalog</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cache Catalog</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cache Catalog</em>' attribute.
	 * @see #setCacheCatalog(boolean)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getGlobalSettings_CacheCatalog()
	 * @model default="true"
	 * @generated
	 */
	boolean isCacheCatalog();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isCacheCatalog <em>Cache Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cache Catalog</em>' attribute.
	 * @see #isCacheCatalog()
	 * @generated
	 */
	void setCacheCatalog(boolean value);

	/**
	 * Returns the value of the '<em><b>Server Address Modifiable</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Server Address Modifiable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Server Address Modifiable</em>' attribute.
	 * @see #setServerAddressModifiable(boolean)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getGlobalSettings_ServerAddressModifiable()
	 * @model default="true"
	 * @generated
	 */
	boolean isServerAddressModifiable();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isServerAddressModifiable <em>Server Address Modifiable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Server Address Modifiable</em>' attribute.
	 * @see #isServerAddressModifiable()
	 * @generated
	 */
	void setServerAddressModifiable(boolean value);

	/**
	 * Returns the value of the '<em><b>Clear Cache</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Clear Cache</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Clear Cache</em>' attribute.
	 * @see #setClearCache(boolean)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getGlobalSettings_ClearCache()
	 * @model
	 * @generated
	 */
	boolean isClearCache();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isClearCache <em>Clear Cache</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Clear Cache</em>' attribute.
	 * @see #isClearCache()
	 * @generated
	 */
	void setClearCache(boolean value);

} // GlobalSettings
