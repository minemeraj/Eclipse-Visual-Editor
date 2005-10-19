/**
 * <copyright>
 * </copyright>
 *
 * $Id: CartActivityEntry.java,v 1.1 2005-10-19 18:35:44 sgunturi Exp $
 */
package org.eclipse.ui.examples.rcp.adventure;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cart Activity Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.CartActivityEntry#getActivity <em>Activity</em>}</li>
 *   <li>{@link org.eclipse.ui.examples.rcp.adventure.CartActivityEntry#getNumPeople <em>Num People</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCartActivityEntry()
 * @model
 * @generated
 */
public interface CartActivityEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Activity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activity</em>' reference.
	 * @see #setActivity(Activity)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCartActivityEntry_Activity()
	 * @model
	 * @generated
	 */
	Activity getActivity();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.CartActivityEntry#getActivity <em>Activity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Activity</em>' reference.
	 * @see #getActivity()
	 * @generated
	 */
	void setActivity(Activity value);

	/**
	 * Returns the value of the '<em><b>Num People</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Num People</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Num People</em>' attribute.
	 * @see #setNumPeople(int)
	 * @see org.eclipse.ui.examples.rcp.adventure.AdventurePackage#getCartActivityEntry_NumPeople()
	 * @model
	 * @generated
	 */
	int getNumPeople();

	/**
	 * Sets the value of the '{@link org.eclipse.ui.examples.rcp.adventure.CartActivityEntry#getNumPeople <em>Num People</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Num People</em>' attribute.
	 * @see #getNumPeople()
	 * @generated
	 */
	void setNumPeople(int value);

} // CartActivityEntry
