/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
 *  $Revision: 1.18 $  $Date: 2006-05-17 20:14:52 $ 
 */

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.cdm.impl.MapEntryImpl;
import org.eclipse.ve.internal.jcm.BeanDecorator;
import org.eclipse.ve.internal.jcm.InstanceLocation;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.cdm.model.KeyedValueHolderHelper;

import org.eclipse.ve.internal.cde.core.CDEPlugin;


import org.eclipse.ve.internal.java.core.*;

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
	 * The flag representing whether the Bean Location attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int BEAN_LOCATION_ESETFLAG = 1 << 8;

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
	 * The flag representing the value of the '{@link #isBeanReturn() <em>Bean Return</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBeanReturn()
	 * @generated
	 * @ordered
	 */
	protected static final int BEAN_RETURN_EFLAG = 1 << 9;

	/**
	 * The flag representing whether the Bean Return attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int BEAN_RETURN_ESETFLAG = 1 << 10;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BeanDecoratorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JCMPackage.Literals.BEAN_DECORATOR;
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
		boolean oldBeanLocationESet = (eFlags & BEAN_LOCATION_ESETFLAG) != 0;
		eFlags |= BEAN_LOCATION_ESETFLAG;
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
		boolean oldBeanLocationESet = (eFlags & BEAN_LOCATION_ESETFLAG) != 0;
		beanLocation = BEAN_LOCATION_EDEFAULT;
		eFlags &= ~BEAN_LOCATION_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JCMPackage.BEAN_DECORATOR__BEAN_LOCATION, oldBeanLocation, BEAN_LOCATION_EDEFAULT, oldBeanLocationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBeanLocation() {
		return (eFlags & BEAN_LOCATION_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isBeanReturn() {
		return (eFlags & BEAN_RETURN_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanReturn(boolean newBeanReturn) {
		boolean oldBeanReturn = (eFlags & BEAN_RETURN_EFLAG) != 0;
		if (newBeanReturn) eFlags |= BEAN_RETURN_EFLAG; else eFlags &= ~BEAN_RETURN_EFLAG;
		boolean oldBeanReturnESet = (eFlags & BEAN_RETURN_ESETFLAG) != 0;
		eFlags |= BEAN_RETURN_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JCMPackage.BEAN_DECORATOR__BEAN_RETURN, oldBeanReturn, newBeanReturn, !oldBeanReturnESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetBeanReturn() {
		boolean oldBeanReturn = (eFlags & BEAN_RETURN_EFLAG) != 0;
		boolean oldBeanReturnESet = (eFlags & BEAN_RETURN_ESETFLAG) != 0;
		if (BEAN_RETURN_EDEFAULT) eFlags |= BEAN_RETURN_EFLAG; else eFlags &= ~BEAN_RETURN_EFLAG;
		eFlags &= ~BEAN_RETURN_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JCMPackage.BEAN_DECORATOR__BEAN_RETURN, oldBeanReturn, BEAN_RETURN_EDEFAULT, oldBeanReturnESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBeanReturn() {
		return (eFlags & BEAN_RETURN_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
				return ((InternalEList)getKeyedValues()).basicRemove(otherEnd, msgs);
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
			case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
				if (coreType) return getKeyedValues();
				else return getKeyedValues().map();
			case JCMPackage.BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME:
				return getBeanProxyClassName();
			case JCMPackage.BEAN_DECORATOR__BEAN_LOCATION:
				return getBeanLocation();
			case JCMPackage.BEAN_DECORATOR__BEAN_RETURN:
				return isBeanReturn() ? Boolean.TRUE : Boolean.FALSE;
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
			case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
				((EStructuralFeature.Setting)getKeyedValues()).set(newValue);
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
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
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
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean eIsSetGen(int featureID) {
		switch (featureID) {
			case JCMPackage.BEAN_DECORATOR__KEYED_VALUES:
				return keyedValues != null && !keyedValues.isEmpty();
			case JCMPackage.BEAN_DECORATOR__BEAN_PROXY_CLASS_NAME:
				return BEAN_PROXY_CLASS_NAME_EDEFAULT == null ? beanProxyClassName != null : !BEAN_PROXY_CLASS_NAME_EDEFAULT.equals(beanProxyClassName);
			case JCMPackage.BEAN_DECORATOR__BEAN_LOCATION:
				return isSetBeanLocation();
			case JCMPackage.BEAN_DECORATOR__BEAN_RETURN:
				return isSetBeanReturn();
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
			case JCMPackage.BEAN_DECORATOR__SOURCE:
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
		if ((eFlags & BEAN_LOCATION_ESETFLAG) != 0) result.append(beanLocation); else result.append("<unset>");
		result.append(", beanReturn: ");
		if ((eFlags & BEAN_RETURN_ESETFLAG) != 0) result.append((eFlags & BEAN_RETURN_EFLAG) != 0); else result.append("<unset>");
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

