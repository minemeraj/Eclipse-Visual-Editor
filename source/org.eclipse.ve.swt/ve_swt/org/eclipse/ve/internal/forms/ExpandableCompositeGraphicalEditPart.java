/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.forms;

import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.swt.CompositeGraphicalEditPart;
import org.eclipse.ve.internal.swt.UnknownLayoutInputPolicy;

/**
 * Graphical EditPart for Expandable Composite.
 * 
 * @since 1.2.0
 */
public class ExpandableCompositeGraphicalEditPart extends CompositeGraphicalEditPart {

	public ExpandableCompositeGraphicalEditPart(Object model) {
		super(model);
	}
	
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new UnknownLayoutInputPolicy(new ExpandableCompositeContainerPolicy(EditDomain.getEditDomain(this))));
	}

}
