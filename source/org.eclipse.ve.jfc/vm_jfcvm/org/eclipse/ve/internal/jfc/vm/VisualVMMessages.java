package org.eclipse.ve.internal.jfc.vm;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualVMMessages.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:34 $ 
 */

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class VisualVMMessages {

	private static final String BUNDLE_NAME =
		"org.eclipse.ve.internal.jfc.vm.messages";	//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);

	private VisualVMMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}