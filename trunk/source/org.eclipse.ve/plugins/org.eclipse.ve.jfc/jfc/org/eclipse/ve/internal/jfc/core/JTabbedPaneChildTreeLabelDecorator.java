package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JTabbedPaneChildTreeLabelDecorator.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * @author pwalker
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JTabbedPaneChildTreeLabelDecorator extends Object implements ILabelDecorator {
	protected EReference sfTabTitle, sfTabComponent, sfTabs;

	public void initializeSFs(EObject component) {
		ResourceSet rset = component.eResource().getResourceSet();
		if (sfTabTitle == null)
			sfTabTitle = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_TITLE);
		if (sfTabComponent == null)
			sfTabComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_COMPONENT);
		if (sfTabs == null)
			sfTabs = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABBEDPANE_TABS);
	}
	/**
	 * @see org.eclipse.jface.viewers.ILabelDecorator#decorateImage(Image, Object)
	 */
	public Image decorateImage(Image image, Object element) {
		return image;
	}

	/**
	 * @see org.eclipse.jface.viewers.ILabelDecorator#decorateText(String, Object)
	 */
	public String decorateText(String text, Object element) {
		if (element == null)
			return ""; //$NON-NLS-1$
		if (!(element instanceof IJavaObjectInstance))
			return element.toString();
		IJavaObjectInstance component = (IJavaObjectInstance) element;
		initializeSFs((EObject) component);
		EObject tabComponent = InverseMaintenanceAdapter.getFirstReferencedBy((EObject) component, sfTabComponent);
		if (tabComponent != null) {
			// See whether the component is in severe error.  If so then don't include it here
			if (BeanProxyUtilities.getBeanProxyHost((IJavaInstance) component).getErrorStatus()
				!= IBeanProxyHost.ERROR_SEVERE) {
				IJavaObjectInstance tabTitle = (IJavaObjectInstance) tabComponent.eGet(sfTabTitle);
				if (tabTitle != null) {
					// We know the constraints value should be a bean so we can use its toString to get the string value
					String title = BeanProxyUtilities.getBeanProxy(tabTitle).toBeanString();
					if (title != null)
						text = MessageFormat.format(VisualMessages.getString("JTabbedPaneChildTreeLabelDecorator.Tab.Title"), new Object[] {text, title}); //$NON-NLS-1$
				}
			}
		}
		return text;
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(Object, String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
	}

}
