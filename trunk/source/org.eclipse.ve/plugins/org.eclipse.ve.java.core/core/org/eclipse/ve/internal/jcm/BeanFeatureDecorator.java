package org.eclipse.ve.internal.jcm;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanFeatureDecorator.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:16:38 $ 
 */

import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.emf.ecore.EAnnotation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bean Feature Decorator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Decorate a bean feature.  For example the mediator for handling bean feature application/canceling in the proxy adapter.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanFeatureDecorator#getBeanProxyMediatorName <em>Bean Proxy Mediator Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanFeatureDecorator#isChildFeature <em>Child Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanFeatureDecorator()
 * @model 
 * @generated
 */
public interface BeanFeatureDecorator extends EAnnotation, KeyedValueHolder{
	/**
	 * Returns the value of the '<em><b>Bean Proxy Mediator Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean Proxy Mediator Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Mediator to handle apply/cancel of bean properties.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Proxy Mediator Name</em>' attribute.
	 * @see #setBeanProxyMediatorName(String)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanFeatureDecorator_BeanProxyMediatorName()
	 * @model 
	 * @generated
	 */
	String getBeanProxyMediatorName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.BeanFeatureDecorator#getBeanProxyMediatorName <em>Bean Proxy Mediator Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Proxy Mediator Name</em>' attribute.
	 * @see #getBeanProxyMediatorName()
	 * @generated
	 */
	void setBeanProxyMediatorName(String value);

	/**
	 * Returns the value of the '<em><b>Child Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
	* If true, then this is a child feature. This means that if the parent is deleted, 
	* then the child should be deleted except if the child is referenced by another feature that is a child feature.
	* It is a basic parent/child relationship, which is usually shown in the tree and graph view. It is used by
	* proxy adapters to know when to recycle the remote proxy.
   * </p>
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * If true, then this is a child feature. This means that if the parent is deleted, then the child should be deleted except if the child is referenced by another feature that is a child feature.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Child Feature</em>' attribute.
	 * @see #setChildFeature(boolean)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanFeatureDecorator_ChildFeature()
	 * @model 
	 * @generated
	 */
  boolean isChildFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.BeanFeatureDecorator#isChildFeature <em>Child Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Feature</em>' attribute.
	 * @see #isChildFeature()
	 * @generated
	 */
  void setChildFeature(boolean value);

} // BeanFeatureDecorator
