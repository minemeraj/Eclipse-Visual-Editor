/**
 * <copyright>
 * </copyright>
 *
 * $Id: AppModel.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>App Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.AppModel#getGlobalSettings <em>Global Settings</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.AppModel#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.AppModel#getOrders <em>Orders</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAppModel()
 * @model
 * @generated
 */
public interface AppModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Global Settings</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Global Settings</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Global Settings</em>' reference.
	 * @see #setGlobalSettings(GlobalSettings)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAppModel_GlobalSettings()
	 * @model
	 * @generated
	 */
	GlobalSettings getGlobalSettings();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.AppModel#getGlobalSettings <em>Global Settings</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Global Settings</em>' reference.
	 * @see #getGlobalSettings()
	 * @generated
	 */
	void setGlobalSettings(GlobalSettings value);

	/**
	 * Returns the value of the '<em><b>Catalog</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Catalog</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Catalog</em>' reference.
	 * @see #setCatalog(Catalog)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAppModel_Catalog()
	 * @model
	 * @generated
	 */
	Catalog getCatalog();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.AppModel#getCatalog <em>Catalog</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Catalog</em>' reference.
	 * @see #getCatalog()
	 * @generated
	 */
	void setCatalog(Catalog value);

	/**
	 * Returns the value of the '<em><b>Orders</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Order}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Orders</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Orders</em>' reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAppModel_Orders()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Order"
	 * @generated
	 */
	EList getOrders();

} // AppModel
