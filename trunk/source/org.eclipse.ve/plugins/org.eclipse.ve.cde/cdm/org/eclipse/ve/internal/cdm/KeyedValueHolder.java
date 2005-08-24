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
package org.eclipse.ve.internal.cdm;
/*
 *  $RCSfile: KeyedValueHolder.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:50 $ 
 */

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Keyed Value Holder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class holds KeyedValues. It is used by mixing it in with multiple inheritance into where it is needed.
 * 
 * The operations allow treating the KeyedValues as properties. They should be used instead of the generic keyedValues relationship. It allows easier usage of the keyed values.
 * 
 * Note: KeyedValues should be treated as dataTypes, in other words, individual KeyedValues shouldn't be updated, they should be replaced instead. This is because typically users area listening for new keyed values, not subvalues within individual keyed values being changed. If the users understands this and still listens on individual KeyedValues, that is OK as long thier model handles unlistening when the keyedvalue is removed.
 * <!-- end-model-doc -->
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
