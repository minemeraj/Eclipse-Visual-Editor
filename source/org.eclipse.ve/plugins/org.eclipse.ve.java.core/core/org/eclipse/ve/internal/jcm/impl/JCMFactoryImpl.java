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
package org.eclipse.ve.internal.jcm.impl;
/*
 *  $RCSfile: JCMFactoryImpl.java,v $
 *  $Revision: 1.11 $  $Date: 2005-10-03 19:20:57 $ 
 */

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.ve.internal.jcm.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JCMFactoryImpl extends EFactoryImpl implements JCMFactory {
	/**
	 * Creates an instance of the factory.
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
			case JCMPackage.KEYED_INSTANCE_LOCATION: return (EObject)createKeyedInstanceLocation();
			case JCMPackage.JAVA_CACHE_DATA: return createJavaCacheData();
			case JCMPackage.NAMES_TO_BEANS: return (EObject)createNamesToBeans();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case JCMPackage.INSTANCE_LOCATION: {
				InstanceLocation result = InstanceLocation.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case JCMPackage.LINK_TYPE: {
				LinkType result = LinkType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case JCMPackage.INSTANCE_LOCATION:
				return instanceValue == null ? null : instanceValue.toString();
			case JCMPackage.LINK_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public BeanDecorator createBeanDecorator() {
		BeanDecoratorImpl beanDecorator = new BeanDecoratorImpl();
		beanDecorator.setSource(BeanDecorator.class.getName());
		return beanDecorator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public BeanFeatureDecorator createBeanFeatureDecorator() {
		BeanFeatureDecoratorImpl beanFeatureDecorator = new BeanFeatureDecoratorImpl();
		beanFeatureDecorator.setSource(BeanFeatureDecorator.class.getName());
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
	public Map.Entry createKeyedInstanceLocation() {
		KeyedInstanceLocationImpl keyedInstanceLocation = new KeyedInstanceLocationImpl();
		return keyedInstanceLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaCacheData createJavaCacheData() {
		JavaCacheDataImpl javaCacheData = new JavaCacheDataImpl();
		return javaCacheData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry createNamesToBeans() {
		NamesToBeansImpl namesToBeans = new NamesToBeansImpl();
		return namesToBeans;
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
	 * @deprecated
	 * @generated
	 */
	public static JCMPackage getPackage() {
		return JCMPackage.eINSTANCE;
	}

} //JCMFactoryImpl
