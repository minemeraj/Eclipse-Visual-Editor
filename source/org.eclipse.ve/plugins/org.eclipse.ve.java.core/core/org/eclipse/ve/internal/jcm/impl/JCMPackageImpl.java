/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
 *  $RCSfile: JCMPackageImpl.java,v $
 *  $Revision: 1.13 $  $Date: 2006-05-17 20:14:52 $ 
 */

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.jem.internal.beaninfo.BeaninfoPackage;
import org.eclipse.jem.internal.instantiation.InstantiationPackage;
import org.eclipse.jem.java.JavaRefPackage;

import org.eclipse.ve.internal.cdm.CDMPackage;

import org.eclipse.ve.internal.jcm.AbstractEventInvocation;
import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.BeanDecorator;
import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.Callback;
import org.eclipse.ve.internal.jcm.EventInvocation;
import org.eclipse.ve.internal.jcm.InstanceLocation;
import org.eclipse.ve.internal.jcm.JCMFactory;
import org.eclipse.ve.internal.jcm.JCMMethod;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.JavaCacheData;
import org.eclipse.ve.internal.jcm.LinkType;
import org.eclipse.ve.internal.jcm.Listener;
import org.eclipse.ve.internal.jcm.ListenerType;
import org.eclipse.ve.internal.jcm.MemberContainer;
import org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation;
import org.eclipse.ve.internal.jcm.PropertyEvent;

import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsFactory;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JCMPackageImpl extends EPackageImpl implements JCMPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beanDecoratorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beanFeatureDecoratorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beanCompositionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractEventInvocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventInvocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertyChangeEventInvocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertyEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beanSubclassCompositionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass jcmMethodEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyedInstanceLocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass javaCacheDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namesToBeansEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum instanceLocationEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum linkTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass listenerTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass listenerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass callbackEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass memberContainerEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private JCMPackageImpl() {
		super(eNS_URI, JCMFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static JCMPackage init() {
		if (isInited) return (JCMPackage)EPackage.Registry.INSTANCE.getEPackage(JCMPackage.eNS_URI);

		// Obtain or create and register package
		JCMPackageImpl theJCMPackage = (JCMPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof JCMPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new JCMPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CDMPackage.eINSTANCE.eClass();
		JavaRefPackage.eINSTANCE.eClass();
		BeaninfoPackage.eINSTANCE.eClass();
		InstantiationPackage.eINSTANCE.eClass();
		EcorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theJCMPackage.createPackageContents();

		// Initialize created meta-data
		theJCMPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJCMPackage.freeze();

		return theJCMPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBeanDecorator() {
		return beanDecoratorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanDecorator_BeanProxyClassName() {
		return (EAttribute)beanDecoratorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanDecorator_BeanLocation() {
		return (EAttribute)beanDecoratorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanDecorator_BeanReturn() {
		return (EAttribute)beanDecoratorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBeanFeatureDecorator() {
		return beanFeatureDecoratorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanFeatureDecorator_BeanProxyMediatorName() {
		return (EAttribute)beanFeatureDecoratorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanFeatureDecorator_LinkType() {
		return (EAttribute)beanFeatureDecoratorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanFeatureDecorator_BeanLocation() {
		return (EAttribute)beanFeatureDecoratorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBeanComposition() {
		return beanCompositionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeanComposition_Components() {
		return (EReference)beanCompositionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeanComposition_ListenerTypes() {
		return (EReference)beanCompositionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractEventInvocation() {
		return abstractEventInvocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractEventInvocation_Callbacks() {
		return (EReference)abstractEventInvocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractEventInvocation_Listener() {
		return (EReference)abstractEventInvocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEventInvocation() {
		return eventInvocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventInvocation_Event() {
		return (EReference)eventInvocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertyChangeEventInvocation() {
		return propertyChangeEventInvocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPropertyChangeEventInvocation_AddMethod() {
		return (EReference)propertyChangeEventInvocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPropertyChangeEventInvocation_Properties() {
		return (EReference)propertyChangeEventInvocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertyEvent() {
		return propertyEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyEvent_PropertyName() {
		return (EAttribute)propertyEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyEvent_UseIfExpression() {
		return (EAttribute)propertyEventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBeanSubclassComposition() {
		return beanSubclassCompositionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeanSubclassComposition_ThisPart() {
		return (EReference)beanSubclassCompositionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeanSubclassComposition_Methods() {
		return (EReference)beanSubclassCompositionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJCMMethod() {
		return jcmMethodEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJCMMethod_Name() {
		return (EAttribute)jcmMethodEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJCMMethod_Initializes() {
		return (EReference)jcmMethodEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJCMMethod_Return() {
		return (EReference)jcmMethodEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyedInstanceLocation() {
		return keyedInstanceLocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedInstanceLocation_Key() {
		return (EAttribute)keyedInstanceLocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyedInstanceLocation_Value() {
		return (EAttribute)keyedInstanceLocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJavaCacheData() {
		return javaCacheDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavaCacheData_NamesToBeans() {
		return (EReference)javaCacheDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamesToBeans() {
		return namesToBeansEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamesToBeans_Key() {
		return (EAttribute)namesToBeansEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNamesToBeans_Value() {
		return (EReference)namesToBeansEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getInstanceLocation() {
		return instanceLocationEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLinkType() {
		return linkTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JCMFactory getJCMFactory() {
		return (JCMFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getListenerType() {
		return listenerTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getListenerType_Name() {
		return (EAttribute)listenerTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getListenerType_ThisPart() {
		return (EAttribute)listenerTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListenerType_Extends() {
		return (EReference)listenerTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListenerType_Implements() {
		return (EReference)listenerTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListenerType_Is() {
		return (EReference)listenerTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListenerType_Listeners() {
		return (EReference)listenerTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getListener() {
		return listenerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListener_ListenedBy() {
		return (EReference)listenerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListener_ListenerType() {
		return (EReference)listenerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCallback() {
		return callbackEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCallback_SharedScope() {
		return (EAttribute)callbackEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCallback_Method() {
		return (EReference)callbackEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCallback_Statements() {
		return (EReference)callbackEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMemberContainer() {
		return memberContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMemberContainer_Members() {
		return (EReference)memberContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMemberContainer_Implicits() {
		return (EReference)memberContainerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMemberContainer_Properties() {
		return (EReference)memberContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		beanDecoratorEClass = createEClass(BEAN_DECORATOR);
		createEAttribute(beanDecoratorEClass, BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME);
		createEAttribute(beanDecoratorEClass, BEAN_DECORATOR__BEAN_LOCATION);
		createEAttribute(beanDecoratorEClass, BEAN_DECORATOR__BEAN_RETURN);

		beanFeatureDecoratorEClass = createEClass(BEAN_FEATURE_DECORATOR);
		createEAttribute(beanFeatureDecoratorEClass, BEAN_FEATURE_DECORATOR__BEAN_PROXY_MEDIATOR_NAME);
		createEAttribute(beanFeatureDecoratorEClass, BEAN_FEATURE_DECORATOR__LINK_TYPE);
		createEAttribute(beanFeatureDecoratorEClass, BEAN_FEATURE_DECORATOR__BEAN_LOCATION);

		beanCompositionEClass = createEClass(BEAN_COMPOSITION);
		createEReference(beanCompositionEClass, BEAN_COMPOSITION__COMPONENTS);
		createEReference(beanCompositionEClass, BEAN_COMPOSITION__LISTENER_TYPES);

		abstractEventInvocationEClass = createEClass(ABSTRACT_EVENT_INVOCATION);
		createEReference(abstractEventInvocationEClass, ABSTRACT_EVENT_INVOCATION__CALLBACKS);
		createEReference(abstractEventInvocationEClass, ABSTRACT_EVENT_INVOCATION__LISTENER);

		listenerTypeEClass = createEClass(LISTENER_TYPE);
		createEAttribute(listenerTypeEClass, LISTENER_TYPE__NAME);
		createEAttribute(listenerTypeEClass, LISTENER_TYPE__THIS_PART);
		createEReference(listenerTypeEClass, LISTENER_TYPE__EXTENDS);
		createEReference(listenerTypeEClass, LISTENER_TYPE__IMPLEMENTS);
		createEReference(listenerTypeEClass, LISTENER_TYPE__IS);
		createEReference(listenerTypeEClass, LISTENER_TYPE__LISTENERS);

		memberContainerEClass = createEClass(MEMBER_CONTAINER);
		createEReference(memberContainerEClass, MEMBER_CONTAINER__PROPERTIES);
		createEReference(memberContainerEClass, MEMBER_CONTAINER__MEMBERS);
		createEReference(memberContainerEClass, MEMBER_CONTAINER__IMPLICITS);

		listenerEClass = createEClass(LISTENER);
		createEReference(listenerEClass, LISTENER__LISTENED_BY);
		createEReference(listenerEClass, LISTENER__LISTENER_TYPE);

		callbackEClass = createEClass(CALLBACK);
		createEAttribute(callbackEClass, CALLBACK__SHARED_SCOPE);
		createEReference(callbackEClass, CALLBACK__METHOD);
		createEReference(callbackEClass, CALLBACK__STATEMENTS);

		eventInvocationEClass = createEClass(EVENT_INVOCATION);
		createEReference(eventInvocationEClass, EVENT_INVOCATION__EVENT);

		propertyChangeEventInvocationEClass = createEClass(PROPERTY_CHANGE_EVENT_INVOCATION);
		createEReference(propertyChangeEventInvocationEClass, PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD);
		createEReference(propertyChangeEventInvocationEClass, PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES);

		propertyEventEClass = createEClass(PROPERTY_EVENT);
		createEAttribute(propertyEventEClass, PROPERTY_EVENT__PROPERTY_NAME);
		createEAttribute(propertyEventEClass, PROPERTY_EVENT__USE_IF_EXPRESSION);

		beanSubclassCompositionEClass = createEClass(BEAN_SUBCLASS_COMPOSITION);
		createEReference(beanSubclassCompositionEClass, BEAN_SUBCLASS_COMPOSITION__THIS_PART);
		createEReference(beanSubclassCompositionEClass, BEAN_SUBCLASS_COMPOSITION__METHODS);

		jcmMethodEClass = createEClass(JCM_METHOD);
		createEAttribute(jcmMethodEClass, JCM_METHOD__NAME);
		createEReference(jcmMethodEClass, JCM_METHOD__INITIALIZES);
		createEReference(jcmMethodEClass, JCM_METHOD__RETURN);

		keyedInstanceLocationEClass = createEClass(KEYED_INSTANCE_LOCATION);
		createEAttribute(keyedInstanceLocationEClass, KEYED_INSTANCE_LOCATION__KEY);
		createEAttribute(keyedInstanceLocationEClass, KEYED_INSTANCE_LOCATION__VALUE);

		javaCacheDataEClass = createEClass(JAVA_CACHE_DATA);
		createEReference(javaCacheDataEClass, JAVA_CACHE_DATA__NAMES_TO_BEANS);

		namesToBeansEClass = createEClass(NAMES_TO_BEANS);
		createEAttribute(namesToBeansEClass, NAMES_TO_BEANS__KEY);
		createEReference(namesToBeansEClass, NAMES_TO_BEANS__VALUE);

		// Create enums
		instanceLocationEEnum = createEEnum(INSTANCE_LOCATION);
		linkTypeEEnum = createEEnum(LINK_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	public void initializePackageContents() {
		if (isInitialized) return;
		initializePackageContentsGen();
		
		// Now initialize the ModelAdapter decorator for the Events objects. This handler prevents it from being moved/dropped from palette or choosebean.
		// This is because these are explicitly handled by code.
		ClassDescriptorDecorator cd = DecoratorsFactory.eINSTANCE.createClassDescriptorDecorator();
		cd.setModelAdapterClassname("org.eclipse.ve.cde/org.eclipse.ve.internal.cde.core.NoParentContainmentHandler");
		getCallback().getEAnnotations().add(cd);
		
		cd = DecoratorsFactory.eINSTANCE.createClassDescriptorDecorator();
		cd.setModelAdapterClassname("org.eclipse.ve.cde/org.eclipse.ve.internal.cde.core.NoParentContainmentHandler");
		getPropertyEvent().getEAnnotations().add(cd);
		
	}
	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContentsGen() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		CDMPackage theCDMPackage = (CDMPackage)EPackage.Registry.INSTANCE.getEPackage(CDMPackage.eNS_URI);
		JavaRefPackage theJavaRefPackage = (JavaRefPackage)EPackage.Registry.INSTANCE.getEPackage(JavaRefPackage.eNS_URI);
		InstantiationPackage theInstantiationPackage = (InstantiationPackage)EPackage.Registry.INSTANCE.getEPackage(InstantiationPackage.eNS_URI);
		BeaninfoPackage theBeaninfoPackage = (BeaninfoPackage)EPackage.Registry.INSTANCE.getEPackage(BeaninfoPackage.eNS_URI);

		// Add supertypes to classes
		beanDecoratorEClass.getESuperTypes().add(theEcorePackage.getEAnnotation());
		beanDecoratorEClass.getESuperTypes().add(theCDMPackage.getKeyedValueHolder());
		beanFeatureDecoratorEClass.getESuperTypes().add(theEcorePackage.getEAnnotation());
		beanFeatureDecoratorEClass.getESuperTypes().add(theCDMPackage.getKeyedValueHolder());
		beanCompositionEClass.getESuperTypes().add(theCDMPackage.getDiagramData());
		beanCompositionEClass.getESuperTypes().add(this.getMemberContainer());
		eventInvocationEClass.getESuperTypes().add(this.getAbstractEventInvocation());
		propertyChangeEventInvocationEClass.getESuperTypes().add(this.getAbstractEventInvocation());
		beanSubclassCompositionEClass.getESuperTypes().add(this.getBeanComposition());
		jcmMethodEClass.getESuperTypes().add(this.getMemberContainer());

		// Initialize classes and features; add operations and parameters
		initEClass(beanDecoratorEClass, BeanDecorator.class, "BeanDecorator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBeanDecorator_BeanProxyClassName(), ecorePackage.getEString(), "beanProxyClassName", null, 0, 1, BeanDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanDecorator_BeanLocation(), this.getInstanceLocation(), "beanLocation", null, 0, 1, BeanDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanDecorator_BeanReturn(), ecorePackage.getEBoolean(), "beanReturn", null, 0, 1, BeanDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(beanFeatureDecoratorEClass, BeanFeatureDecorator.class, "BeanFeatureDecorator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBeanFeatureDecorator_BeanProxyMediatorName(), ecorePackage.getEString(), "beanProxyMediatorName", null, 0, 1, BeanFeatureDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanFeatureDecorator_LinkType(), this.getLinkType(), "linkType", "NORMAL", 0, 1, BeanFeatureDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanFeatureDecorator_BeanLocation(), this.getInstanceLocation(), "beanLocation", "GLOBAL_GLOBAL", 0, 1, BeanFeatureDecorator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(beanCompositionEClass, BeanComposition.class, "BeanComposition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBeanComposition_Components(), theEcorePackage.getEObject(), null, "components", null, 0, -1, BeanComposition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBeanComposition_ListenerTypes(), this.getListenerType(), null, "listenerTypes", null, 0, -1, BeanComposition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractEventInvocationEClass, AbstractEventInvocation.class, "AbstractEventInvocation", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAbstractEventInvocation_Callbacks(), this.getCallback(), null, "callbacks", null, 0, -1, AbstractEventInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractEventInvocation_Listener(), this.getListener(), this.getListener_ListenedBy(), "listener", null, 0, 1, AbstractEventInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(listenerTypeEClass, ListenerType.class, "ListenerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getListenerType_Name(), ecorePackage.getEString(), "name", null, 0, 1, ListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getListenerType_ThisPart(), ecorePackage.getEBoolean(), "thisPart", null, 0, 1, ListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getListenerType_Extends(), theJavaRefPackage.getJavaClass(), null, "extends", null, 0, 1, ListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getListenerType_Implements(), theJavaRefPackage.getJavaClass(), null, "implements", null, 0, -1, ListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getListenerType_Is(), theJavaRefPackage.getJavaClass(), null, "is", null, 0, 1, ListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getListenerType_Listeners(), this.getListener(), this.getListener_ListenerType(), "listeners", null, 0, -1, ListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(memberContainerEClass, MemberContainer.class, "MemberContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMemberContainer_Properties(), theEcorePackage.getEObject(), null, "properties", null, 0, -1, MemberContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMemberContainer_Members(), theEcorePackage.getEObject(), null, "members", null, 0, -1, MemberContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMemberContainer_Implicits(), theEcorePackage.getEObject(), null, "implicits", null, 0, -1, MemberContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(listenerEClass, Listener.class, "Listener", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getListener_ListenedBy(), this.getAbstractEventInvocation(), this.getAbstractEventInvocation_Listener(), "listenedBy", null, 0, -1, Listener.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getListener_ListenerType(), this.getListenerType(), this.getListenerType_Listeners(), "listenerType", null, 0, 1, Listener.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(callbackEClass, Callback.class, "Callback", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCallback_SharedScope(), ecorePackage.getEBoolean(), "sharedScope", null, 0, 1, Callback.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCallback_Method(), theJavaRefPackage.getMethod(), null, "method", null, 1, 1, Callback.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCallback_Statements(), theInstantiationPackage.getPTExpression(), null, "statements", null, 0, -1, Callback.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eventInvocationEClass, EventInvocation.class, "EventInvocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEventInvocation_Event(), theBeaninfoPackage.getBeanEvent(), null, "event", null, 0, 1, EventInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertyChangeEventInvocationEClass, PropertyChangeEventInvocation.class, "PropertyChangeEventInvocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPropertyChangeEventInvocation_AddMethod(), theJavaRefPackage.getMethod(), null, "addMethod", null, 1, 1, PropertyChangeEventInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPropertyChangeEventInvocation_Properties(), this.getPropertyEvent(), null, "properties", null, 0, -1, PropertyChangeEventInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertyEventEClass, PropertyEvent.class, "PropertyEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPropertyEvent_PropertyName(), ecorePackage.getEString(), "propertyName", null, 0, 1, PropertyEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPropertyEvent_UseIfExpression(), ecorePackage.getEBoolean(), "useIfExpression", null, 0, 1, PropertyEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(beanSubclassCompositionEClass, BeanSubclassComposition.class, "BeanSubclassComposition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBeanSubclassComposition_ThisPart(), theInstantiationPackage.getIJavaObjectInstance(), null, "thisPart", null, 1, 1, BeanSubclassComposition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBeanSubclassComposition_Methods(), this.getJCMMethod(), null, "methods", null, 0, -1, BeanSubclassComposition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(jcmMethodEClass, JCMMethod.class, "JCMMethod", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJCMMethod_Name(), ecorePackage.getEString(), "name", null, 0, 1, JCMMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJCMMethod_Initializes(), theEcorePackage.getEObject(), null, "initializes", null, 0, -1, JCMMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJCMMethod_Return(), theEcorePackage.getEObject(), null, "return", null, 0, 1, JCMMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(keyedInstanceLocationEClass, Map.Entry.class, "KeyedInstanceLocation", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getKeyedInstanceLocation_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getKeyedInstanceLocation_Value(), this.getInstanceLocation(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(javaCacheDataEClass, JavaCacheData.class, "JavaCacheData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getJavaCacheData_NamesToBeans(), this.getNamesToBeans(), null, "NamesToBeans", null, 0, -1, JavaCacheData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namesToBeansEClass, Map.Entry.class, "NamesToBeans", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamesToBeans_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamesToBeans_Value(), theEcorePackage.getEObject(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(instanceLocationEEnum, InstanceLocation.class, "InstanceLocation");
		addEEnumLiteral(instanceLocationEEnum, InstanceLocation.GLOBAL_GLOBAL_LITERAL);
		addEEnumLiteral(instanceLocationEEnum, InstanceLocation.GLOBAL_LOCAL_LITERAL);
		addEEnumLiteral(instanceLocationEEnum, InstanceLocation.LOCAL_LITERAL);
		addEEnumLiteral(instanceLocationEEnum, InstanceLocation.PROPERTY_LITERAL);

		initEEnum(linkTypeEEnum, LinkType.class, "LinkType");
		addEEnumLiteral(linkTypeEEnum, LinkType.NORMAL_LITERAL);
		addEEnumLiteral(linkTypeEEnum, LinkType.CHILD_LITERAL);
		addEEnumLiteral(linkTypeEEnum, LinkType.DEPENDENCY_LITERAL);

		// Create resource
		createResource(eNS_URI);
	}

} //JCMPackageImpl
