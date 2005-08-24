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
 *  $RCSfile: PropertyDescriptorInformation.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:51 $ 
 */


import org.eclipse.emf.ecore.EAnnotation;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Descriptor Information</b></em>'.
 * This is the information required to establish a PropertyDescriptorAdapter or a PropertyDescriptor.
 * 
 * This will be decorating a EStructuralFeature to indicate what  PropertyDescriptorAdapter or PropertyDescscriptor  to use for this feature.
 * 
 * It depends upon whether the type flag is Adapter is true. If the flag is true, then an adapter will be created once for the feature.
 * 
 * If the flag is false, then it will create a descriptor each time a source is asked for descriptors. It will assume to have either a constructor that takes a EObject and a EStructuralFeature or one that takes just a EObject. The EObject is the source of the feature. This is useful when the descriptor needs to know the source to be able to answer its questions correctly, such as the celleditor, which may vary depending upon the source.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is the information required to establish a PropertyDescriptorAdapter or a PropertyDescriptor.
 * 
 * This will be decorating a RefStructuralFeature to indicate what  PropertyDescriptorAdapter or PropertyDescscriptor  to use for this feature.
 * 
 * It depends upon whether the type flag is Adapter is true. If the flag is true, then an adapter will be created once for the feature.
 * 
 * If the flag is false, then it will create a descriptor each time a source is asked for descriptors. It will assume to have either a constructor that takes a RefObject and a RefStructuralFeature or one that takes just a RefObject. The RefObject is the source of the feature. This is useful when the descriptor needs to know the source to be able to answer its questions correctly, such as the celleditor, which may vary depending upon the source.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation#isAdapter <em>Adapter</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation#getPropertyDescriptorClassname <em>Property Descriptor Classname</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getPropertyDescriptorInformation()
 * @model 
 * @generated
 */
public interface PropertyDescriptorInformation extends EAnnotation{


	/**
	 * Returns the value of the '<em><b>Adapter</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adapter</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Adapter</em>' attribute.
	 * @see #setAdapter(boolean)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getPropertyDescriptorInformation_Adapter()
	 * @model default="true"
	 * @generated
	 */
	boolean isAdapter();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation#isAdapter <em>Adapter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Adapter</em>' attribute.
	 * @see #isAdapter()
	 * @generated
	 */
	void setAdapter(boolean value);

	/**
	 * Returns the value of the '<em><b>Property Descriptor Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property Descriptor Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property Descriptor Classname</em>' attribute.
	 * @see #setPropertyDescriptorClassname(String)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getPropertyDescriptorInformation_PropertyDescriptorClassname()
	 * @model 
	 * @generated
	 */
	String getPropertyDescriptorClassname();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation#getPropertyDescriptorClassname <em>Property Descriptor Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property Descriptor Classname</em>' attribute.
	 * @see #getPropertyDescriptorClassname()
	 * @generated
	 */
	void setPropertyDescriptorClassname(String value);

}
