/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanDirectEditPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2005-03-21 22:48:06 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IDirectEditableEditPart;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * Direct edit policy for Java Beans
 * 
 * @since 1.1.0
 */
public class BeanDirectEditPolicy extends DirectEditPolicy {

	protected Command getDirectEditCommand(DirectEditRequest request) {
		String newText = (String) request.getCellEditor().getValue();
		EditDomain domain = EditDomain.getEditDomain(getHost());
		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		IJavaObjectInstance component = (IJavaObjectInstance) getHost().getModel();
		IJavaObjectInstance stringObject = BeanUtilities.createString(component.eResource().getResourceSet(), newText);
		cb.applyAttributeSetting(component, ((IDirectEditableEditPart) getHost()).getSfDirectEditProperty(), stringObject);
		return cb.getCommand();
	}

	protected void showCurrentEditValue(DirectEditRequest request) {
		// We don't use this
	}

}
