package org.eclipse.ve.internal.propertysheet;
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
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */


import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Plugin class for the property sheet.
 */
public class PSheetPlugin extends AbstractUIPlugin {
	private static PSheetPlugin sPlugin;
	
	public PSheetPlugin(IPluginDescriptor pd) {
		super(pd);
		sPlugin = this;
	}
	
	public static PSheetPlugin getPlugin() {
		return sPlugin;
	}	
}