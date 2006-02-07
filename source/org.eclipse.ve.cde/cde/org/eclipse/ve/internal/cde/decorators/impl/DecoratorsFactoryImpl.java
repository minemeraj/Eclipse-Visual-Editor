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
 *  $Revision: 1.8 $  $Date: 2006-02-07 17:21:33 $ 
 */
import org.eclipse.ve.internal.cde.decorators.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DecoratorsFactoryImpl extends EFactoryImpl implements DecoratorsFactory {

	
	
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DecoratorsFactory init() {
		try {
			DecoratorsFactory theDecoratorsFactory = (DecoratorsFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/ve/internal/cde/decorators.ecore"); 
			if (theDecoratorsFactory != null) {
				return theDecoratorsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DecoratorsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
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
	 * 
	 */
	public BasePropertyDecorator createBasePropertyDecorator() {
		BasePropertyDecoratorImpl basePropertyDecorator = new BasePropertyDecoratorImpl();
		basePropertyDecorator.setSource(BasePropertyDecorator.class.getName());
		return basePropertyDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public ClassDescriptorDecorator createClassDescriptorDecorator() {
		ClassDescriptorDecoratorImpl classDescriptorDecorator = new ClassDescriptorDecoratorImpl();
		classDescriptorDecorator.setSource(ClassDescriptorDecorator.class.getName());
		return classDescriptorDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public PropertyDescriptorDecorator createPropertyDescriptorDecorator() {
		PropertyDescriptorDecoratorImpl propertyDescriptorDecorator = new PropertyDescriptorDecoratorImpl();
		propertyDescriptorDecorator.setSource(PropertyDescriptorDecorator.class.getName());
		return propertyDescriptorDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public PropertySourceAdapterInformation createPropertySourceAdapterInformation() {
		PropertySourceAdapterInformationImpl propertySourceAdapterInformation = new PropertySourceAdapterInformationImpl();
		propertySourceAdapterInformation.setSource(PropertySourceAdapterInformation.class.getName());
		return propertySourceAdapterInformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public PropertyDescriptorInformation createPropertyDescriptorInformation() {
		PropertyDescriptorInformationImpl propertyDescriptorInformation = new PropertyDescriptorInformationImpl();
		propertyDescriptorInformation.setSource(PropertyDescriptorInformation.class.getName());
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
