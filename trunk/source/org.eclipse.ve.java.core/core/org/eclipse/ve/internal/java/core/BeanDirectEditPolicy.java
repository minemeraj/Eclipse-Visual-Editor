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
 *  $Revision: 1.2 $  $Date: 2005-06-15 20:19:38 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.rules.RuledPropertySetCommand;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;

/**
 * Direct edit policy for Java Beans
 * 
 * @since 1.1.0
 */
public class BeanDirectEditPolicy extends DirectEditPolicy {

	protected Command getDirectEditCommand(DirectEditRequest request) {
		String newText = (String) request.getCellEditor().getValue();
		EditDomain domain = EditDomain.getEditDomain(getHost());
		// We'll use property source so that wrappered properties may also be used.
		IPropertySource ps = (IPropertySource) getHost().getAdapter(IPropertySource.class);
		IPropertyDescriptor property = (IPropertyDescriptor) request.getDirectEditFeature();
		IJavaObjectInstance stringObject = BeanUtilities.createString(EMFEditDomainHelper.getResourceSet(domain), newText);		
		if (property instanceof ICommandPropertyDescriptor)
			return ((ICommandPropertyDescriptor) property).setValue(ps, stringObject);
		else
			return new RuledPropertySetCommand(domain, ps, property.getId(), stringObject);
	}

	protected void showCurrentEditValue(DirectEditRequest request) {
		// We don't use this
	}

}
