/**
 * <copyright>
 * </copyright>
 *
 * $Id: InstanceLocation.java,v 1.3 2005-09-15 21:33:50 rkulp Exp $
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Instance Location</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Enumeration for the location of where the instance variable and initialization will be located.
 * <!-- end-model-doc -->
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getInstanceLocation()
 * @model
 * @generated
 */
public final class InstanceLocation extends AbstractEnumerator {
	/**
	 * The '<em><b>GLOBAL GLOBAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Instance Variable will be GLOBAL (i.e. a member of the class) and the initialization will be GLOBAL (i.e. there is a separate initialization method for the instance).
	 * <!-- end-model-doc -->
	 * @see #GLOBAL_GLOBAL_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int GLOBAL_GLOBAL = 0;

	/**
	 * The '<em><b>GLOBAL LOCAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The instance variable will be GLOBAL (i.e. it will be a member of the class) and the initialization will be LOCAL (i.e. there is not a separate initialization method, the initialization will be in the initialization method of another instance),
	 * <!-- end-model-doc -->
	 * @see #GLOBAL_LOCAL_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int GLOBAL_LOCAL = 1;

	/**
	 * The '<em><b>LOCAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The instance and initialization will be LOCAL (i.e. it will be declared and initialized in the initialization method of another instance).
	 * <!-- end-model-doc -->
	 * @see #LOCAL_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LOCAL = 2;

	/**
	 * The '<em><b>PROPERTY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * There will be no instance variable. It will be a property. This will be applicable if there are no settings on the instance. If there are any settings, then it will promote up to LOCAL automatically.
	 * <!-- end-model-doc -->
	 * @see #PROPERTY_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PROPERTY = 3;

	/**
	 * The '<em><b>GLOBAL GLOBAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GLOBAL_GLOBAL
	 * @generated
	 * @ordered
	 */
	public static final InstanceLocation GLOBAL_GLOBAL_LITERAL = new InstanceLocation(GLOBAL_GLOBAL, "GLOBAL_GLOBAL");

	/**
	 * The '<em><b>GLOBAL LOCAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GLOBAL_LOCAL
	 * @generated
	 * @ordered
	 */
	public static final InstanceLocation GLOBAL_LOCAL_LITERAL = new InstanceLocation(GLOBAL_LOCAL, "GLOBAL_LOCAL");

	/**
	 * The '<em><b>LOCAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LOCAL
	 * @generated
	 * @ordered
	 */
	public static final InstanceLocation LOCAL_LITERAL = new InstanceLocation(LOCAL, "LOCAL");

	/**
	 * The '<em><b>PROPERTY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PROPERTY
	 * @generated
	 * @ordered
	 */
	public static final InstanceLocation PROPERTY_LITERAL = new InstanceLocation(PROPERTY, "PROPERTY");

	/**
	 * An array of all the '<em><b>Instance Location</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final InstanceLocation[] VALUES_ARRAY =
		new InstanceLocation[] {
			GLOBAL_GLOBAL_LITERAL,
			GLOBAL_LOCAL_LITERAL,
			LOCAL_LITERAL,
			PROPERTY_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Instance Location</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Instance Location</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static InstanceLocation get(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InstanceLocation result = VALUES_ARRAY[i];
			if (result.toString().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Instance Location</b></em>' literal with the specified value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static InstanceLocation get(int value) {
		switch (value) {
			case GLOBAL_GLOBAL: return GLOBAL_GLOBAL_LITERAL;
			case GLOBAL_LOCAL: return GLOBAL_LOCAL_LITERAL;
			case LOCAL: return LOCAL_LITERAL;
			case PROPERTY: return PROPERTY_LITERAL;
		}
		return null;	
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private InstanceLocation(int value, String name) {
		super(value, name);
	}

} //InstanceLocation
