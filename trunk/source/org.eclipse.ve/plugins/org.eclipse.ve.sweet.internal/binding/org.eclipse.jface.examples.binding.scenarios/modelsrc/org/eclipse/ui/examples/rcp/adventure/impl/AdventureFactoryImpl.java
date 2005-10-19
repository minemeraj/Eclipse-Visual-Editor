/**
 * <copyright>
 * </copyright>
 *
 * $Id: AdventureFactoryImpl.java,v 1.1 2005-10-19 18:35:45 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure.impl;

import java.util.Calendar;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.ui.examples.rcp.adventure.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AdventureFactoryImpl extends EFactoryImpl implements AdventureFactory {
	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdventureFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case AdventurePackage.CATALOG: return createCatalog();
			case AdventurePackage.CATEGORY: return createCategory();
			case AdventurePackage.ADVENTURE: return createAdventure();
			case AdventurePackage.ACTIVITY: return createActivity();
			case AdventurePackage.LODGING: return createLodging();
			case AdventurePackage.TRANSPORTATION: return createTransportation();
			case AdventurePackage.SIGNON: return createSignon();
			case AdventurePackage.ACCOUNT: return createAccount();
			case AdventurePackage.CART: return createCart();
			case AdventurePackage.CART_ACTIVITY_ENTRY: return createCartActivityEntry();
			case AdventurePackage.GLOBAL_SETTINGS: return createGlobalSettings();
			case AdventurePackage.ORDER: return createOrder();
			case AdventurePackage.APP_MODEL: return createAppModel();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case AdventurePackage.JAVA_CALENDAR:
				return createJavaCalendarFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case AdventurePackage.JAVA_CALENDAR:
				return convertJavaCalendarToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Catalog createCatalog() {
		CatalogImpl catalog = new CatalogImpl();
		return catalog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Category createCategory() {
		CategoryImpl category = new CategoryImpl();
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adventure createAdventure() {
		AdventureImpl adventure = new AdventureImpl();
		return adventure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Activity createActivity() {
		ActivityImpl activity = new ActivityImpl();
		return activity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Lodging createLodging() {
		LodgingImpl lodging = new LodgingImpl();
		return lodging;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transportation createTransportation() {
		TransportationImpl transportation = new TransportationImpl();
		return transportation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Signon createSignon() {
		SignonImpl signon = new SignonImpl();
		return signon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Account createAccount() {
		AccountImpl account = new AccountImpl();
		return account;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cart createCart() {
		CartImpl cart = new CartImpl();
		return cart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CartActivityEntry createCartActivityEntry() {
		CartActivityEntryImpl cartActivityEntry = new CartActivityEntryImpl();
		return cartActivityEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GlobalSettings createGlobalSettings() {
		GlobalSettingsImpl globalSettings = new GlobalSettingsImpl();
		return globalSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Order createOrder() {
		OrderImpl order = new OrderImpl();
		return order;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AppModel createAppModel() {
		AppModelImpl appModel = new AppModelImpl();
		return appModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Calendar createJavaCalendarFromString(EDataType eDataType, String initialValue) {
		return (Calendar)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertJavaCalendarToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdventurePackage getAdventurePackage() {
		return (AdventurePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static AdventurePackage getPackage() {
		return AdventurePackage.eINSTANCE;
	}

} //AdventureFactoryImpl
