/**
 * <copyright>
 * </copyright>
 *
 * $Id: AccountImpl.java,v 1.1 2005-10-19 18:35:45 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.ui.examples.rcp.adventure.Account;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Account</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getAddress1 <em>Address1</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getAddress2 <em>Address2</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getCity <em>City</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getCountry <em>Country</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getEmail <em>Email</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getFirstName <em>First Name</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getLastName <em>Last Name</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getPhone <em>Phone</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getState <em>State</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl#getZip <em>Zip</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AccountImpl extends EObjectImpl implements Account {
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
	 * The default value of the '{@link #getAddress1() <em>Address1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress1()
	 * @generated
	 * @ordered
	 */
	protected static final String ADDRESS1_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAddress1() <em>Address1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress1()
	 * @generated
	 * @ordered
	 */
	protected String address1 = ADDRESS1_EDEFAULT;

	/**
	 * The default value of the '{@link #getAddress2() <em>Address2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress2()
	 * @generated
	 * @ordered
	 */
	protected static final String ADDRESS2_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAddress2() <em>Address2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress2()
	 * @generated
	 * @ordered
	 */
	protected String address2 = ADDRESS2_EDEFAULT;

	/**
	 * The default value of the '{@link #getCity() <em>City</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCity()
	 * @generated
	 * @ordered
	 */
	protected static final String CITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCity() <em>City</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCity()
	 * @generated
	 * @ordered
	 */
	protected String city = CITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getCountry() <em>Country</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountry()
	 * @generated
	 * @ordered
	 */
	protected static final String COUNTRY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCountry() <em>Country</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountry()
	 * @generated
	 * @ordered
	 */
	protected String country = COUNTRY_EDEFAULT;

	/**
	 * The default value of the '{@link #getEmail() <em>Email</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmail()
	 * @generated
	 * @ordered
	 */
	protected static final String EMAIL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEmail() <em>Email</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmail()
	 * @generated
	 * @ordered
	 */
	protected String email = EMAIL_EDEFAULT;

	/**
	 * The default value of the '{@link #getFirstName() <em>First Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFirstName()
	 * @generated
	 * @ordered
	 */
	protected static final String FIRST_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFirstName() <em>First Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFirstName()
	 * @generated
	 * @ordered
	 */
	protected String firstName = FIRST_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getLastName() <em>Last Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastName()
	 * @generated
	 * @ordered
	 */
	protected static final String LAST_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLastName() <em>Last Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastName()
	 * @generated
	 * @ordered
	 */
	protected String lastName = LAST_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPhone() <em>Phone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhone()
	 * @generated
	 * @ordered
	 */
	protected static final String PHONE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPhone() <em>Phone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhone()
	 * @generated
	 * @ordered
	 */
	protected String phone = PHONE_EDEFAULT;

	/**
	 * The default value of the '{@link #getState() <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getState()
	 * @generated
	 * @ordered
	 */
	protected static final String STATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getState() <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getState()
	 * @generated
	 * @ordered
	 */
	protected String state = STATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getZip() <em>Zip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZip()
	 * @generated
	 * @ordered
	 */
	protected static final String ZIP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getZip() <em>Zip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZip()
	 * @generated
	 * @ordered
	 */
	protected String zip = ZIP_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AccountImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return AdventurePackage.eINSTANCE.getAccount();
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
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAddress1(String newAddress1) {
		String oldAddress1 = address1;
		address1 = newAddress1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__ADDRESS1, oldAddress1, address1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAddress2(String newAddress2) {
		String oldAddress2 = address2;
		address2 = newAddress2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__ADDRESS2, oldAddress2, address2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCity() {
		return city;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCity(String newCity) {
		String oldCity = city;
		city = newCity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__CITY, oldCity, city));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCountry(String newCountry) {
		String oldCountry = country;
		country = newCountry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__COUNTRY, oldCountry, country));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEmail(String newEmail) {
		String oldEmail = email;
		email = newEmail;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__EMAIL, oldEmail, email));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFirstName(String newFirstName) {
		String oldFirstName = firstName;
		firstName = newFirstName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__FIRST_NAME, oldFirstName, firstName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastName(String newLastName) {
		String oldLastName = lastName;
		lastName = newLastName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__LAST_NAME, oldLastName, lastName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPhone(String newPhone) {
		String oldPhone = phone;
		phone = newPhone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__PHONE, oldPhone, phone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getState() {
		return state;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setState(String newState) {
		String oldState = state;
		state = newState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__STATE, oldState, state));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setZip(String newZip) {
		String oldZip = zip;
		zip = newZip;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ACCOUNT__ZIP, oldZip, zip));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case AdventurePackage.ACCOUNT__ID:
				return getId();
			case AdventurePackage.ACCOUNT__ADDRESS1:
				return getAddress1();
			case AdventurePackage.ACCOUNT__ADDRESS2:
				return getAddress2();
			case AdventurePackage.ACCOUNT__CITY:
				return getCity();
			case AdventurePackage.ACCOUNT__COUNTRY:
				return getCountry();
			case AdventurePackage.ACCOUNT__EMAIL:
				return getEmail();
			case AdventurePackage.ACCOUNT__FIRST_NAME:
				return getFirstName();
			case AdventurePackage.ACCOUNT__LAST_NAME:
				return getLastName();
			case AdventurePackage.ACCOUNT__PHONE:
				return getPhone();
			case AdventurePackage.ACCOUNT__STATE:
				return getState();
			case AdventurePackage.ACCOUNT__ZIP:
				return getZip();
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
			case AdventurePackage.ACCOUNT__ID:
				setId((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__ADDRESS1:
				setAddress1((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__ADDRESS2:
				setAddress2((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__CITY:
				setCity((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__COUNTRY:
				setCountry((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__EMAIL:
				setEmail((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__FIRST_NAME:
				setFirstName((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__LAST_NAME:
				setLastName((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__PHONE:
				setPhone((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__STATE:
				setState((String)newValue);
				return;
			case AdventurePackage.ACCOUNT__ZIP:
				setZip((String)newValue);
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
			case AdventurePackage.ACCOUNT__ID:
				setId(ID_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__ADDRESS1:
				setAddress1(ADDRESS1_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__ADDRESS2:
				setAddress2(ADDRESS2_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__CITY:
				setCity(CITY_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__COUNTRY:
				setCountry(COUNTRY_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__EMAIL:
				setEmail(EMAIL_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__FIRST_NAME:
				setFirstName(FIRST_NAME_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__LAST_NAME:
				setLastName(LAST_NAME_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__PHONE:
				setPhone(PHONE_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__STATE:
				setState(STATE_EDEFAULT);
				return;
			case AdventurePackage.ACCOUNT__ZIP:
				setZip(ZIP_EDEFAULT);
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
			case AdventurePackage.ACCOUNT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case AdventurePackage.ACCOUNT__ADDRESS1:
				return ADDRESS1_EDEFAULT == null ? address1 != null : !ADDRESS1_EDEFAULT.equals(address1);
			case AdventurePackage.ACCOUNT__ADDRESS2:
				return ADDRESS2_EDEFAULT == null ? address2 != null : !ADDRESS2_EDEFAULT.equals(address2);
			case AdventurePackage.ACCOUNT__CITY:
				return CITY_EDEFAULT == null ? city != null : !CITY_EDEFAULT.equals(city);
			case AdventurePackage.ACCOUNT__COUNTRY:
				return COUNTRY_EDEFAULT == null ? country != null : !COUNTRY_EDEFAULT.equals(country);
			case AdventurePackage.ACCOUNT__EMAIL:
				return EMAIL_EDEFAULT == null ? email != null : !EMAIL_EDEFAULT.equals(email);
			case AdventurePackage.ACCOUNT__FIRST_NAME:
				return FIRST_NAME_EDEFAULT == null ? firstName != null : !FIRST_NAME_EDEFAULT.equals(firstName);
			case AdventurePackage.ACCOUNT__LAST_NAME:
				return LAST_NAME_EDEFAULT == null ? lastName != null : !LAST_NAME_EDEFAULT.equals(lastName);
			case AdventurePackage.ACCOUNT__PHONE:
				return PHONE_EDEFAULT == null ? phone != null : !PHONE_EDEFAULT.equals(phone);
			case AdventurePackage.ACCOUNT__STATE:
				return STATE_EDEFAULT == null ? state != null : !STATE_EDEFAULT.equals(state);
			case AdventurePackage.ACCOUNT__ZIP:
				return ZIP_EDEFAULT == null ? zip != null : !ZIP_EDEFAULT.equals(zip);
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
		result.append(" (id: ");
		result.append(id);
		result.append(", address1: ");
		result.append(address1);
		result.append(", address2: ");
		result.append(address2);
		result.append(", city: ");
		result.append(city);
		result.append(", country: ");
		result.append(country);
		result.append(", email: ");
		result.append(email);
		result.append(", firstName: ");
		result.append(firstName);
		result.append(", lastName: ");
		result.append(lastName);
		result.append(", phone: ");
		result.append(phone);
		result.append(", state: ");
		result.append(state);
		result.append(", zip: ");
		result.append(zip);
		result.append(')');
		return result.toString();
	}

} //AccountImpl
