package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ComponentDirectEditPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

public class ComponentDirectEditPolicy
	extends DirectEditPolicy {
		
		/**
		 * @see DirectEditPolicy#getDirectEditCommand(DirectEditRequest)
		 */
		protected Command getDirectEditCommand(DirectEditRequest edit) {
			String newText = (String)edit.getCellEditor().getValue();
			EditDomain domain = EditDomain.getEditDomain(getHost());			
			RuledCommandBuilder cb = new RuledCommandBuilder(domain);
			IJavaObjectInstance component = (IJavaObjectInstance) getHost().getModel();
			IJavaObjectInstance stringObject = BeanUtilities.createString(component.eResource().getResourceSet(), newText);
			cb.applyAttributeSetting(component, ((ComponentGraphicalEditPart)getHost()).getSfDirectEditProperty(), stringObject);
			return cb.getCommand();
		}

		/**
		 * @see DirectEditPolicy#showCurrentEditValue(DirectEditRequest)
		 */
		protected void showCurrentEditValue(DirectEditRequest request) {
			// We don't use this
		}
}
