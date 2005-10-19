/**
 * <copyright>
 * </copyright>
 *
 * $Id: Transportation.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transportation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getArrivalTime <em>Arrival Time</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getCarrier <em>Carrier</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDepartureTime <em>Departure Time</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDestination <em>Destination</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getOrigin <em>Origin</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getPrice <em>Price</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getTransportationClass <em>Transportation Class</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation()
 * @model
 * @generated
 */
public interface Transportation extends EObject {
	/**
	 * Returns the value of the '<em><b>Arrival Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arrival Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arrival Time</em>' attribute.
	 * @see #setArrivalTime(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_ArrivalTime()
	 * @model
	 * @generated
	 */
	String getArrivalTime();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getArrivalTime <em>Arrival Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arrival Time</em>' attribute.
	 * @see #getArrivalTime()
	 * @generated
	 */
	void setArrivalTime(String value);

	/**
	 * Returns the value of the '<em><b>Carrier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Carrier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Carrier</em>' attribute.
	 * @see #setCarrier(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_Carrier()
	 * @model
	 * @generated
	 */
	String getCarrier();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getCarrier <em>Carrier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Carrier</em>' attribute.
	 * @see #getCarrier()
	 * @generated
	 */
	void setCarrier(String value);

	/**
	 * Returns the value of the '<em><b>Departure Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Departure Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Departure Time</em>' attribute.
	 * @see #setDepartureTime(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_DepartureTime()
	 * @model
	 * @generated
	 */
	String getDepartureTime();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDepartureTime <em>Departure Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Departure Time</em>' attribute.
	 * @see #getDepartureTime()
	 * @generated
	 */
	void setDepartureTime(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Destination</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination</em>' attribute.
	 * @see #setDestination(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_Destination()
	 * @model
	 * @generated
	 */
	String getDestination();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDestination <em>Destination</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination</em>' attribute.
	 * @see #getDestination()
	 * @generated
	 */
	void setDestination(String value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Origin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin</em>' attribute.
	 * @see #setOrigin(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_Origin()
	 * @model
	 * @generated
	 */
	String getOrigin();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getOrigin <em>Origin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin</em>' attribute.
	 * @see #getOrigin()
	 * @generated
	 */
	void setOrigin(String value);

	/**
	 * Returns the value of the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' attribute.
	 * @see #setPrice(double)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_Price()
	 * @model
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(double value);

	/**
	 * Returns the value of the '<em><b>Transportation Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transportation Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transportation Class</em>' attribute.
	 * @see #setTransportationClass(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getTransportation_TransportationClass()
	 * @model
	 * @generated
	 */
	String getTransportationClass();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getTransportationClass <em>Transportation Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transportation Class</em>' attribute.
	 * @see #getTransportationClass()
	 * @generated
	 */
	void setTransportationClass(String value);

} // Transportation
