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
 *  $RCSfile: TreeGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 15:41:47 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IArrayBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
 

/**
 * swt Tree Graphical edit part.
 * @since 1.2.0
 */
public class TreeGraphicalEditPart extends AbstractTableTreeGraphicalEditPart {

	/**
	 * @param aModel
	 * 
	 * @since 1.2.0
	 */
	public TreeGraphicalEditPart(Object aModel) {
		super(aModel);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.AbstractTableTreeGraphicalEditPart#getContainerPolicy()
	 */
	protected ContainerPolicy getContainerPolicy() {
		return new TreeContainerPolicy(getEditDomain());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.AbstractTableTreeGraphicalEditPart#getAllColumnRects(org.eclipse.jem.internal.proxy.core.IBeanProxy)
	 */
	protected IArrayBeanProxy getAllColumnRects(IBeanProxy modelProxy) {
		return BeanSWTUtilities.invoke_Tree_getAllColumnRects(modelProxy);
	}
	
	public void setModel(Object model) {
		super.setModel(model);
		sfColumns = JavaInstantiation.getSFeature(((EObject) model).eClass().eResource().getResourceSet(), SWTConstants.SF_TREE_COLUMNS);
	}

}
