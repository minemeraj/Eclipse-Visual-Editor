package org.eclipse.ve.internal.cde.decorators;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PropertySourceAdapterInformation.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */


import org.eclipse.emf.ecore.EAnnotation;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Source Adapter Information</b></em>'.
 * This is the information required to establish a PropertySourceAdapter.
 * 
 * This will be decorating a EClassifier to indicate what  PropertySourceAdapter to use for this EClassifier.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation#getPropertySourceAdapterClassname <em>Property Source Adapter Classname</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getPropertySourceAdapterInformation()
 * @model 
 * @generated
 */
public interface PropertySourceAdapterInformation extends EAnnotation {


	/**
	 * Returns the value of the '<em><b>Property Source Adapter Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property Source Adapter Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property Source Adapter Classname</em>' attribute.
	 * @see #isSetPropertySourceAdapterClassname()
	 * @see #unsetPropertySourceAdapterClassname()
	 * @see #setPropertySourceAdapterClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getPropertySourceAdapterInformation_PropertySourceAdapterClassname()
	 * @model unsettable="true"
	 * @generated
	 */
	String getPropertySourceAdapterClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation#getPropertySourceAdapterClassname <em>Property Source Adapter Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property Source Adapter Classname</em>' attribute.
	 * @see #isSetPropertySourceAdapterClassname()
	 * @see #unsetPropertySourceAdapterClassname()
	 * @see #getPropertySourceAdapterClassname()
	 * @generated
	 */
	void setPropertySourceAdapterClassname(String value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation#getPropertySourceAdapterClassname <em>Property Source Adapter Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPropertySourceAdapterClassname()
	 * @see #getPropertySourceAdapterClassname()
	 * @see #setPropertySourceAdapterClassname(String)
	 * @generated
	 */
	void unsetPropertySourceAdapterClassname();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation#getPropertySourceAdapterClassname <em>Property Source Adapter Classname</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Property Source Adapter Classname</em>' attribute is set.
	 * @see #unsetPropertySourceAdapterClassname()
	 * @see #getPropertySourceAdapterClassname()
	 * @see #setPropertySourceAdapterClassname(String)
	 * @generated
	 */
	boolean isSetPropertySourceAdapterClassname();

}
