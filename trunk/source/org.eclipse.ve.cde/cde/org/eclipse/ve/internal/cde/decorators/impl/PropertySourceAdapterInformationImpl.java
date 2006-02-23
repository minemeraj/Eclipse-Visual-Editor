/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $RCSfile: PropertySourceAdapterInformationImpl.java,v $
 *  $Revision: 1.10 $  $Date: 2006-02-23 22:36:39 $ 
 */

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.decorators.PropertySourceAdapterInformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Source Adapter Information</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.decorators.impl.PropertySourceAdapterInformationImpl#getPropertySourceAdapterClassname <em>Property Source Adapter Classname</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class PropertySourceAdapterInformationImpl extends EAnnotationImpl implements PropertySourceAdapterInformation {

	/**
	 * The default value of the '{@link #getPropertySourceAdapterClassname() <em>Property Source Adapter Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertySourceAdapterClassname()
	 * @generated
	 * @ordered
	 */
	protected static final String PROPERTY_SOURCE_ADAPTER_CLASSNAME_EDEFAULT = null;

	
	/**
	 * The cached value of the '{@link #getPropertySourceAdapterClassname() <em>Property Source Adapter Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertySourceAdapterClassname()
	 * @generated
	 * @ordered
	 */
	protected String propertySourceAdapterClassname = PROPERTY_SOURCE_ADAPTER_CLASSNAME_EDEFAULT;
	
	/**
	 * The flag representing whether the Property Source Adapter Classname attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int PROPERTY_SOURCE_ADAPTER_CLASSNAME_ESETFLAG = 1 << 8;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected PropertySourceAdapterInformationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DecoratorsPackage.Literals.PROPERTY_SOURCE_ADAPTER_INFORMATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPropertySourceAdapterClassname() {
		return propertySourceAdapterClassname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPropertySourceAdapterClassname(String newPropertySourceAdapterClassname) {
		String oldPropertySourceAdapterClassname = propertySourceAdapterClassname;
		propertySourceAdapterClassname = newPropertySourceAdapterClassname;
		boolean oldPropertySourceAdapterClassnameESet = (eFlags & PROPERTY_SOURCE_ADAPTER_CLASSNAME_ESETFLAG) != 0;
		eFlags |= PROPERTY_SOURCE_ADAPTER_CLASSNAME_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME, oldPropertySourceAdapterClassname, propertySourceAdapterClassname, !oldPropertySourceAdapterClassnameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPropertySourceAdapterClassname() {
		String oldPropertySourceAdapterClassname = propertySourceAdapterClassname;
		boolean oldPropertySourceAdapterClassnameESet = (eFlags & PROPERTY_SOURCE_ADAPTER_CLASSNAME_ESETFLAG) != 0;
		propertySourceAdapterClassname = PROPERTY_SOURCE_ADAPTER_CLASSNAME_EDEFAULT;
		eFlags &= ~PROPERTY_SOURCE_ADAPTER_CLASSNAME_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME, oldPropertySourceAdapterClassname, PROPERTY_SOURCE_ADAPTER_CLASSNAME_EDEFAULT, oldPropertySourceAdapterClassnameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPropertySourceAdapterClassname() {
		return (eFlags & PROPERTY_SOURCE_ADAPTER_CLASSNAME_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME:
				return getPropertySourceAdapterClassname();
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME:
				setPropertySourceAdapterClassname((String)newValue);
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME:
				unsetPropertySourceAdapterClassname();
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME:
				return isSetPropertySourceAdapterClassname();
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__SOURCE:
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
		result.append(" (propertySourceAdapterClassname: ");
		if ((eFlags & PROPERTY_SOURCE_ADAPTER_CLASSNAME_ESETFLAG) != 0) result.append(propertySourceAdapterClassname); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

}
