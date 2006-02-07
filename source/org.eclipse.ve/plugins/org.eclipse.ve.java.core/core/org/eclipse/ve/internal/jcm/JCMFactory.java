/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jcm;
/*
 *  $RCSfile: JCMFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2006-02-07 17:21:37 $ 
 */

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ve.internal.jcm.JCMPackage
 * @generated
 */
public interface JCMFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	JCMFactory eINSTANCE = org.eclipse.ve.internal.jcm.impl.JCMFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Bean Decorator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Bean Decorator</em>'.
	 * @generated
	 */
	BeanDecorator createBeanDecorator();

	/**
	 * Returns a new object of class '<em>Bean Feature Decorator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Bean Feature Decorator</em>'.
	 * @generated
	 */
	BeanFeatureDecorator createBeanFeatureDecorator();

	/**
	 * Returns a new object of class '<em>Bean Composition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Bean Composition</em>'.
	 * @generated
	 */
	BeanComposition createBeanComposition();

	/**
	 * Returns a new object of class '<em>Event Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Event Invocation</em>'.
	 * @generated
	 */
	EventInvocation createEventInvocation();

	/**
	 * Returns a new object of class '<em>Property Change Event Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Property Change Event Invocation</em>'.
	 * @generated
	 */
	PropertyChangeEventInvocation createPropertyChangeEventInvocation();

	/**
	 * Returns a new object of class '<em>Property Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Property Event</em>'.
	 * @generated
	 */
	PropertyEvent createPropertyEvent();

	/**
	 * Returns a new object of class '<em>Bean Subclass Composition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Bean Subclass Composition</em>'.
	 * @generated
	 */
	BeanSubclassComposition createBeanSubclassComposition();

	/**
	 * Returns a new object of class '<em>Method</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Method</em>'.
	 * @generated
	 */
	JCMMethod createJCMMethod();

	/**
	 * Returns a new object of class '<em>Java Cache Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Java Cache Data</em>'.
	 * @generated
	 */
	JavaCacheData createJavaCacheData();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	JCMPackage getJCMPackage();

	/**
	 * Returns a new object of class '<em>Listener Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Listener Type</em>'.
	 * @generated
	 */
	ListenerType createListenerType();

	/**
	 * Returns a new object of class '<em>Listener</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Listener</em>'.
	 * @generated
	 */
	Listener createListener();

	/**
	 * Returns a new object of class '<em>Callback</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Callback</em>'.
	 * @generated
	 */
	Callback createCallback();

	/**
	 * Returns a new object of class '<em>Member Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Member Container</em>'.
	 * @generated
	 */
	MemberContainer createMemberContainer();

} //JCMFactory
