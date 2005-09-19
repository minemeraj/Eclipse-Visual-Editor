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
		return DecoratorsPackage.eINSTANCE.getPropertySourceAdapterInformation();
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
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT, msgs);
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
				case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__DETAILS:
					return ((InternalEList)getDetails()).basicRemove(otherEnd, msgs);
				case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT:
					return eBasicSetContainer(null, DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT, msgs);
				case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__CONTENTS:
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
				case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT:
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EANNOTATIONS:
				return getEAnnotations();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__SOURCE:
				return getSource();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__DETAILS:
				return getDetails();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT:
				return getEModelElement();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__CONTENTS:
				return getContents();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__REFERENCES:
				return getReferences();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME:
				return getPropertySourceAdapterClassname();
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__SOURCE:
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__DETAILS:
				return details != null && !details.isEmpty();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT:
				return getEModelElement() != null;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__CONTENTS:
				return contents != null && !contents.isEmpty();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__REFERENCES:
				return references != null && !references.isEmpty();
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME:
				return isSetPropertySourceAdapterClassname();
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__SOURCE:
				setSource((String)newValue);
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__DETAILS:
				getDetails().clear();
				getDetails().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT:
				setEModelElement((EModelElement)newValue);
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection)newValue);
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME:
				setPropertySourceAdapterClassname((String)newValue);
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
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__DETAILS:
				getDetails().clear();
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__EMODEL_ELEMENT:
				setEModelElement((EModelElement)null);
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__CONTENTS:
				getContents().clear();
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__REFERENCES:
				getReferences().clear();
				return;
			case DecoratorsPackage.PROPERTY_SOURCE_ADAPTER_INFORMATION__PROPERTY_SOURCE_ADAPTER_CLASSNAME:
				unsetPropertySourceAdapterClassname();
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
		result.append(" (propertySourceAdapterClassname: ");
		if ((eFlags & PROPERTY_SOURCE_ADAPTER_CLASSNAME_ESETFLAG) != 0) result.append(propertySourceAdapterClassname); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

}
