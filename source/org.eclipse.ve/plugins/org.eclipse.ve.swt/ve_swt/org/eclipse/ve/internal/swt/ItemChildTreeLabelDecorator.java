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
/*
 *  $RCSfile: ItemChildTreeLabelDecorator.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-22 16:24:10 $ 
 */
package org.eclipse.ve.internal.swt;

import java.text.MessageFormat;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * 
 * Label decorator for the children of a Item Parents where the item has a control, such as TabFolder. It adds in the tab text to
 * the control child.
 * 
 * @since 1.1.0
 */
public class ItemChildTreeLabelDecorator extends Object implements ILabelDecorator {

	protected EReference sfItemText;

	private ListenerList listeners = new ListenerList(1);

	private Adapter itemAdapter;

	protected EObject tab;

	public ItemChildTreeLabelDecorator(EObject tab) {
		initializeSFs(tab);
		this.tab = tab;
		if (tab != null)
			tab.eAdapters().add(getTabAdapter());
	}

	private Adapter getTabAdapter() {
		if (itemAdapter == null) {
			itemAdapter = new Adapter() {

				public void notifyChanged(Notification notification) {
					if (notification.getFeature() == sfItemText)
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
		return itemAdapter;
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
		if (sfItemText == null)
			sfItemText = JavaInstantiation.getReference(rset, SWTConstants.SF_ITEM_TEXT);
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
		if (tab != null) {
			if (BeanProxyUtilities.getBeanProxyHost(component).isBeanProxyInstantiated()) {
				IJavaObjectInstance tabTitle = (IJavaObjectInstance) tab.eGet(sfItemText);
				if (tabTitle != null) {
					String title = BeanProxyUtilities.getBeanProxy(tabTitle).toBeanString();
					if (title != null)
						text = MessageFormat.format(SWTMessages.ItemChildTreeLabelDecorator_Tab_Text, new Object[] { text, title}); 
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
		if (tab != null)
			tab.eAdapters().remove(getTabAdapter());
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