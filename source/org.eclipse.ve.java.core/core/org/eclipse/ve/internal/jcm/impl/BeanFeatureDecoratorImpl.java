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
 *  $RCSfile: BeanFeatureDecoratorImpl.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.cdm.impl.MapEntryImpl;
import org.eclipse.ve.internal.cdm.model.KeyedValueHolderHelper;
import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;
import org.eclipse.ve.internal.jcm.JCMPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bean Feature Decorator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl#getKeyedValues <em>Keyed Values</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl#getBeanProxyMediatorName <em>Bean Proxy Mediator Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl#isChildFeature <em>Child Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BeanFeatureDecoratorImpl extends EAnnotationImpl implements BeanFeatureDecorator {
	
	/*
	 * This method is here only because generation of KeyedValueHolders implementations
	 * have an import for MapEntryImpl, even though never actually used. This gets rid
	 * of the unused import warning that would occur after every generation.
	 */
	private static MapEntryImpl dummy() {
		return null;
	}	

	/**
	 * The cached value of the '{@link #getKeyedValues() <em>Keyed Values</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyedValues()
	 * @generated
	 * @ordered
	 */
	protected EMap keyedValues = null;

	/**
	 * The default value of the '{@link #getBeanProxyMediatorName() <em>Bean Proxy Mediator Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanProxyMediatorName()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_PROXY_MEDIATOR_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanProxyMediatorName() <em>Bean Proxy Mediator Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanProxyMediatorName()
	 * @generated
	 * @ordered
	 */
	protected String beanProxyMediatorName = BEAN_PROXY_MEDIATOR_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isChildFeature() <em>Child Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isChildFeature()
	 * @generated
	 * @ordered
	 */
  protected static final boolean CHILD_FEATURE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isChildFeature() <em>Child Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isChildFeature()
	 * @generated
	 * @ordered
	 */
  protected boolean childFeature = CHILD_FEATURE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected BeanFeatureDecoratorImpl() {
		super();
		setSource(this.getClass().getName());		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getBeanFeatureDecorator();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EMap getKeyedValues() {
		if (keyedValues == null) {
			dummy();	// Here just to get rid of non-used method warning.
			keyedValues = KeyedValueHolderHelper.createKeyedValuesEMap(this, JCMPackage.BEAN_FEATURE_DECORATOR__KEYED_VALUES);
		}
		return keyedValues;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanProxyMediatorName() {
		return beanProxyMediatorName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanProxyMediatorName(String newBeanProxyMediatorName) {
		String oldBeanProxyMediatorName = beanProxyMediatorName;
		beanProxyMediatorName = newBeanProxyMediatorName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_PROXY_MEDIATOR_NAME, oldBeanProxyMediatorName, beanProxyMediatorName));
	}

	/**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public boolean isChildFeature() {
		return childFeature;
	}

	/**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void setChildFeature(boolean newChildFeature) {
		boolean oldChildFeature = childFeature;
		childFeature = newChildFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_FEATURE_DECORATOR__CHILD_FEATURE, oldChildFeature, childFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.BEAN_FEATURE_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT, msgs);
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
				case JCMPackage.BEAN_FEATURE_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_FEATURE_DECORATOR__DETAILS:
					return ((InternalEList)getDetails()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT:
					return eBasicSetContainer(null, JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT, msgs);
				case JCMPackage.BEAN_FEATURE_DECORATOR__CONTENTS:
					return ((InternalEList)getContents()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_FEATURE_DECORATOR__KEYED_VALUES:
					return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
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
				case JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT:
					return ((InternalEObject)eContainer).eInverseRemove(this, EcorePackage.EMODEL_ELEMENT__EANNOTATIONS, EModelElement.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return ((InternalEObject)eContainer).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case JCMPackage.BEAN_FEATURE_DECORATOR__EANNOTATIONS:
				return getEAnnotations();
			case JCMPackage.BEAN_FEATURE_DECORATOR__SOURCE:
				return getSource();
			case JCMPackage.BEAN_FEATURE_DECORATOR__DETAILS:
				return getDetails();
			case JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement();
			case JCMPackage.BEAN_FEATURE_DECORATOR__CONTENTS:
				return getContents();
			case JCMPackage.BEAN_FEATURE_DECORATOR__REFERENCES:
				return getReferences();
			case JCMPackage.BEAN_FEATURE_DECORATOR__KEYED_VALUES:
				return getKeyedValues();
			case JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_PROXY_MEDIATOR_NAME:
				return getBeanProxyMediatorName();
			case JCMPackage.BEAN_FEATURE_DECORATOR__CHILD_FEATURE:
				return isChildFeature() ? Boolean.TRUE : Boolean.FALSE;
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case JCMPackage.BEAN_FEATURE_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__SOURCE:
				setSource((String)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__DETAILS:
				getDetails().clear();
				getDetails().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_PROXY_MEDIATOR_NAME:
				setBeanProxyMediatorName((String)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__CHILD_FEATURE:
				setChildFeature(((Boolean)newValue).booleanValue());
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
			case JCMPackage.BEAN_FEATURE_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__DETAILS:
				getDetails().clear();
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)null);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__CONTENTS:
				getContents().clear();
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__REFERENCES:
				getReferences().clear();
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_PROXY_MEDIATOR_NAME:
				setBeanProxyMediatorName(BEAN_PROXY_MEDIATOR_NAME_EDEFAULT);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__CHILD_FEATURE:
				setChildFeature(CHILD_FEATURE_EDEFAULT);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case JCMPackage.BEAN_FEATURE_DECORATOR__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case JCMPackage.BEAN_FEATURE_DECORATOR__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case JCMPackage.BEAN_FEATURE_DECORATOR__DETAILS:
				return details != null && !details.isEmpty();
			case JCMPackage.BEAN_FEATURE_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement() != null;
			case JCMPackage.BEAN_FEATURE_DECORATOR__CONTENTS:
				return contents != null && !contents.isEmpty();
			case JCMPackage.BEAN_FEATURE_DECORATOR__REFERENCES:
				return references != null && !references.isEmpty();
			case JCMPackage.BEAN_FEATURE_DECORATOR__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_PROXY_MEDIATOR_NAME:
				return BEAN_PROXY_MEDIATOR_NAME_EDEFAULT == null ? beanProxyMediatorName != null : !BEAN_PROXY_MEDIATOR_NAME_EDEFAULT.equals(beanProxyMediatorName);
			case JCMPackage.BEAN_FEATURE_DECORATOR__CHILD_FEATURE:
				return childFeature != CHILD_FEATURE_EDEFAULT;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class baseClass) {
		if (baseClass == KeyedValueHolder.class) {
			switch (derivedFeatureID) {
				case JCMPackage.BEAN_FEATURE_DECORATOR__KEYED_VALUES: return CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES;
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
		if (baseClass == KeyedValueHolder.class) {
			switch (baseFeatureID) {
				case CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES: return JCMPackage.BEAN_FEATURE_DECORATOR__KEYED_VALUES;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (beanProxyMediatorName: ");
		result.append(beanProxyMediatorName);
		result.append(", childFeature: ");
		result.append(childFeature);
		result.append(')');
		return result.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.InternalEObject#eObjectForURIFragmentSegment(java.lang.String)
	 */
	public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
		EObject eo = KeyedValueHolderHelper.eObjectForURIFragmentSegment(this, uriFragmentSegment);
		return eo == KeyedValueHolderHelper.NOT_KEYED_VALUES_FRAGMENT ? super.eObjectForURIFragmentSegment(uriFragmentSegment) : eo;
	}	

} //BeanFeatureDecoratorImpl
