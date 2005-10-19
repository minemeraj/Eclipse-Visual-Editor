/**
 * <copyright>
 * </copyright>
 *
 * $Id: CatalogImpl.java,v 1.1 2005-10-19 18:35:45 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.impl;

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

import org.eclipse.ui.examples.rcp.adventure.Account;
import org.eclipse.ui.examples.rcp.adventure.Activity;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Category;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
import org.eclipse.ui.examples.rcp.adventure.Signon;
import org.eclipse.ui.examples.rcp.adventure.Transportation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Catalog</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getLastModified <em>Last Modified</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getTimeToRead <em>Time To Read</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getCategories <em>Categories</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getLodgings <em>Lodgings</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getActivities <em>Activities</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getTransportations <em>Transportations</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getSignons <em>Signons</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl#getAccounts <em>Accounts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CatalogImpl extends EObjectImpl implements Catalog {
	/**
	 * The default value of the '{@link #getLastModified() <em>Last Modified</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastModified()
	 * @generated
	 * @ordered
	 */
	protected static final long LAST_MODIFIED_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getLastModified() <em>Last Modified</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastModified()
	 * @generated
	 * @ordered
	 */
	protected long lastModified = LAST_MODIFIED_EDEFAULT;

	/**
	 * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected static final String SOURCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected String source = SOURCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTimeToRead() <em>Time To Read</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeToRead()
	 * @generated
	 * @ordered
	 */
	protected static final long TIME_TO_READ_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getTimeToRead() <em>Time To Read</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeToRead()
	 * @generated
	 * @ordered
	 */
	protected long timeToRead = TIME_TO_READ_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCategories() <em>Categories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategories()
	 * @generated
	 * @ordered
	 */
	protected EList categories = null;

	/**
	 * The cached value of the '{@link #getLodgings() <em>Lodgings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLodgings()
	 * @generated
	 * @ordered
	 */
	protected EList lodgings = null;

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
	 * The cached value of the '{@link #getTransportations() <em>Transportations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransportations()
	 * @generated
	 * @ordered
	 */
	protected EList transportations = null;

	/**
	 * The cached value of the '{@link #getSignons() <em>Signons</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSignons()
	 * @generated
	 * @ordered
	 */
	protected EList signons = null;

	/**
	 * The cached value of the '{@link #getAccounts() <em>Accounts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccounts()
	 * @generated
	 * @ordered
	 */
	protected EList accounts = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CatalogImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return AdventurePackage.eINSTANCE.getCatalog();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastModified(long newLastModified) {
		long oldLastModified = lastModified;
		lastModified = newLastModified;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CATALOG__LAST_MODIFIED, oldLastModified, lastModified));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSource() {
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource(String newSource) {
		String oldSource = source;
		source = newSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CATALOG__SOURCE, oldSource, source));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getTimeToRead() {
		return timeToRead;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeToRead(long newTimeToRead) {
		long oldTimeToRead = timeToRead;
		timeToRead = newTimeToRead;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CATALOG__TIME_TO_READ, oldTimeToRead, timeToRead));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getCategories() {
		if (categories == null) {
			categories = new EObjectContainmentEList(Category.class, this, AdventurePackage.CATALOG__CATEGORIES);
		}
		return categories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getLodgings() {
		if (lodgings == null) {
			lodgings = new EObjectContainmentEList(Lodging.class, this, AdventurePackage.CATALOG__LODGINGS);
		}
		return lodgings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getActivities() {
		if (activities == null) {
			activities = new EObjectContainmentEList(Activity.class, this, AdventurePackage.CATALOG__ACTIVITIES);
		}
		return activities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getTransportations() {
		if (transportations == null) {
			transportations = new EObjectContainmentEList(Transportation.class, this, AdventurePackage.CATALOG__TRANSPORTATIONS);
		}
		return transportations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getSignons() {
		if (signons == null) {
			signons = new EObjectContainmentEList(Signon.class, this, AdventurePackage.CATALOG__SIGNONS);
		}
		return signons;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getAccounts() {
		if (accounts == null) {
			accounts = new EObjectContainmentEList(Account.class, this, AdventurePackage.CATALOG__ACCOUNTS);
		}
		return accounts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getActivities(String location) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getLodgings(String location) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getOriginsForDestination(String destination) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getTransportations(String origin, String destination) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case AdventurePackage.CATALOG__CATEGORIES:
					return ((InternalEList)getCategories()).basicRemove(otherEnd, msgs);
				case AdventurePackage.CATALOG__LODGINGS:
					return ((InternalEList)getLodgings()).basicRemove(otherEnd, msgs);
				case AdventurePackage.CATALOG__ACTIVITIES:
					return ((InternalEList)getActivities()).basicRemove(otherEnd, msgs);
				case AdventurePackage.CATALOG__TRANSPORTATIONS:
					return ((InternalEList)getTransportations()).basicRemove(otherEnd, msgs);
				case AdventurePackage.CATALOG__SIGNONS:
					return ((InternalEList)getSignons()).basicRemove(otherEnd, msgs);
				case AdventurePackage.CATALOG__ACCOUNTS:
					return ((InternalEList)getAccounts()).basicRemove(otherEnd, msgs);
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
			case AdventurePackage.CATALOG__LAST_MODIFIED:
				return new Long(getLastModified());
			case AdventurePackage.CATALOG__SOURCE:
				return getSource();
			case AdventurePackage.CATALOG__TIME_TO_READ:
				return new Long(getTimeToRead());
			case AdventurePackage.CATALOG__CATEGORIES:
				return getCategories();
			case AdventurePackage.CATALOG__LODGINGS:
				return getLodgings();
			case AdventurePackage.CATALOG__ACTIVITIES:
				return getActivities();
			case AdventurePackage.CATALOG__TRANSPORTATIONS:
				return getTransportations();
			case AdventurePackage.CATALOG__SIGNONS:
				return getSignons();
			case AdventurePackage.CATALOG__ACCOUNTS:
				return getAccounts();
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
			case AdventurePackage.CATALOG__LAST_MODIFIED:
				setLastModified(((Long)newValue).longValue());
				return;
			case AdventurePackage.CATALOG__SOURCE:
				setSource((String)newValue);
				return;
			case AdventurePackage.CATALOG__TIME_TO_READ:
				setTimeToRead(((Long)newValue).longValue());
				return;
			case AdventurePackage.CATALOG__CATEGORIES:
				getCategories().clear();
				getCategories().addAll((Collection)newValue);
				return;
			case AdventurePackage.CATALOG__LODGINGS:
				getLodgings().clear();
				getLodgings().addAll((Collection)newValue);
				return;
			case AdventurePackage.CATALOG__ACTIVITIES:
				getActivities().clear();
				getActivities().addAll((Collection)newValue);
				return;
			case AdventurePackage.CATALOG__TRANSPORTATIONS:
				getTransportations().clear();
				getTransportations().addAll((Collection)newValue);
				return;
			case AdventurePackage.CATALOG__SIGNONS:
				getSignons().clear();
				getSignons().addAll((Collection)newValue);
				return;
			case AdventurePackage.CATALOG__ACCOUNTS:
				getAccounts().clear();
				getAccounts().addAll((Collection)newValue);
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
			case AdventurePackage.CATALOG__LAST_MODIFIED:
				setLastModified(LAST_MODIFIED_EDEFAULT);
				return;
			case AdventurePackage.CATALOG__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case AdventurePackage.CATALOG__TIME_TO_READ:
				setTimeToRead(TIME_TO_READ_EDEFAULT);
				return;
			case AdventurePackage.CATALOG__CATEGORIES:
				getCategories().clear();
				return;
			case AdventurePackage.CATALOG__LODGINGS:
				getLodgings().clear();
				return;
			case AdventurePackage.CATALOG__ACTIVITIES:
				getActivities().clear();
				return;
			case AdventurePackage.CATALOG__TRANSPORTATIONS:
				getTransportations().clear();
				return;
			case AdventurePackage.CATALOG__SIGNONS:
				getSignons().clear();
				return;
			case AdventurePackage.CATALOG__ACCOUNTS:
				getAccounts().clear();
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
			case AdventurePackage.CATALOG__LAST_MODIFIED:
				return lastModified != LAST_MODIFIED_EDEFAULT;
			case AdventurePackage.CATALOG__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case AdventurePackage.CATALOG__TIME_TO_READ:
				return timeToRead != TIME_TO_READ_EDEFAULT;
			case AdventurePackage.CATALOG__CATEGORIES:
				return categories != null && !categories.isEmpty();
			case AdventurePackage.CATALOG__LODGINGS:
				return lodgings != null && !lodgings.isEmpty();
			case AdventurePackage.CATALOG__ACTIVITIES:
				return activities != null && !activities.isEmpty();
			case AdventurePackage.CATALOG__TRANSPORTATIONS:
				return transportations != null && !transportations.isEmpty();
			case AdventurePackage.CATALOG__SIGNONS:
				return signons != null && !signons.isEmpty();
			case AdventurePackage.CATALOG__ACCOUNTS:
				return accounts != null && !accounts.isEmpty();
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
		result.append(" (lastModified: ");
		result.append(lastModified);
		result.append(", source: ");
		result.append(source);
		result.append(", timeToRead: ");
		result.append(timeToRead);
		result.append(')');
		return result.toString();
	}

} //CatalogImpl
