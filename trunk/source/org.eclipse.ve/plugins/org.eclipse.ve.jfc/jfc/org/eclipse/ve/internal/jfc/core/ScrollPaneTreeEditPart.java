/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: ScrollPaneTreeEditPart.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:09 $ 
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
