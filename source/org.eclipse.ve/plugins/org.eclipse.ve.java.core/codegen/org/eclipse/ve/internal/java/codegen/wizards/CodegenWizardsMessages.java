/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodegenWizardsMessages.java,v $
 *  $Revision: 1.2 $  $Date: 2005-04-13 00:11:31 $ 
 */
package org.eclipse.ve.internal.java.codegen.wizards;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CodegenWizardsMessages {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.java.codegen.wizards.messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private CodegenWizardsMessages() {
	}

	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getFormattedString(String key, Object arg) {
		return MessageFormat.format(getString(key),new Object[] {arg});
	}

	public static String getFormattedString(String key, Object arg1, Object arg2) {
		return MessageFormat.format(getString(key),new Object[] {arg1,arg2});
	}
}
