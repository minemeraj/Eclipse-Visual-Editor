/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.decorators;
/*
 *  $RCSfile: DecoratorsFactory.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-07 17:21:33 $ 
 */


import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage
 * @generated
 */

public interface DecoratorsFactory extends EFactory {


	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DecoratorsFactory eINSTANCE = org.eclipse.ve.internal.cde.decorators.impl.DecoratorsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Base Property Decorator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Property Decorator</em>'.
	 * @generated
	 */
	BasePropertyDecorator createBasePropertyDecorator();

	/**
	 * Returns a new object of class '<em>Class Descriptor Decorator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Class Descriptor Decorator</em>'.
	 * @generated
	 */
	ClassDescriptorDecorator createClassDescriptorDecorator();

	/**
	 * Returns a new object of class '<em>Property Descriptor Decorator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Property Descriptor Decorator</em>'.
	 * @generated
	 */
	PropertyDescriptorDecorator createPropertyDescriptorDecorator();

	/**
	 * Returns a new object of class '<em>Property Source Adapter Information</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Property Source Adapter Information</em>'.
	 * @generated
	 */
	PropertySourceAdapterInformation createPropertySourceAdapterInformation();

	/**
	 * Returns a new object of class '<em>Property Descriptor Information</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Property Descriptor Information</em>'.
	 * @generated
	 */
	PropertyDescriptorInformation createPropertyDescriptorInformation();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DecoratorsPackage getDecoratorsPackage();

}
