package org.eclipse.ve.internal.cdm;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: KeyedValueHolder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Keyed Value Holder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cdm.KeyedValueHolder#getKeyedValues <em>Keyed Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cdm.CDMPackage#getKeyedValueHolder()
 * @model abstract="true"
 * @generated
 */
public interface KeyedValueHolder extends EObject{
	/**
	 * Returns the value of the '<em><b>Keyed Values</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Keyed Values</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Keyed Values</em>' map.
	 * @see org.eclipse.ve.internal.cdm.CDMPackage#getKeyedValueHolder_KeyedValues()
	 * @model mapType="org.eclipse.ve.internal.cdm.MapEntry" keyType="java.lang.String" valueType="java.lang.String"
	 * @generated
	 */
	EMap getKeyedValues();

} // KeyedValueHolder
