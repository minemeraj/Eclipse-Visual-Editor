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
 *  $RCSfile: InternalVisualMessages.java,v $
 *  $Revision: 1.1 $  $Date: 2005-09-22 13:04:03 $ 
 */
package org.eclipse.ve.internal.java.visual;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class InternalVisualMessages {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.visual.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private InternalVisualMessages() {
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
