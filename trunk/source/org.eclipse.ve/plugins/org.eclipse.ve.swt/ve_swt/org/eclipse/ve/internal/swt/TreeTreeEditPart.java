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
 *  $RCSfile: TreeTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 15:41:48 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
 

/**
 * swt Tree tree edit part.
 * @since 1.2.0
 */
public class TreeTreeEditPart extends AbstractTableTreeEditPart {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public TreeTreeEditPart(Object model) {
		super(model);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.AbstractTableTreeEditPart#getContainerPolicy()
	 */
	protected ContainerPolicy getContainerPolicy() {
		return new TreeContainerPolicy(getEditDomain());
	}
	
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sfColumns = JavaInstantiation.getSFeature(rset, SWTConstants.SF_TREE_COLUMNS);
	}

}
