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
 *  $RCSfile: SelectionCreationToolEntry.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-20 23:54:40 $ 
 */


import org.eclipse.gef.EditDomain;
import org.eclipse.gef.tools.CreationTool;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Selection Creation Tool Entry</b></em>'.
 *  creation tool entry where the actual object created is determined by the selectionCreation class. This allows the actual object created to be determined at selection time rather than statically defined in the palette entry itself.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A creation tool entry where the actual object created is determined by the selectionCreation class. This allows the actual object created to be determined at selection time rather than statically defined in the palette entry itself.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry#getSelectorClassName <em>Selector Class Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getSelectionCreationToolEntry()
 * @model
 * @generated
 */
public interface SelectionCreationToolEntry extends CreationToolEntry{


	/**
	 * This is the interface that this tool entry will use to select the
	 * object at activation time on the tool. It will return a new object
	 * when called. It is passed this entry so that it can access further
	 * info, such as the ResourceSet for creating the entry.
	 */
	public interface ISelector {
		/** 
		 * Called to request the selector to return the new object and type.
		 * @param creationTool
		 * @param domain
		 * @return Return a two element array where element [0] is the new object and
		 * element [1] is the type of the object (e.g. java.lang.Class if a
		 * java object, or an EClassifier if the object is an instance of a
		 * EMF EClassifier). Return <code>null</code> if object could not be created.
		 * 
		 * @since 1.1.0
		 */
		public Object[] getNewObjectAndType(CreationTool creationTool, EditDomain domain);
	}
		
	/**
	 * Returns the value of the '<em><b>Selector Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selector Class Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is the classname of the selector class. It must implement the ISelector interface.  Because it is not known the namespace for the class, you need to use a special format:
	 * <p>
	 * <ul>
	 * <li><b>packagename.classname</b>: This means it must be available from the default class loader. (In Eclipse, this will be the org.eclipse.ve.cde plugin. It must be visible to this to be found).
	 * <li><b>namespace/packagename.classname</b>: This means it will be found  in the namespace. (In Eclipse the namespace is the name of a bundle. It will look within that bundle to find the class).
	 * </ul>
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Selector Class Name</em>' attribute.
	 * @see #setSelectorClassName(String)
	 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getSelectionCreationToolEntry_SelectorClassName()
	 * @model
	 * @generated
	 */
	String getSelectorClassName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry#getSelectorClassName <em>Selector Class Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selector Class Name</em>' attribute.
	 * @see #getSelectorClassName()
	 * @generated
	 */
	void setSelectorClassName(String value);

}
