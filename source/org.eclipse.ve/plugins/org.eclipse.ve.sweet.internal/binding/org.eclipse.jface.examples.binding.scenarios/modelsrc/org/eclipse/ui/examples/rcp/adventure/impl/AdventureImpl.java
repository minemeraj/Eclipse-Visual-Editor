/**
 * <copyright>
 * </copyright>
 *
 * $Id: AdventureImpl.java,v 1.2 2005-10-19 21:47:59 sgunturi Exp $
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

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ui.examples.rcp.adventure.Activity;
import org.eclipse.ui.examples.rcp.adventure.Adventure;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.Category;
import org.eclipse.ui.examples.rcp.adventure.Lodging;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Adventure</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#getDefaultActivities <em>Default Activities</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#getDefaultLodging <em>Default Lodging</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl#isPetsAllowed <em>Pets Allowed</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AdventureImpl extends EObjectImpl implements Adventure {
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
	 * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected String location = LOCATION_EDEFAULT;

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
	 * The cached value of the '{@link #getDefaultActivities() <em>Default Activities</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultActivities()
	 * @generated
	 * @ordered
	 */
	protected EList defaultActivities = null;

	/**
	 * The cached value of the '{@link #getDefaultLodging() <em>Default Lodging</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLodging()
	 * @generated
	 * @ordered
	 */
	protected Lodging defaultLodging = null;

	/**
	 * The default value of the '{@link #isPetsAllowed() <em>Pets Allowed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPetsAllowed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PETS_ALLOWED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPetsAllowed() <em>Pets Allowed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPetsAllowed()
	 * @generated
	 * @ordered
	 */
	protected boolean petsAllowed = PETS_ALLOWED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AdventureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return AdventurePackage.eINSTANCE.getAdventure();
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
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ADVENTURE__ID, oldId, id));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ADVENTURE__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ADVENTURE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(String newLocation) {
		String oldLocation = location;
		location = newLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ADVENTURE__LOCATION, oldLocation, location));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ADVENTURE__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getDefaultActivities() {
		if (defaultActivities == null) {
			defaultActivities = new EObjectResolvingEList(Activity.class, this, AdventurePackage.ADVENTURE__DEFAULT_ACTIVITIES);
		}
		return defaultActivities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Category getCategory() {
		if (eContainerFeatureID != AdventurePackage.ADVENTURE__CATEGORY) return null;
		return (Category)eContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCategory(Category newCategory) {
		if (newCategory != eContainer || (eContainerFeatureID != AdventurePackage.ADVENTURE__CATEGORY && newCategory != null)) {
			if (EcoreUtil.isAncestor(this, newCategory))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eContainer != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newCategory != null)
				msgs = ((InternalEObject)newCategory).eInverseAdd(this, AdventurePackage.CATEGORY__ADVENTURES, Category.class, msgs);
			msgs = eBasicSetContainer((InternalEObject)newCategory, AdventurePackage.ADVENTURE__CATEGORY, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ADVENTURE__CATEGORY, newCategory, newCategory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Lodging getDefaultLodging() {
		if (defaultLodging != null && defaultLodging.eIsProxy()) {
			Lodging oldDefaultLodging = defaultLodging;
			defaultLodging = (Lodging)eResolveProxy((InternalEObject)defaultLodging);
			if (defaultLodging != oldDefaultLodging) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.ADVENTURE__DEFAULT_LODGING, oldDefaultLodging, defaultLodging));
			}
		}
		return defaultLodging;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Lodging basicGetDefaultLodging() {
		return defaultLodging;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultLodging(Lodging newDefaultLodging) {
		Lodging oldDefaultLodging = defaultLodging;
		defaultLodging = newDefaultLodging;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ADVENTURE__DEFAULT_LODGING, oldDefaultLodging, defaultLodging));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isPetsAllowed() {
		return petsAllowed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPetsAllowed(boolean newPetsAllowed) {
		boolean oldPetsAllowed = petsAllowed;
		petsAllowed = newPetsAllowed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.ADVENTURE__PETS_ALLOWED, oldPetsAllowed, petsAllowed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case AdventurePackage.ADVENTURE__CATEGORY:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, AdventurePackage.ADVENTURE__CATEGORY, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case AdventurePackage.ADVENTURE__CATEGORY:
					return eBasicSetContainer(null, AdventurePackage.ADVENTURE__CATEGORY, msgs);
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
	public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs) {
		if (eContainerFeatureID >= 0) {
			switch (eContainerFeatureID) {
				case AdventurePackage.ADVENTURE__CATEGORY:
					return eContainer.eInverseRemove(this, AdventurePackage.CATEGORY__ADVENTURES, Category.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return eContainer.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case AdventurePackage.ADVENTURE__ID:
				return getId();
			case AdventurePackage.ADVENTURE__NAME:
				return getName();
			case AdventurePackage.ADVENTURE__DESCRIPTION:
				return getDescription();
			case AdventurePackage.ADVENTURE__LOCATION:
				return getLocation();
			case AdventurePackage.ADVENTURE__PRICE:
				return new Double(getPrice());
			case AdventurePackage.ADVENTURE__DEFAULT_ACTIVITIES:
				return getDefaultActivities();
			case AdventurePackage.ADVENTURE__CATEGORY:
				return getCategory();
			case AdventurePackage.ADVENTURE__DEFAULT_LODGING:
				if (resolve) return getDefaultLodging();
				return basicGetDefaultLodging();
			case AdventurePackage.ADVENTURE__PETS_ALLOWED:
				return isPetsAllowed() ? Boolean.TRUE : Boolean.FALSE;
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
			case AdventurePackage.ADVENTURE__ID:
				setId((String)newValue);
				return;
			case AdventurePackage.ADVENTURE__NAME:
				setName((String)newValue);
				return;
			case AdventurePackage.ADVENTURE__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case AdventurePackage.ADVENTURE__LOCATION:
				setLocation((String)newValue);
				return;
			case AdventurePackage.ADVENTURE__PRICE:
				setPrice(((Double)newValue).doubleValue());
				return;
			case AdventurePackage.ADVENTURE__DEFAULT_ACTIVITIES:
				getDefaultActivities().clear();
				getDefaultActivities().addAll((Collection)newValue);
				return;
			case AdventurePackage.ADVENTURE__CATEGORY:
				setCategory((Category)newValue);
				return;
			case AdventurePackage.ADVENTURE__DEFAULT_LODGING:
				setDefaultLodging((Lodging)newValue);
				return;
			case AdventurePackage.ADVENTURE__PETS_ALLOWED:
				setPetsAllowed(((Boolean)newValue).booleanValue());
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
			case AdventurePackage.ADVENTURE__ID:
				setId(ID_EDEFAULT);
				return;
			case AdventurePackage.ADVENTURE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AdventurePackage.ADVENTURE__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case AdventurePackage.ADVENTURE__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case AdventurePackage.ADVENTURE__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case AdventurePackage.ADVENTURE__DEFAULT_ACTIVITIES:
				getDefaultActivities().clear();
				return;
			case AdventurePackage.ADVENTURE__CATEGORY:
				setCategory((Category)null);
				return;
			case AdventurePackage.ADVENTURE__DEFAULT_LODGING:
				setDefaultLodging((Lodging)null);
				return;
			case AdventurePackage.ADVENTURE__PETS_ALLOWED:
				setPetsAllowed(PETS_ALLOWED_EDEFAULT);
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
			case AdventurePackage.ADVENTURE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case AdventurePackage.ADVENTURE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AdventurePackage.ADVENTURE__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case AdventurePackage.ADVENTURE__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case AdventurePackage.ADVENTURE__PRICE:
				return price != PRICE_EDEFAULT;
			case AdventurePackage.ADVENTURE__DEFAULT_ACTIVITIES:
				return defaultActivities != null && !defaultActivities.isEmpty();
			case AdventurePackage.ADVENTURE__CATEGORY:
				return getCategory() != null;
			case AdventurePackage.ADVENTURE__DEFAULT_LODGING:
				return defaultLodging != null;
			case AdventurePackage.ADVENTURE__PETS_ALLOWED:
				return petsAllowed != PETS_ALLOWED_EDEFAULT;
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
		result.append(", name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(", location: ");
		result.append(location);
		result.append(", price: ");
		result.append(price);
		result.append(", petsAllowed: ");
		result.append(petsAllowed);
		result.append(')');
		return result.toString();
	}

} //AdventureImpl
