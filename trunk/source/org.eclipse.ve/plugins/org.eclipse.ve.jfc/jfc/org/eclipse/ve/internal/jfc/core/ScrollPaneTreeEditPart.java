package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: ScrollPaneTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * Tree edit part for handling ScrollPane in the Beans viewer
 */
public class ScrollPaneTreeEditPart extends ContainerTreeEditPart {

	public ScrollPaneTreeEditPart(Object model) {
		super(model);
	}

	protected ContainerPolicy getContainerPolicy() {
		return new ScrollPaneContainerPolicy(EditDomain.getEditDomain(this));	// AWT standard Contained Edit Policy
	}
}