package org.eclipse.ve.internal.jcm.impl;
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
 *  $RCSfile: JCMFactoryImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JCMFactoryImpl extends EFactoryImpl implements JCMFactory {
	/**
	 * Creates and instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JCMFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case JCMPackage.BEAN_DECORATOR: return createBeanDecorator();
			case JCMPackage.BEAN_FEATURE_DECORATOR: return createBeanFeatureDecorator();
			case JCMPackage.BEAN_COMPOSITION: return createBeanComposition();
			case JCMPackage.LISTENER_TYPE: return createListenerType();
			case JCMPackage.MEMBER_CONTAINER: return createMemberContainer();
			case JCMPackage.LISTENER: return createListener();
			case JCMPackage.CALLBACK: return createCallback();
			case JCMPackage.EVENT_INVOCATION: return createEventInvocation();
			case JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION: return createPropertyChangeEventInvocation();
			case JCMPackage.PROPERTY_EVENT: return createPropertyEvent();
			case JCMPackage.BEAN_SUBCLASS_COMPOSITION: return createBeanSubclassComposition();
			case JCMPackage.JCM_METHOD: return createJCMMethod();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BeanDecorator createBeanDecorator() {
		BeanDecoratorImpl beanDecorator = new BeanDecoratorImpl();
		return beanDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BeanFeatureDecorator createBeanFeatureDecorator() {
		BeanFeatureDecoratorImpl beanFeatureDecorator = new BeanFeatureDecoratorImpl();
		return beanFeatureDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BeanComposition createBeanComposition() {
		BeanCompositionImpl beanComposition = new BeanCompositionImpl();
		return beanComposition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventInvocation createEventInvocation() {
		EventInvocationImpl eventInvocation = new EventInvocationImpl();
		return eventInvocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyChangeEventInvocation createPropertyChangeEventInvocation() {
		PropertyChangeEventInvocationImpl propertyChangeEventInvocation = new PropertyChangeEventInvocationImpl();
		return propertyChangeEventInvocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyEvent createPropertyEvent() {
		PropertyEventImpl propertyEvent = new PropertyEventImpl();
		return propertyEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BeanSubclassComposition createBeanSubclassComposition() {
		BeanSubclassCompositionImpl beanSubclassComposition = new BeanSubclassCompositionImpl();
		return beanSubclassComposition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JCMMethod createJCMMethod() {
		JCMMethodImpl jcmMethod = new JCMMethodImpl();
		return jcmMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JCMPackage getJCMPackage() {
		return (JCMPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ListenerType createListenerType() {
		ListenerTypeImpl listenerType = new ListenerTypeImpl();
		return listenerType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Listener createListener() {
		ListenerImpl listener = new ListenerImpl();
		return listener;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Callback createCallback() {
		CallbackImpl callback = new CallbackImpl();
		return callback;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MemberContainer createMemberContainer() {
		MemberContainerImpl memberContainer = new MemberContainerImpl();
		return memberContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static JCMPackage getPackage() {
		return JCMPackage.eINSTANCE;
	}

} //JCMFactoryImpl
