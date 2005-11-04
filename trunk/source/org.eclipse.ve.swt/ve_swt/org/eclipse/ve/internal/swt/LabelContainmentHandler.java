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
 *  $RCSfile: LabelContainmentHandler.java,v $
 *  $Revision: 1.3 $  $Date: 2005-11-04 17:30:52 $ 
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
public class LabelContainmentHandler extends WidgetContainmentHandler {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public LabelContainmentHandler(Object model) {
		super(model);
	}
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		if (creation && child instanceof IJavaObjectInstance && !((IJavaObjectInstance)child).isSetAllocation()) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			preCmds.append(new LabelCreationCommand(jo, "text", SWTMessages.LabelPolicy_text_Label, domain));
		}
		return super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);
	}

}
