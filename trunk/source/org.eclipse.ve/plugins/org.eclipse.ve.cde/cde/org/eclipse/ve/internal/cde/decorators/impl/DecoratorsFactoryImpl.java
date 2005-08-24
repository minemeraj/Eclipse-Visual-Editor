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
package org.eclipse.ve.internal.cde.decorators.impl;
/*
 *  $RCSfile: DecoratorsFactoryImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:48 $ 
 */
import org.eclipse.ve.internal.cde.decorators.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DecoratorsFactoryImpl extends EFactoryImpl implements DecoratorsFactory {

	
	
	/**
	 * Creates and instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	public DecoratorsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DecoratorsPackage.BASE_PROPERTY_DECORATOR: return createBasePropertyDecorator();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION: return createPropertySourceAdapterInformation();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_DECORATOR: return createPropertyDescriptorDecorator();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION: return createPropertyDescriptorInformation();
			case DecoratorsPackage.CLASS_DESCRIPTOR_DECORATOR: return createClassDescriptorDecorator();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BasePropertyDecorator createBasePropertyDecorator() {
		BasePropertyDecoratorImpl basePropertyDecorator = new BasePropertyDecoratorImpl();
		return basePropertyDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ClassDescriptorDecorator createClassDescriptorDecorator() {
		ClassDescriptorDecoratorImpl classDescriptorDecorator = new ClassDescriptorDecoratorImpl();
		return classDescriptorDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyDescriptorDecorator createPropertyDescriptorDecorator() {
		PropertyDescriptorDecoratorImpl propertyDescriptorDecorator = new PropertyDescriptorDecoratorImpl();
		return propertyDescriptorDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertySourceAdapterInformation createPropertySourceAdapterInformation() {
		PropertySourceAdapterInformationImpl propertySourceAdapterInformation = new PropertySourceAdapterInformationImpl();
		return propertySourceAdapterInformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyDescriptorInformation createPropertyDescriptorInformation() {
		PropertyDescriptorInformationImpl propertyDescriptorInformation = new PropertyDescriptorInformationImpl();
		return propertyDescriptorInformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DecoratorsPackage getDecoratorsPackage() {
		return (DecoratorsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static DecoratorsPackage getPackage() {
		return DecoratorsPackage.eINSTANCE;
	}
}
