/**
 * <copyright>
 * </copyright>
 *
 * $Id: AdventureFactory.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage
 * @generated
 */
public interface AdventureFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AdventureFactory eINSTANCE = new org.eclipse.ui.examples.rcp.adventure.impl.AdventureFactoryImpl();

	/**
	 * Returns a new object of class '<em>Catalog</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Catalog</em>'.
	 * @generated
	 */
	Catalog createCatalog();

	/**
	 * Returns a new object of class '<em>Category</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Category</em>'.
	 * @generated
	 */
	Category createCategory();

	/**
	 * Returns a new object of class '<em>Adventure</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Adventure</em>'.
	 * @generated
	 */
	Adventure createAdventure();

	/**
	 * Returns a new object of class '<em>Activity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Activity</em>'.
	 * @generated
	 */
	Activity createActivity();

	/**
	 * Returns a new object of class '<em>Lodging</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Lodging</em>'.
	 * @generated
	 */
	Lodging createLodging();

	/**
	 * Returns a new object of class '<em>Transportation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Transportation</em>'.
	 * @generated
	 */
	Transportation createTransportation();

	/**
	 * Returns a new object of class '<em>Signon</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Signon</em>'.
	 * @generated
	 */
	Signon createSignon();

	/**
	 * Returns a new object of class '<em>Account</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Account</em>'.
	 * @generated
	 */
	Account createAccount();

	/**
	 * Returns a new object of class '<em>Cart</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cart</em>'.
	 * @generated
	 */
	Cart createCart();

	/**
	 * Returns a new object of class '<em>Cart Activity Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cart Activity Entry</em>'.
	 * @generated
	 */
	CartActivityEntry createCartActivityEntry();

	/**
	 * Returns a new object of class '<em>Global Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Global Settings</em>'.
	 * @generated
	 */
	GlobalSettings createGlobalSettings();

	/**
	 * Returns a new object of class '<em>Order</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Order</em>'.
	 * @generated
	 */
	Order createOrder();

	/**
	 * Returns a new object of class '<em>App Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>App Model</em>'.
	 * @generated
	 */
	AppModel createAppModel();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AdventurePackage getAdventurePackage();

} //AdventureFactory
