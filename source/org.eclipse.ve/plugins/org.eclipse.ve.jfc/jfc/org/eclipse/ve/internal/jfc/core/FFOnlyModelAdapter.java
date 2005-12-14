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
 *  $RCSfile: FFOnlyModelAdapter.java,v $
 *  $Revision: 1.8 $  $Date: 2005-12-14 21:37:05 $ 
 */

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

public class FFOnlyModelAdapter extends AbstractComponentModelContainmentHandler {
	
	public FFOnlyModelAdapter(Object model) {
		super(model);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		// return child only for parents that are the freeform surface
		if (!(parent instanceof DiagramData))
				throw new StopRequestException(JFCMessages.FFOnlyModelAdapter_StopRequest_InvaidParent);
		return child;
	}

}
