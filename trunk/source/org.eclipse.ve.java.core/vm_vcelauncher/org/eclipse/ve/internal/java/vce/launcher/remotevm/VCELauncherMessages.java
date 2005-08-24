/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce.launcher.remotevm;
/*
 *  $RCSfile: VCELauncherMessages.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class VCELauncherMessages {

	private static final String BUNDLE_NAME =
		"org.eclipse.ve.internal.java.vce.launcher.remotevm.messages";	//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);

	private VCELauncherMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	public static char getChar(String key) {
		try { 
			return RESOURCE_BUNDLE.getString(key).charAt(0);
		} catch (MissingResourceException e){
			return ' ';
		}
	}
}
