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
 *  $Revision: 1.2 $  $Date: 2004-03-04 02:13:08 $ 
 */

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Tree edit part for handling ScrollPane in the Beans viewer
 */
public class ScrollPaneTreeEditPart extends ContainerTreeEditPart {

	public ScrollPaneTreeEditPart(Object model) {
		super(model);
	}

	protected VisualContainerPolicy getContainerPolicy() {
		return new ScrollPaneContainerPolicy(EditDomain.getEditDomain(this));	// AWT standard Contained Edit Policy
	}
}