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
 *  $RCSfile: TabFolderMessages.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:54:57 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author sri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TabFolderMessages {

	private static final String BUNDLE_NAME = "org.eclipse.swt.widgets.beaninfo.tabfolder";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private TabFolderMessages() {
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