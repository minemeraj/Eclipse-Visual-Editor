/**
 * <copyright>
 * </copyright>
 *
 * $Id: Order.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Order</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Order#getCart <em>Cart</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Order#getAccount <em>Account</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Order#getOrderId <em>Order Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getOrder()
 * @model
 * @generated
 */
public interface Order extends EObject {
	/**
	 * Returns the value of the '<em><b>Cart</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cart</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cart</em>' reference.
	 * @see #setCart(Cart)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getOrder_Cart()
	 * @model
	 * @generated
	 */
	Cart getCart();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Order#getCart <em>Cart</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cart</em>' reference.
	 * @see #getCart()
	 * @generated
	 */
	void setCart(Cart value);

	/**
	 * Returns the value of the '<em><b>Account</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Account</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Account</em>' reference.
	 * @see #setAccount(Account)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getOrder_Account()
	 * @model
	 * @generated
	 */
	Account getAccount();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Order#getAccount <em>Account</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Account</em>' reference.
	 * @see #getAccount()
	 * @generated
	 */
	void setAccount(Account value);

	/**
	 * Returns the value of the '<em><b>Order Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Order Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Order Id</em>' attribute.
	 * @see #setOrderId(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getOrder_OrderId()
	 * @model
	 * @generated
	 */
	String getOrderId();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Order#getOrderId <em>Order Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Order Id</em>' attribute.
	 * @see #getOrderId()
	 * @generated
	 */
	void setOrderId(String value);

} // Order
