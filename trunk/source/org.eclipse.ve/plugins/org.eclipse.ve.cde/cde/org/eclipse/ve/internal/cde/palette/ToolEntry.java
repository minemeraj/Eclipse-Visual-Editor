/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $RCSfile: ToolEntry.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:51 $ 
 */


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.ToolEntry#getToolClassName <em>Tool Class Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getToolEntry()
 * @model 
 * @generated
 */

public interface ToolEntry extends AbstractToolEntry{


	/**
	 * Returns the value of the '<em><b>Tool Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tool Class Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Tool class name.
	 * <p>
	 * The classname of the tool entry. Because it is not known the namespace for the class, you need to use a special format:
	 * <p>
	 * <ul>
	 * <li><b>packagename.classname</b>: This means it must be available from the default class loader. (In Eclipse, this will be the org.eclipse.ve.cde plugin. It must be visible to this to be found).
	 * <li><b>namespace/packagename.classname</b>: This means it will be found  in the namespace. (In Eclipse the namespace is the name of a bundle. It will look within that bundle to find the class).
	 * </ul>
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Tool Class Name</em>' attribute.
	 * @see #setToolClassName(String)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getToolEntry_ToolClassName()
	 * @model
	 * @generated
	 */
	String getToolClassName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.ToolEntry#getToolClassName <em>Tool Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tool Class Name</em>' attribute.
	 * @see #getToolClassName()
	 * @generated
	 */
	void setToolClassName(String value);

}
