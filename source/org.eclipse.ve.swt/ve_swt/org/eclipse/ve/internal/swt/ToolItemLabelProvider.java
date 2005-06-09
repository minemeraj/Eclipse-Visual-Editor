/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ToolItemLabelProvider.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-09 17:32:29 $ 
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
 * ToolItem Label Provider. Puts up a different icon depending on the style, e.g. CheckBox, Radio.
 * 
 * @since 1.1.0
 */
public class ToolItemLabelProvider extends DefaultLabelProviderWithNameAndAttribute {

	private static final String CHECK_BOX = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/checkbox_obj.gif"; //$NON-NLS-1$
		
	private static final String RADIO_BUTTON = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/radiobutton_obj.gif"; //$NON-NLS-1$
	
	private static final String DROP_DOWN = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/choice_obj.gif"; //$NON-NLS-1$
	
	private static Image CHECK_BOX_IMAGE, RADIO_BUTTON_IMAGE, DROP_DOWN_IMAGE;
	
	/**
	 * Get the Check box image.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static Image getCheckBox() {
    	if (CHECK_BOX_IMAGE == null)
			try{
				CHECK_BOX_IMAGE = ImageDescriptor.createFromURL(new URL(CHECK_BOX)).createImage();
			} catch(MalformedURLException mue){
				CHECK_BOX_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
			}
		return CHECK_BOX_IMAGE;	
	}
	
	/**
	 * Get radio image.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static Image getRadio() {
    	if (RADIO_BUTTON_IMAGE == null)
			try{
				RADIO_BUTTON_IMAGE = ImageDescriptor.createFromURL(new URL(RADIO_BUTTON)).createImage();
			} catch(MalformedURLException mue){
				RADIO_BUTTON_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
			}
		return RADIO_BUTTON_IMAGE;	
	}
	
	public Image getImage(Object element) {
		if (element instanceof IJavaObjectInstance) {
			WidgetProxyAdapter widgetProxyAdapter = 
				(WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) element);
			// We may not yet have a proxy adapter because this can be called by rename on drop dialog before we have adapter.
			if (widgetProxyAdapter != null) {
				int style = widgetProxyAdapter.getStyle();

				if ((style & SWT.CHECK) != 0) {
					return getCheckBox();
				} else if ((style & SWT.RADIO) != 0) {
					return getRadio();
				} else if ((style & SWT.DROP_DOWN) != 0) {
					if (DROP_DOWN_IMAGE == null)
						try {
							DROP_DOWN_IMAGE = ImageDescriptor.createFromURL(new URL(DROP_DOWN)).createImage();
						} catch (MalformedURLException mue) {
							DROP_DOWN_IMAGE = ImageDescriptor.getMissingImageDescriptor().createImage();
						}
					return DROP_DOWN_IMAGE;
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
