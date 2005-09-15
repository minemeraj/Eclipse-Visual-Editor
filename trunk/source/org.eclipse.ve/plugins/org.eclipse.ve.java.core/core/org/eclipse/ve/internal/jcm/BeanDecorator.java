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
package org.eclipse.ve.internal.jcm;
/*
 *  $RCSfile: BeanDecorator.java,v $
 *  $Revision: 1.8 $  $Date: 2005-09-15 21:33:50 $ 
 */

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.ve.internal.cdm.KeyedValueHolder;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bean Decorator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Decorates the bean for bean specific inforamation, for example the BeanProxyAdapter class.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanProxyClassName <em>Bean Proxy Class Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanLocation <em>Bean Location</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.jcm.BeanDecorator#isBeanReturn <em>Bean Return</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanDecorator()
 * @model
 * @generated
 */
public interface BeanDecorator extends EAnnotation, KeyedValueHolder{
	/**
	 * Returns the value of the '<em><b>Bean Proxy Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean Proxy Class Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of the class for the BeanProxyAdapter.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Proxy Class Name</em>' attribute.
	 * @see #setBeanProxyClassName(String)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanDecorator_BeanProxyClassName()
	 * @model
	 * @generated
	 */
	String getBeanProxyClassName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanProxyClassName <em>Bean Proxy Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Proxy Class Name</em>' attribute.
	 * @see #getBeanProxyClassName()
	 * @generated
	 */
	void setBeanProxyClassName(String value);

	/**
	 * Returns the value of the '<em><b>Bean Location</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.ve.internal.jcm.InstanceLocation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The location of any beans of this type, or subclasses that are not overridden, if not specifically overridden by the BeanFeatureDecorator or the Annotation key of INSTANCELOC.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Location</em>' attribute.
	 * @see org.eclipse.ve.internal.jcm.InstanceLocation
	 * @see #isSetBeanLocation()
	 * @see #unsetBeanLocation()
	 * @see #setBeanLocation(InstanceLocation)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanDecorator_BeanLocation()
	 * @model unsettable="true"
	 * @generated
	 */
	InstanceLocation getBeanLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanLocation <em>Bean Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Location</em>' attribute.
	 * @see org.eclipse.ve.internal.jcm.InstanceLocation
	 * @see #isSetBeanLocation()
	 * @see #unsetBeanLocation()
	 * @see #getBeanLocation()
	 * @generated
	 */
	void setBeanLocation(InstanceLocation value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanLocation <em>Bean Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBeanLocation()
	 * @see #getBeanLocation()
	 * @see #setBeanLocation(InstanceLocation)
	 * @generated
	 */
	void unsetBeanLocation();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.jcm.BeanDecorator#getBeanLocation <em>Bean Location</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Bean Location</em>' attribute is set.
	 * @see #unsetBeanLocation()
	 * @see #getBeanLocation()
	 * @see #setBeanLocation(InstanceLocation)
	 * @generated
	 */
	boolean isSetBeanLocation();

	/**
	 * Returns the value of the '<em><b>Bean Return</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Will this bean have a return method or not. A return method is one that when called will answer the bean. This will only be used for locations of GLOBAL type.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Return</em>' attribute.
	 * @see #isSetBeanReturn()
	 * @see #unsetBeanReturn()
	 * @see #setBeanReturn(boolean)
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getBeanDecorator_BeanReturn()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isBeanReturn();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.jcm.BeanDecorator#isBeanReturn <em>Bean Return</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Return</em>' attribute.
	 * @see #isSetBeanReturn()
	 * @see #unsetBeanReturn()
	 * @see #isBeanReturn()
	 * @generated
	 */
	void setBeanReturn(boolean value);

	/**
	 * Unsets the value of the '{@link org.eclipse.ve.internal.jcm.BeanDecorator#isBeanReturn <em>Bean Return</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBeanReturn()
	 * @see #isBeanReturn()
	 * @see #setBeanReturn(boolean)
	 * @generated
	 */
	void unsetBeanReturn();

	/**
	 * Returns whether the value of the '{@link org.eclipse.ve.internal.jcm.BeanDecorator#isBeanReturn <em>Bean Return</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Bean Return</em>' attribute is set.
	 * @see #unsetBeanReturn()
	 * @see #isBeanReturn()
	 * @see #setBeanReturn(boolean)
	 * @generated
	 */
	boolean isSetBeanReturn();

	/**
	 * Return the BeanProxyAdapter for the Notifier.  
	 * @param adaptable is an IJavaInstance being wrapped by the result
	 * @param domaim is the IBeanProxyDomain 
	 * @return an IBeanProxyHost
	 */
	IBeanProxyHost createBeanProxy(Notifier adaptable, IBeanProxyDomain domain);

} // BeanDecorator
