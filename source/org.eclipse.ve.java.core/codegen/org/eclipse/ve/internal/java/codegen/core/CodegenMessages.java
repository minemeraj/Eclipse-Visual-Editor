package org.eclipse.ve.internal.java.codegen.core;
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
 *  $RCSfile: CodegenMessages.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class CodegenMessages {

	private static final String BUNDLE_NAME =
		"org.eclipse.ve.internal.java.codegen.core.messages";	//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);

	private CodegenMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	public static String getPluginPropertyString(String key){
		try {		
			// TODO Why were these strings in the plugin.properties. I couldn't find them being used by the plugin.xml. That is usually why they would be in plugin.properties.
			return JavaVEPlugin.getPlugin().getDescriptor().getResourceString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}			
	}
}