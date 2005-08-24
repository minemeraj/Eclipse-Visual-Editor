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
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.swt;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.DefaultLabelProviderWithNameAndAttribute;
 
/**
 * MenuItem Label provider. It gives a different image depending on the style, e.g. Check, radio.
 * 
 * Currently we only have item and separator. We need the others too. We have https://bugs.eclipse.org/bugs/show_bug.cgi?id=107379 to address this.
 * 
 * @since 1.1.0
 */
public class MenuItemLabelProvider extends DefaultLabelProviderWithNameAndAttribute {
	
	private static final String ITEM = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/menuitem_obj.gif"; //$NON-NLS-1$
	
	private static final String SEPARATOR = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/menuseparator_obj.gif"; //$NON-NLS-1$
	
	private static Image ITEM_IMAGE, SEPARATOR_IMAGE;

	public Image getImage(Object element) {
		if (element instanceof IJavaObjectInstance) {
			WidgetProxyAdapter widgetProxyAdapter = 
				(WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) element);
			
			// We may not yet have a proxy adapter because this can be called by rename on drop dialog before we have adapter.
			if (widgetProxyAdapter != null) {
				int style = widgetProxyAdapter.getStyle();

				if ((style & SWT.SEPARATOR) == 0) {
					try{
						ITEM_IMAGE = ImageDescriptor.createFromURL(new URL(ITEM)).createImage();
					} catch(MalformedURLException mue){
						ITEM_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
					}
					return ITEM_IMAGE;
				} else if ((style & SWT.SEPARATOR) != 0) { 
					try{
						SEPARATOR_IMAGE = ImageDescriptor.createFromURL(new URL(SEPARATOR)).createImage();
					} catch(MalformedURLException mue){
						SEPARATOR_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
					}
					return SEPARATOR_IMAGE;
				}
			}
		} 
		return super.getImage(element);	
	}
	
	public boolean isLabelProperty(Object element, String property) {
		// The allocation feature change would cause the image to need refresh.
		if (JavaInstantiation.ALLOCATION.equals(property))
			return true;
		return super.isLabelProperty(element, property);
	}	
}
