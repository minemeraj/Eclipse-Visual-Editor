/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:10 $ 
 */
package org.eclipse.ve.tests;

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
	public VETestsPlugin() {
		PLUGIN = this;
	}
	
	public static VETestsPlugin getPlugin() {
		return PLUGIN;
	}

}
