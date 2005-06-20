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
 *  $RCSfile: CBannerTreeEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-20 20:37:01 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;


public class CBannerTreeEditPart extends CompositeTreeEditPart {
	
	public CBannerTreeEditPart(Object model) {
		super(model);
	}
	
	protected VisualContainerPolicy getContainerPolicy() {
		return new CBannerContainerPolicy(EditDomain.getEditDomain(this));
	}

	protected void createLayoutPolicyHelper() {
		treeContainerPolicy.setPolicyHelper(new CBannerLayoutPolicyHelper());
	}
}
