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
 *  $Revision: 1.3 $  $Date: 2005-06-20 23:54:40 $ 
 */


import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.palette.PaletteEntry;

import org.eclipse.ve.internal.cde.utility.AbstractString;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The root of the palette hierarchy.
 * <p>
 * It is not abstract, but it is treated as basically abstract.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getIcon16Name <em>Icon16 Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getIcon32Name <em>Icon32 Name</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#isVisible <em>Visible</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#isDefaultEntry <em>Default Entry</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getModification <em>Modification</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getEntryLabel <em>Entry Label</em>}</li>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.Entry#getEntryShortDescription <em>Entry Short Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry()
 * @model abstract="true"
 * @generated
 */
public interface Entry extends EObject{

	
	/**
	 * Get the GEF Palette Entry that this EMF Entry represents. This will be used
	 * in the actual palette.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public PaletteEntry getEntry();

	/**
	 * Returns the value of the '<em><b>Icon16 Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Icon16 Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The URL for the 16x16 (small) icon for the entry. If not set, then a default will be used. Some entries do not have an image as a default, others do. For example ToolEntries do not have an image while Drawers do.
	 * <!-- end-model-doc -->
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
	 * <!-- begin-model-doc -->
	 * The URL for the 32x32 (large) icon. If not specified then the small icon will be used.
	 * <!-- end-model-doc -->
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
	 * Returns the value of the '<em><b>Visible</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Is entry visible? Default is true. false is useful for letting palette modifications occur and user can then turn it on.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #setVisible(boolean)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_Visible()
	 * @model default="true"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#isVisible <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Visible</em>' attribute.
	 * @see #isVisible()
	 * @generated
	 */
	void setVisible(boolean value);

	/**
	 * Returns the value of the '<em><b>Default Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @deprecated Use {@link Root#getDefEntry()} instead.
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is obsolete. Only here for compatibility. It is ignored. Use Palette.defaultEntry instead.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Default Entry</em>' attribute.
	 * @see #setDefaultEntry(boolean)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_DefaultEntry()
	 * @model transient="true" volatile="true"
	 * @generated
	 */
	boolean isDefaultEntry();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#isDefaultEntry <em>Default Entry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @deprecated Use {@link Root#setDefEntry(AbstractToolEntry)} instead.
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Entry</em>' attribute.
	 * @see #isDefaultEntry()
	 * @generated
	 */
	void setDefaultEntry(boolean value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Entry ID. (optional)
	 * <p>
	 * Only used for programatically inserting other palette entries. It provides a reference point for palette contributors to find and add into the section that contains this palette entry.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Modification</b></em>' attribute.
	 * The default value is <code>"Default"</code>.
	 * The literals are from the enumeration {@link org.eclipse.ve.internal.cde.palette.Permissions}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modification</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modification</em>' attribute.
	 * @see org.eclipse.ve.internal.cde.palette.Permissions
	 * @see #setModification(Permissions)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getEntry_Modification()
	 * @model default="Default"
	 * @generated
	 */
	Permissions getModification();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.Entry#getModification <em>Modification</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Modification</em>' attribute.
	 * @see org.eclipse.ve.internal.cde.palette.Permissions
	 * @see #getModification()
	 * @generated
	 */
	void setModification(Permissions value);

	/**
	 * Returns the value of the '<em><b>Entry Label</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entry Label</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Label for the entry
	 * <!-- end-model-doc -->
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
	 * <!-- begin-model-doc -->
	 * Short description for the entry (optional)
	 * <!-- end-model-doc -->
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
