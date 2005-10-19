/**
 * <copyright>
 * </copyright>
 *
 * $Id: Category.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Category</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Category#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Category#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.Category#getAdventures <em>Adventures</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCategory()
 * @model
 * @generated
 */
public interface Category extends EObject {
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
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCategory_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Category#getId <em>Id</em>}' attribute.
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
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCategory_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.Category#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Adventures</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ui.examples.rcp.adventure.Adventure}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.ui.examples.rcp.adventure.Adventure#getCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adventures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Adventures</em>' containment reference list.
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCategory_Adventures()
	 * @see org.eclipse.ui.examples.rcp.adventure.Adventure#getCategory
	 * @model type="org.eclipse.ui.examples.rcp.adventure.Adventure" opposite="category" containment="true"
	 * @generated
	 */
	EList getAdventures();

} // Category
