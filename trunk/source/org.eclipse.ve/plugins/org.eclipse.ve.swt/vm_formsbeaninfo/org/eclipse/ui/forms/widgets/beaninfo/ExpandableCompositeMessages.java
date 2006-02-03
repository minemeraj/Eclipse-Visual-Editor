/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ExpandableCompositeMessages.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-03 15:22:02 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @since 1.0.0
 */
public class ExpandableCompositeMessages {

	private static final String BUNDLE_NAME = "org.eclipse.ui.forms.widgets.beaninfo.expandablecomposite";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private ExpandableCompositeMessages() {
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
