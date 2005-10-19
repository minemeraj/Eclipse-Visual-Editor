/**
 * <copyright>
 * </copyright>
 *
 * $Id: CartActivityEntryImpl.java,v 1.1 2005-10-19 18:35:45 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.ui.examples.rcp.adventure.Activity;
import org.eclipse.ui.examples.rcp.adventure.AdventurePackage;
import org.eclipse.ui.examples.rcp.adventure.CartActivityEntry;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cart Activity Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartActivityEntryImpl#getActivity <em>Activity</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.impl.CartActivityEntryImpl#getNumPeople <em>Num People</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CartActivityEntryImpl extends EObjectImpl implements CartActivityEntry {
	/**
	 * The cached value of the '{@link #getActivity() <em>Activity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivity()
	 * @generated
	 * @ordered
	 */
	protected Activity activity = null;

	/**
	 * The default value of the '{@link #getNumPeople() <em>Num People</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumPeople()
	 * @generated
	 * @ordered
	 */
	protected static final int NUM_PEOPLE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumPeople() <em>Num People</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumPeople()
	 * @generated
	 * @ordered
	 */
	protected int numPeople = NUM_PEOPLE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CartActivityEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return AdventurePackage.eINSTANCE.getCartActivityEntry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Activity getActivity() {
		if (activity != null && activity.eIsProxy()) {
			Activity oldActivity = activity;
			activity = (Activity)eResolveProxy((InternalEObject)activity);
			if (activity != oldActivity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AdventurePackage.CART_ACTIVITY_ENTRY__ACTIVITY, oldActivity, activity));
			}
		}
		return activity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Activity basicGetActivity() {
		return activity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActivity(Activity newActivity) {
		Activity oldActivity = activity;
		activity = newActivity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART_ACTIVITY_ENTRY__ACTIVITY, oldActivity, activity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumPeople() {
		return numPeople;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumPeople(int newNumPeople) {
		int oldNumPeople = numPeople;
		numPeople = newNumPeople;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdventurePackage.CART_ACTIVITY_ENTRY__NUM_PEOPLE, oldNumPeople, numPeople));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case AdventurePackage.CART_ACTIVITY_ENTRY__ACTIVITY:
				if (resolve) return getActivity();
				return basicGetActivity();
			case AdventurePackage.CART_ACTIVITY_ENTRY__NUM_PEOPLE:
				return new Integer(getNumPeople());
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
			case AdventurePackage.CART_ACTIVITY_ENTRY__ACTIVITY:
				setActivity((Activity)newValue);
				return;
			case AdventurePackage.CART_ACTIVITY_ENTRY__NUM_PEOPLE:
				setNumPeople(((Integer)newValue).intValue());
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
			case AdventurePackage.CART_ACTIVITY_ENTRY__ACTIVITY:
				setActivity((Activity)null);
				return;
			case AdventurePackage.CART_ACTIVITY_ENTRY__NUM_PEOPLE:
				setNumPeople(NUM_PEOPLE_EDEFAULT);
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
			case AdventurePackage.CART_ACTIVITY_ENTRY__ACTIVITY:
				return activity != null;
			case AdventurePackage.CART_ACTIVITY_ENTRY__NUM_PEOPLE:
				return numPeople != NUM_PEOPLE_EDEFAULT;
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
		result.append(" (numPeople: ");
		result.append(numPeople);
		result.append(')');
		return result.toString();
	}

} //CartActivityEntryImpl
