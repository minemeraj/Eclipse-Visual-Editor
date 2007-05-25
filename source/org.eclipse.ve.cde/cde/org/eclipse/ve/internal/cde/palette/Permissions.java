/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: Permissions.java,v $
 *  $Revision: 1.4 $  $Date: 2007-05-25 04:09:36 $ 
 */
package org.eclipse.ve.internal.cde.palette;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Permissions</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * The valid modification permissions for this entry. Used in the palette customizer.
 * <!-- end-model-doc -->
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPermissions()
 * @model
 * @generated
 */
public enum Permissions implements Enumerator
{
	/**
	 * The '<em><b>Default</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DEFAULT
	 * @generated
	 * @ordered
	 */
	DEFAULT_LITERAL(0, "Default", "Default"),
	/**
	 * The '<em><b>Full</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FULL
	 * @generated
	 * @ordered
	 */
	FULL_LITERAL(1, "Full", "Full"),
	/**
	 * The '<em><b>Hide Only</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HIDE_ONLY
	 * @generated
	 * @ordered
	 */
	HIDE_ONLY_LITERAL(2, "HideOnly", "HideOnly"),
	/**
	 * The '<em><b>Limited</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LIMITED
	 * @generated
	 * @ordered
	 */
	LIMITED_LITERAL(3, "Limited", "Limited"),
	/**
	 * The '<em><b>None</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
	NONE_LITERAL(4, "None", "None");
	/**
	 * The '<em><b>Default</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is the default permission for the entry. Each entry type has a default permission. It may different depending on the entry type.
	 * <!-- end-model-doc -->
	 * @see #DEFAULT_LITERAL
	 * @model name="Default"
	 * @generated
	 * @ordered
	 */
	public static final int DEFAULT = 0;

	/**
	 * The '<em><b>Full</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Full modification by the customizer is allowed.
	 * <!-- end-model-doc -->
	 * @see #FULL_LITERAL
	 * @model name="Full"
	 * @generated
	 * @ordered
	 */
	public static final int FULL = 1;

	/**
	 * The '<em><b>Hide Only</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The customizer can only hide/show the entry.
	 * <!-- end-model-doc -->
	 * @see #HIDE_ONLY_LITERAL
	 * @model name="HideOnly"
	 * @generated
	 * @ordered
	 */
	public static final int HIDE_ONLY = 2;

	/**
	 * The '<em><b>Limited</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Limited modification is allowed. It is not allowed to move to another container or to be deleted.
	 * <!-- end-model-doc -->
	 * @see #LIMITED_LITERAL
	 * @model name="Limited"
	 * @generated
	 * @ordered
	 */
	public static final int LIMITED = 3;

	/**
	 * The '<em><b>None</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * No modification is allowed.
	 * <!-- end-model-doc -->
	 * @see #NONE_LITERAL
	 * @model name="None"
	 * @generated
	 * @ordered
	 */
	public static final int NONE = 4;

	/**
	 * An array of all the '<em><b>Permissions</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final Permissions[] VALUES_ARRAY =
		new Permissions[] {
			DEFAULT_LITERAL,
			FULL_LITERAL,
			HIDE_ONLY_LITERAL,
			LIMITED_LITERAL,
			NONE_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Permissions</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<Permissions> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Permissions</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Permissions get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Permissions result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Permissions</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Permissions getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Permissions result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Permissions</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Permissions get(int value) {
		switch (value) {
			case DEFAULT: return DEFAULT_LITERAL;
			case FULL: return FULL_LITERAL;
			case HIDE_ONLY: return HIDE_ONLY_LITERAL;
			case LIMITED: return LIMITED_LITERAL;
			case NONE: return NONE_LITERAL;
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
	private Permissions(int value, String name, String literal) {
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
