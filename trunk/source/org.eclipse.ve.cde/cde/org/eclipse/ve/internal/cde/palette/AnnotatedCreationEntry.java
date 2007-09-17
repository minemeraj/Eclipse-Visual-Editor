/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.palette;
/*
 *  $RCSfile: AnnotatedCreationEntry.java,v $
 *  $Revision: 1.8 $  $Date: 2007-09-17 14:17:14 $ 
 */

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotated Creation Entry</b></em>'.
 * @deprecated Use {@link org.eclipse.gef.palette.CreationToolEntry#getKeyedValues()}
 * <p>
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This will wrapper a CreationToolEntry and provide for creating an annotation too. This is the entry that should be in the palette when an annotation entry is desired.
 * <p>
 * This is obsolete. The CreationToolEntry now can handle this itself.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry#getObjectCreationEntry <em>Object Creation Entry</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry#getValues <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getAnnotatedCreationEntry()
 * @model
 * @generated
 */
public interface AnnotatedCreationEntry extends AbstractToolEntry{


	/**
	 * Returns the value of the '<em><b>Values</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Values</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' containment reference list.
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getAnnotatedCreationEntry_Values()
	 * @model containment="true"
	 * @generated
	 */
	EList<EObject> getValues();

	/**
	 * Returns the value of the '<em><b>Object Creation Entry</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object Creation Entry</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object Creation Entry</em>' containment reference.
	 * @see #setObjectCreationEntry(CreationToolEntry)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getAnnotatedCreationEntry_ObjectCreationEntry()
	 * @model containment="true" required="true"
	 * @generated
	 */
	CreationToolEntry getObjectCreationEntry();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.AnnotatedCreationEntry#getObjectCreationEntry <em>Object Creation Entry</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object Creation Entry</em>' containment reference.
	 * @see #getObjectCreationEntry()
	 * @generated
	 */
	void setObjectCreationEntry(CreationToolEntry value);

}

