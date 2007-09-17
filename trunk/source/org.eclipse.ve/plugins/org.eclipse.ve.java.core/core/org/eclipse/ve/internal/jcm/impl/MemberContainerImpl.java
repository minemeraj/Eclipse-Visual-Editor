/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.ve.internal.jcm.impl;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: MemberContainerImpl.java,v $
 *  $Revision: 1.5 $  $Date: 2007-09-17 14:21:53 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.MemberContainer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Member Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.MemberContainerImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.MemberContainerImpl#getMembers <em>Members</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.MemberContainerImpl#getImplicits <em>Implicits</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MemberContainerImpl extends EObjectImpl implements MemberContainer {
	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> properties;

	/**
	 * The cached value of the '{@link #getMembers() <em>Members</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembers()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> members;

	/**
	 * The cached value of the '{@link #getImplicits() <em>Implicits</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImplicits()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> implicits;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MemberContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JCMPackage.Literals.MEMBER_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getMembers() {
		if (members == null) {
			members = new EObjectContainmentEList<EObject>(EObject.class, this, JCMPackage.MEMBER_CONTAINER__MEMBERS);
		}
		return members;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getImplicits() {
		if (implicits == null) {
			implicits = new EObjectContainmentEList<EObject>(EObject.class, this, JCMPackage.MEMBER_CONTAINER__IMPLICITS);
		}
		return implicits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.MEMBER_CONTAINER__PROPERTIES:
				return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
			case JCMPackage.MEMBER_CONTAINER__MEMBERS:
				return ((InternalEList<?>)getMembers()).basicRemove(otherEnd, msgs);
			case JCMPackage.MEMBER_CONTAINER__IMPLICITS:
				return ((InternalEList<?>)getImplicits()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JCMPackage.MEMBER_CONTAINER__PROPERTIES:
				return getProperties();
			case JCMPackage.MEMBER_CONTAINER__MEMBERS:
				return getMembers();
			case JCMPackage.MEMBER_CONTAINER__IMPLICITS:
				return getImplicits();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
		@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case JCMPackage.MEMBER_CONTAINER__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends EObject>)newValue);
				return;
			case JCMPackage.MEMBER_CONTAINER__MEMBERS:
				getMembers().clear();
				getMembers().addAll((Collection<? extends EObject>)newValue);
				return;
			case JCMPackage.MEMBER_CONTAINER__IMPLICITS:
				getImplicits().clear();
				getImplicits().addAll((Collection<? extends EObject>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case JCMPackage.MEMBER_CONTAINER__PROPERTIES:
				getProperties().clear();
				return;
			case JCMPackage.MEMBER_CONTAINER__MEMBERS:
				getMembers().clear();
				return;
			case JCMPackage.MEMBER_CONTAINER__IMPLICITS:
				getImplicits().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case JCMPackage.MEMBER_CONTAINER__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case JCMPackage.MEMBER_CONTAINER__MEMBERS:
				return members != null && !members.isEmpty();
			case JCMPackage.MEMBER_CONTAINER__IMPLICITS:
				return implicits != null && !implicits.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList<EObject>(EObject.class, this, JCMPackage.MEMBER_CONTAINER__PROPERTIES);
		}
		return properties;
	}

} //MemberContainerImpl
