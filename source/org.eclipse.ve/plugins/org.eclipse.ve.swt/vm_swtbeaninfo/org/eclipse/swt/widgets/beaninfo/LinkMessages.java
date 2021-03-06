/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LinkMessages.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:52:53 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author jon
 *
 * Messages class for type Link.
 */
public class LinkMessages {

	private static final String BUNDLE_NAME = "org.eclipse.swt.widgets.beaninfo.link";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LinkMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
