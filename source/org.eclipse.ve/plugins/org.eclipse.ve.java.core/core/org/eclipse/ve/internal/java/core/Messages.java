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
 *  Created Jan 21, 2005 by Gili Mendel
 * 
 *  $RCSfile: Messages.java,v $
 *  $Revision: 1.1 $  $Date: 2005-01-21 21:20:55 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @since 1.1.0
 */
public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.core.messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
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