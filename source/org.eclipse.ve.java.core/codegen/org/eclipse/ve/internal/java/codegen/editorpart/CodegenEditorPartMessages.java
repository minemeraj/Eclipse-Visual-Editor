package org.eclipse.ve.internal.java.codegen.editorpart;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodegenEditorPartMessages.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:28:35 $ 
 */

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CodegenEditorPartMessages {

	private static final String BUNDLE_NAME =
		"org.eclipse.ve.internal.java.codegen.editorpart.messages";	//$NON-NLS-1$

	public static final ResourceBundle RESOURCE_BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);

	private CodegenEditorPartMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}