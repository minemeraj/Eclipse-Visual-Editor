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
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:38 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

import org.eclipse.ve.internal.cde.core.CDETreeDirectEditManager;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

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

	protected Command getDirectEditCommand(String newText, EditPart ep, IPropertyDescriptor property) {
		// We'll use property source so that wrappered properties may also be used.
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		EditDomain domain = EditDomain.getEditDomain(ep);
		IJavaObjectInstance stringObject = BeanUtilities.createString(EMFEditDomainHelper.getResourceSet(domain), newText);		
		if (property instanceof ICommandPropertyDescriptor)
			return ((ICommandPropertyDescriptor) property).setValue(ps, stringObject);
		else
			return new RuledPropertySetCommand(domain, ps, property.getId(), stringObject);
	}

	protected String getPropertyValue(EditPart ep, IPropertyDescriptor property) {
		// We'll use property source so that wrappered properties may also be used.
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (PropertySourceAdapter.isPropertySet(ps, property)) {
			Object textObj = PropertySourceAdapter.getPropertyValue(ps, property);
			if (textObj != null) {
				if (textObj instanceof IPropertySource)
					textObj = ((IPropertySource) textObj).getEditableValue();
				// Get the value from the remote vm of the externalized string
				try {
					IBeanProxyHost host = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) textObj);
					if (host.isBeanProxyInstantiated()) {
						IBeanProxy propProxy = host.getBeanProxy();
						return ((IStringBeanProxy) propProxy).stringValue();
					}
				} catch (Exception e) {
				}
			}
		}

		return "";
	}

}
