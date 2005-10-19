/**
 * <copyright>
 * </copyright>
 *
 * $Id: TransportationImpl.java,v 1.1 2005-10-19 18:35:45 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Transportation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transportation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getArrivalTime <em>Arrival Time</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getCarrier <em>Carrier</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getDepartureTime <em>Departure Time</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getDestination <em>Destination</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getOrigin <em>Origin</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl#getTransportationClass <em>Transportation Class</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransportationImpl extends EObjectImpl implements Transportation {
	/**
	 * The default value of the '{@link #getArrivalTime() <em>Arrival Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArrivalTime()
	 * @generated
	 * @ordered
	 */
	protected static final String ARRIVAL_TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getArrivalTime() <em>Arrival Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArrivalTime()
	 * @generated
	 * @ordered
	 */
	protected String arrivalTime = ARRIVAL_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCarrier() <em>Carrier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCarrier()
	 * @generated
	 * @ordered
	 */
	protected static final String CARRIER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCarrier() <em>Carrier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCarrier()
	 * @generated
	 * @ordered
	 */
	protected String carrier = CARRIER_EDEFAULT;

	/**
	 * The default value of the '{@link #getDepartureTime() <em>Departure Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepartureTime()
	 * @generated
	 * @ordered
	 */
	protected static final String DEPARTURE_TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDepartureTime() <em>Departure Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepartureTime()
	 * @generated
	 * @ordered
	 */
	protected String departureTime = DEPARTURE_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getDestination() <em>Destination</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected static final String DESTINATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDestination() <em>Destination</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected String destination = DESTINATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getOrigin() <em>Origin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrigin()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGIN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOrigin() <em>Origin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrigin()
	 * @generated
	 * @ordered
	 */
	protected String origin = ORIGIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected double price = PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransportationClass() <em>Transportation Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransportationClass()
	 * @generated
	 * @ordered
	 */
	protected static final String TRANSPORTATION_CLASS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTransportationClass() <em>Transportation Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransportationClass()
	 * @generated
	 * @ordered
	 */
	protected String transportationClass = TRANSPORTATION_CLASS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransportationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return AdventurePackage.eINSTANCE.getTransportation();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArrivalTime(String newArrivalTime) {
		String oldArrivalTime = arrivalTime;
		arrivalTime = newArrivalTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__ARRIVAL_TIME, oldArrivalTime, arrivalTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCarrier(String newCarrier) {
		String oldCarrier = carrier;
		carrier = newCarrier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__CARRIER, oldCarrier, carrier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDepartureTime() {
		return departureTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDepartureTime(String newDepartureTime) {
		String oldDepartureTime = departureTime;
		departureTime = newDepartureTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__DEPARTURE_TIME, oldDepartureTime, departureTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestination(String newDestination) {
		String oldDestination = destination;
		destination = newDestination;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__DESTINATION, oldDestination, destination));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOrigin(String newOrigin) {
		String oldOrigin = origin;
		origin = newOrigin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__ORIGIN, oldOrigin, origin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrice(double newPrice) {
		double oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTransportationClass() {
		return transportationClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransportationClass(String newTransportationClass) {
		String oldTransportationClass = transportationClass;
		transportationClass = newTransportationClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.TRANSPORTATION__TRANSPORTATION_CLASS, oldTransportationClass, transportationClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case AdventurePackage.TRANSPORTATION__ARRIVAL_TIME:
				return getArrivalTime();
			case AdventurePackage.TRANSPORTATION__CARRIER:
				return getCarrier();
			case AdventurePackage.TRANSPORTATION__DEPARTURE_TIME:
				return getDepartureTime();
			case AdventurePackage.TRANSPORTATION__DESCRIPTION:
				return getDescription();
			case AdventurePackage.TRANSPORTATION__DESTINATION:
				return getDestination();
			case AdventurePackage.TRANSPORTATION__ID:
				return getId();
			case AdventurePackage.TRANSPORTATION__NAME:
				return getName();
			case AdventurePackage.TRANSPORTATION__ORIGIN:
				return getOrigin();
			case AdventurePackage.TRANSPORTATION__PRICE:
				return new Double(getPrice());
			case AdventurePackage.TRANSPORTATION__TRANSPORTATION_CLASS:
				return getTransportationClass();
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
			case AdventurePackage.TRANSPORTATION__ARRIVAL_TIME:
				setArrivalTime((String)newValue);
				return;
			case AdventurePackage.TRANSPORTATION__CARRIER:
				setCarrier((String)newValue);
				return;
			case AdventurePackage.TRANSPORTATION__DEPARTURE_TIME:
				setDepartureTime((String)newValue);
				return;
			case AdventurePackage.TRANSPORTATION__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case AdventurePackage.TRANSPORTATION__DESTINATION:
				setDestination((String)newValue);
				return;
			case AdventurePackage.TRANSPORTATION__ID:
				setId((String)newValue);
				return;
			case AdventurePackage.TRANSPORTATION__NAME:
				setName((String)newValue);
				return;
			case AdventurePackage.TRANSPORTATION__ORIGIN:
				setOrigin((String)newValue);
				return;
			case AdventurePackage.TRANSPORTATION__PRICE:
				setPrice(((Double)newValue).doubleValue());
				return;
			case AdventurePackage.TRANSPORTATION__TRANSPORTATION_CLASS:
				setTransportationClass((String)newValue);
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
			case AdventurePackage.TRANSPORTATION__ARRIVAL_TIME:
				setArrivalTime(ARRIVAL_TIME_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__CARRIER:
				setCarrier(CARRIER_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__DEPARTURE_TIME:
				setDepartureTime(DEPARTURE_TIME_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__DESTINATION:
				setDestination(DESTINATION_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__ID:
				setId(ID_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__ORIGIN:
				setOrigin(ORIGIN_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case AdventurePackage.TRANSPORTATION__TRANSPORTATION_CLASS:
				setTransportationClass(TRANSPORTATION_CLASS_EDEFAULT);
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
			case AdventurePackage.TRANSPORTATION__ARRIVAL_TIME:
				return ARRIVAL_TIME_EDEFAULT == null ? arrivalTime != null : !ARRIVAL_TIME_EDEFAULT.equals(arrivalTime);
			case AdventurePackage.TRANSPORTATION__CARRIER:
				return CARRIER_EDEFAULT == null ? carrier != null : !CARRIER_EDEFAULT.equals(carrier);
			case AdventurePackage.TRANSPORTATION__DEPARTURE_TIME:
				return DEPARTURE_TIME_EDEFAULT == null ? departureTime != null : !DEPARTURE_TIME_EDEFAULT.equals(departureTime);
			case AdventurePackage.TRANSPORTATION__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case AdventurePackage.TRANSPORTATION__DESTINATION:
				return DESTINATION_EDEFAULT == null ? destination != null : !DESTINATION_EDEFAULT.equals(destination);
			case AdventurePackage.TRANSPORTATION__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case AdventurePackage.TRANSPORTATION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AdventurePackage.TRANSPORTATION__ORIGIN:
				return ORIGIN_EDEFAULT == null ? origin != null : !ORIGIN_EDEFAULT.equals(origin);
			case AdventurePackage.TRANSPORTATION__PRICE:
				return price != PRICE_EDEFAULT;
			case AdventurePackage.TRANSPORTATION__TRANSPORTATION_CLASS:
				return TRANSPORTATION_CLASS_EDEFAULT == null ? transportationClass != null : !TRANSPORTATION_CLASS_EDEFAULT.equals(transportationClass);
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
		result.append(" (arrivalTime: ");
		result.append(arrivalTime);
		result.append(", carrier: ");
		result.append(carrier);
		result.append(", departureTime: ");
		result.append(departureTime);
		result.append(", description: ");
		result.append(description);
		result.append(", destination: ");
		result.append(destination);
		result.append(", id: ");
		result.append(id);
		result.append(", name: ");
		result.append(name);
		result.append(", origin: ");
		result.append(origin);
		result.append(", price: ");
		result.append(price);
		result.append(", transportationClass: ");
		result.append(transportationClass);
		result.append(')');
		return result.toString();
	}

} //TransportationImpl
