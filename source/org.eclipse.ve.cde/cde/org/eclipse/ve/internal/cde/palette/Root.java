/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: Root.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-20 23:54:40 $ 
 */
package org.eclipse.ve.internal.cde.palette;



/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is the palette root.
 * <p>
 * Its children can be anything except a Tool or a Stack.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Root#getDefEntry <em>Def Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getRoot()
 * @model
 * @generated
 */
public interface Root extends Container{
	/**
	 * Returns the value of the '<em><b>Def Entry</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Default Tool Entry
	 * <p>
	 * The entry selected when no entry is explicitly selected.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Def Entry</em>' reference.
	 * @see #setDefEntry(AbstractToolEntry)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getRoot_DefEntry()
	 * @model
	 * @generated
	 */
	AbstractToolEntry getDefEntry();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Root#getDefEntry <em>Def Entry</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Def Entry</em>' reference.
	 * @see #getDefEntry()
	 * @generated
	 */
	void setDefEntry(AbstractToolEntry value);

} // Root
