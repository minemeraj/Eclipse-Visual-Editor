/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: Palette.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:18:01 $ 
 */


import java.util.List;

import org.eclipse.ve.internal.cde.utility.AbstractString;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Palette</b></em>'.
 * This is the abstract base palette. There are two standard implementations of it:
 * 
 * PaletteCmp: This is a palette where the categories and control group are composite aggregations. (i.e. they are contained within the  PaletteCmp).
 * 
 * PaletteRef: This is a palette where the categories and control group are shared aggregations. (i.e. they are actually contained somewhere else and only referenced here). This type of palette allows references to pieces of other palettes to form together a new palette.
 * 
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is the abstract base palette. There are two standard implementations of it:
 * 
 * PaletteCmp: This is a palette where the categories and control group are composite aggregations. (i.e. they are contained within the  PaletteCmp).
 * 
 * PaletteRef: This is a palette where the categories and control group are shared aggregations. (i.e. they are actually contained somewhere else and only referenced here). This type of palette allows references to pieces of other palettes to form together a new palette.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Palette#getPaletteLabel <em>Palette Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPalette()
 * @model abstract="true"
 * @generated
 */
public interface Palette extends Container{


	/**
	 * Returns the value of the '<em><b>Palette Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Palette Label</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Palette Label</em>' containment reference.
	 * @see #setPaletteLabel(AbstractString)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getPalette_PaletteLabel()
	 * @model containment="true"
	 * @generated
	 */
	AbstractString getPaletteLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Palette#getPaletteLabel <em>Palette Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Palette Label</em>' containment reference.
	 * @see #getPaletteLabel()
	 * @generated
	 */
	void setPaletteLabel(AbstractString value);

	/**
	 * Return the Control Group.
	 */
	public Group getControlGroup();
	
	/**
	 * Return the categories
	 */	
	public List getCategories();		
}
