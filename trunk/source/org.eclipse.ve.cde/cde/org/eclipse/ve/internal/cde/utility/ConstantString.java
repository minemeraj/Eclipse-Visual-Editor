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
 *  $RCSfile: ConstantString.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.lang.String;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constant String</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.ConstantString#getString <em>String</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getConstantString()
 * @model 
 * @generated
 */
public interface ConstantString extends AbstractString {


	/**
	 * Returns the value of the '<em><b>String</b></em>' attribute.
	 * The default value is <code>" "</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>String</em>' attribute.
	 * @see #setString(String)
	 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getConstantString_String()
	 * @model default=" "
	 * @generated
	 */
	String getString();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.utility.ConstantString#getString <em>String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>String</em>' attribute.
	 * @see #getString()
	 * @generated
	 */
	void setString(String value);

}
