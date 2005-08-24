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
 *  $RCSfile: FeatureDescriptorDecorator.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:51 $ 
 */


import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Descriptor Decorator</b></em>'.
 * This descriptor decorator for any kind of feature. It contains info that is generalized to features.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This descriptor decorator for any kind of feature. It contains info that is generalized to features.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#isHidden <em>Hidden</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getHelpContextIdsString <em>Help Context Ids String</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#isPreferred <em>Preferred</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getCategoryString <em>Category String</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getFilterFlagStrings <em>Filter Flag Strings</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getDisplayNameString <em>Display Name String</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getDescriptionString <em>Description String</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getFeatureDescriptorDecorator()
 * @model abstract="true"
 * @generated
 */
public interface FeatureDescriptorDecorator extends EAnnotation{


	/**
	 * Returns the value of the '<em><b>Display Name String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Display Name String</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Display Name String</em>' containment reference.
	 * @see #setDisplayNameString(AbstractString)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getFeatureDescriptorDecorator_DisplayNameString()
	 * @model containment="true"
	 * @generated
	 */
	AbstractString getDisplayNameString();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getDisplayNameString <em>Display Name String</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Display Name String</em>' containment reference.
	 * @see #getDisplayNameString()
	 * @generated
	 */
	void setDisplayNameString(AbstractString value);

	/**
	 * Returns the value of the '<em><b>Description String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description String</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description String</em>' containment reference.
	 * @see #setDescriptionString(AbstractString)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getFeatureDescriptorDecorator_DescriptionString()
	 * @model containment="true"
	 * @generated
	 */
	AbstractString getDescriptionString();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getDescriptionString <em>Description String</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description String</em>' containment reference.
	 * @see #getDescriptionString()
	 * @generated
	 */
	void setDescriptionString(AbstractString value);

	/**
	 * Returns the value of the '<em><b>Hidden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hidden</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hidden</em>' attribute.
	 * @see #setHidden(boolean)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getFeatureDescriptorDecorator_Hidden()
	 * @model 
	 * @generated
	 */
	boolean isHidden();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#isHidden <em>Hidden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hidden</em>' attribute.
	 * @see #isHidden()
	 * @generated
	 */
	void setHidden(boolean value);

	/**
	 * Returns the value of the '<em><b>Help Context Ids String</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Help Context Ids String</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Help Context Ids String</em>' attribute list.
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getFeatureDescriptorDecorator_HelpContextIdsString()
	 * @model type="java.lang.String"
	 * @generated
	 */
	EList getHelpContextIdsString();

	/**
	 * Returns the value of the '<em><b>Preferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Preferred</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Preferred</em>' attribute.
	 * @see #setPreferred(boolean)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getFeatureDescriptorDecorator_Preferred()
	 * @model 
	 * @generated
	 */
	boolean isPreferred();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#isPreferred <em>Preferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Preferred</em>' attribute.
	 * @see #isPreferred()
	 * @generated
	 */
	void setPreferred(boolean value);

	/**
	 * Returns the value of the '<em><b>Category String</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category String</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category String</em>' reference.
	 * @see #setCategoryString(AbstractString)
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getFeatureDescriptorDecorator_CategoryString()
	 * @model 
	 * @generated
	 */
	AbstractString getCategoryString();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.decorators.FeatureDescriptorDecorator#getCategoryString <em>Category String</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category String</em>' reference.
	 * @see #getCategoryString()
	 * @generated
	 */
	void setCategoryString(AbstractString value);

	/**
	 * Returns the value of the '<em><b>Filter Flag Strings</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.ve.internal.cde.utility.AbstractString}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filter Flag Strings</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Filter Flag Strings</em>' reference list.
	 * @see org.eclipse.ve.internal.cde.decorators.DecoratorsPackage#getFeatureDescriptorDecorator_FilterFlagStrings()
	 * @model type="org.eclipse.ve.internal.cde.utility.AbstractString"
	 * @generated
	 */
	EList getFilterFlagStrings();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Answer whether this filter flag passed in is in this features filterFlags.
	 * <!-- end-model-doc -->
	 * @model 
	 * @generated
	 */
	boolean isFiltered(String flag);

}
