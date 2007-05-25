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
 *  $RCSfile: PropertyDescriptorDecorator.java,v $
 *  $Revision: 1.7 $  $Date: 2007-05-25 04:09:36 $ 
 */

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Descriptor Decorator</b></em>'.
 * This is the decorator for a StructuralFeature to supply the information for editing this property.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is the decorator for a StructuralFeature to supply the information for editing this property.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isDesigntimeProperty <em>Designtime Property</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isAlwaysIncompatible <em>Always Incompatible</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getPropertyDescriptorDecorator()
 * @model
 * @generated
 */
public interface PropertyDescriptorDecorator extends FeatureDescriptorDecorator, BasePropertyDecorator{


	/**
	 * Returns the value of the '<em><b>Designtime Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Designtime Property</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether this property is designtime. If not explicitly set, then ignore this setting. If true then it is shown only on property sheet and not available for connections. If false, then it is available only for connections and not on property sheet.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Designtime Property</em>' attribute.
	 * @see #isSetDesigntimeProperty()
	 * @see #unsetDesigntimeProperty()
	 * @see #setDesigntimeProperty(boolean)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getPropertyDescriptorDecorator_DesigntimeProperty()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isDesigntimeProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isDesigntimeProperty <em>Designtime Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Designtime Property</em>' attribute.
	 * @see #isSetDesigntimeProperty()
	 * @see #unsetDesigntimeProperty()
	 * @see #isDesigntimeProperty()
	 * @generated
	 */
	void setDesigntimeProperty(boolean value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isDesigntimeProperty <em>Designtime Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDesigntimeProperty()
	 * @see #isDesigntimeProperty()
	 * @see #setDesigntimeProperty(boolean)
	 * @generated
	 */
	void unsetDesigntimeProperty();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isDesigntimeProperty <em>Designtime Property</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Designtime Property</em>' attribute is set.
	 * @see #unsetDesigntimeProperty()
	 * @see #isDesigntimeProperty()
	 * @see #setDesigntimeProperty(boolean)
	 * @generated
	 */
	boolean isSetDesigntimeProperty();

	/**
	 * Returns the value of the '<em><b>Always Incompatible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Always Incompatible</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * If true, then when multiple selections of this property is made, they will be considered incompatible.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Always Incompatible</em>' attribute.
	 * @see #setAlwaysIncompatible(boolean)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getPropertyDescriptorDecorator_AlwaysIncompatible()
	 * @model
	 * @generated
	 */
	boolean isAlwaysIncompatible();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator#isAlwaysIncompatible <em>Always Incompatible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Always Incompatible</em>' attribute.
	 * @see #isAlwaysIncompatible()
	 * @generated
	 */
	void setAlwaysIncompatible(boolean value);

}
