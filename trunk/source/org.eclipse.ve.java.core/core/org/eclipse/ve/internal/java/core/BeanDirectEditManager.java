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
 *  $Revision: 1.2 $  $Date: 2005-04-05 22:48:23 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

import org.eclipse.ve.internal.cde.core.CDEDirectEditManager;

/**
 * Direct edit manager for Java Beans.
 * 
 * @since 1.1.0
 */

public class BeanDirectEditManager extends CDEDirectEditManager {

	public BeanDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator, EStructuralFeature property) {
		super(source, editorType, locator, property);
	}

	/*
	 * Gets the property value for the specified structural feature sfProperty
	 */
	protected String getPropertyValue(EStructuralFeature sfProperty) {
		String initialText = ""; //$NON-NLS-1$
		// retrieve the property's value from the model
		IJavaObjectInstance bean = (IJavaObjectInstance) getEditPart().getModel();
		if (bean.eIsSet(sfProperty)) {
			EObject textObj = (EObject) bean.eGet(sfProperty);
			if (textObj != null) {
				// Get the value from the remote vm of the externalized string
				try {
					IBeanProxyHost host = BeanProxyUtilities.getBeanProxyHost(bean);
					IBeanProxy propProxy = host.getBeanPropertyProxyValue(sfProperty);
					initialText = ((IStringBeanProxy) propProxy).stringValue();
				} catch (Exception e) {
				}
			}
		}
		return initialText;
	}

}
