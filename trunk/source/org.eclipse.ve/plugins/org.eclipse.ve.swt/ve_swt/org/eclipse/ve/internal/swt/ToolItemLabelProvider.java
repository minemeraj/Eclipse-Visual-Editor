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
 *  $Revision: 1.1 $  $Date: 2005-06-08 20:51:45 $ 
 */
package org.eclipse.ve.internal.swt;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.DefaultLabelProviderWithNameAndAttribute;
 

public class ToolItemLabelProvider extends DefaultLabelProviderWithNameAndAttribute {

	private Image image = null;
	
	private final String CHECK_BOX = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/checkbox_obj.gif"; //$NON-NLS-1$
		
	private final String RADIO_BUTTON = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/radiobutton_obj.gif"; //$NON-NLS-1$
	
	private final String DROP_DOWN = "platform:/plugin/org.eclipse.ve.swt/icons/full/clcl16/choice_obj.gif"; //$NON-NLS-1$
	
	public Image getImage(Object element) {
		if (element instanceof IJavaObjectInstance) {
			URL iconURL = null;

			WidgetProxyAdapter widgetProxyAdapter = 
				(WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) element);
			
			int style = widgetProxyAdapter.getStyle();
			
	        if(( style & SWT.CHECK) != 0){
				try{
					iconURL = new URL(CHECK_BOX);
				} catch(MalformedURLException mue){}
	        } else if (( style & SWT.RADIO) != 0){
				try{
					iconURL = new URL(RADIO_BUTTON);
				} catch(MalformedURLException mue){}		        	
	        } else if (( style & SWT.DROP_DOWN) != 0){
				try{
					iconURL = new URL(DROP_DOWN);
				} catch(MalformedURLException mue){}		        	
	        }
			if ( iconURL != null) {
				ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(iconURL);
				image = imageDescriptor.createImage();
				return image;
			}
		} 
		return super.getImage(element);	
	}

	public void dispose() {
		if(image != null){
			image.dispose();
		}
		super.dispose();
	}
}
