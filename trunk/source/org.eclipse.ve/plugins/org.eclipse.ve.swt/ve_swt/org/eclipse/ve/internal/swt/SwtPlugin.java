/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;

import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jem.util.logger.proxyrender.EclipseLogger;


/**
 * The main plugin class to be used in the desktop.
 */
public class SwtPlugin extends Plugin {
	
	public static String DELEGATE_CONTROL = "delegate_control";	 //$NON-NLS-1$
	public static final String VIEWPART_CLASSNAME = "org.eclipse.ui.part.ViewPart"; //$NON-NLS-1$
	public static final String CONCRETE_VIEWPART_CLASSNAME = "org.eclipse.ve.internal.jface.targetvm.ConcreteViewPart";	 //$NON-NLS-1$
	
	//The shared instance.
	private static SwtPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public SwtPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static SwtPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	private Logger logger;
	public Logger getLogger() {
		if (logger == null)
			logger = EclipseLogger.getEclipseLogger(this);
		return logger;
	}
	
	
}
