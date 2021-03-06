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
 *  $RCSfile: InitialState.java,v $
 *  $Revision: 1.4 $  $Date: 2007-05-25 04:09:36 $ 
 */
package org.eclipse.ve.internal.cde.palette;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Initial State</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Drawer initial state.
 * <!-- end-model-doc -->
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getInitialState()
 * @model
 * @generated
 */
public enum InitialState implements Enumerator
{
	/**
	 * The '<em><b>Open</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #OPEN
	 * @generated
	 * @ordered
	 */
	OPEN_LITERAL(0, "Open", "Open"),
	/**
	 * The '<em><b>Closed</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CLOSED
	 * @generated
	 * @ordered
	 */
	CLOSED_LITERAL(1, "Closed", "Closed"),
	/**
	 * The '<em><b>Pinned Open</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PINNED_OPEN
	 * @generated
	 * @ordered
	 */
	PINNED_OPEN_LITERAL(2, "PinnedOpen", "PinnedOpen");
	/**
	 * The '<em><b>Open</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Initially open.
	 * <!-- end-model-doc -->
	 * @see #OPEN_LITERAL
	 * @model name="Open"
	 * @generated
	 * @ordered
	 */
	public static final int OPEN = 0;

	/**
	 * The '<em><b>Closed</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Initially closed.
	 * <!-- end-model-doc -->
	 * @see #CLOSED_LITERAL
	 * @model name="Closed"
	 * @generated
	 * @ordered
	 */
	public static final int CLOSED = 1;

	/**
	 * The '<em><b>Pinned Open</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Initially pinned open.
	 * <!-- end-model-doc -->
	 * @see #PINNED_OPEN_LITERAL
	 * @model name="PinnedOpen"
	 * @generated
	 * @ordered
	 */
	public static final int PINNED_OPEN = 2;

	/**
	 * An array of all the '<em><b>Initial State</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final InitialState[] VALUES_ARRAY =
		new InitialState[] {
			OPEN_LITERAL,
			CLOSED_LITERAL,
			PINNED_OPEN_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Initial State</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<InitialState> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Initial State</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static InitialState get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InitialState result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Initial State</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static InitialState getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InitialState result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Initial State</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static InitialState get(int value) {
		switch (value) {
			case OPEN: return OPEN_LITERAL;
			case CLOSED: return CLOSED_LITERAL;
			case PINNED_OPEN: return PINNED_OPEN_LITERAL;
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
	private InitialState(int value, String name, String literal) {
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
