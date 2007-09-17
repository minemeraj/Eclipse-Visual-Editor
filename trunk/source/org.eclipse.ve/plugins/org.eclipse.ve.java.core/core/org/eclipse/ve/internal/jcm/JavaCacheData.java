/**
 * <copyright>
 * </copyright>
 *
 * $Id: JavaCacheData.java,v 1.5 2007-09-17 14:21:53 srobenalt Exp $
 */
package org.eclipse.ve.internal.jcm;
/*******************************************************************************
 * Copyright (c)  2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

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
public interface JavaCacheData extends EObject{
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
	 * @model mapType="org.eclipse.ve.internal.jcm.NamesToBeans<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EObject>"
	 * @generated
	 */
	EMap<String, EObject> getNamesToBeans();

} // JavaCacheData
