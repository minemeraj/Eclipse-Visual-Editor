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
 *  $RCSfile: AppletContainmentHandler.java,v $
 *  $Revision: 1.3 $  $Date: 2005-12-14 21:37:03 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
 

/**
 * Containment Handler for Applet.
 * @since 1.2.0
 */
public class AppletContainmentHandler extends FFOnlyModelAdapter {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public AppletContainmentHandler(Object model) {
		super(model);
	}
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		child = super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);
		// Only for creation do we do layout modification. Add assumes layout already handled.
		if (creation && child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			// Only interested in Applet directly (not a subclass). This
			// is because subclasses may already have their layout specified. No way to know that developer wanted the layout
			// to be supplied by users or the developer supplied the layout.
			if (jo.getJavaType().getQualifiedNameForReflection().equals("java.applet.Applet")) { //$NON-NLS-1$
				preCmds.append(DefaultJFCLayoutPolicy.processDefaultLayout(domain, jo, null));
			}
		}
		return child;		
	}

}
