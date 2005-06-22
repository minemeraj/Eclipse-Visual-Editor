/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: PropertysheetMessages.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-22 15:21:41 $ 
 */

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertysheetMessages {

	private static final String BUNDLE_NAME =
		"org.eclipse.ve.internal.propertysheet.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);

	private PropertysheetMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
