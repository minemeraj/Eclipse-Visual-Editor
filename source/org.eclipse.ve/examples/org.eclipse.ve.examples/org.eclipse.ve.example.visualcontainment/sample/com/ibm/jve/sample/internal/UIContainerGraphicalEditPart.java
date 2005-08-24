/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.jve.sample.internal;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ve.internal.cde.core.CDELayoutEditPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.swt.ControlGraphicalEditPart;

public class UIContainerGraphicalEditPart extends ControlGraphicalEditPart {

	public UIContainerGraphicalEditPart(Object model) {
		super(model);
	}
	
	protected IFigure createFigure() {
		IFigure fig = super.createFigure();
		fig.setLayoutManager(new XYLayout());
		return fig;
	}	
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		// Edit policy to add children to the "parts" relationship if they are UIBasePart types
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CDELayoutEditPolicy(
				new UIContainerPartContainerPolicy(EditDomain.getEditDomain(this))
				));
						
	}

}
