/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanDirectEditManager.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-15 20:19:38 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

import org.eclipse.ve.internal.cde.core.CDEDirectEditManager;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

/**
 * Direct edit manager for Java Beans.
 * 
 * @since 1.1.0
 */

public class BeanDirectEditManager extends CDEDirectEditManager {

	public BeanDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator, IPropertyDescriptor property) {
		super(source, editorType, locator, property);
	}

	protected String getPropertyValue(IPropertyDescriptor property) {
		String initialText = ""; //$NON-NLS-1$
		// retrieve the property's value from the model
		IPropertySource ps = (IPropertySource) getEditPart().getAdapter(IPropertySource.class);
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
						initialText = ((IStringBeanProxy) propProxy).stringValue();
					}
				} catch (Exception e) {
				}
			}
		}
		return initialText;
	}

}
