/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CLabelContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-03 19:20:48 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.LabelCreationCommand;
 

/**
 * Containment handler for SWT Label.
 * @since 1.2.0
 */
public class CLabelContainmentHandler extends WidgetContainmentHandler {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public CLabelContainmentHandler(Object model) {
		super(model);
	}
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws NoAddException {
		child = super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);
		if (creation && child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			preCmds.append(new LabelCreationCommand(jo, "text", SWTMessages.CLabelPolicy_text_CLabel, domain));
		}
		return child;		
	}

}
