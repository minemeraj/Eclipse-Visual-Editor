/**
 * <copyright>
 * </copyright>
 *
 * $Id: JavaCacheData.java,v 1.1 2005-01-05 18:41:43 gmendel Exp $
 */
package org.eclipse.ve.internal.jcm;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Java Cache Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is used to cache info for quick caching of the model. It is not part of the model itself.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.jcm.JavaCacheData#getNamesToBeans <em>Names To Beans</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getJavaCacheData()
 * @model 
 * @generated
 */
public interface JavaCacheData extends EObject {
	/**
	 * Returns the value of the '<em><b>Names To Beans</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.emf.ecore.EObject},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Names To Beans</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Names To Beans</em>' map.
	 * @see org.eclipse.ve.internal.jcm.JCMPackage#getJavaCacheData_NamesToBeans()
	 * @model mapType="org.eclipse.ve.internal.jcm.NamesToBeans" keyType="java.lang.String" valueType="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EMap getNamesToBeans();

} // JavaCacheData
