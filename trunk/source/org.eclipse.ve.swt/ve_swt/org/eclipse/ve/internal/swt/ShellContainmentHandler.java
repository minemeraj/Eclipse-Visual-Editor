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
 *  $RCSfile: ShellContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-03 19:20:48 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
 

/**
 * Containment Handler for a shell.
 * @since 1.2.0
 */
public class ShellContainmentHandler extends FFOnlyModelAdapter {

	public ShellContainmentHandler(Object model) {
		super(model);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws NoAddException {
		child = super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);	// Let super handle is on FF.
		if (child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			if (!jo.isSetAllocation()) {
				// Needs an allocation.
				ParseTreeAllocation parseTreeAllocation = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
				PTClassInstanceCreation classInstanceCreation = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation("org.eclipse.swt.widgets.Shell",null); //$NON-NLS-1$
				parseTreeAllocation.setExpression(classInstanceCreation);
				
				preCmds.applyAttributeSetting((EObject)child, JavaInstantiation.getAllocationFeature((IJavaInstance) child), parseTreeAllocation);
			}
			if (creation)
				preCmds.append(DefaultSWTLayoutPolicy.processDefaultLayout(domain, jo, null));
		}
		return child;		
	}
}
