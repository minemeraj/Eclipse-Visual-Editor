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
 *  $RCSfile: BeanDecorator.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:16:38 $ 
 */

import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.emf.ecore.EAnnotation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bean Decorator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Decorates the bean for bean specific inforamation, for example the BeanProxyAdapter class.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanProxyClassName <em>Bean Proxy Class Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanDecorator()
 * @model 
 * @generated
 */
public interface BeanDecorator extends EAnnotation, KeyedValueHolder{
	/**
	 * Returns the value of the '<em><b>Bean Proxy Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean Proxy Class Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of the class for the BeanProxyAdapter.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Proxy Class Name</em>' attribute.
	 * @see #setBeanProxyClassName(String)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanDecorator_BeanProxyClassName()
	 * @model 
	 * @generated
	 */
	String getBeanProxyClassName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanProxyClassName <em>Bean Proxy Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Proxy Class Name</em>' attribute.
	 * @see #getBeanProxyClassName()
	 * @generated
	 */
	void setBeanProxyClassName(String value);

} // BeanDecorator
