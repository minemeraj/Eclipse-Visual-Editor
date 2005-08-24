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
 *  $RCSfile: ButtonLabelProvider.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:55 $ 
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
 * Button Label provider. It gives a different image depending on the style, e.g. Check, radio.
 * 
 * @since 1.1.0
 */
public class ButtonLabelProvider extends DefaultLabelProviderWithNameAndAttribute {
	
	private static final String CHECK_BOX = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/checkbox_obj.gif"; //$NON-NLS-1$
	
	private static final String RADIO_BUTTON = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/radiobutton_obj.gif"; //$NON-NLS-1$
	
	private static Image CHECK_BOX_IMAGE, RADIO_BUTTON_IMAGE;

	public Image getImage(Object element) {
		if (element instanceof IJavaObjectInstance) {
			WidgetProxyAdapter widgetProxyAdapter = 
				(WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) element);
			
			// We may not yet have a proxy adapter because this can be called by rename on drop dialog before we have adapter.
			if (widgetProxyAdapter != null) {
				int style = widgetProxyAdapter.getStyle();

				if ((style & SWT.CHECK) != 0) {
					try{
						CHECK_BOX_IMAGE = ImageDescriptor.createFromURL(new URL(CHECK_BOX)).createImage();
					} catch(MalformedURLException mue){
						CHECK_BOX_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
					}
					return CHECK_BOX_IMAGE;
				} else if ((style & SWT.RADIO) != 0) { 
					try{
						RADIO_BUTTON_IMAGE = ImageDescriptor.createFromURL(new URL(RADIO_BUTTON)).createImage();
					} catch(MalformedURLException mue){
						RADIO_BUTTON_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
					}
					return RADIO_BUTTON_IMAGE;
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
