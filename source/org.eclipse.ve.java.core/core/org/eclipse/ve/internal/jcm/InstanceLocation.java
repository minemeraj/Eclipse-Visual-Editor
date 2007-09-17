/**
 * <copyright>
 * </copyright>
 *
 * $Id: InstanceLocation.java,v 1.6 2007-09-17 14:21:53 srobenalt Exp $
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

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Instance Location</b></em>',
 * and utility methods for working with them.
 * <p>
 * To use in an annotation for bean, use an {@link org.eclipse.ve.internal.jcm.impl.KeyedInstanceLocationImpl} for the key/value pair.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Enumeration for the location of where the instance variable and initialization will be located.
 * <!-- end-model-doc -->
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getInstanceLocation()
 * @model
 * @generated
 */
public enum InstanceLocation implements Enumerator
{
	/**
	 * The '<em><b>GLOBAL GLOBAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GLOBAL_GLOBAL
	 * @generated
	 * @ordered
	 */
	GLOBAL_GLOBAL_LITERAL(0, "GLOBAL_GLOBAL", "GLOBAL_GLOBAL"),
	/**
	 * The '<em><b>GLOBAL LOCAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GLOBAL_LOCAL
	 * @generated
	 * @ordered
	 */
	GLOBAL_LOCAL_LITERAL(1, "GLOBAL_LOCAL", "GLOBAL_LOCAL"),
	/**
	 * The '<em><b>LOCAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LOCAL
	 * @generated
	 * @ordered
	 */
	LOCAL_LITERAL(2, "LOCAL", "LOCAL"),
	/**
	 * The '<em><b>PROPERTY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PROPERTY
	 * @generated
	 * @ordered
	 */
	PROPERTY_LITERAL(3, "PROPERTY", "PROPERTY");
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
	public static final List<InstanceLocation> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Instance Location</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static InstanceLocation get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InstanceLocation result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Instance Location</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static InstanceLocation getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InstanceLocation result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Instance Location</b></em>' literal with the specified integer value.
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private InstanceLocation(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
}
