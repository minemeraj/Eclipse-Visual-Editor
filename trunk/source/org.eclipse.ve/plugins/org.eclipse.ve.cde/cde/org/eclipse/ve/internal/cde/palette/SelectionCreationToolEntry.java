package org.eclipse.ve.internal.cde.palette;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SelectionCreationToolEntry.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */


import java.util.List;

import org.eclipse.ve.internal.cde.emf.EMFCreationTool;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Selection Creation Tool Entry</b></em>'.
 *  creation tool entry where the actual object created is determined by the selectionCreation class. This allows the actual object created to be determined at selection time rather than statically defined in the palette entry itself.
 * <!-- end-user-doc -->
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
public interface SelectionCreationToolEntry extends CreationToolEntry {


	/**
	 * This is the interface that this tool entry will use to select the
	 * object at activation time on the tool. It will return a new object
	 * when called. It is passed this entry so that it can access further
	 * info, such as the ResourceSet for creating the entry.
	 */
	public interface ISelector {
		/**
		 * Return a two element array where element [0] is the new object and
		 * element [1] is the type of the object (e.g. java.lang.Class if a
		 * java object, or an EClassifier if the object is an instance of a
		 * EMF EClassifier).
		 *
		 * Note: Return null if new object can't be created.
		 */
		public Object[] getNewObjectAndType(SelectionCreationTool selectionCreationTool);
	}
	
	/**
	 * SelectionCreationTool
	 */
	public abstract static class SelectionCreationTool extends EMFCreationTool {
				
		/**
		 * Return the SelectionCreationToolEntry
		 */
		public abstract SelectionCreationToolEntry getSelectionToolEntry();
	}		
		
	/**
	 * Return the selection history list.
	 */
	public List getSelectionHistory();
	
	/**
	 * Returns the value of the '<em><b>Selector Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selector Class Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
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
