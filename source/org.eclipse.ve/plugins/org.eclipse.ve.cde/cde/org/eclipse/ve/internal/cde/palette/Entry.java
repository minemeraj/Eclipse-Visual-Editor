package org.eclipse.ve.internal.cde.palette;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: Entry.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:18:01 $ 
 */


import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getIcon16Name <em>Icon16 Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getIcon32Name <em>Icon32 Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#isDefaultEntry <em>Default Entry</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getEntryLabel <em>Entry Label</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getEntryShortDescription <em>Entry Short Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry()
 * @model 
 * @generated
 */
public interface Entry extends EObject, ICDEPaletteEntry {


	/**
	 * Returns the value of the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Icon16 Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Icon16 Name</em>' attribute.
	 * @see #setIcon16Name(String)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_Icon16Name()
	 * @model 
	 * @generated
	 */
	String getIcon16Name();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#getIcon16Name <em>Icon16 Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Icon16 Name</em>' attribute.
	 * @see #getIcon16Name()
	 * @generated
	 */
	void setIcon16Name(String value);

	/**
	 * Returns the value of the '<em><b>Icon32 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Icon32 Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Icon32 Name</em>' attribute.
	 * @see #setIcon32Name(String)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_Icon32Name()
	 * @model 
	 * @generated
	 */
	String getIcon32Name();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#getIcon32Name <em>Icon32 Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Icon32 Name</em>' attribute.
	 * @see #getIcon32Name()
	 * @generated
	 */
	void setIcon32Name(String value);

	/**
	 * Returns the value of the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Entry</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Entry</em>' attribute.
	 * @see #setDefaultEntry(boolean)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_DefaultEntry()
	 * @model 
	 * @generated
	 */
	boolean isDefaultEntry();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#isDefaultEntry <em>Default Entry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Entry</em>' attribute.
	 * @see #isDefaultEntry()
	 * @generated
	 */
	void setDefaultEntry(boolean value);

	/**
	 * Returns the value of the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entry Label</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entry Label</em>' containment reference.
	 * @see #setEntryLabel(AbstractString)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_EntryLabel()
	 * @model containment="true"
	 * @generated
	 */
	AbstractString getEntryLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#getEntryLabel <em>Entry Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entry Label</em>' containment reference.
	 * @see #getEntryLabel()
	 * @generated
	 */
	void setEntryLabel(AbstractString value);

	/**
	 * Returns the value of the '<em><b>Entry Short Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entry Short Description</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entry Short Description</em>' containment reference.
	 * @see #setEntryShortDescription(AbstractString)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_EntryShortDescription()
	 * @model containment="true"
	 * @generated
	 */
	AbstractString getEntryShortDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#getEntryShortDescription <em>Entry Short Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entry Short Description</em>' containment reference.
	 * @see #getEntryShortDescription()
	 * @generated
	 */
	void setEntryShortDescription(AbstractString value);

}
