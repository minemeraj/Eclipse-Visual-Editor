/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.palette;

import org.eclipse.gef.requests.CreationFactory;

/*
 *  $RCSfile: CreationToolEntry.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:35 $ 
 */


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Creation Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCreationToolEntry()
 * @model abstract="true"
 * @generated
 */
public interface CreationToolEntry extends AbstractToolEntry{


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return the creation factory that will be used by this entry.
	 * <!-- end-model-doc -->
	 * @model dataType="org.eclipse.ve.internal.cde.palette.CreationFactory" 
	 * @generated
	 */
	CreationFactory createFactory();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Set the factory that will be used by this entry in the creation tool. This allows wrappering the factory returned by createFactory() with another one (such as the AnnotationCreationFactory).
	 * <!-- end-model-doc -->
	 * @model parameters="org.eclipse.ve.internal.cde.palette.CreationFactory"
	 * @generated
	 */
	void setFactory(CreationFactory factory);

}
