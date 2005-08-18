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
 *  $RCSfile: ToolBarLayoutEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-18 21:55:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy;

/**
 * 
 * @since 1.0.0
 */
public class ToolBarLayoutEditPolicy extends FlowLayoutEditPolicy {

	public ToolBarLayoutEditPolicy(ToolBarGraphicalEditPart editpart) {
		super(new ToolBarContainerPolicy(EditDomain.getEditDomain(editpart)), Boolean.TRUE);
	}

}