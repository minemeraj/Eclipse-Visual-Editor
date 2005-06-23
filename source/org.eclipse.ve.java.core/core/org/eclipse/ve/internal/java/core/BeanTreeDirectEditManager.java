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
 *  $RCSfile: BeanTreeDirectEditManager.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-23 16:08:44 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.CDETreeDirectEditManager;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.rules.RuledPropertySetCommand;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;

/**
 * Tree Direct Editmanager for Beans.
 * 
 * @since 1.1.0
 */
public class BeanTreeDirectEditManager extends CDETreeDirectEditManager {

	public static final String VIEWER_DATA_KEY = "Bean_TreeDirectEditManager"; //$NON-NLS-1$

	/**
	 * Get the Bean Tree Direct Edit Manager for the given domain and viewer.
	 * 
	 * @param domain
	 * @param viewer
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static BeanTreeDirectEditManager getDirectEditManager(EditDomain domain, EditPartViewer viewer) {
		BeanTreeDirectEditManager manager = (BeanTreeDirectEditManager) domain.getViewerData(viewer, BeanTreeDirectEditManager.VIEWER_DATA_KEY);
		if (manager == null) {
			manager = new BeanTreeDirectEditManager(viewer);
			domain.setViewerData(viewer, BeanTreeDirectEditManager.VIEWER_DATA_KEY, manager);
		}
		return manager;
	}

	public BeanTreeDirectEditManager(EditPartViewer v) {
		super(v);
	}

	protected Command getDirectEditCommand(Object newValue, EditPart ep, IPropertyDescriptor property) {
		// We'll use property source so that wrappered properties may also be used.
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		EditDomain domain = EditDomain.getEditDomain(ep);
		if (property instanceof ICommandPropertyDescriptor)
			return ((ICommandPropertyDescriptor) property).setValue(ps, newValue);
		else
			return new RuledPropertySetCommand(domain, ps, property.getId(), newValue);
	}

}
