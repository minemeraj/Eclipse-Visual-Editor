/**
 * <copyright>
 * </copyright>
 *
 * $Id: Catalog.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Catalog</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getLastModified <em>Last Modified</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getTimeToRead <em>Time To Read</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getCategories <em>Categories</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getLodgings <em>Lodgings</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getActivities <em>Activities</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getTransportations <em>Transportations</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getSignons <em>Signons</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getAccounts <em>Accounts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog()
 * @model
 * @generated
 */
public interface Catalog extends EObject {
	/**
	 * Returns the value of the '<em><b>Last Modified</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Modified</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Modified</em>' attribute.
	 * @see #setLastModified(long)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_LastModified()
	 * @model
	 * @generated
	 */
	long getLastModified();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getLastModified <em>Last Modified</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Modified</em>' attribute.
	 * @see #getLastModified()
	 * @generated
	 */
	void setLastModified(long value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see #setSource(String)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_Source()
	 * @model
	 * @generated
	 */
	String getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getSource <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' attribute.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(String value);

	/**
	 * Returns the value of the '<em><b>Time To Read</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time To Read</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time To Read</em>' attribute.
	 * @see #setTimeToRead(long)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_TimeToRead()
	 * @model
	 * @generated
	 */
	long getTimeToRead();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Catalog#getTimeToRead <em>Time To Read</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time To Read</em>' attribute.
	 * @see #getTimeToRead()
	 * @generated
	 */
	void setTimeToRead(long value);

	/**
	 * Returns the value of the '<em><b>Categories</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Category}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Categories</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Categories</em>' containment reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_Categories()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Category" containment="true"
	 * @generated
	 */
	EList getCategories();

	/**
	 * Returns the value of the '<em><b>Lodgings</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Lodging}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lodgings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lodgings</em>' containment reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_Lodgings()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Lodging" containment="true"
	 * @generated
	 */
	EList getLodgings();

	/**
	 * Returns the value of the '<em><b>Activities</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Activity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activities</em>' containment reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_Activities()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Activity" containment="true"
	 * @generated
	 */
	EList getActivities();

	/**
	 * Returns the value of the '<em><b>Transportations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Transportation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transportations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transportations</em>' containment reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_Transportations()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Transportation" containment="true"
	 * @generated
	 */
	EList getTransportations();

	/**
	 * Returns the value of the '<em><b>Signons</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Signon}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Signons</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Signons</em>' containment reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_Signons()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Signon" containment="true"
	 * @generated
	 */
	EList getSignons();

	/**
	 * Returns the value of the '<em><b>Accounts</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Account}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Accounts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accounts</em>' containment reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCatalog_Accounts()
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Account" containment="true"
	 * @generated
	 */
	EList getAccounts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Activity" locationRequired="true"
	 * @generated
	 */
	EList getActivities(String location);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Lodging" locationRequired="true"
	 * @generated
	 */
	EList getLodgings(String location);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model type="java.lang.String"
	 * @generated
	 */
	EList getOriginsForDestination(String destination);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Transportation"
	 * @generated
	 */
	EList getTransportations(String origin, String destination);

} // Catalog
