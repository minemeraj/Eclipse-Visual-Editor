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

import org.eclipse.ve.internal.java.core.*;
 
/**
 * Menu Label provider. It gives a different image depending on the style, e.g. Bar, Drop, Popup.
 * 
 * @since 1.1.0
 */
public class MenuLabelProvider extends DefaultJavaBeanLabelProvider {
	
	private static final String BAR = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/menubar_obj.gif"; //$NON-NLS-1$
	
	private static final String MENU = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/menu_obj.gif"; //$NON-NLS-1$
	
	private static Image BAR_IMAGE, MENU_IMAGE;

	public Image getImage(Object element) {
		if (element instanceof IJavaObjectInstance) {
			WidgetProxyAdapter widgetProxyAdapter = 
				(WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) element);
			
			// We may not yet have a proxy adapter because this can be called by rename on drop dialog before we have adapter.
			if (widgetProxyAdapter != null) {
				int style = widgetProxyAdapter.getStyle();

				if ((style & SWT.BAR) != 0) {
					try{
						BAR_IMAGE = ImageDescriptor.createFromURL(new URL(BAR)).createImage();
					} catch(MalformedURLException mue){
						BAR_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
					}
					return BAR_IMAGE;
				} else if ((style & (SWT.DROP_DOWN | SWT.POP_UP)) != 0) { 
					try{
						MENU_IMAGE = ImageDescriptor.createFromURL(new URL(MENU)).createImage();
					} catch(MalformedURLException mue){
						MENU_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
					}
					return MENU_IMAGE;
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
