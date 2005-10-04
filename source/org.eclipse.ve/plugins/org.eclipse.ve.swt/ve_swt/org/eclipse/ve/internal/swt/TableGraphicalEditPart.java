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
 *  $RCSfile: TableGraphicalEditPart.java,v $
 *  $Revision: 1.12 $  $Date: 2005-10-04 15:41:47 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IArrayBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
 

/**
 * SWT Table graphical editpart.
 * @since 1.2.0
 */
public class TableGraphicalEditPart extends AbstractTableTreeGraphicalEditPart {

	/**
	 * @param aModel
	 * 
	 * @since 1.2.0
	 */
	public TableGraphicalEditPart(Object aModel) {
		super(aModel);
	}

	protected ContainerPolicy getContainerPolicy() {
		return new TableContainerPolicy(getEditDomain());
	}

	protected IArrayBeanProxy getAllColumnRects(IBeanProxy modelProxy) {
		return BeanSWTUtilities.invoke_Table_getAllColumnRects(modelProxy);
	}

	public void setModel(Object model) {
		super.setModel(model);
		sfColumns = JavaInstantiation.getSFeature(((EObject) model).eClass().eResource().getResourceSet(), SWTConstants.SF_TABLE_COLUMNS);
	}
}
