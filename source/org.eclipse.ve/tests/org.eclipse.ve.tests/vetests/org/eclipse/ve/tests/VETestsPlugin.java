package org.eclipse.ve.tests;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VETestsPlugin.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:38:46 $ 
 */
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Plugin;

/**
 * @author richkulp
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class VETestsPlugin extends Plugin {

	private static VETestsPlugin PLUGIN;
	/**
	 * @param descriptor
	 */
	public VETestsPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		PLUGIN = this;
	}
	
	public static VETestsPlugin getPlugin() {
		return PLUGIN;
	}

}
