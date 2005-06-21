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
 *  $RCSfile: JFCCodegenMessages.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-21 22:28:36 $ 
 */
package org.eclipse.ve.internal.jfc.codegen;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class JFCCodegenMessages {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.jfc.codegen.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private JFCCodegenMessages() {
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
