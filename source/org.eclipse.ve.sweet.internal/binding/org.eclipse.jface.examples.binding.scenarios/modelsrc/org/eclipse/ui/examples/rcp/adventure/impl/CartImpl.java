/**
 * <copyright>
 * </copyright>
 *
 * $Id: CartImpl.java,v 1.1 2005-10-19 18:35:45 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.impl;

import java.util.Calendar;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Cart;
import org.eclipse.ui.examples.rcp.adventure.CartActivityEntry;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
import org.eclipse.ui.examples.rcp.adventure.Transportation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cart</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getAdventure <em>Adventure</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getAdventureDays <em>Adventure Days</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getHeadCount <em>Head Count</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getLodging <em>Lodging</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getLodgingDays <em>Lodging Days</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getLodgingRoomCount <em>Lodging Room Count</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getLodgingTotal <em>Lodging Total</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getActivities <em>Activities</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getActivityTotal <em>Activity Total</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getDepartureDate <em>Departure Date</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getTransportationTotal <em>Transportation Total</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getTotalPrice <em>Total Price</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getDepartureFlight <em>Departure Flight</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl#getReturnFlight <em>Return Flight</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CartImpl extends EObjectImpl implements Cart {
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
	 * The cached value of the '{@link #getAdventure() <em>Adventure</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdventure()
	 * @generated
	 * @ordered
	 */
	protected Adventure adventure = null;

	/**
	 * The default value of the '{@link #getAdventureDays() <em>Adventure Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdventureDays()
	 * @generated
	 * @ordered
	 */
	protected static final int ADVENTURE_DAYS_EDEFAULT = 3;

	/**
	 * The cached value of the '{@link #getAdventureDays() <em>Adventure Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdventureDays()
	 * @generated
	 * @ordered
	 */
	protected int adventureDays = ADVENTURE_DAYS_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeadCount() <em>Head Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeadCount()
	 * @generated
	 * @ordered
	 */
	protected static final int HEAD_COUNT_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getHeadCount() <em>Head Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeadCount()
	 * @generated
	 * @ordered
	 */
	protected int headCount = HEAD_COUNT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLodging() <em>Lodging</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLodging()
	 * @generated
	 * @ordered
	 */
	protected Lodging lodging = null;

	/**
	 * The default value of the '{@link #getLodgingDays() <em>Lodging Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLodgingDays()
	 * @generated
	 * @ordered
	 */
	protected static final int LODGING_DAYS_EDEFAULT = 0;

	/**
	 * The default value of the '{@link #getLodgingRoomCount() <em>Lodging Room Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLodgingRoomCount()
	 * @generated
	 * @ordered
	 */
	protected static final int LODGING_ROOM_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLodgingRoomCount() <em>Lodging Room Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLodgingRoomCount()
	 * @generated
	 * @ordered
	 */
	protected int lodgingRoomCount = LODGING_ROOM_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLodgingTotal() <em>Lodging Total</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLodgingTotal()
	 * @generated
	 * @ordered
	 */
	protected static final double LODGING_TOTAL_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getActivities() <em>Activities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivities()
	 * @generated
	 * @ordered
	 */
	protected EList activities = null;

	/**
	 * The default value of the '{@link #getActivityTotal() <em>Activity Total</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivityTotal()
	 * @generated
	 * @ordered
	 */
	protected static final double ACTIVITY_TOTAL_EDEFAULT = 0.0;

	/**
	 * The default value of the '{@link #getDepartureDate() <em>Departure Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepartureDate()
	 * @generated
	 * @ordered
	 */
	protected static final Calendar DEPARTURE_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDepartureDate() <em>Departure Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepartureDate()
	 * @generated
	 * @ordered
	 */
	protected Calendar departureDate = DEPARTURE_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransportationTotal() <em>Transportation Total</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransportationTotal()
	 * @generated
	 * @ordered
	 */
	protected static final double TRANSPORTATION_TOTAL_EDEFAULT = 0.0;

	/**
	 * The default value of the '{@link #getTotalPrice() <em>Total Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double TOTAL_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDepartureFlight() <em>Departure Flight</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDepartureFlight()
	 * @generated
	 * @ordered
	 */
	protected Transportation departureFlight = null;

	/**
	 * The cached value of the '{@link #getReturnFlight() <em>Return Flight</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnFlight()
	 * @generated
	 * @ordered
	 */
	protected Transportation returnFlight = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CartImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return AdventurePackage.eINSTANCE.getCart();
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.CART__CATALOG, oldCatalog, catalog));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__CATALOG, oldCatalog, catalog));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adventure getAdventure() {
		if (adventure != null && adventure.eIsProxy()) {
			Adventure oldAdventure = adventure;
			adventure = (Adventure)eResolveProxy((InternalEObject)adventure);
			if (adventure != oldAdventure) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.CART__ADVENTURE, oldAdventure, adventure));
			}
		}
		return adventure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adventure basicGetAdventure() {
		return adventure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAdventure(Adventure newAdventure) {
		Adventure oldAdventure = adventure;
		adventure = newAdventure;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__ADVENTURE, oldAdventure, adventure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getAdventureDays() {
		return adventureDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAdventureDays(int newAdventureDays) {
		int oldAdventureDays = adventureDays;
		adventureDays = newAdventureDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__ADVENTURE_DAYS, oldAdventureDays, adventureDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHeadCount() {
		return headCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeadCount(int newHeadCount) {
		int oldHeadCount = headCount;
		headCount = newHeadCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__HEAD_COUNT, oldHeadCount, headCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Lodging getLodging() {
		if (lodging != null && lodging.eIsProxy()) {
			Lodging oldLodging = lodging;
			lodging = (Lodging)eResolveProxy((InternalEObject)lodging);
			if (lodging != oldLodging) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.CART__LODGING, oldLodging, lodging));
			}
		}
		return lodging;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Lodging basicGetLodging() {
		return lodging;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLodging(Lodging newLodging) {
		Lodging oldLodging = lodging;
		lodging = newLodging;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__LODGING, oldLodging, lodging));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getLodgingDays() {
		return getAdventureDays() - 1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLodgingRoomCount() {
		return lodgingRoomCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLodgingRoomCount(int newLodgingRoomCount) {
		int oldLodgingRoomCount = lodgingRoomCount;
		lodgingRoomCount = newLodgingRoomCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__LODGING_ROOM_COUNT, oldLodgingRoomCount, lodgingRoomCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getLodgingTotal() {
		// TODO: implement this method to return the 'Lodging Total' attribute
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getActivities() {
		if (activities == null) {
			activities = new EObjectContainmentEList(CartActivityEntry.class, this, AdventurePackage.CART__ACTIVITIES);
		}
		return activities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getActivityTotal() {
		// TODO: implement this method to return the 'Activity Total' attribute
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Calendar getDepartureDate() {
		return departureDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDepartureDate(Calendar newDepartureDate) {
		Calendar oldDepartureDate = departureDate;
		departureDate = newDepartureDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__DEPARTURE_DATE, oldDepartureDate, departureDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getTransportationTotal() {
		// TODO: implement this method to return the 'Transportation Total' attribute
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getTotalPrice() {
		// TODO: implement this method to return the 'Total Price' attribute
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transportation getDepartureFlight() {
		if (departureFlight != null && departureFlight.eIsProxy()) {
			Transportation oldDepartureFlight = departureFlight;
			departureFlight = (Transportation)eResolveProxy((InternalEObject)departureFlight);
			if (departureFlight != oldDepartureFlight) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.CART__DEPARTURE_FLIGHT, oldDepartureFlight, departureFlight));
			}
		}
		return departureFlight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transportation basicGetDepartureFlight() {
		return departureFlight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDepartureFlight(Transportation newDepartureFlight) {
		Transportation oldDepartureFlight = departureFlight;
		departureFlight = newDepartureFlight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__DEPARTURE_FLIGHT, oldDepartureFlight, departureFlight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transportation getReturnFlight() {
		if (returnFlight != null && returnFlight.eIsProxy()) {
			Transportation oldReturnFlight = returnFlight;
			returnFlight = (Transportation)eResolveProxy((InternalEObject)returnFlight);
			if (returnFlight != oldReturnFlight) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.CART__RETURN_FLIGHT, oldReturnFlight, returnFlight));
			}
		}
		return returnFlight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transportation basicGetReturnFlight() {
		return returnFlight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReturnFlight(Transportation newReturnFlight) {
		Transportation oldReturnFlight = returnFlight;
		returnFlight = newReturnFlight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART__RETURN_FLIGHT, oldReturnFlight, returnFlight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case AdventurePackage.CART__ACTIVITIES:
					return ((InternalEList)getActivities()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case AdventurePackage.CART__CATALOG:
				if (resolve) return getCatalog();
				return basicGetCatalog();
			case AdventurePackage.CART__ADVENTURE:
				if (resolve) return getAdventure();
				return basicGetAdventure();
			case AdventurePackage.CART__ADVENTURE_DAYS:
				return new Integer(getAdventureDays());
			case AdventurePackage.CART__HEAD_COUNT:
				return new Integer(getHeadCount());
			case AdventurePackage.CART__LODGING:
				if (resolve) return getLodging();
				return basicGetLodging();
			case AdventurePackage.CART__LODGING_DAYS:
				return new Integer(getLodgingDays());
			case AdventurePackage.CART__LODGING_ROOM_COUNT:
				return new Integer(getLodgingRoomCount());
			case AdventurePackage.CART__LODGING_TOTAL:
				return new Double(getLodgingTotal());
			case AdventurePackage.CART__ACTIVITIES:
				return getActivities();
			case AdventurePackage.CART__ACTIVITY_TOTAL:
				return new Double(getActivityTotal());
			case AdventurePackage.CART__DEPARTURE_DATE:
				return getDepartureDate();
			case AdventurePackage.CART__TRANSPORTATION_TOTAL:
				return new Double(getTransportationTotal());
			case AdventurePackage.CART__TOTAL_PRICE:
				return new Double(getTotalPrice());
			case AdventurePackage.CART__DEPARTURE_FLIGHT:
				if (resolve) return getDepartureFlight();
				return basicGetDepartureFlight();
			case AdventurePackage.CART__RETURN_FLIGHT:
				if (resolve) return getReturnFlight();
				return basicGetReturnFlight();
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
			case AdventurePackage.CART__CATALOG:
				setCatalog((Catalog)newValue);
				return;
			case AdventurePackage.CART__ADVENTURE:
				setAdventure((Adventure)newValue);
				return;
			case AdventurePackage.CART__ADVENTURE_DAYS:
				setAdventureDays(((Integer)newValue).intValue());
				return;
			case AdventurePackage.CART__HEAD_COUNT:
				setHeadCount(((Integer)newValue).intValue());
				return;
			case AdventurePackage.CART__LODGING:
				setLodging((Lodging)newValue);
				return;
			case AdventurePackage.CART__LODGING_ROOM_COUNT:
				setLodgingRoomCount(((Integer)newValue).intValue());
				return;
			case AdventurePackage.CART__ACTIVITIES:
				getActivities().clear();
				getActivities().addAll((Collection)newValue);
				return;
			case AdventurePackage.CART__DEPARTURE_DATE:
				setDepartureDate((Calendar)newValue);
				return;
			case AdventurePackage.CART__DEPARTURE_FLIGHT:
				setDepartureFlight((Transportation)newValue);
				return;
			case AdventurePackage.CART__RETURN_FLIGHT:
				setReturnFlight((Transportation)newValue);
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
			case AdventurePackage.CART__CATALOG:
				setCatalog((Catalog)null);
				return;
			case AdventurePackage.CART__ADVENTURE:
				setAdventure((Adventure)null);
				return;
			case AdventurePackage.CART__ADVENTURE_DAYS:
				setAdventureDays(ADVENTURE_DAYS_EDEFAULT);
				return;
			case AdventurePackage.CART__HEAD_COUNT:
				setHeadCount(HEAD_COUNT_EDEFAULT);
				return;
			case AdventurePackage.CART__LODGING:
				setLodging((Lodging)null);
				return;
			case AdventurePackage.CART__LODGING_ROOM_COUNT:
				setLodgingRoomCount(LODGING_ROOM_COUNT_EDEFAULT);
				return;
			case AdventurePackage.CART__ACTIVITIES:
				getActivities().clear();
				return;
			case AdventurePackage.CART__DEPARTURE_DATE:
				setDepartureDate(DEPARTURE_DATE_EDEFAULT);
				return;
			case AdventurePackage.CART__DEPARTURE_FLIGHT:
				setDepartureFlight((Transportation)null);
				return;
			case AdventurePackage.CART__RETURN_FLIGHT:
				setReturnFlight((Transportation)null);
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
			case AdventurePackage.CART__CATALOG:
				return catalog != null;
			case AdventurePackage.CART__ADVENTURE:
				return adventure != null;
			case AdventurePackage.CART__ADVENTURE_DAYS:
				return adventureDays != ADVENTURE_DAYS_EDEFAULT;
			case AdventurePackage.CART__HEAD_COUNT:
				return headCount != HEAD_COUNT_EDEFAULT;
			case AdventurePackage.CART__LODGING:
				return lodging != null;
			case AdventurePackage.CART__LODGING_DAYS:
				return getLodgingDays() != LODGING_DAYS_EDEFAULT;
			case AdventurePackage.CART__LODGING_ROOM_COUNT:
				return lodgingRoomCount != LODGING_ROOM_COUNT_EDEFAULT;
			case AdventurePackage.CART__LODGING_TOTAL:
				return getLodgingTotal() != LODGING_TOTAL_EDEFAULT;
			case AdventurePackage.CART__ACTIVITIES:
				return activities != null && !activities.isEmpty();
			case AdventurePackage.CART__ACTIVITY_TOTAL:
				return getActivityTotal() != ACTIVITY_TOTAL_EDEFAULT;
			case AdventurePackage.CART__DEPARTURE_DATE:
				return DEPARTURE_DATE_EDEFAULT == null ? departureDate != null : !DEPARTURE_DATE_EDEFAULT.equals(departureDate);
			case AdventurePackage.CART__TRANSPORTATION_TOTAL:
				return getTransportationTotal() != TRANSPORTATION_TOTAL_EDEFAULT;
			case AdventurePackage.CART__TOTAL_PRICE:
				return getTotalPrice() != TOTAL_PRICE_EDEFAULT;
			case AdventurePackage.CART__DEPARTURE_FLIGHT:
				return departureFlight != null;
			case AdventurePackage.CART__RETURN_FLIGHT:
				return returnFlight != null;
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
		result.append(" (adventureDays: ");
		result.append(adventureDays);
		result.append(", headCount: ");
		result.append(headCount);
		result.append(", lodgingRoomCount: ");
		result.append(lodgingRoomCount);
		result.append(", departureDate: ");
		result.append(departureDate);
		result.append(')');
		return result.toString();
	}

} //CartImpl
