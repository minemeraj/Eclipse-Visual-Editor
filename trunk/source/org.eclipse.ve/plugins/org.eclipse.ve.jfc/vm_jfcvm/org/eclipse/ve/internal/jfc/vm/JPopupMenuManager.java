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
package org.eclipse.ve.internal.jfc.vm;
/*
 *  $RCSfile: JPopupMenuManager.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:13 $ 
 */

import javax.swing.JPopupMenu;

/**
 * This is the manager for a javax.swing.JPopupMenu. 
 * It handles removing actions and strings; and hides/shows JPopupMenu's again
 * in order to force the popup to pack and resize when new components
 * are added or removed.
 * 
 * Note: It is static. No need to have one for each popup menu... it will always be passed in.
 */
public class JPopupMenuManager {

	/**
	 * Hide and show the popup menu in order to resize it based on it's children.
	 */
	public static void revalidate(JPopupMenu popup) {
		if (popup != null) {
			popup.setVisible(false);
			popup.setVisible(true);
		}
	}

}
