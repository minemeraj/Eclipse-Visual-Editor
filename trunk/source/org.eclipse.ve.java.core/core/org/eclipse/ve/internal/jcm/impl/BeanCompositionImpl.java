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
 *  $RCSfile: BeanCompositionImpl.java,v $
 *  $Revision: 1.13 $  $Date: 2006-02-07 17:21:37 $ 
 */

import java.util.Collection;

import java.util.*;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;


import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cdm.impl.DiagramDataImpl;
import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.ListenerType;
import org.eclipse.ve.internal.jcm.MemberContainer;

import org.eclipse.ve.internal.java.core.BeanUtilities;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bean Composition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanCompositionImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanCompositionImpl#getMembers <em>Members</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanCompositionImpl#getImplicits <em>Implicits</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanCompositionImpl#getComponents <em>Components</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanCompositionImpl#getListenerTypes <em>Listener Types</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BeanCompositionImpl extends DiagramDataImpl implements BeanComposition {
	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList properties = null;

	/**
	 * The cached value of the '{@link #getMembers() <em>Members</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembers()
	 * @generated
	 * @ordered
	 */
	protected EList members = null;

	/**
	 * The cached value of the '{@link #getImplicits() <em>Implicits</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImplicits()
	 * @generated
	 * @ordered
	 */
	protected EList implicits = null;

	/**
	 * The cached value of the '{@link #getComponents() <em>Components</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponents()
	 * @generated
	 * @ordered
	 */
	protected EList components = null;

	/**
	 * The cached value of the '{@link #getListenerTypes() <em>Listener Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListenerTypes()
	 * @generated
	 * @ordered
	 */
	protected EList listenerTypes = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BeanCompositionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.Literals.BEAN_COMPOSITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getMembers() {
		if (members == null) {
			members = new EObjectContainmentEList(EObject.class, this, JCMPackage.BEAN_COMPOSITION__MEMBERS);
		}
		return members;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getImplicits() {
		if (implicits == null) {
			implicits = new EObjectContainmentEList(EObject.class, this, JCMPackage.BEAN_COMPOSITION__IMPLICITS);
		}
		return implicits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList(EObject.class, this, JCMPackage.BEAN_COMPOSITION__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getComponents() {
		if (components == null) {
			components = new EObjectResolvingEList(EObject.class, this, JCMPackage.BEAN_COMPOSITION__COMPONENTS);
		}
		return components;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getListenerTypes() {
		if (listenerTypes == null) {
			listenerTypes = new EObjectContainmentEList(ListenerType.class, this, JCMPackage.BEAN_COMPOSITION__LISTENER_TYPES);
		}
		return listenerTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.BEAN_COMPOSITION__PROPERTIES:
				return ((InternalEList)getProperties()).basicRemove(otherEnd, msgs);
			case JCMPackage.BEAN_COMPOSITION__MEMBERS:
				return ((InternalEList)getMembers()).basicRemove(otherEnd, msgs);
			case JCMPackage.BEAN_COMPOSITION__IMPLICITS:
				return ((InternalEList)getImplicits()).basicRemove(otherEnd, msgs);
			case JCMPackage.BEAN_COMPOSITION__LISTENER_TYPES:
				return ((InternalEList)getListenerTypes()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JCMPackage.BEAN_COMPOSITION__PROPERTIES:
				return getProperties();
			case JCMPackage.BEAN_COMPOSITION__MEMBERS:
				return getMembers();
			case JCMPackage.BEAN_COMPOSITION__IMPLICITS:
				return getImplicits();
			case JCMPackage.BEAN_COMPOSITION__COMPONENTS:
				return getComponents();
			case JCMPackage.BEAN_COMPOSITION__LISTENER_TYPES:
				return getListenerTypes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case JCMPackage.BEAN_COMPOSITION__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_COMPOSITION__MEMBERS:
				getMembers().clear();
				getMembers().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_COMPOSITION__IMPLICITS:
				getImplicits().clear();
				getImplicits().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_COMPOSITION__COMPONENTS:
				getComponents().clear();
				getComponents().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_COMPOSITION__LISTENER_TYPES:
				getListenerTypes().clear();
				getListenerTypes().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case JCMPackage.BEAN_COMPOSITION__PROPERTIES:
				getProperties().clear();
				return;
			case JCMPackage.BEAN_COMPOSITION__MEMBERS:
				getMembers().clear();
				return;
			case JCMPackage.BEAN_COMPOSITION__IMPLICITS:
				getImplicits().clear();
				return;
			case JCMPackage.BEAN_COMPOSITION__COMPONENTS:
				getComponents().clear();
				return;
			case JCMPackage.BEAN_COMPOSITION__LISTENER_TYPES:
				getListenerTypes().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case JCMPackage.BEAN_COMPOSITION__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case JCMPackage.BEAN_COMPOSITION__MEMBERS:
				return members != null && !members.isEmpty();
			case JCMPackage.BEAN_COMPOSITION__IMPLICITS:
				return implicits != null && !implicits.isEmpty();
			case JCMPackage.BEAN_COMPOSITION__COMPONENTS:
				return components != null && !components.isEmpty();
			case JCMPackage.BEAN_COMPOSITION__LISTENER_TYPES:
				return listenerTypes != null && !listenerTypes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class baseClass) {
		if (baseClass == MemberContainer.class) {
			switch (derivedFeatureID) {
				case JCMPackage.BEAN_COMPOSITION__PROPERTIES: return JCMPackage.MEMBER_CONTAINER__PROPERTIES;
				case JCMPackage.BEAN_COMPOSITION__MEMBERS: return JCMPackage.MEMBER_CONTAINER__MEMBERS;
				case JCMPackage.BEAN_COMPOSITION__IMPLICITS: return JCMPackage.MEMBER_CONTAINER__IMPLICITS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class baseClass) {
		if (baseClass == MemberContainer.class) {
			switch (baseFeatureID) {
				case JCMPackage.MEMBER_CONTAINER__PROPERTIES: return JCMPackage.BEAN_COMPOSITION__PROPERTIES;
				case JCMPackage.MEMBER_CONTAINER__MEMBERS: return JCMPackage.BEAN_COMPOSITION__MEMBERS;
				case JCMPackage.MEMBER_CONTAINER__IMPLICITS: return JCMPackage.BEAN_COMPOSITION__IMPLICITS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * 
	 * @param classNameToCollect - The string of the class being searched for, e.g. "java.awt.Button" to find all buttons
	 * @param objects - the objects are added to this list (will be the IJavaInstance)
	 * @param labels - the labels that come from the label providers
	 * @param beanComposition - bean composition 
	 * @param editDomain - edit domain
	 * 
	 * @since 1.0.0
	 */
	public static void collectFreeFormParts(String classNameToCollect, List objects, List labels, BeanComposition beanComposition,EditDomain editDomain){
		
		// Having got the root object we can now ask it for all of its child objects that match the desired type we are supposed to list
		JavaClass javaClass = Utilities.getJavaClass(classNameToCollect,((BeanComposition)editDomain.getDiagramData()).eResource().getResourceSet());
		
		Iterator components = beanComposition.getMembers().iterator();
		while(components.hasNext()){
			Object component = components.next();
			if ( component instanceof IJavaObjectInstance ) { // We might have non java components, nor are we interested in primitives.	 
				// Test the class
				IJavaInstance javaComponent = (IJavaObjectInstance) component;
				JavaHelpers componentType= javaComponent.getJavaType();
				if ( javaClass.isAssignableFrom(componentType)) {
					objects.add(component);
					String label = BeanUtilities.getLabel(javaComponent,editDomain);
					labels.add(label);
				}
			}
		}
		// Now we know the children set the items
		String[] items = new String[labels.size()];
		System.arraycopy(labels.toArray(),0,items,0,items.length);

		Arrays.sort(items);
		ArrayList a = new ArrayList() ;
		for (int i = 0; i < items.length; i++) {			
			for (int j = 0; j < labels.size(); j++) {
				if (items[i].equals(labels.get(j))) {
					a.add(objects.get(j));
					break;
				}
			}		
		}
	}
	

} //BeanCompositionImpl
