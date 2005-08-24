/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: DefaultJavaBeanLabelProvider.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jem.internal.beaninfo.BeanDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.properties.DefaultLabelProviderWithName;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;


public class DefaultJavaBeanLabelProvider extends DefaultLabelProviderWithName {

	static final ImageRegistry registry = new ImageRegistry();
/* 
 * This label provider is designed for JavaBeans.  For the URL of the Image we ask the BeanDecorator
 * This returns a URL, and the Image itself is held in a static cache to manage SWT resources
 */
public Image getImage(Object element) {
	
	// TODO this needs thinking about caching and disposing the image correctly
	JavaClass javaClass = (JavaClass) ((IJavaObjectInstance)element).getJavaType();
	BeanDecorator beanDecorator = Utilities.getBeanDecorator(javaClass);
	if ( beanDecorator != null ) {
		URL iconURL = beanDecorator.getIconURL();
		if ( iconURL != null) {
			// For the URL we return the image from the registry if there is one
			Image existingImage = registry.get(iconURL.toExternalForm());
			if ( existingImage != null ) {
				return existingImage;
			} else {
				// Otherwise create one and then register it ,, before returning it
				ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(iconURL);
				Image image = imageDescriptor.createImage();
				registry.put(iconURL.toExternalForm(),image);
				return image;
			}
		}
	} 
	return super.getImage(element);		
}
}
