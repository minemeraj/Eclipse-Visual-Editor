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
/*
 * $RCSfile: JWindowJDialogContainmentHandler.java,v $ $Revision: 1.2 $ $Date: 2005-11-04 17:30:48 $
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * JWindow/JDialog containment handler.
 * <p>
 * It handles dropping on Window that requires a frame. It will see if over a frame, and if not, are there any frames available. If none, then drop
 * wiht null ctor. It will pop up a selection dialog if there is at least one.
 * <p>
 * If there is an allocation already then it will only allow drop if on the freeform. It will not popup all frames available because we don't know
 * which arg is for the frame parent. (Maybe later we can get fancier if the need arises).
 * 
 * @since 1.2.0
 */
public class JWindowJDialogContainmentHandler extends AbstractComponentModelContainmentHandler {

	public JWindowJDialogContainmentHandler(Object component) {
		super(component);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation,
			final EditDomain domain) throws StopRequestException {
		if (child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			if (creation) {
				// Now see if JWindow or JDialog directly. If so we need to handle the content pane.
				// We must call this BEFORE we handle the window drop.
				String childType = jo.getJavaType().getQualifiedNameForReflection();
				if (childType.equals("javax.swing.JDialog") || childType.equals("javax.swing.JWindow")) {
					RootPaneContainmentHandler.handleContentPane(jo, domain, preCmds);
				}
			}
			
			// Now drop the window.
			child = WindowContainmentHandler.dropWindow(parent, jo, domain, preCmds);
		}
		return child;
	}

}
