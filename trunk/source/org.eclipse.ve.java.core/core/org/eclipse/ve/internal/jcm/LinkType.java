/**
 * <copyright>
 * </copyright>
 *
 * $Id: LinkType.java,v 1.4 2006-02-07 17:21:37 rkulp Exp $
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
 * A representation of the literals of the enumeration '<em><b>Link Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * This indicates the linkage type of a feature (through the BeanFeatureDecorator).
 * <!-- end-model-doc -->
 * @see org.eclipse.ve.internal.jcm.JCMPackage#getLinkType()
 * @model
 * @generated
 */
public final class LinkType extends AbstractEnumerator {
	/**
	 * The '<em><b>NORMAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>This is a normal linkage (which is the default). When a source object is being deleted and a target object is pointed to through a feature listed as NORMAL, then this target object will be deleted if there are no other references to it.
	 * <!-- end-model-doc -->
	 * @see #NORMAL_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NORMAL = 0;

	/**
	 * The '<em><b>CHILD</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>This is a child linkage. When a source object is being deleted and there is a target object pointed to through a CHILD linkage, then the target object will be deleted except if there are any other CHILD references to the target.
	 * <p>
	 * A child reference basically means that the target is a child of the source and if the source goes away you want the target to go away.
	 * <!-- end-model-doc -->
	 * @see #CHILD_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CHILD = 1;

	/**
	 * The '<em><b>DEPENDENCY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>This is a dependency linkage. This is slightly reversed from NORMAL and CHILD in that it comes into effect only from the target side instead of the source side.
	 * <p>
	 * So, when a target is being deleted, it will look at all references to it and for any that are marked as dependency the source object will be investigated. If the source object has no child references to it, then the source object will be deleted too.
	 * <p>
	 * That is what dependency means. It means the source depends on the target and if the target goes away the source should go away, except if it is being held on to by a strong link such as a CHILD link.
	 * <!-- end-model-doc -->
	 * @see #DEPENDENCY_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DEPENDENCY = 2;

	/**
	 * The '<em><b>NORMAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NORMAL
	 * @generated
	 * @ordered
	 */
	public static final LinkType NORMAL_LITERAL = new LinkType(NORMAL, "NORMAL", "NORMAL");

	/**
	 * The '<em><b>CHILD</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHILD
	 * @generated
	 * @ordered
	 */
	public static final LinkType CHILD_LITERAL = new LinkType(CHILD, "CHILD", "CHILD");

	/**
	 * The '<em><b>DEPENDENCY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DEPENDENCY
	 * @generated
	 * @ordered
	 */
	public static final LinkType DEPENDENCY_LITERAL = new LinkType(DEPENDENCY, "DEPENDENCY", "DEPENDENCY");

	/**
	 * An array of all the '<em><b>Link Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final LinkType[] VALUES_ARRAY =
		new LinkType[] {
			NORMAL_LITERAL,
			CHILD_LITERAL,
			DEPENDENCY_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Link Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Link Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LinkType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LinkType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Link Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LinkType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LinkType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Link Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LinkType get(int value) {
		switch (value) {
			case NORMAL: return NORMAL_LITERAL;
			case CHILD: return CHILD_LITERAL;
			case DEPENDENCY: return DEPENDENCY_LITERAL;
		}
		return null;	
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private LinkType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //LinkType
