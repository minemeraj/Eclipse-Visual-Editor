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
/*
 *  $RCSfile: AbstractToolEntry.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:51 $ 
 */
package org.eclipse.ve.internal.cde.palette;


import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Base ToolEntry.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.AbstractToolEntry#getStringProperties <em>String Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getAbstractToolEntry()
 * @model abstract="true"
 * @generated
 */
public interface AbstractToolEntry extends Entry{
	/**
	 * Returns the value of the '<em><b>String Properties</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Key/Value properties.
	 * <p>
	 * This is a map of property name to string property values. These will be applied to the GEF ToolEntry using the key/value setProperty method.
	 * <p>
	 * If string keys and string values is not sufficient, you will need to create your own AbstractToolEntry subclass instead to supply those.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>String Properties</em>' map.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getAbstractToolEntry_StringProperties()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String"
	 * @generated
	 */
	EMap getStringProperties();

}
