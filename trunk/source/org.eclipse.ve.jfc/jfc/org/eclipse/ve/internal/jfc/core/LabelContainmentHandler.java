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
 *  $Revision: 1.1 $  $Date: 2005-10-03 19:21:01 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler;

import org.eclipse.ve.internal.java.core.JavaMessages;
import org.eclipse.ve.internal.java.core.LabelCreationCommand;
 

/**
 * Containment handler for Label/JLabel. It handles the setting of the text if not set.
 * @since 1.2.0
 */
public class LabelContainmentHandler extends ComponentModelAdapter implements IContainmentHandler {

	/**
	 * @param component
	 * 
	 * @since 1.2.0
	 */
	public LabelContainmentHandler(Object component) {
		super(component);
	}
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws NoAddException {
		if (creation && child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			// Only if exactly Label or JLabel. Subclasses would assume to have their text already set.
			String classname = jo.getJavaType().getQualifiedNameForReflection();
			if (classname.equals("java.awt.Label"))
				preCmds.append(new LabelCreationCommand(jo, "text", JavaMessages.LabelPolicy_text_Label, domain));
			else if (classname.equals("javax.swing.JLabel"))
				preCmds.append(new LabelCreationCommand(jo, "text", JavaMessages.LabelPolicy_text_JLabel, domain));
		}
		return child;
	}

}
