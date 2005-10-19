/**
 * <copyright>
 * </copyright>
 *
 * $Id: AdventurePackage.java,v 1.2 2005-10-19 21:47:59 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.ui.examples.rcp.adventure.AdventureFactory
 * @model kind="package"
 * @generated
 */
public interface AdventurePackage extends EPackage{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "adventure";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "org.eclipse.ui.examples.rcp";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.ui.examples.rcp";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AdventurePackage eINSTANCE = org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl <em>Catalog</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.CatalogImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getCatalog()
	 * @generated
	 */
	int CATALOG = 0;

	/**
	 * The feature id for the '<em><b>Last Modified</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__LAST_MODIFIED = 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__SOURCE = 1;

	/**
	 * The feature id for the '<em><b>Time To Read</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__TIME_TO_READ = 2;

	/**
	 * The feature id for the '<em><b>Categories</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__CATEGORIES = 3;

	/**
	 * The feature id for the '<em><b>Lodgings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__LODGINGS = 4;

	/**
	 * The feature id for the '<em><b>Activities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__ACTIVITIES = 5;

	/**
	 * The feature id for the '<em><b>Transportations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__TRANSPORTATIONS = 6;

	/**
	 * The feature id for the '<em><b>Signons</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__SIGNONS = 7;

	/**
	 * The feature id for the '<em><b>Accounts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG__ACCOUNTS = 8;

	/**
	 * The number of structural features of the the '<em>Catalog</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATALOG_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.CategoryImpl <em>Category</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.CategoryImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getCategory()
	 * @generated
	 */
	int CATEGORY = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__NAME = 1;

	/**
	 * The feature id for the '<em><b>Adventures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ADVENTURES = 2;

	/**
	 * The number of structural features of the the '<em>Category</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl <em>Adventure</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventureImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getAdventure()
	 * @generated
	 */
	int ADVENTURE = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__DESCRIPTION = 2;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__LOCATION = 3;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__PRICE = 4;

	/**
	 * The feature id for the '<em><b>Default Activities</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__DEFAULT_ACTIVITIES = 5;

	/**
	 * The feature id for the '<em><b>Category</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__CATEGORY = 6;

	/**
	 * The feature id for the '<em><b>Default Lodging</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__DEFAULT_LODGING = 7;

	/**
	 * The feature id for the '<em><b>Pets Allowed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE__PETS_ALLOWED = 8;

	/**
	 * The number of structural features of the the '<em>Adventure</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADVENTURE_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.ActivityImpl <em>Activity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.ActivityImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getActivity()
	 * @generated
	 */
	int ACTIVITY = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY__NAME = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY__DESCRIPTION = 2;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY__LOCATION = 3;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY__PRICE = 4;

	/**
	 * The number of structural features of the the '<em>Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.LodgingImpl <em>Lodging</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.LodgingImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getLodging()
	 * @generated
	 */
	int LODGING = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LODGING__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LODGING__NAME = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LODGING__DESCRIPTION = 2;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LODGING__LOCATION = 3;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LODGING__PRICE = 4;

	/**
	 * The number of structural features of the the '<em>Lodging</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LODGING_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl <em>Transportation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.TransportationImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getTransportation()
	 * @generated
	 */
	int TRANSPORTATION = 5;

	/**
	 * The feature id for the '<em><b>Arrival Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__ARRIVAL_TIME = 0;

	/**
	 * The feature id for the '<em><b>Carrier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__CARRIER = 1;

	/**
	 * The feature id for the '<em><b>Departure Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__DEPARTURE_TIME = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Destination</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__DESTINATION = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__ID = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__NAME = 6;

	/**
	 * The feature id for the '<em><b>Origin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__ORIGIN = 7;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__PRICE = 8;

	/**
	 * The feature id for the '<em><b>Transportation Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION__TRANSPORTATION_CLASS = 9;

	/**
	 * The number of structural features of the the '<em>Transportation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSPORTATION_FEATURE_COUNT = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.SignonImpl <em>Signon</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.SignonImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getSignon()
	 * @generated
	 */
	int SIGNON = 6;

	/**
	 * The feature id for the '<em><b>User Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIGNON__USER_NAME = 0;

	/**
	 * The feature id for the '<em><b>Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIGNON__PASSWORD = 1;

	/**
	 * The number of structural features of the the '<em>Signon</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIGNON_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl <em>Account</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AccountImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getAccount()
	 * @generated
	 */
	int ACCOUNT = 7;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__ID = 0;

	/**
	 * The feature id for the '<em><b>Address1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__ADDRESS1 = 1;

	/**
	 * The feature id for the '<em><b>Address2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__ADDRESS2 = 2;

	/**
	 * The feature id for the '<em><b>City</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__CITY = 3;

	/**
	 * The feature id for the '<em><b>Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__COUNTRY = 4;

	/**
	 * The feature id for the '<em><b>Email</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__EMAIL = 5;

	/**
	 * The feature id for the '<em><b>First Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__FIRST_NAME = 6;

	/**
	 * The feature id for the '<em><b>Last Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__LAST_NAME = 7;

	/**
	 * The feature id for the '<em><b>Phone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__PHONE = 8;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__STATE = 9;

	/**
	 * The feature id for the '<em><b>Zip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT__ZIP = 10;

	/**
	 * The number of structural features of the the '<em>Account</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCOUNT_FEATURE_COUNT = 11;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.CartImpl <em>Cart</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.CartImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getCart()
	 * @generated
	 */
	int CART = 8;

	/**
	 * The feature id for the '<em><b>Catalog</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__CATALOG = 0;

	/**
	 * The feature id for the '<em><b>Adventure</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__ADVENTURE = 1;

	/**
	 * The feature id for the '<em><b>Adventure Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__ADVENTURE_DAYS = 2;

	/**
	 * The feature id for the '<em><b>Head Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__HEAD_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Lodging</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__LODGING = 4;

	/**
	 * The feature id for the '<em><b>Lodging Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__LODGING_DAYS = 5;

	/**
	 * The feature id for the '<em><b>Lodging Room Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__LODGING_ROOM_COUNT = 6;

	/**
	 * The feature id for the '<em><b>Lodging Total</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__LODGING_TOTAL = 7;

	/**
	 * The feature id for the '<em><b>Activities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__ACTIVITIES = 8;

	/**
	 * The feature id for the '<em><b>Activity Total</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__ACTIVITY_TOTAL = 9;

	/**
	 * The feature id for the '<em><b>Departure Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__DEPARTURE_DATE = 10;

	/**
	 * The feature id for the '<em><b>Transportation Total</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__TRANSPORTATION_TOTAL = 11;

	/**
	 * The feature id for the '<em><b>Total Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__TOTAL_PRICE = 12;

	/**
	 * The feature id for the '<em><b>Departure Flight</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__DEPARTURE_FLIGHT = 13;

	/**
	 * The feature id for the '<em><b>Return Flight</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART__RETURN_FLIGHT = 14;

	/**
	 * The number of structural features of the the '<em>Cart</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART_FEATURE_COUNT = 15;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.CartActivityEntryImpl <em>Cart Activity Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.CartActivityEntryImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getCartActivityEntry()
	 * @generated
	 */
	int CART_ACTIVITY_ENTRY = 9;

	/**
	 * The feature id for the '<em><b>Activity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART_ACTIVITY_ENTRY__ACTIVITY = 0;

	/**
	 * The feature id for the '<em><b>Num People</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART_ACTIVITY_ENTRY__NUM_PEOPLE = 1;

	/**
	 * The number of structural features of the the '<em>Cart Activity Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CART_ACTIVITY_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.GlobalSettingsImpl <em>Global Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.GlobalSettingsImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getGlobalSettings()
	 * @generated
	 */
	int GLOBAL_SETTINGS = 10;

	/**
	 * The feature id for the '<em><b>Disconnected Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GLOBAL_SETTINGS__DISCONNECTED_MODE = 0;

	/**
	 * The feature id for the '<em><b>Cache Catalog</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GLOBAL_SETTINGS__CACHE_CATALOG = 1;

	/**
	 * The feature id for the '<em><b>Server Address Modifiable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GLOBAL_SETTINGS__SERVER_ADDRESS_MODIFIABLE = 2;

	/**
	 * The feature id for the '<em><b>Clear Cache</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GLOBAL_SETTINGS__CLEAR_CACHE = 3;

	/**
	 * The number of structural features of the the '<em>Global Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GLOBAL_SETTINGS_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.OrderImpl <em>Order</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.OrderImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getOrder()
	 * @generated
	 */
	int ORDER = 11;

	/**
	 * The feature id for the '<em><b>Cart</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDER__CART = 0;

	/**
	 * The feature id for the '<em><b>Account</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDER__ACCOUNT = 1;

	/**
	 * The feature id for the '<em><b>Order Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDER__ORDER_ID = 2;

	/**
	 * The number of structural features of the the '<em>Order</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDER_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.ui.examples.rcp.adventure.impl.AppModelImpl <em>App Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AppModelImpl
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getAppModel()
	 * @generated
	 */
	int APP_MODEL = 12;

	/**
	 * The feature id for the '<em><b>Global Settings</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APP_MODEL__GLOBAL_SETTINGS = 0;

	/**
	 * The feature id for the '<em><b>Catalog</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APP_MODEL__CATALOG = 1;

	/**
	 * The feature id for the '<em><b>Orders</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APP_MODEL__ORDERS = 2;

	/**
	 * The number of structural features of the the '<em>App Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APP_MODEL_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '<em>Java Calendar</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.Calendar
	 * @see org.eclipse.ui.examples.rcp.adventure.impl.AdventurePackageImpl#getJavaCalendar()
	 * @generated
	 */
	int JAVA_CALENDAR = 13;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Catalog <em>Catalog</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Catalog</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog
	 * @generated
	 */
	EClass getCatalog();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getLastModified <em>Last Modified</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Modified</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getLastModified()
	 * @see #getCatalog()
	 * @generated
	 */
	EAttribute getCatalog_LastModified();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getSource()
	 * @see #getCatalog()
	 * @generated
	 */
	EAttribute getCatalog_Source();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getTimeToRead <em>Time To Read</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time To Read</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getTimeToRead()
	 * @see #getCatalog()
	 * @generated
	 */
	EAttribute getCatalog_TimeToRead();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getCategories <em>Categories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Categories</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getCategories()
	 * @see #getCatalog()
	 * @generated
	 */
	EReference getCatalog_Categories();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getLodgings <em>Lodgings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lodgings</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getLodgings()
	 * @see #getCatalog()
	 * @generated
	 */
	EReference getCatalog_Lodgings();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getActivities <em>Activities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Activities</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getActivities()
	 * @see #getCatalog()
	 * @generated
	 */
	EReference getCatalog_Activities();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getTransportations <em>Transportations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Transportations</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getTransportations()
	 * @see #getCatalog()
	 * @generated
	 */
	EReference getCatalog_Transportations();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getSignons <em>Signons</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Signons</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getSignons()
	 * @see #getCatalog()
	 * @generated
	 */
	EReference getCatalog_Signons();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getAccounts <em>Accounts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Accounts</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Catalog#getAccounts()
	 * @see #getCatalog()
	 * @generated
	 */
	EReference getCatalog_Accounts();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Category <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Category</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Category
	 * @generated
	 */
	EClass getCategory();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Category#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Category#getId()
	 * @see #getCategory()
	 * @generated
	 */
	EAttribute getCategory_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Category#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Category#getName()
	 * @see #getCategory()
	 * @generated
	 */
	EAttribute getCategory_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ui.examples.rcp.adventure.Category#getAdventures <em>Adventures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Adventures</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Category#getAdventures()
	 * @see #getCategory()
	 * @generated
	 */
	EReference getCategory_Adventures();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Adventure <em>Adventure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Adventure</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure
	 * @generated
	 */
	EClass getAdventure();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getId()
	 * @see #getAdventure()
	 * @generated
	 */
	EAttribute getAdventure_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getName()
	 * @see #getAdventure()
	 * @generated
	 */
	EAttribute getAdventure_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getDescription()
	 * @see #getAdventure()
	 * @generated
	 */
	EAttribute getAdventure_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getLocation()
	 * @see #getAdventure()
	 * @generated
	 */
	EAttribute getAdventure_Location();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getPrice()
	 * @see #getAdventure()
	 * @generated
	 */
	EAttribute getAdventure_Price();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getDefaultActivities <em>Default Activities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Default Activities</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getDefaultActivities()
	 * @see #getAdventure()
	 * @generated
	 */
	EReference getAdventure_DefaultActivities();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Category</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getCategory()
	 * @see #getAdventure()
	 * @generated
	 */
	EReference getAdventure_Category();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getDefaultLodging <em>Default Lodging</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Default Lodging</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getDefaultLodging()
	 * @see #getAdventure()
	 * @generated
	 */
	EReference getAdventure_DefaultLodging();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#isPetsAllowed <em>Pets Allowed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pets Allowed</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#isPetsAllowed()
	 * @see #getAdventure()
	 * @generated
	 */
	EAttribute getAdventure_PetsAllowed();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Activity <em>Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Activity</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Activity
	 * @generated
	 */
	EClass getActivity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Activity#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Activity#getId()
	 * @see #getActivity()
	 * @generated
	 */
	EAttribute getActivity_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Activity#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Activity#getName()
	 * @see #getActivity()
	 * @generated
	 */
	EAttribute getActivity_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Activity#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Activity#getDescription()
	 * @see #getActivity()
	 * @generated
	 */
	EAttribute getActivity_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Activity#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Activity#getLocation()
	 * @see #getActivity()
	 * @generated
	 */
	EAttribute getActivity_Location();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Activity#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Activity#getPrice()
	 * @see #getActivity()
	 * @generated
	 */
	EAttribute getActivity_Price();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Lodging <em>Lodging</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lodging</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Lodging
	 * @generated
	 */
	EClass getLodging();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Lodging#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Lodging#getId()
	 * @see #getLodging()
	 * @generated
	 */
	EAttribute getLodging_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Lodging#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Lodging#getName()
	 * @see #getLodging()
	 * @generated
	 */
	EAttribute getLodging_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Lodging#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Lodging#getDescription()
	 * @see #getLodging()
	 * @generated
	 */
	EAttribute getLodging_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Lodging#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Lodging#getLocation()
	 * @see #getLodging()
	 * @generated
	 */
	EAttribute getLodging_Location();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Lodging#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Lodging#getPrice()
	 * @see #getLodging()
	 * @generated
	 */
	EAttribute getLodging_Price();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Transportation <em>Transportation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transportation</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation
	 * @generated
	 */
	EClass getTransportation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getArrivalTime <em>Arrival Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arrival Time</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getArrivalTime()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_ArrivalTime();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getCarrier <em>Carrier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Carrier</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getCarrier()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_Carrier();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDepartureTime <em>Departure Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Departure Time</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getDepartureTime()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_DepartureTime();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getDescription()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getDestination <em>Destination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Destination</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getDestination()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_Destination();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getId()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getName()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getOrigin <em>Origin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getOrigin()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_Origin();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getPrice()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_Price();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Transportation#getTransportationClass <em>Transportation Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transportation Class</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Transportation#getTransportationClass()
	 * @see #getTransportation()
	 * @generated
	 */
	EAttribute getTransportation_TransportationClass();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Signon <em>Signon</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Signon</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Signon
	 * @generated
	 */
	EClass getSignon();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Signon#getUserName <em>User Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>User Name</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Signon#getUserName()
	 * @see #getSignon()
	 * @generated
	 */
	EAttribute getSignon_UserName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Signon#getPassword <em>Password</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Password</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Signon#getPassword()
	 * @see #getSignon()
	 * @generated
	 */
	EAttribute getSignon_Password();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Account <em>Account</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Account</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account
	 * @generated
	 */
	EClass getAccount();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getId()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getAddress1 <em>Address1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Address1</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getAddress1()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_Address1();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getAddress2 <em>Address2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Address2</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getAddress2()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_Address2();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getCity <em>City</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>City</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getCity()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_City();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getCountry <em>Country</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Country</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getCountry()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_Country();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getEmail <em>Email</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Email</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getEmail()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_Email();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getFirstName <em>First Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>First Name</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getFirstName()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_FirstName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getLastName <em>Last Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Name</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getLastName()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_LastName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getPhone <em>Phone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Phone</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getPhone()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_Phone();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getState <em>State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getState()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_State();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Account#getZip <em>Zip</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Zip</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Account#getZip()
	 * @see #getAccount()
	 * @generated
	 */
	EAttribute getAccount_Zip();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Cart <em>Cart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cart</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart
	 * @generated
	 */
	EClass getCart();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getCatalog <em>Catalog</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Catalog</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getCatalog()
	 * @see #getCart()
	 * @generated
	 */
	EReference getCart_Catalog();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getAdventure <em>Adventure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Adventure</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getAdventure()
	 * @see #getCart()
	 * @generated
	 */
	EReference getCart_Adventure();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getAdventureDays <em>Adventure Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Adventure Days</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getAdventureDays()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_AdventureDays();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getHeadCount <em>Head Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Head Count</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getHeadCount()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_HeadCount();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodging <em>Lodging</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lodging</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getLodging()
	 * @see #getCart()
	 * @generated
	 */
	EReference getCart_Lodging();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingDays <em>Lodging Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lodging Days</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingDays()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_LodgingDays();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingRoomCount <em>Lodging Room Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lodging Room Count</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingRoomCount()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_LodgingRoomCount();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingTotal <em>Lodging Total</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lodging Total</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getLodgingTotal()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_LodgingTotal();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getActivities <em>Activities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Activities</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getActivities()
	 * @see #getCart()
	 * @generated
	 */
	EReference getCart_Activities();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getActivityTotal <em>Activity Total</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Activity Total</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getActivityTotal()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_ActivityTotal();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getDepartureDate <em>Departure Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Departure Date</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getDepartureDate()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_DepartureDate();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getTransportationTotal <em>Transportation Total</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transportation Total</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getTransportationTotal()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_TransportationTotal();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getTotalPrice <em>Total Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Price</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getTotalPrice()
	 * @see #getCart()
	 * @generated
	 */
	EAttribute getCart_TotalPrice();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getDepartureFlight <em>Departure Flight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Departure Flight</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getDepartureFlight()
	 * @see #getCart()
	 * @generated
	 */
	EReference getCart_DepartureFlight();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.Cart#getReturnFlight <em>Return Flight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Return Flight</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Cart#getReturnFlight()
	 * @see #getCart()
	 * @generated
	 */
	EReference getCart_ReturnFlight();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.CartActivityEntry <em>Cart Activity Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cart Activity Entry</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.CartActivityEntry
	 * @generated
	 */
	EClass getCartActivityEntry();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.CartActivityEntry#getActivity <em>Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Activity</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.CartActivityEntry#getActivity()
	 * @see #getCartActivityEntry()
	 * @generated
	 */
	EReference getCartActivityEntry_Activity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.CartActivityEntry#getNumPeople <em>Num People</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Num People</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.CartActivityEntry#getNumPeople()
	 * @see #getCartActivityEntry()
	 * @generated
	 */
	EAttribute getCartActivityEntry_NumPeople();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings <em>Global Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Global Settings</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.GlobalSettings
	 * @generated
	 */
	EClass getGlobalSettings();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isDisconnectedMode <em>Disconnected Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Disconnected Mode</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isDisconnectedMode()
	 * @see #getGlobalSettings()
	 * @generated
	 */
	EAttribute getGlobalSettings_DisconnectedMode();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isCacheCatalog <em>Cache Catalog</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cache Catalog</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isCacheCatalog()
	 * @see #getGlobalSettings()
	 * @generated
	 */
	EAttribute getGlobalSettings_CacheCatalog();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isServerAddressModifiable <em>Server Address Modifiable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Server Address Modifiable</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isServerAddressModifiable()
	 * @see #getGlobalSettings()
	 * @generated
	 */
	EAttribute getGlobalSettings_ServerAddressModifiable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isClearCache <em>Clear Cache</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Clear Cache</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.GlobalSettings#isClearCache()
	 * @see #getGlobalSettings()
	 * @generated
	 */
	EAttribute getGlobalSettings_ClearCache();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.Order <em>Order</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Order</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Order
	 * @generated
	 */
	EClass getOrder();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.Order#getCart <em>Cart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cart</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Order#getCart()
	 * @see #getOrder()
	 * @generated
	 */
	EReference getOrder_Cart();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.Order#getAccount <em>Account</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Account</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Order#getAccount()
	 * @see #getOrder()
	 * @generated
	 */
	EReference getOrder_Account();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ui.examples.rcp.adventure.Order#getOrderId <em>Order Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Order Id</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.Order#getOrderId()
	 * @see #getOrder()
	 * @generated
	 */
	EAttribute getOrder_OrderId();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ui.examples.rcp.adventure.AppModel <em>App Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>App Model</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.AppModel
	 * @generated
	 */
	EClass getAppModel();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.AppModel#getGlobalSettings <em>Global Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Global Settings</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.AppModel#getGlobalSettings()
	 * @see #getAppModel()
	 * @generated
	 */
	EReference getAppModel_GlobalSettings();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ui.examples.rcp.adventure.AppModel#getCatalog <em>Catalog</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Catalog</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.AppModel#getCatalog()
	 * @see #getAppModel()
	 * @generated
	 */
	EReference getAppModel_Catalog();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ui.examples.rcp.adventure.AppModel#getOrders <em>Orders</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Orders</em>'.
	 * @see org.eclipse.ui.examples.rcp.adventure.AppModel#getOrders()
	 * @see #getAppModel()
	 * @generated
	 */
	EReference getAppModel_Orders();

	/**
	 * Returns the meta object for data type '{@link java.util.Calendar <em>Java Calendar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Java Calendar</em>'.
	 * @see java.util.Calendar
	 * @model instanceClass="java.util.Calendar"
	 * @generated
	 */
	EDataType getJavaCalendar();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AdventureFactory getAdventureFactory();

} //AdventurePackage
