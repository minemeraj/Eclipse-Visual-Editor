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
 *  $RCSfile: JCMPackage.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:47 $ 
 */

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;


/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.ve.internal.jcm.JCMFactory
 * @generated
 */
public interface JCMPackage extends EPackage{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "jcm"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/ve/internal/jcm.ecore"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.ve.internal.jcm"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	JCMPackage eINSTANCE = org.eclipse.ve.internal.jcm.impl.JCMPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.BeanDecoratorImpl <em>Bean Decorator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.BeanDecoratorImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getBeanDecorator()
	 * @generated
	 */
	int BEAN_DECORATOR = 0;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__EANNOTATIONS = EcorePackage.EANNOTATION__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__SOURCE = EcorePackage.EANNOTATION__SOURCE;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__DETAILS = EcorePackage.EANNOTATION__DETAILS;

	/**
	 * The feature id for the '<em><b>EModel Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__EMODEL_ELEMENT = EcorePackage.EANNOTATION__EMODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__CONTENTS = EcorePackage.EANNOTATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__REFERENCES = EcorePackage.EANNOTATION__REFERENCES;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__KEYED_VALUES = EcorePackage.EANNOTATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Bean Proxy Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME = EcorePackage.EANNOTATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Bean Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__BEAN_LOCATION = EcorePackage.EANNOTATION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Bean Return</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR__BEAN_RETURN = EcorePackage.EANNOTATION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the the '<em>Bean Decorator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_DECORATOR_FEATURE_COUNT = EcorePackage.EANNOTATION_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl <em>Bean Feature Decorator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getBeanFeatureDecorator()
	 * @generated
	 */
	int BEAN_FEATURE_DECORATOR = 1;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__EANNOTATIONS = EcorePackage.EANNOTATION__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__SOURCE = EcorePackage.EANNOTATION__SOURCE;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__DETAILS = EcorePackage.EANNOTATION__DETAILS;

	/**
	 * The feature id for the '<em><b>EModel Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT = EcorePackage.EANNOTATION__EMODEL_ELEMENT;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__CONTENTS = EcorePackage.EANNOTATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__REFERENCES = EcorePackage.EANNOTATION__REFERENCES;

	/**
	 * The feature id for the '<em><b>Keyed Values</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__KEYED_VALUES = EcorePackage.EANNOTATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Bean Proxy Mediator Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__BEAN_PROXY_MEDIATOR_NAME = EcorePackage.EANNOTATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Link Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__LINK_TYPE = EcorePackage.EANNOTATION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Bean Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR__BEAN_LOCATION = EcorePackage.EANNOTATION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the the '<em>Bean Feature Decorator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_DECORATOR_FEATURE_COUNT = EcorePackage.EANNOTATION_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.BeanCompositionImpl <em>Bean Composition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.BeanCompositionImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getBeanComposition()
	 * @generated
	 */
	int BEAN_COMPOSITION = 2;

	/**
	 * The feature id for the '<em><b>Diagrams</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_COMPOSITION__DIAGRAMS = CDMPackage.DIAGRAM_DATA__DIAGRAMS;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_COMPOSITION__ANNOTATIONS = CDMPackage.DIAGRAM_DATA__ANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_COMPOSITION__PROPERTIES = CDMPackage.DIAGRAM_DATA_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_COMPOSITION__MEMBERS = CDMPackage.DIAGRAM_DATA_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Components</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_COMPOSITION__COMPONENTS = CDMPackage.DIAGRAM_DATA_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Listener Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_COMPOSITION__LISTENER_TYPES = CDMPackage.DIAGRAM_DATA_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the the '<em>Bean Composition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_COMPOSITION_FEATURE_COUNT = CDMPackage.DIAGRAM_DATA_FEATURE_COUNT + 4;


	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.AbstractEventInvocationImpl <em>Abstract Event Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.AbstractEventInvocationImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getAbstractEventInvocation()
	 * @generated
	 */
	int ABSTRACT_EVENT_INVOCATION = 3;

	/**
	 * The feature id for the '<em><b>Callbacks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_EVENT_INVOCATION__CALLBACKS = 0;

	/**
	 * The feature id for the '<em><b>Listener</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_EVENT_INVOCATION__LISTENER = 1;

	/**
	 * The number of structural features of the the '<em>Abstract Event Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_EVENT_INVOCATION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.EventInvocationImpl <em>Event Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.EventInvocationImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getEventInvocation()
	 * @generated
	 */
	int EVENT_INVOCATION = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.PropertyChangeEventInvocationImpl <em>Property Change Event Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.PropertyChangeEventInvocationImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getPropertyChangeEventInvocation()
	 * @generated
	 */
	int PROPERTY_CHANGE_EVENT_INVOCATION = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.ListenerTypeImpl <em>Listener Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.ListenerTypeImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getListenerType()
	 * @generated
	 */
	int LISTENER_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER_TYPE__NAME = 0;

	/**
	 * The feature id for the '<em><b>This Part</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER_TYPE__THIS_PART = 1;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER_TYPE__EXTENDS = 2;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER_TYPE__IMPLEMENTS = 3;

	/**
	 * The feature id for the '<em><b>Is</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER_TYPE__IS = 4;

	/**
	 * The feature id for the '<em><b>Listeners</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER_TYPE__LISTENERS = 5;

	/**
	 * The number of structural features of the the '<em>Listener Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.ListenerImpl <em>Listener</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.ListenerImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getListener()
	 * @generated
	 */
	int LISTENER = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.CallbackImpl <em>Callback</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.CallbackImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getCallback()
	 * @generated
	 */
	int CALLBACK = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.MemberContainerImpl <em>Member Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.MemberContainerImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getMemberContainer()
	 * @generated
	 */
	int MEMBER_CONTAINER = 5;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_CONTAINER__PROPERTIES = 0;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_CONTAINER__MEMBERS = 1;

	/**
	 * The number of structural features of the the '<em>Member Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_CONTAINER_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Listened By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER__LISTENED_BY = 0;

	/**
	 * The feature id for the '<em><b>Listener Type</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER__LISTENER_TYPE = 1;

	/**
	 * The number of structural features of the the '<em>Listener</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LISTENER_FEATURE_COUNT = 2;


	/**
	 * The feature id for the '<em><b>Shared Scope</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALLBACK__SHARED_SCOPE = 0;

	/**
	 * The feature id for the '<em><b>Method</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALLBACK__METHOD = 1;

	/**
	 * The feature id for the '<em><b>Statements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALLBACK__STATEMENTS = 2;

	/**
	 * The number of structural features of the the '<em>Callback</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALLBACK_FEATURE_COUNT = 3;


	/**
	 * The feature id for the '<em><b>Callbacks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_INVOCATION__CALLBACKS = ABSTRACT_EVENT_INVOCATION__CALLBACKS;

	/**
	 * The feature id for the '<em><b>Listener</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_INVOCATION__LISTENER = ABSTRACT_EVENT_INVOCATION__LISTENER;

	/**
	 * The feature id for the '<em><b>Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_INVOCATION__EVENT = ABSTRACT_EVENT_INVOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Event Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_INVOCATION_FEATURE_COUNT = ABSTRACT_EVENT_INVOCATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Callbacks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CHANGE_EVENT_INVOCATION__CALLBACKS = ABSTRACT_EVENT_INVOCATION__CALLBACKS;

	/**
	 * The feature id for the '<em><b>Listener</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CHANGE_EVENT_INVOCATION__LISTENER = ABSTRACT_EVENT_INVOCATION__LISTENER;

	/**
	 * The feature id for the '<em><b>Add Method</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CHANGE_EVENT_INVOCATION__ADD_METHOD = ABSTRACT_EVENT_INVOCATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES = ABSTRACT_EVENT_INVOCATION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Property Change Event Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CHANGE_EVENT_INVOCATION_FEATURE_COUNT = ABSTRACT_EVENT_INVOCATION_FEATURE_COUNT + 2;


	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.PropertyEventImpl <em>Property Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.PropertyEventImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getPropertyEvent()
	 * @generated
	 */
	int PROPERTY_EVENT = 10;

	/**
	 * The feature id for the '<em><b>Property Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_EVENT__PROPERTY_NAME = 0;

	/**
	 * The feature id for the '<em><b>Use If Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_EVENT__USE_IF_EXPRESSION = 1;

	/**
	 * The number of structural features of the the '<em>Property Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_EVENT_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.BeanSubclassCompositionImpl <em>Bean Subclass Composition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.BeanSubclassCompositionImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getBeanSubclassComposition()
	 * @generated
	 */
	int BEAN_SUBCLASS_COMPOSITION = 11;

	/**
	 * The feature id for the '<em><b>Diagrams</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION__DIAGRAMS = BEAN_COMPOSITION__DIAGRAMS;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION__ANNOTATIONS = BEAN_COMPOSITION__ANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION__PROPERTIES = BEAN_COMPOSITION__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION__MEMBERS = BEAN_COMPOSITION__MEMBERS;

	/**
	 * The feature id for the '<em><b>Components</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION__COMPONENTS = BEAN_COMPOSITION__COMPONENTS;

	/**
	 * The feature id for the '<em><b>Listener Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION__LISTENER_TYPES = BEAN_COMPOSITION__LISTENER_TYPES;

	/**
	 * The feature id for the '<em><b>This Part</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION__THIS_PART = BEAN_COMPOSITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION__METHODS = BEAN_COMPOSITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Bean Subclass Composition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_SUBCLASS_COMPOSITION_FEATURE_COUNT = BEAN_COMPOSITION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.JCMMethodImpl <em>Method</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.JCMMethodImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getJCMMethod()
	 * @generated
	 */
	int JCM_METHOD = 12;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JCM_METHOD__PROPERTIES = MEMBER_CONTAINER__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JCM_METHOD__MEMBERS = MEMBER_CONTAINER__MEMBERS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JCM_METHOD__NAME = MEMBER_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Initializes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JCM_METHOD__INITIALIZES = MEMBER_CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Return</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JCM_METHOD__RETURN = MEMBER_CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the the '<em>Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JCM_METHOD_FEATURE_COUNT = MEMBER_CONTAINER_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.KeyedInstanceLocationImpl <em>Keyed Instance Location</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.KeyedInstanceLocationImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getKeyedInstanceLocation()
	 * @generated
	 */
	int KEYED_INSTANCE_LOCATION = 13;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_INSTANCE_LOCATION__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_INSTANCE_LOCATION__VALUE = 1;

	/**
	 * The number of structural features of the the '<em>Keyed Instance Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEYED_INSTANCE_LOCATION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.JavaCacheDataImpl <em>Java Cache Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.JavaCacheDataImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getJavaCacheData()
	 * @generated
	 */
	int JAVA_CACHE_DATA = 14;

	/**
	 * The feature id for the '<em><b>Names To Beans</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JAVA_CACHE_DATA__NAMES_TO_BEANS = 0;

	/**
	 * The number of structural features of the the '<em>Java Cache Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JAVA_CACHE_DATA_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.impl.NamesToBeansImpl <em>Names To Beans</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.impl.NamesToBeansImpl
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getNamesToBeans()
	 * @generated
	 */
	int NAMES_TO_BEANS = 15;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMES_TO_BEANS__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMES_TO_BEANS__VALUE = 1;

	/**
	 * The number of structural features of the the '<em>Names To Beans</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMES_TO_BEANS_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.InstanceLocation <em>Instance Location</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.InstanceLocation
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getInstanceLocation()
	 * @generated
	 */
	int INSTANCE_LOCATION = 16;


	/**
	 * The meta object id for the '{@link org.eclipse.ve.internal.jcm.LinkType <em>Link Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.ve.internal.jcm.LinkType
	 * @see org.eclipse.ve.internal.jcm.impl.JCMPackageImpl#getLinkType()
	 * @generated
	 */
	int LINK_TYPE = 17;


	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.BeanDecorator <em>Bean Decorator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bean Decorator</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanDecorator
	 * @generated
	 */
	EClass getBeanDecorator();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanProxyClassName <em>Bean Proxy Class Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Proxy Class Name</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanDecorator#getBeanProxyClassName()
	 * @see #getBeanDecorator()
	 * @generated
	 */
	EAttribute getBeanDecorator_BeanProxyClassName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanLocation <em>Bean Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Location</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanDecorator#getBeanLocation()
	 * @see #getBeanDecorator()
	 * @generated
	 */
	EAttribute getBeanDecorator_BeanLocation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.BeanDecorator#isBeanReturn <em>Bean Return</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Return</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanDecorator#isBeanReturn()
	 * @see #getBeanDecorator()
	 * @generated
	 */
	EAttribute getBeanDecorator_BeanReturn();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.BeanFeatureDecorator <em>Bean Feature Decorator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bean Feature Decorator</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanFeatureDecorator
	 * @generated
	 */
	EClass getBeanFeatureDecorator();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.BeanFeatureDecorator#getBeanProxyMediatorName <em>Bean Proxy Mediator Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Proxy Mediator Name</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanFeatureDecorator#getBeanProxyMediatorName()
	 * @see #getBeanFeatureDecorator()
	 * @generated
	 */
	EAttribute getBeanFeatureDecorator_BeanProxyMediatorName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.BeanFeatureDecorator#getLinkType <em>Link Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Link Type</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanFeatureDecorator#getLinkType()
	 * @see #getBeanFeatureDecorator()
	 * @generated
	 */
	EAttribute getBeanFeatureDecorator_LinkType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.BeanFeatureDecorator#getBeanLocation <em>Bean Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Location</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanFeatureDecorator#getBeanLocation()
	 * @see #getBeanFeatureDecorator()
	 * @generated
	 */
	EAttribute getBeanFeatureDecorator_BeanLocation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.BeanComposition <em>Bean Composition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bean Composition</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanComposition
	 * @generated
	 */
	EClass getBeanComposition();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ve.internal.jcm.BeanComposition#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Components</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanComposition#getComponents()
	 * @see #getBeanComposition()
	 * @generated
	 */
	EReference getBeanComposition_Components();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.jcm.BeanComposition#getListenerTypes <em>Listener Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Listener Types</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanComposition#getListenerTypes()
	 * @see #getBeanComposition()
	 * @generated
	 */
	EReference getBeanComposition_ListenerTypes();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.AbstractEventInvocation <em>Abstract Event Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Event Invocation</em>'.
	 * @see org.eclipse.ve.internal.jcm.AbstractEventInvocation
	 * @generated
	 */
	EClass getAbstractEventInvocation();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.jcm.AbstractEventInvocation#getCallbacks <em>Callbacks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Callbacks</em>'.
	 * @see org.eclipse.ve.internal.jcm.AbstractEventInvocation#getCallbacks()
	 * @see #getAbstractEventInvocation()
	 * @generated
	 */
	EReference getAbstractEventInvocation_Callbacks();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.jcm.AbstractEventInvocation#getListener <em>Listener</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Listener</em>'.
	 * @see org.eclipse.ve.internal.jcm.AbstractEventInvocation#getListener()
	 * @see #getAbstractEventInvocation()
	 * @generated
	 */
	EReference getAbstractEventInvocation_Listener();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.EventInvocation <em>Event Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event Invocation</em>'.
	 * @see org.eclipse.ve.internal.jcm.EventInvocation
	 * @generated
	 */
	EClass getEventInvocation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.jcm.EventInvocation#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Event</em>'.
	 * @see org.eclipse.ve.internal.jcm.EventInvocation#getEvent()
	 * @see #getEventInvocation()
	 * @generated
	 */
	EReference getEventInvocation_Event();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation <em>Property Change Event Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Change Event Invocation</em>'.
	 * @see org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation
	 * @generated
	 */
	EClass getPropertyChangeEventInvocation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation#getAddMethod <em>Add Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Add Method</em>'.
	 * @see org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation#getAddMethod()
	 * @see #getPropertyChangeEventInvocation()
	 * @generated
	 */
	EReference getPropertyChangeEventInvocation_AddMethod();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation#getProperties()
	 * @see #getPropertyChangeEventInvocation()
	 * @generated
	 */
	EReference getPropertyChangeEventInvocation_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.PropertyEvent <em>Property Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Event</em>'.
	 * @see org.eclipse.ve.internal.jcm.PropertyEvent
	 * @generated
	 */
	EClass getPropertyEvent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.PropertyEvent#getPropertyName <em>Property Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property Name</em>'.
	 * @see org.eclipse.ve.internal.jcm.PropertyEvent#getPropertyName()
	 * @see #getPropertyEvent()
	 * @generated
	 */
	EAttribute getPropertyEvent_PropertyName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.PropertyEvent#isUseIfExpression <em>Use If Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use If Expression</em>'.
	 * @see org.eclipse.ve.internal.jcm.PropertyEvent#isUseIfExpression()
	 * @see #getPropertyEvent()
	 * @generated
	 */
	EAttribute getPropertyEvent_UseIfExpression();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.BeanSubclassComposition <em>Bean Subclass Composition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bean Subclass Composition</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanSubclassComposition
	 * @generated
	 */
	EClass getBeanSubclassComposition();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.ve.internal.jcm.BeanSubclassComposition#getThisPart <em>This Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>This Part</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanSubclassComposition#getThisPart()
	 * @see #getBeanSubclassComposition()
	 * @generated
	 */
	EReference getBeanSubclassComposition_ThisPart();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.jcm.BeanSubclassComposition#getMethods <em>Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Methods</em>'.
	 * @see org.eclipse.ve.internal.jcm.BeanSubclassComposition#getMethods()
	 * @see #getBeanSubclassComposition()
	 * @generated
	 */
	EReference getBeanSubclassComposition_Methods();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.JCMMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Method</em>'.
	 * @see org.eclipse.ve.internal.jcm.JCMMethod
	 * @generated
	 */
	EClass getJCMMethod();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.JCMMethod#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ve.internal.jcm.JCMMethod#getName()
	 * @see #getJCMMethod()
	 * @generated
	 */
	EAttribute getJCMMethod_Name();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ve.internal.jcm.JCMMethod#getInitializes <em>Initializes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Initializes</em>'.
	 * @see org.eclipse.ve.internal.jcm.JCMMethod#getInitializes()
	 * @see #getJCMMethod()
	 * @generated
	 */
	EReference getJCMMethod_Initializes();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.jcm.JCMMethod#getReturn <em>Return</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Return</em>'.
	 * @see org.eclipse.ve.internal.jcm.JCMMethod#getReturn()
	 * @see #getJCMMethod()
	 * @generated
	 */
	EReference getJCMMethod_Return();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Keyed Instance Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keyed Instance Location</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="java.lang.String" valueType="org.eclipse.ve.internal.jcm.InstanceLocation" 
	 * @generated
	 */
	EClass getKeyedInstanceLocation();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedInstanceLocation()
	 * @generated
	 */
	EAttribute getKeyedInstanceLocation_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getKeyedInstanceLocation()
	 * @generated
	 */
	EAttribute getKeyedInstanceLocation_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.JavaCacheData <em>Java Cache Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Java Cache Data</em>'.
	 * @see org.eclipse.ve.internal.jcm.JavaCacheData
	 * @generated
	 */
	EClass getJavaCacheData();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.ve.internal.jcm.JavaCacheData#getNamesToBeans <em>Names To Beans</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Names To Beans</em>'.
	 * @see org.eclipse.ve.internal.jcm.JavaCacheData#getNamesToBeans()
	 * @see #getJavaCacheData()
	 * @generated
	 */
	EReference getJavaCacheData_NamesToBeans();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Names To Beans</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Names To Beans</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="java.lang.String" valueType="org.eclipse.emf.ecore.EObject" 
	 * @generated
	 */
	EClass getNamesToBeans();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getNamesToBeans()
	 * @generated
	 */
	EAttribute getNamesToBeans_Key();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getNamesToBeans()
	 * @generated
	 */
	EReference getNamesToBeans_Value();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.ve.internal.jcm.InstanceLocation <em>Instance Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Instance Location</em>'.
	 * @see org.eclipse.ve.internal.jcm.InstanceLocation
	 * @generated
	 */
	EEnum getInstanceLocation();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.ve.internal.jcm.LinkType <em>Link Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Link Type</em>'.
	 * @see org.eclipse.ve.internal.jcm.LinkType
	 * @generated
	 */
	EEnum getLinkType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	JCMFactory getJCMFactory();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.ListenerType <em>Listener Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Listener Type</em>'.
	 * @see org.eclipse.ve.internal.jcm.ListenerType
	 * @generated
	 */
	EClass getListenerType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.ListenerType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.ve.internal.jcm.ListenerType#getName()
	 * @see #getListenerType()
	 * @generated
	 */
	EAttribute getListenerType_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.ListenerType#isThisPart <em>This Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>This Part</em>'.
	 * @see org.eclipse.ve.internal.jcm.ListenerType#isThisPart()
	 * @see #getListenerType()
	 * @generated
	 */
	EAttribute getListenerType_ThisPart();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.jcm.ListenerType#getExtends <em>Extends</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Extends</em>'.
	 * @see org.eclipse.ve.internal.jcm.ListenerType#getExtends()
	 * @see #getListenerType()
	 * @generated
	 */
	EReference getListenerType_Extends();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ve.internal.jcm.ListenerType#getImplements <em>Implements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Implements</em>'.
	 * @see org.eclipse.ve.internal.jcm.ListenerType#getImplements()
	 * @see #getListenerType()
	 * @generated
	 */
	EReference getListenerType_Implements();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.jcm.ListenerType#getIs <em>Is</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Is</em>'.
	 * @see org.eclipse.ve.internal.jcm.ListenerType#getIs()
	 * @see #getListenerType()
	 * @generated
	 */
	EReference getListenerType_Is();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.jcm.ListenerType#getListeners <em>Listeners</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Listeners</em>'.
	 * @see org.eclipse.ve.internal.jcm.ListenerType#getListeners()
	 * @see #getListenerType()
	 * @generated
	 */
	EReference getListenerType_Listeners();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.Listener <em>Listener</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Listener</em>'.
	 * @see org.eclipse.ve.internal.jcm.Listener
	 * @generated
	 */
	EClass getListener();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.ve.internal.jcm.Listener#getListenedBy <em>Listened By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Listened By</em>'.
	 * @see org.eclipse.ve.internal.jcm.Listener#getListenedBy()
	 * @see #getListener()
	 * @generated
	 */
	EReference getListener_ListenedBy();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.ve.internal.jcm.Listener#getListenerType <em>Listener Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Listener Type</em>'.
	 * @see org.eclipse.ve.internal.jcm.Listener#getListenerType()
	 * @see #getListener()
	 * @generated
	 */
	EReference getListener_ListenerType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.Callback <em>Callback</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Callback</em>'.
	 * @see org.eclipse.ve.internal.jcm.Callback
	 * @generated
	 */
	EClass getCallback();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.ve.internal.jcm.Callback#isSharedScope <em>Shared Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shared Scope</em>'.
	 * @see org.eclipse.ve.internal.jcm.Callback#isSharedScope()
	 * @see #getCallback()
	 * @generated
	 */
	EAttribute getCallback_SharedScope();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.ve.internal.jcm.Callback#getMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Method</em>'.
	 * @see org.eclipse.ve.internal.jcm.Callback#getMethod()
	 * @see #getCallback()
	 * @generated
	 */
	EReference getCallback_Method();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.jcm.Callback#getStatements <em>Statements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Statements</em>'.
	 * @see org.eclipse.ve.internal.jcm.Callback#getStatements()
	 * @see #getCallback()
	 * @generated
	 */
	EReference getCallback_Statements();

	/**
	 * Returns the meta object for class '{@link org.eclipse.ve.internal.jcm.MemberContainer <em>Member Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Member Container</em>'.
	 * @see org.eclipse.ve.internal.jcm.MemberContainer
	 * @generated
	 */
	EClass getMemberContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.jcm.MemberContainer#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Members</em>'.
	 * @see org.eclipse.ve.internal.jcm.MemberContainer#getMembers()
	 * @see #getMemberContainer()
	 * @generated
	 */
	EReference getMemberContainer_Members();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.ve.internal.jcm.MemberContainer#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.ve.internal.jcm.MemberContainer#getProperties()
	 * @see #getMemberContainer()
	 * @generated
	 */
	EReference getMemberContainer_Properties();

} //JCMPackage
