/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
import org.eclipse.core.runtime.Preferences;

import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jem.util.logger.proxyrender.EclipseLogger;


/**
 * The main plugin class to be used in the desktop.
 */
public class SwtPlugin extends Plugin {
	
	public static String DELEGATE_CONTROL = "delegate_control";	 //$NON-NLS-1$
	public static final String VIEWPART_CLASSNAME = "org.eclipse.ui.part.ViewPart"; //$NON-NLS-1$
	public static final String CONCRETE_VIEWPART_CLASSNAME = "org.eclipse.ve.internal.jface.targetvm.ConcreteViewPart";	 //$NON-NLS-1$
	public static final String PLUGIN_ID = "org.eclipse.ve.swt"; //$NON-NLS-1$
	public static final String PARENT_COMPOSITE_TOKEN = "{parentComposite}"; // Token to represent the parentComposite in a parse tree that will be replaced by the true parent	 //$NON-NLS-1$
	public static final String FORM_TOOLKIT_CLASSNAME = "org.eclipse.ui.forms.widgets.FormToolkit"; //$NON-NLS-1$
	public static final String FORM_TOOLKIT_TOKEN = "{formToolkit}"; // Token to represent the formToolkit in a parse tree that will be replaced by a real formtoolkit //$NON-NLS-1$
	public static final String DISPLAY_CLASSNAME = "org.eclipse.swt.widgets.Display"; //$NON-NLS-1$	
	public static final String DEFAULT_LAYOUT = "DEFAULT_LAYOUT"; //$NON-NLS-1$
	public static final String DEFAULT_LAYOUT_VAUE = "org.eclipse.swt.layout.GridLayout";  //$NON-NLS-1$	
	public static final String PREFERENCE_PAGE_ID =  "org.eclipse.ve.internal.swt.SWTPreferencePage"; //$NON-NLS-1$	
	public static final String NULL_LAYOUT =  "null"; //$NON-NLS-1$	
	
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
	private String[][] layouts;
	public Logger getLogger() {
		if (logger == null)
			logger = EclipseLogger.getEclipseLogger(this);
		return logger;
	}
	
	public static void initializeDefaultPluginPreferences(Preferences aStore) {	
	}

	public String[][] getLayouts() {
		if(layouts == null){
			layouts = new String[][] {
			  new String[] {"null","GridLayout","FillLayout","RowLayout","FormLayout"},
			  new String[] {NULL_LAYOUT,"org.eclipse.swt.layout.GridLayout","org.eclipse.swt.layout.FillLayout","org.eclipse.swt.layout.RowLayout","org.eclipse.swt.layout.FormLayout"}
			};
		};
		return layouts;
	}
}