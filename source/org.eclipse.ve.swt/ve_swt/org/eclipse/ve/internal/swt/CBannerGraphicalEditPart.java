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
 *  $RCSfile: CBannerGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-22 16:22:09 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * GraphicalEditPart for a CBanner.
 * 
 * @since 1.1
 */
public class CBannerGraphicalEditPart extends CompositeGraphicalEditPart {

	public CBannerGraphicalEditPart(Object model) {
		super(model);
	}
	
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CBannerLayoutEditPolicy((EditDomain.getEditDomain(this))));
	}
}
