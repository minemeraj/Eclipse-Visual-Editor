/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.decorators.impl;
/*
 *  $RCSfile: PropertyDescriptorInformationImpl.java,v $
 *  $Revision: 1.11 $  $Date: 2006-05-17 20:13:52 $ 
 */


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Descriptor Information</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorInformationImpl#isAdapter <em>Adapter</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertyDescriptorInformationImpl#getPropertyDescriptorClassname <em>Property Descriptor Classname</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class PropertyDescriptorInformationImpl extends EAnnotationImpl implements PropertyDescriptorInformation {

	/**
	 * The default value of the '{@link #isAdapter() <em>Adapter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAdapter()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ADAPTER_EDEFAULT = true;

	/**
	 * The flag representing the value of the '{@link #isAdapter() <em>Adapter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAdapter()
	 * @generated
	 * @ordered
	 */
	protected static final int ADAPTER_EFLAG = 1 << 8;

	/**
	 * The default value of the '{@link #getPropertyDescriptorClassname() <em>Property Descriptor Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertyDescriptorClassname()
	 * @generated
	 * @ordered
	 */
	protected static final String PROPERTY_DESCRIPTOR_CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPropertyDescriptorClassname() <em>Property Descriptor Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertyDescriptorClassname()
	 * @generated
	 * @ordered
	 */
	protected String propertyDescriptorClassname = PROPERTY_DESCRIPTOR_CLASSNAME_EDEFAULT;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected PropertyDescriptorInformationImpl() {
		super();
		eFlags |= ADAPTER_EFLAG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DecoratorsPackage.Literals.PROPERTY_DESCRIPTOR_INFORMATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAdapter() {
		return (eFlags & ADAPTER_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAdapter(boolean newAdapter) {
		boolean oldAdapter = (eFlags & ADAPTER_EFLAG) != 0;
		if (newAdapter) eFlags |= ADAPTER_EFLAG; else eFlags &= ~ADAPTER_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER, oldAdapter, newAdapter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPropertyDescriptorClassname() {
		return propertyDescriptorClassname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPropertyDescriptorClassname(String newPropertyDescriptorClassname) {
		String oldPropertyDescriptorClassname = propertyDescriptorClassname;
		propertyDescriptorClassname = newPropertyDescriptorClassname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME, oldPropertyDescriptorClassname, propertyDescriptorClassname));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER:
				return isAdapter() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME:
				return getPropertyDescriptorClassname();
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
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER:
				setAdapter(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME:
				setPropertyDescriptorClassname((String)newValue);
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
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER:
				setAdapter(ADAPTER_EDEFAULT);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME:
				setPropertyDescriptorClassname(PROPERTY_DESCRIPTOR_CLASSNAME_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean eIsSetGen(int featureID) {
		switch (featureID) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER:
				return ((eFlags & ADAPTER_EFLAG) != 0) != ADAPTER_EDEFAULT;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME:
				return PROPERTY_DESCRIPTOR_CLASSNAME_EDEFAULT == null ? propertyDescriptorClassname != null : !PROPERTY_DESCRIPTOR_CLASSNAME_EDEFAULT.equals(propertyDescriptorClassname);
		}
		return super.eIsSet(featureID);
	}

	/*
	 * Called by overrides to eIsSet to test if source is set. This is because for the 
	 * FeatureDecorator and subclasses, setting source to the classname is considered
	 * to be not set since that is the new default for each class level. By doing this
	 * when serializing it won't waste space and time adding a copy of the source string
	 * to the serialized output and then creating a NEW copy on each decorator loaded
	 * from an XMI file. 
	 * 
	 * @return <code>true</code> if source is not null and not equal to class name.
	 * 
	 * @since 1.1.0
	 */
	public boolean eIsSet(int feature) {
		switch (feature) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__SOURCE:
				return source != null && !eClass().getInstanceClassName().equals(source);
			default:
				return eIsSetGen(feature);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (adapter: ");
		result.append((eFlags & ADAPTER_EFLAG) != 0);
		result.append(", propertyDescriptorClassname: ");
		result.append(propertyDescriptorClassname);
		result.append(')');
		return result.toString();
	}

}
