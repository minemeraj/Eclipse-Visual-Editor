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
 *  $Revision: 1.3 $  $Date: 2005-12-14 21:37:04 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaMessages;
import org.eclipse.ve.internal.java.core.LabelCreationCommand;
 

/**
 * Containment handler for Label/JLabel. It handles the setting of the text if not set.
 * @since 1.2.0
 */
public class LabelContainmentHandler extends AbstractComponentModelContainmentHandler {

	/**
	 * @param component
	 * 
	 * @since 1.2.0
	 */
	public LabelContainmentHandler(Object component) {
		super(component);
	}
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		if (creation && child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			// Only if exactly Label or JLabel. Subclasses would assume to have their text already set.
			String classname = jo.getJavaType().getQualifiedNameForReflection();
			if (classname.equals("java.awt.Label")) //$NON-NLS-1$
				preCmds.append(new LabelCreationCommand(jo, "text", JavaMessages.LabelPolicy_text_Label, domain)); //$NON-NLS-1$
			else if (classname.equals("javax.swing.JLabel")) //$NON-NLS-1$
				preCmds.append(new LabelCreationCommand(jo, "text", JavaMessages.LabelPolicy_text_JLabel, domain)); //$NON-NLS-1$
		}
		return child;
	}

}
