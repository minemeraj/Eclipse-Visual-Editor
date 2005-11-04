/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on 19-Sep-03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler;
;

/**
 * @author JoeWin
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FFOnlyModelAdapter extends ControlModelAdapter implements IContainmentHandler {
	
	public FFOnlyModelAdapter(Object model) {
		super(model);
	}
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		// return child only for parents that are the freeform surface
		if (!(parent instanceof DiagramData))
				throw new StopRequestException("Parent is invalid for this child.");
		return child;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#contributeToRemoveRequest(java.lang.Object, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder, boolean, org.eclipse.ve.internal.cde.core.EditDomain)
	 */
	public Object contributeToRemoveRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean orphan, EditDomain domain) throws StopRequestException {
		return child;
	}

}
