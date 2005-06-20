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
 *  $RCSfile: Drawer.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-20 23:54:40 $ 
 */
package org.eclipse.ve.internal.cde.palette;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Drawer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Palette Drawer.
 * <p>
 * This is a collapsable collection of entries. It accepts anything except a Group or a Drawer.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Drawer#getInitialState <em>Initial State</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getDrawer()
 * @model
 * @generated
 */
public interface Drawer extends Container {
	/**
	 * Returns the value of the '<em><b>Initial State</b></em>' attribute.
	 * The default value is <code>"Closed"</code>.
	 * The literals are from the enumeration {@link org.eclipse.ve.internal.cde.palette.InitialState}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial State</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial State</em>' attribute.
	 * @see org.eclipse.ve.internal.cde.palette.InitialState
	 * @see #setInitialState(InitialState)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getDrawer_InitialState()
	 * @model default="Closed"
	 * @generated
	 */
	InitialState getInitialState();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Drawer#getInitialState <em>Initial State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial State</em>' attribute.
	 * @see org.eclipse.ve.internal.cde.palette.InitialState
	 * @see #getInitialState()
	 * @generated
	 */
	void setInitialState(InitialState value);

} // Drawer
