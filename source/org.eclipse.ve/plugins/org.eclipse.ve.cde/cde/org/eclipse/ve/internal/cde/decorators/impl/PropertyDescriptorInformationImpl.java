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
 *  $RCSfile: PropertyDescriptorInformationImpl.java,v $
 *  $Revision: 1.8 $  $Date: 2005-09-19 15:45:35 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

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
		return DecoratorsPackage.eINSTANCE.getPropertyDescriptorInformation();
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
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__DETAILS:
					return ((InternalEList)getDetails()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT:
					return eBasicSetContainer(null, DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT, msgs);
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__CONTENTS:
					return ((InternalEList)getContents()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs) {
		if (eContainerFeatureID >= 0) {
			switch (eContainerFeatureID) {
				case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT:
					return eContainer.eInverseRemove(this, EcorePackage.EMODEL_ELEMENT__EANNOTATIONS, EModelElement.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return eContainer.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EANNOTATIONS:
				return getEAnnotations();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__SOURCE:
				return getSource();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__DETAILS:
				return getDetails();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT:
				return getEModelElement();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__CONTENTS:
				return getContents();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__REFERENCES:
				return getReferences();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER:
				return isAdapter() ? Boolean.TRUE : Boolean.FALSE;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME:
				return getPropertyDescriptorClassname();
		}
		return eDynamicGet(eFeature, resolve);
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
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__SOURCE:
				return source != null && !getClass().getName().equals(source);
			default:
				return eIsSetGen(eFeature);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSetGen(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__DETAILS:
				return details != null && !details.isEmpty();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT:
				return getEModelElement() != null;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__CONTENTS:
				return contents != null && !contents.isEmpty();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__REFERENCES:
				return references != null && !references.isEmpty();
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER:
				return ((eFlags & ADAPTER_EFLAG) != 0) != ADAPTER_EDEFAULT;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME:
				return PROPERTY_DESCRIPTOR_CLASSNAME_EDEFAULT == null ? propertyDescriptorClassname != null : !PROPERTY_DESCRIPTOR_CLASSNAME_EDEFAULT.equals(propertyDescriptorClassname);
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__SOURCE:
				setSource((String)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__DETAILS:
				getDetails().clear();
				getDetails().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT:
				setEModelElement((EModelElement)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER:
				setAdapter(((Boolean)newValue).booleanValue());
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME:
				setPropertyDescriptorClassname((String)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__DETAILS:
				getDetails().clear();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__EMODEL_ELEMENT:
				setEModelElement((EModelElement)null);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__CONTENTS:
				getContents().clear();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__REFERENCES:
				getReferences().clear();
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__ADAPTER:
				setAdapter(ADAPTER_EDEFAULT);
				return;
			case DecoratorsPackage.PROPERTY_DESCRIPTOR_INFORMATION__PROPERTY_DESCRIPTOR_CLASSNAME:
				setPropertyDescriptorClassname(PROPERTY_DESCRIPTOR_CLASSNAME_EDEFAULT);
				return;
		}
		eDynamicUnset(eFeature);
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
