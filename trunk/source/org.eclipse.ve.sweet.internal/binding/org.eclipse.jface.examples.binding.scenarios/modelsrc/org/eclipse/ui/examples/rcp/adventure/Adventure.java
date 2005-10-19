/**
 * <copyright>
 * </copyright>
 *
 * $Id: Adventure.java,v 1.2 2005-10-19 21:47:59 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Adventure</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getPrice <em>Price</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getDefaultActivities <em>Default Activities</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getDefaultLodging <em>Default Lodging</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Adventure#isPetsAllowed <em>Pets Allowed</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure()
 * @model
 * @generated
 */
public interface Adventure extends EObject{
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
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getId <em>Id</em>}' attribute.
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
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' attribute.
	 * @see #setLocation(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_Location()
	 * @model
	 * @generated
	 */
	String getLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(String value);

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
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_Price()
	 * @model
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(double value);

	/**
	 * Returns the value of the '<em><b>Default Activities</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Activity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Activities</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Activities</em>' reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_DefaultActivities()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Activity"
	 * @generated
	 */
	EList getDefaultActivities();

	/**
	 * Returns the value of the '<em><b>Category</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ui.examples.rcp.adventure.Category#getAdventures <em>Adventures</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' container reference.
	 * @see #setCategory(Category)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_Category()
	 * @see org.eclipse.ui.examples.rcp.adventure.Category#getAdventures
	 * @model opposite="adventures"
	 * @generated
	 */
	Category getCategory();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getCategory <em>Category</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category</em>' container reference.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(Category value);

	/**
	 * Returns the value of the '<em><b>Default Lodging</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Lodging</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Lodging</em>' reference.
	 * @see #setDefaultLodging(Lodging)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_DefaultLodging()
	 * @model
	 * @generated
	 */
	Lodging getDefaultLodging();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getDefaultLodging <em>Default Lodging</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Lodging</em>' reference.
	 * @see #getDefaultLodging()
	 * @generated
	 */
	void setDefaultLodging(Lodging value);

	/**
	 * Returns the value of the '<em><b>Pets Allowed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pets Allowed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pets Allowed</em>' attribute.
	 * @see #setPetsAllowed(boolean)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getAdventure_PetsAllowed()
	 * @model
	 * @generated
	 */
	boolean isPetsAllowed();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#isPetsAllowed <em>Pets Allowed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pets Allowed</em>' attribute.
	 * @see #isPetsAllowed()
	 * @generated
	 */
	void setPetsAllowed(boolean value);

} // Adventure
