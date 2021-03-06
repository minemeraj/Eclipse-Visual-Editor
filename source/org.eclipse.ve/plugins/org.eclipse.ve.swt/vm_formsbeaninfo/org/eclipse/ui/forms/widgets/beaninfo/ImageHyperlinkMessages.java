/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ImageHyperlinkMessages.java,v $
 *  $Revision: 1.2 $  $Date: 2006-05-17 20:15:54 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @since 1.0.0
 */
public class ImageHyperlinkMessages {

	private static final String BUNDLE_NAME = "org.eclipse.ui.forms.widgets.beaninfo.imagehyperlink";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private ImageHyperlinkMessages() {
	}

	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
