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
 *  $Revision: 1.6 $  $Date: 2005-10-03 19:21:01 $ 
 */

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler;

public class FFOnlyModelAdapter extends ComponentModelAdapter implements IContainmentHandler {
	
	public FFOnlyModelAdapter(Object model) {
		super(model);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws NoAddException {
		// return child only for parents that are the freeform surface
		if (!(parent instanceof DiagramData))
				throw new NoAddException("Parent is invalid for this child.");
		return child;
	}

}
