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
 *  $RCSfile: BeanFeatureDecoratorImpl.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:30:46 $ 
 */

import java.util.Collection;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.cdm.impl.MapEntryImpl;
import org.eclipse.ve.internal.cdm.model.KeyedValueHolderHelper;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;
import org.eclipse.ve.internal.jcm.InstanceLocation;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.jcm.LinkType;

import org.eclipse.ve.internal.java.core.IBeanProxyFeatureMediator;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bean Feature Decorator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl#getKeyedValues <em>Keyed Values</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl#getBeanProxyMediatorName <em>Bean Proxy Mediator Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl#getLinkType <em>Link Type</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanFeatureDecoratorImpl#getBeanLocation <em>Bean Location</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BeanFeatureDecoratorImpl extends EAnnotationImpl implements BeanFeatureDecorator {
	
	/*
	 * This method is here only because generation of KeyedValueHolders implementations
	 * have an import for MapEntryImpl and ECoreEMap, even though never actually used. This gets rid
	 * of the unused import warning that would occur after every generation.
	 */
	private static MapEntryImpl dummy() {
		dummy2();
		return null;
	}	
	private static EcoreEMap dummy2() {
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
	 * The default value of the '{@link #getLinkType() <em>Link Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkType()
	 * @generated
	 * @ordered
	 */
	protected static final LinkType LINK_TYPE_EDEFAULT = LinkType.NORMAL_LITERAL;

	/**
	 * The cached value of the '{@link #getLinkType() <em>Link Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkType()
	 * @generated
	 * @ordered
	 */
	protected LinkType linkType = LINK_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBeanLocation() <em>Bean Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanLocation()
	 * @generated
	 * @ordered
	 */
	protected static final InstanceLocation BEAN_LOCATION_EDEFAULT = InstanceLocation.GLOBAL_GLOBAL_LITERAL;

	/**
	 * The cached value of the '{@link #getBeanLocation() <em>Bean Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanLocation()
	 * @generated
	 * @ordered
	 */
	protected InstanceLocation beanLocation = BEAN_LOCATION_EDEFAULT;

	/**
	 * This is true if the Bean Location attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean beanLocationESet = false;

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

	public void setBeanProxyMediatorName(String newBeanProxyMediatorName) {
		hasRetrievedProxyMediatorClass = false;
		beanProxyMediatorClass = null;
		setBeanProxyMediatorNameGen(newBeanProxyMediatorName);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanProxyMediatorNameGen(String newBeanProxyMediatorName) {
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
	public LinkType getLinkType() {
		return linkType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLinkType(LinkType newLinkType) {
		LinkType oldLinkType = linkType;
		linkType = newLinkType == null ? LINK_TYPE_EDEFAULT : newLinkType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_FEATURE_DECORATOR__LINK_TYPE, oldLinkType, linkType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InstanceLocation getBeanLocation() {
		return beanLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanLocation(InstanceLocation newBeanLocation) {
		InstanceLocation oldBeanLocation = beanLocation;
		beanLocation = newBeanLocation == null ? BEAN_LOCATION_EDEFAULT : newBeanLocation;
		boolean oldBeanLocationESet = beanLocationESet;
		beanLocationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_LOCATION, oldBeanLocation, beanLocation, !oldBeanLocationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetBeanLocation() {
		InstanceLocation oldBeanLocation = beanLocation;
		boolean oldBeanLocationESet = beanLocationESet;
		beanLocation = BEAN_LOCATION_EDEFAULT;
		beanLocationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_LOCATION, oldBeanLocation, BEAN_LOCATION_EDEFAULT, oldBeanLocationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBeanLocation() {
		return beanLocationESet;
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
			case JCMPackage.BEAN_FEATURE_DECORATOR__LINK_TYPE:
				return getLinkType();
			case JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_LOCATION:
				return getBeanLocation();
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
			case JCMPackage.BEAN_FEATURE_DECORATOR__LINK_TYPE:
				setLinkType((LinkType)newValue);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_LOCATION:
				setBeanLocation((InstanceLocation)newValue);
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
			case JCMPackage.BEAN_FEATURE_DECORATOR__LINK_TYPE:
				setLinkType(LINK_TYPE_EDEFAULT);
				return;
			case JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_LOCATION:
				unsetBeanLocation();
				return;
		}
		eDynamicUnset(eFeature);
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
			case JCMPackage.BEAN_FEATURE_DECORATOR__SOURCE:
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
			case JCMPackage.BEAN_FEATURE_DECORATOR__LINK_TYPE:
				return linkType != LINK_TYPE_EDEFAULT;
			case JCMPackage.BEAN_FEATURE_DECORATOR__BEAN_LOCATION:
				return isSetBeanLocation();
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
		result.append(", linkType: ");
		result.append(linkType);
		result.append(", beanLocation: ");
		if (beanLocationESet) result.append(beanLocation); else result.append("<unset>");
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
	
	private Class beanProxyMediatorClass;
	private boolean hasRetrievedProxyMediatorClass;
	
	/** 
	 * @return IBeanProxyFeatureMediator 
	 * This is cached for performance
	 */
	public IBeanProxyFeatureMediator getBeanProxyMediator() {
		try {		
			if (!hasRetrievedProxyMediatorClass) {
				if (getBeanProxyMediatorName() != null) {
					beanProxyMediatorClass = CDEPlugin.getClassFromString(getBeanProxyMediatorName());
				}
				hasRetrievedProxyMediatorClass = true;
			}
			
			if (beanProxyMediatorClass != null)
				return (IBeanProxyFeatureMediator) beanProxyMediatorClass.newInstance();
		} catch ( Exception exc ) {
			JavaVEPlugin.log(exc, Level.WARNING);							
		}
		return null;
	}	

} //BeanFeatureDecoratorImpl
