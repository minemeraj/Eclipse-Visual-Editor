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
 *  $RCSfile: ColorPropertyEditorMessages.java,v $
 *  $Revision: 1.1 $  $Date: 2005-04-01 19:49:30 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ColorPropertyEditorMessages {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.swt.colorpropertyeditor"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
