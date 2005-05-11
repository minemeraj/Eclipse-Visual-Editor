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
 *  $RCSfile: ScrolledCompositeGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-11 21:42:58 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.EditDomain;


public class ScrolledCompositeGraphicalEditPart extends CompositeGraphicalEditPart {

	public ScrolledCompositeGraphicalEditPart(Object model) {
		super(model);
	}
	
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ScrolledCompositeLayoutEditPolicy((EditDomain.getEditDomain(this))));
	}
}
