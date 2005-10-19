/**
 * <copyright>
 * </copyright>
 *
 * $Id: Cart.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import java.util.Calendar;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cart</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getAdventure <em>Adventure</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getAdventureDays <em>Adventure Days</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getHeadCount <em>Head Count</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodging <em>Lodging</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingDays <em>Lodging Days</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingRoomCount <em>Lodging Room Count</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingTotal <em>Lodging Total</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getActivities <em>Activities</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getActivityTotal <em>Activity Total</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getDepartureDate <em>Departure Date</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getTransportationTotal <em>Transportation Total</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getTotalPrice <em>Total Price</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getDepartureFlight <em>Departure Flight</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Cart#getReturnFlight <em>Return Flight</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart()
 * @model
 * @generated
 */
public interface Cart extends EObject {
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
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_Catalog()
	 * @model
	 * @generated
	 */
	Catalog getCatalog();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getCatalog <em>Catalog</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Catalog</em>' reference.
	 * @see #getCatalog()
	 * @generated
	 */
	void setCatalog(Catalog value);

	/**
	 * Returns the value of the '<em><b>Adventure</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adventure</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Adventure</em>' reference.
	 * @see #setAdventure(Adventure)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_Adventure()
	 * @model
	 * @generated
	 */
	Adventure getAdventure();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getAdventure <em>Adventure</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Adventure</em>' reference.
	 * @see #getAdventure()
	 * @generated
	 */
	void setAdventure(Adventure value);

	/**
	 * Returns the value of the '<em><b>Adventure Days</b></em>' attribute.
	 * The default value is <code>"3"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adventure Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Adventure Days</em>' attribute.
	 * @see #setAdventureDays(int)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_AdventureDays()
	 * @model default="3"
	 * @generated
	 */
	int getAdventureDays();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getAdventureDays <em>Adventure Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Adventure Days</em>' attribute.
	 * @see #getAdventureDays()
	 * @generated
	 */
	void setAdventureDays(int value);

	/**
	 * Returns the value of the '<em><b>Head Count</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Head Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Head Count</em>' attribute.
	 * @see #setHeadCount(int)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_HeadCount()
	 * @model default="1"
	 * @generated
	 */
	int getHeadCount();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getHeadCount <em>Head Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Head Count</em>' attribute.
	 * @see #getHeadCount()
	 * @generated
	 */
	void setHeadCount(int value);

	/**
	 * Returns the value of the '<em><b>Lodging</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lodging</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lodging</em>' reference.
	 * @see #setLodging(Lodging)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_Lodging()
	 * @model
	 * @generated
	 */
	Lodging getLodging();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodging <em>Lodging</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lodging</em>' reference.
	 * @see #getLodging()
	 * @generated
	 */
	void setLodging(Lodging value);

	/**
	 * Returns the value of the '<em><b>Lodging Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lodging Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lodging Days</em>' attribute.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_LodgingDays()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	int getLodgingDays();

	/**
	 * Returns the value of the '<em><b>Lodging Room Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lodging Room Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lodging Room Count</em>' attribute.
	 * @see #setLodgingRoomCount(int)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_LodgingRoomCount()
	 * @model
	 * @generated
	 */
	int getLodgingRoomCount();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingRoomCount <em>Lodging Room Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lodging Room Count</em>' attribute.
	 * @see #getLodgingRoomCount()
	 * @generated
	 */
	void setLodgingRoomCount(int value);

	/**
	 * Returns the value of the '<em><b>Lodging Total</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lodging Total</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lodging Total</em>' attribute.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_LodgingTotal()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	double getLodgingTotal();

	/**
	 * Returns the value of the '<em><b>Activities</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.CartActivityEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activities</em>' containment reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_Activities()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.CartActivityEntry" containment="true"
	 * @generated
	 */
	EList getActivities();

	/**
	 * Returns the value of the '<em><b>Activity Total</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activity Total</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activity Total</em>' attribute.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_ActivityTotal()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	double getActivityTotal();

	/**
	 * Returns the value of the '<em><b>Departure Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Departure Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Departure Date</em>' attribute.
	 * @see #setDepartureDate(Calendar)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_DepartureDate()
	 * @model dataType="org.eclipse.ui.examples.rcp.adventure.JavaCalendar"
	 * @generated
	 */
	Calendar getDepartureDate();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getDepartureDate <em>Departure Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Departure Date</em>' attribute.
	 * @see #getDepartureDate()
	 * @generated
	 */
	void setDepartureDate(Calendar value);

	/**
	 * Returns the value of the '<em><b>Transportation Total</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transportation Total</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transportation Total</em>' attribute.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_TransportationTotal()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	double getTransportationTotal();

	/**
	 * Returns the value of the '<em><b>Total Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Total Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Total Price</em>' attribute.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_TotalPrice()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	double getTotalPrice();

	/**
	 * Returns the value of the '<em><b>Departure Flight</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Departure Flight</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Departure Flight</em>' reference.
	 * @see #setDepartureFlight(Transportation)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_DepartureFlight()
	 * @model
	 * @generated
	 */
	Transportation getDepartureFlight();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getDepartureFlight <em>Departure Flight</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Departure Flight</em>' reference.
	 * @see #getDepartureFlight()
	 * @generated
	 */
	void setDepartureFlight(Transportation value);

	/**
	 * Returns the value of the '<em><b>Return Flight</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Flight</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Flight</em>' reference.
	 * @see #setReturnFlight(Transportation)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCart_ReturnFlight()
	 * @model
	 * @generated
	 */
	Transportation getReturnFlight();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getReturnFlight <em>Return Flight</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Flight</em>' reference.
	 * @see #getReturnFlight()
	 * @generated
	 */
	void setReturnFlight(Transportation value);

} // Cart
