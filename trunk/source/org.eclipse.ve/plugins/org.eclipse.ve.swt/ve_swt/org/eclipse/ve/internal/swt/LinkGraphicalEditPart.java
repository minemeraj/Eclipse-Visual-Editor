/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: LinkGraphicalEditPart.java,v $ 
 * $Revision: 1.1 $ $Date: 2005-04-11 20:14:04 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.EditPolicy;
import org.eclipse.ve.internal.cde.core.DefaultComponentEditPolicy;

public class LinkGraphicalEditPart extends ControlGraphicalEditPart {

	public LinkGraphicalEditPart(Object model){
		super(model);
	}
	
	protected void createEditPolicies() {
		// Default component role allows delete and basic behavior of a component within a parent edit part that contains it
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
	}
	
}
