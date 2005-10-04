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
 *  $RCSfile: TableTreeEditPart.java,v $
 *  $Revision: 1.9 $  $Date: 2005-10-04 15:41:47 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
 

/**
 * Tree Editpart for Table.
 * @since 1.2.0
 */
public class TableTreeEditPart extends AbstractTableTreeEditPart {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public TableTreeEditPart(Object model) {
		super(model);
	}

	protected ContainerPolicy getContainerPolicy() {
		return new TableContainerPolicy(getEditDomain());
	}
	
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sfColumns = JavaInstantiation.getSFeature(rset, SWTConstants.SF_TABLE_COLUMNS);
	}

}
