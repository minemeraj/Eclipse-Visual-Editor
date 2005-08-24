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
 *  $RCSfile: UtilityFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */


import org.eclipse.emf.ecore.EFactory;
/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage
 * @generated
 */
public interface UtilityFactory extends EFactory {


	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UtilityFactory eINSTANCE = new org.eclipse.ve.internal.cde.utility.impl.UtilityFactoryImpl();

	/**
	 * Some helper methods for creating the string objects.
	 */
	public ConstantString createConstantString(String aString);
	public TranslatableString createTranslatableString(ResourceBundle bundle, String key);
	public ResourceBundle createURLResourceBundle(String[] urls, String bundleName);
	public ResourceBundle createURLResourceBundle(String url, String bundleName);
	public GIFFileGraphic createGIFFileGraphic(String resourceName);	
		
	/**
	 * Returns a new object of class '<em>GIF File Graphic</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>GIF File Graphic</em>'.
	 * @generated
	 */
	GIFFileGraphic createGIFFileGraphic();

	/**
	 * Returns a new object of class '<em>Constant String</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Constant String</em>'.
	 * @generated
	 */
	ConstantString createConstantString();

	/**
	 * Returns a new object of class '<em>Translatable String</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Translatable String</em>'.
	 * @generated
	 */
	TranslatableString createTranslatableString();

	/**
	 * Returns a new object of class '<em>URL Resource Bundle</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>URL Resource Bundle</em>'.
	 * @generated
	 */
	URLResourceBundle createURLResourceBundle();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	UtilityPackage getUtilityPackage();

}
