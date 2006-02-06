/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;

/*
 *  $RCSfile: JTabbedPaneChildTreeLabelDecorator.java,v $
 *  $Revision: 1.9 $  $Date: 2006-02-06 23:38:32 $ 
 */

import java.text.MessageFormat;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * @author pwalker
 * 
 * Label decorator for the children of a JTabbedPane. In this case although Eclipse would rather use one decorator for all the children, there is one
 * decorator created for each of the children. Also note that from a user perspective it looks like we are setting the tab title for the Swing
 * component child but the tab title is a property of the poofed up wrappered component JTabComponent which encapsulates all the extra properties
 * needed in the addTab method for a JTabbedPane.
 */
public class JTabbedPaneChildTreeLabelDecorator extends Object implements ILabelDecorator {

	protected EReference sfTabTitle, sfTabComponent, sfTabs;

	private ListenerList listeners = new ListenerList(ListenerList.IDENTITY);

	private Adapter tabcomponentAdapter;

	protected EObject tabComponent;

	public JTabbedPaneChildTreeLabelDecorator(EObject tabComponent) {
		initializeSFs(tabComponent);
		this.tabComponent = tabComponent;
		if (tabComponent != null)
			tabComponent.eAdapters().add(getTabComponentAdapter());
	}

	private Adapter getTabComponentAdapter() {
		if (tabcomponentAdapter == null) {
			tabcomponentAdapter = new Adapter() {

				public void notifyChanged(Notification notification) {
					if (notification.getFeature() == sfTabTitle)
						fireLabelProviderChanged();
				}

				public Notifier getTarget() {
					return null;
				}

				public void setTarget(Notifier newTarget) {
				}

				public boolean isAdapterForType(Object type) {
					return false;
				}
			};
		}
		return tabcomponentAdapter;
	}

	protected void fireLabelProviderChanged() {
		Object[] listeners = this.listeners.getListeners();
		final LabelProviderChangedEvent labelProviderChangeEvent = new LabelProviderChangedEvent(this);
		for (int i = 0; i < listeners.length; ++i) {
			final ILabelProviderListener l = (ILabelProviderListener) listeners[i];
			Platform.run(new SafeRunnable() {

				public void run() {
					l.labelProviderChanged(labelProviderChangeEvent);
				}
			});
		}
	}

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
		if (tabComponent != null) {
			// See whether the component is in severe error. If so then don't include it here
			if (BeanProxyUtilities.getBeanProxyHost(component).isBeanProxyInstantiated()) {
				IJavaObjectInstance tabTitle = (IJavaObjectInstance) tabComponent.eGet(sfTabTitle);
				if (tabTitle != null) {
					// We know the constraints value should be a bean so we can use its toString to get the string value
					String title = BeanProxyUtilities.getBeanProxy(tabTitle).toBeanString();
					if (title != null)
						text = MessageFormat.format(
								JFCMessages.JTabbedPaneChildTreeLabelDecorator_Tab_Title, new Object[] { text, title}); 
				}
			}
		}
		return text;
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		if (tabComponent != null)
			tabComponent.eAdapters().remove(getTabComponentAdapter());
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
		listeners.remove(listener);
	}

}
