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
package org.eclipse.ve.internal.cde.utility;
/*
 *  $RCSfile: URLResourceBundle.java,v $
 *  $Revision: 1.7 $  $Date: 2007-05-25 04:09:36 $ 
 */


import org.eclipse.emf.common.util.EList;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>URL Resource Bundle</b></em>'.
 * A bundle url is of the form:
 * 
 * ...directory...
 * 
 * For example:
 * 
 * platform://plugin//pluginname//nls//
 * 
 * This will look in the plugin directory "nls//" in the plugin "pluginname".
 * 
 * NOTE: If this is a directory then the URL MUST end with '//' or it won't work, it will think "nls" is a file and not a directory.
 * 
 * There can be more than one if they are to be searched in order.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A bundle url is of the form:
 * 
 * ...directory...
 * 
 * For example:
 * 
 * platform:/plugin/pluginname/nls/
 * 
 * This will look in the plugin directory "nls/" in the plugin "pluginname".
 * 
 * NOTE: If this is a directory then the URL MUST end with '/' or it won't work, it will think "nls" is a file and not a directory.
 * 
 * There can be more than one if they are to be searched in order.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.URLResourceBundle#getBundleName <em>Bundle Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.URLResourceBundle#getBundleURLs <em>Bundle UR Ls</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getURLResourceBundle()
 * @model
 * @generated
 */
public interface URLResourceBundle extends ResourceBundle{


	/**
	 * Returns the value of the '<em><b>Bundle Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bundle Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bundle Name</em>' attribute.
	 * @see #setBundleName(String)
	 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getURLResourceBundle_BundleName()
	 * @model
	 * @generated
	 */
	String getBundleName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.utility.URLResourceBundle#getBundleName <em>Bundle Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bundle Name</em>' attribute.
	 * @see #getBundleName()
	 * @generated
	 */
	void setBundleName(String value);

	/**
	 * Returns the value of the '<em><b>Bundle UR Ls</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bundle UR Ls</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The URL's to use to search for the bundle. If there is more, then they will be searched in order for the bundle (or bundles if this is locale specific override type bundle).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bundle UR Ls</em>' attribute list.
	 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getURLResourceBundle_BundleURLs()
	 * @model type="java.lang.String"
	 * @generated
	 */
	EList<String> getBundleURLs();

}
