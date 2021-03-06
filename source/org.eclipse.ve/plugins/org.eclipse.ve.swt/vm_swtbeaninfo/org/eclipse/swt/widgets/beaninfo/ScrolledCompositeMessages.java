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
 *  $RCSfile: ScrolledCompositeMessages.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-03 21:16:44 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ScrolledCompositeMessages {

	private static final String BUNDLE_NAME = "org.eclipse.swt.widgets.beaninfo.scrolledcomposite";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private ScrolledCompositeMessages() {
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
