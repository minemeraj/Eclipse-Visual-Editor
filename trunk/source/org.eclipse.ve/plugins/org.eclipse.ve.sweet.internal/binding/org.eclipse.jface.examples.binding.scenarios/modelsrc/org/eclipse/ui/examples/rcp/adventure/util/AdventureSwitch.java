/**
 * <copyright>
 * </copyright>
 *
 * $Id: AdventureSwitch.java,v 1.1 2005-10-19 18:35:45 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.ui.examples.rcp.adventure.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage
 * @generated
 */
public class AdventureSwitch {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AdventurePackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdventureSwitch() {
		if (modelPackage == null) {
			modelPackage = AdventurePackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch((EClass)eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case AdventurePackage.CATALOG: {
				Catalog catalog = (Catalog)theEObject;
				Object result = caseCatalog(catalog);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.CATEGORY: {
				Category category = (Category)theEObject;
				Object result = caseCategory(category);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.ADVENTURE: {
				Adventure adventure = (Adventure)theEObject;
				Object result = caseAdventure(adventure);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.ACTIVITY: {
				Activity activity = (Activity)theEObject;
				Object result = caseActivity(activity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.LODGING: {
				Lodging lodging = (Lodging)theEObject;
				Object result = caseLodging(lodging);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.TRANSPORTATION: {
				Transportation transportation = (Transportation)theEObject;
				Object result = caseTransportation(transportation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.SIGNON: {
				Signon signon = (Signon)theEObject;
				Object result = caseSignon(signon);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.ACCOUNT: {
				Account account = (Account)theEObject;
				Object result = caseAccount(account);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.CART: {
				Cart cart = (Cart)theEObject;
				Object result = caseCart(cart);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.CART_ACTIVITY_ENTRY: {
				CartActivityEntry cartActivityEntry = (CartActivityEntry)theEObject;
				Object result = caseCartActivityEntry(cartActivityEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.GLOBAL_SETTINGS: {
				GlobalSettings globalSettings = (GlobalSettings)theEObject;
				Object result = caseGlobalSettings(globalSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.ORDER: {
				Order order = (Order)theEObject;
				Object result = caseOrder(order);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdventurePackage.APP_MODEL: {
				AppModel appModel = (AppModel)theEObject;
				Object result = caseAppModel(appModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Catalog</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Catalog</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseCatalog(Catalog object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Category</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Category</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseCategory(Category object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Adventure</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Adventure</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAdventure(Adventure object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Activity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Activity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseActivity(Activity object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Lodging</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Lodging</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLodging(Lodging object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Transportation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Transportation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTransportation(Transportation object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Signon</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Signon</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseSignon(Signon object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Account</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Account</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAccount(Account object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Cart</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Cart</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseCart(Cart object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Cart Activity Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Cart Activity Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseCartActivityEntry(CartActivityEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Global Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Global Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGlobalSettings(GlobalSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Order</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Order</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseOrder(Order object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>App Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>App Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAppModel(AppModel object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} //AdventureSwitch
