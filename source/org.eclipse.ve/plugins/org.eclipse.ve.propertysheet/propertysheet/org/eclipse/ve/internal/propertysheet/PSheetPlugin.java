/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PSheetPlugin.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-02 15:57:15 $ 
 */
package org.eclipse.ve.internal.propertysheet;

import org.eclipse.core.runtime.Plugin;

/**
 * Plugin class for the property sheet.
 */
public class PSheetPlugin extends Plugin {

	private static PSheetPlugin sPlugin;

	public PSheetPlugin() {
		sPlugin = this;
	}

	public static PSheetPlugin getPlugin() {
		return sPlugin;
	}
}