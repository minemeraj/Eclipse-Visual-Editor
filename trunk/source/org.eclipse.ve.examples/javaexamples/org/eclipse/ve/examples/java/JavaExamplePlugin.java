package org.eclipse.ve.examples.java;
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
 *  $RCSfile: JavaExamplePlugin.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:42:31 $ 
 */

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Plugin;

public class JavaExamplePlugin extends Plugin {

	private static JavaExamplePlugin PLUGIN;

	public JavaExamplePlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		PLUGIN = this;
	}

	public static JavaExamplePlugin getPlugin() {
		return PLUGIN;
	}

}