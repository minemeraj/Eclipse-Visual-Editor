/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PSheetPlugin.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:47:33 $ 
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
