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
 *  $RCSfile: Stack.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-20 23:54:40 $ 
 */
package org.eclipse.ve.internal.cde.palette;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Stack</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Stack Entry.
 * <p>
 * A grouping of ToolEntries. They are represented by one entry on the palette and there can be a dropdown list to select from and a current active entry.
 * <p>
 * It can only accept ToolEntries.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Stack#getActiveEntry <em>Active Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getStack()
 * @model
 * @generated
 */
public interface Stack extends Container {
	/**
	 * Returns the value of the '<em><b>Active Entry</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The current active entry, if any. This is the entry that would be selected if the stack was selected.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Active Entry</em>' reference.
	 * @see #setActiveEntry(Entry)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getStack_ActiveEntry()
	 * @model
	 * @generated
	 */
	Entry getActiveEntry();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Stack#getActiveEntry <em>Active Entry</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Active Entry</em>' reference.
	 * @see #getActiveEntry()
	 * @generated
	 */
	void setActiveEntry(Entry value);

} // Stack
