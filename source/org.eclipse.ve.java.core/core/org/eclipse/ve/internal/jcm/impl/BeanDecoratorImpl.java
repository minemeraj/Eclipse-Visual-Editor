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
 *  $RCSfile: BeanDecoratorImpl.java,v $
 *  $Revision: 1.12 $  $Date: 2005-08-24 23:30:46 $ 
 */

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
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

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bean Decorator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanDecoratorImpl#getKeyedValues <em>Keyed Values</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanDecoratorImpl#getBeanProxyClassName <em>Bean Proxy Class Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanDecoratorImpl#getBeanLocation <em>Bean Location</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.impl.BeanDecoratorImpl#isBeanReturn <em>Bean Return</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BeanDecoratorImpl extends EAnnotationImpl implements BeanDecorator {
	
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
	 * The default value of the '{@link #getBeanProxyClassName() <em>Bean Proxy Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanProxyClassName()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_PROXY_CLASS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanProxyClassName() <em>Bean Proxy Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanProxyClassName()
	 * @generated
	 * @ordered
	 */
	protected String beanProxyClassName = BEAN_PROXY_CLASS_NAME_EDEFAULT;

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
	 * The default value of the '{@link #isBeanReturn() <em>Bean Return</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBeanReturn()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BEAN_RETURN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBeanReturn() <em>Bean Return</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBeanReturn()
	 * @generated
	 * @ordered
	 */
	protected boolean beanReturn = BEAN_RETURN_EDEFAULT;

	/**
	 * This is true if the Bean Return attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean beanReturnESet = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected BeanDecoratorImpl() {
		super();
		setSource(this.getClass().getName());		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.eINSTANCE.getBeanDecorator();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EMap getKeyedValues() {
		if (keyedValues == null) {
			dummy();	// Here just to get rid of non-used method warning.
			keyedValues = KeyedValueHolderHelper.createKeyedValuesEMap(this, JCMPackage.BEAN_DECORATOR__KEYED_VALUES);
		}
		return keyedValues;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanProxyClassName() {
		return beanProxyClassName;
	}

	public void setBeanProxyClassName(String newBeanProxyClassName) {
		hasRetrievedBeanProxyAdapterClass = false;
		beanProxyAdapterClass = null;
		beanProxyAdapterClassConstructor = null;
		setBeanProxyClassNameGen(newBeanProxyClassName);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanProxyClassNameGen(String newBeanProxyClassName) {
		String oldBeanProxyClassName = beanProxyClassName;
		beanProxyClassName = newBeanProxyClassName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME, oldBeanProxyClassName, beanProxyClassName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_DECORATOR__BEAN_LOCATION, oldBeanLocation, beanLocation, !oldBeanLocationESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, JCMPackage.BEAN_DECORATOR__BEAN_LOCATION, oldBeanLocation, BEAN_LOCATION_EDEFAULT, oldBeanLocationESet));
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
	public boolean isBeanReturn() {
		return beanReturn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanReturn(boolean newBeanReturn) {
		boolean oldBeanReturn = beanReturn;
		beanReturn = newBeanReturn;
		boolean oldBeanReturnESet = beanReturnESet;
		beanReturnESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_DECORATOR__BEAN_RETURN, oldBeanReturn, beanReturn, !oldBeanReturnESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetBeanReturn() {
		boolean oldBeanReturn = beanReturn;
		boolean oldBeanReturnESet = beanReturnESet;
		beanReturn = BEAN_RETURN_EDEFAULT;
		beanReturnESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JCMPackage.BEAN_DECORATOR__BEAN_RETURN, oldBeanReturn, BEAN_RETURN_EDEFAULT, oldBeanReturnESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBeanReturn() {
		return beanReturnESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case JCMPackage.BEAN_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT, msgs);
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
				case JCMPackage.BEAN_DECORATOR__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_DECORATOR__DETAILS:
					return ((InternalEList)getDetails()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT:
					return eBasicSetContainer(null, JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT, msgs);
				case JCMPackage.BEAN_DECORATOR__CONTENTS:
					return ((InternalEList)getContents()).basicRemove(otherEnd, msgs);
				case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
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
				case JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT:
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
			case JCMPackage.BEAN_DECORATOR__EANNOTATIONS:
				return getEAnnotations();
			case JCMPackage.BEAN_DECORATOR__SOURCE:
				return getSource();
			case JCMPackage.BEAN_DECORATOR__DETAILS:
				return getDetails();
			case JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement();
			case JCMPackage.BEAN_DECORATOR__CONTENTS:
				return getContents();
			case JCMPackage.BEAN_DECORATOR__REFERENCES:
				return getReferences();
			case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
				return getKeyedValues();
			case JCMPackage.BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME:
				return getBeanProxyClassName();
			case JCMPackage.BEAN_DECORATOR__BEAN_LOCATION:
				return getBeanLocation();
			case JCMPackage.BEAN_DECORATOR__BEAN_RETURN:
				return isBeanReturn() ? Boolean.TRUE : Boolean.FALSE;
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
			case JCMPackage.BEAN_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__SOURCE:
				setSource((String)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__DETAILS:
				getDetails().clear();
				getDetails().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
				getKeyedValues().clear();
				getKeyedValues().addAll((Collection)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME:
				setBeanProxyClassName((String)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__BEAN_LOCATION:
				setBeanLocation((InstanceLocation)newValue);
				return;
			case JCMPackage.BEAN_DECORATOR__BEAN_RETURN:
				setBeanReturn(((Boolean)newValue).booleanValue());
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
			case JCMPackage.BEAN_DECORATOR__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case JCMPackage.BEAN_DECORATOR__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case JCMPackage.BEAN_DECORATOR__DETAILS:
				getDetails().clear();
				return;
			case JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT:
				setEModelElement((EModelElement)null);
				return;
			case JCMPackage.BEAN_DECORATOR__CONTENTS:
				getContents().clear();
				return;
			case JCMPackage.BEAN_DECORATOR__REFERENCES:
				getReferences().clear();
				return;
			case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
				getKeyedValues().clear();
				return;
			case JCMPackage.BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME:
				setBeanProxyClassName(BEAN_PROXY_CLASS_NAME_EDEFAULT);
				return;
			case JCMPackage.BEAN_DECORATOR__BEAN_LOCATION:
				unsetBeanLocation();
				return;
			case JCMPackage.BEAN_DECORATOR__BEAN_RETURN:
				unsetBeanReturn();
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
			case JCMPackage.BEAN_DECORATOR__SOURCE:
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
			case JCMPackage.BEAN_DECORATOR__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case JCMPackage.BEAN_DECORATOR__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case JCMPackage.BEAN_DECORATOR__DETAILS:
				return details != null && !details.isEmpty();
			case JCMPackage.BEAN_DECORATOR__EMODEL_ELEMENT:
				return getEModelElement() != null;
			case JCMPackage.BEAN_DECORATOR__CONTENTS:
				return contents != null && !contents.isEmpty();
			case JCMPackage.BEAN_DECORATOR__REFERENCES:
				return references != null && !references.isEmpty();
			case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case JCMPackage.BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME:
				return BEAN_PROXY_CLASS_NAME_EDEFAULT == null ? beanProxyClassName != null : !BEAN_PROXY_CLASS_NAME_EDEFAULT.equals(beanProxyClassName);
			case JCMPackage.BEAN_DECORATOR__BEAN_LOCATION:
				return isSetBeanLocation();
			case JCMPackage.BEAN_DECORATOR__BEAN_RETURN:
				return isSetBeanReturn();
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
				case JCMPackage.BEAN_DECORATOR__KEYED_VALUES: return CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES;
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
				case CDMPackage.KEYED_VALUE_HOLDER__KEYED_VALUES: return JCMPackage.BEAN_DECORATOR__KEYED_VALUES;
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
		result.append(" (beanProxyClassName: ");
		result.append(beanProxyClassName);
		result.append(", beanLocation: ");
		if (beanLocationESet) result.append(beanLocation); else result.append("<unset>");
		result.append(", beanReturn: ");
		if (beanReturnESet) result.append(beanReturn); else result.append("<unset>");
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
	
	private Class beanProxyAdapterClass;
	private Constructor beanProxyAdapterClassConstructor;
	private String initializationData;
	private boolean hasRetrievedBeanProxyAdapterClass;
	
	/**
	 * Return the adapter for the argument which will be an IJavaInstance.  The class and its contrusctor are cached for performance
	 */
	public IBeanProxyHost createBeanProxy(Notifier adaptable, IBeanProxyDomain aBeanProxyDomain) {
		
		if (!hasRetrievedBeanProxyAdapterClass) {
			if (getBeanProxyClassName() != null) {
				try {
					// If the class is not the default we need to load it using the correct
					// class lodaed.  CDEPlugin can do this for us but if the name is BeanProxyAdapter or PrimitiveBeanProxyAdapter we can
					// just get the default class faster with a .class reference here
					if (getBeanProxyClassName().equals("org.eclipse.ve.java.core/org.eclipse.ve.internal.java.core.BeanProxyAdapter")) //$NON-NLS-1$
						beanProxyAdapterClass = BeanProxyAdapter.class;
					else if (getBeanProxyClassName().equals("org.eclipse.ve.java.core/org.eclipse.ve.internal.java.core.PrimitiveProxyAdapter")) //$NON-NLS-1$
						beanProxyAdapterClass = PrimitiveProxyAdapter.class;
					else {
						initializationData = getBeanProxyClassName();
						beanProxyAdapterClass = CDEPlugin.getClassFromString(initializationData);
						if (!CDEPlugin.hasInitializationData(initializationData))
							initializationData = null;
						if (beanProxyAdapterClass != null) {
							try {
								// There must be a constructor that takes an argument with the IBeanProxyDomain.
								beanProxyAdapterClassConstructor = beanProxyAdapterClass.getConstructor(new Class[] { IBeanProxyDomain.class });
							} catch (Exception e) {
								JavaVEPlugin.log(e, Level.WARNING);
								beanProxyAdapterClass = BeanProxyAdapter.class;
							}
						}
					}
				} catch (ClassNotFoundException e) {
					beanProxyAdapterClass = BeanProxyAdapter.class;
					JavaVEPlugin.log(e, Level.WARNING);
				}
			} else
				beanProxyAdapterClass = BeanProxyAdapter.class;
			hasRetrievedBeanProxyAdapterClass = true;
		}
		
		// Use the cache'd constructor is one is available
		if(beanProxyAdapterClassConstructor != null){
			try {			
				IBeanProxyHost adapter = (IBeanProxyHost) beanProxyAdapterClassConstructor.newInstance(new Object[] { aBeanProxyDomain });
				if (initializationData != null)
					CDEPlugin.setInitializationData(adapter, initializationData, null);
				return adapter;
			} catch (Exception e) {
				JavaVEPlugin.log(e, Level.WARNING);
				return new BeanProxyAdapter(aBeanProxyDomain);
			}				
		} else if (beanProxyAdapterClass == BeanProxyAdapter.class) 
			return new BeanProxyAdapter(aBeanProxyDomain);
		else
			return new PrimitiveProxyAdapter(aBeanProxyDomain);
	}	

} //BeanDecoratorImpl

