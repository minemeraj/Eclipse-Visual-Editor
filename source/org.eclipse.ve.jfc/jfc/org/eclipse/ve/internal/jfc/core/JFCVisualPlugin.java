/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JFCVisualPlugin.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-02 15:57:29 $ 
 */
package org.eclipse.ve.internal.jfc.core;


import org.eclipse.core.runtime.Plugin;

public class JFCVisualPlugin extends Plugin {

	private static JFCVisualPlugin PLUGIN;


	public JFCVisualPlugin() {
		PLUGIN = this;
	}

	public static JFCVisualPlugin getPlugin() {
		return PLUGIN;
	}
	

}