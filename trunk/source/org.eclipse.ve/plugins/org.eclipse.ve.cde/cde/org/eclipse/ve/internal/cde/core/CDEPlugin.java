package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: CDEPlugin.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * This is the top-level class of the Common Diagram Editor plugin
 */
public final class CDEPlugin extends AbstractUIPlugin {
	
	// Preference store keys
	public static final String SHOW_XML = "SHOW_XML"; //$NON-NLS-1$
	public static final String PREF_SHOW_OVERVIEW_KEY = "ShowOver"; //$NON-NLS-1$	

	public static String ALIGNMENTWINDOW_X = "ALIGNMENTWINDOW_X"; //$NON-NLS-1$
	public static String ALIGNMENTWINDOW_Y = "ALIGNMENTWINDOW_Y"; //$NON-NLS-1$	

	// Strings needed for the "Adapter by type" extension point.

	public static final String ADAPTER_BY_TYPE_ID = "adapter"; // Extension point id //$NON-NLS-1$
	public static final String ADAPTER_ELEMENT = "adapter"; // Adapter element name //$NON-NLS-1$
	public static final String DECORATOR_TYPE_CLASS = "typeclass"; // decorator type class key //$NON-NLS-1$
	public static final String ADAPTER_CLASS = "adapterclass"; // adapter class key		 //$NON-NLS-1$

	private static CDEPlugin CDE_PLUGIN = null;

	public CDEPlugin(IPluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
		CDE_PLUGIN = this;
	}

	/**
	 * Accessor to return the plugin as the OCM plugin.
	 */
	public static CDEPlugin getPlugin() {
		return CDE_PLUGIN;
	}

	public String getPluginID() {
		return getDescriptor().getUniqueIdentifier();
	}

	/**
	 * Handle getting a class from the string. The format of the string is either:
	 *   a) pluginName/className - Find class within the plugin
	 *   b) className - Find class within EMFPlugin classpath.
	 * Note:
	 * Will throw ClassNotFoundException.
	 */
	static public Class getClassFromString(String pluginFormatedClassName) throws ClassNotFoundException {
		return getClassFromString(null, pluginFormatedClassName);
	}

	/**
	 * Handle getting a class from the string. The format of the string is either:
	 *   a) pluginName/className - Find class within the plugin
	 *   b) className - Find class within the plugin classpath of the plugin descriptor passed in.
	 *
	 *   If of the form "className:data" or "pluginName/className:data" the ":data" portion will be stripped off.
	 * Note:
	 * Will throw ClassNotFoundException.
	 */
	static public Class getClassFromString(IPluginDescriptor descriptor, String pluginFormatedClassName) throws ClassNotFoundException {

		int slashNdx = pluginFormatedClassName.indexOf('/');
		int colonNdx = pluginFormatedClassName.indexOf(':', slashNdx + 1);
		String className = colonNdx == -1 ? pluginFormatedClassName : pluginFormatedClassName.substring(0, colonNdx);
		// Strip off data portion
		if (slashNdx != -1) {
			String pluginName = className.substring(0, slashNdx);
			className = className.substring(slashNdx + 1);
			IPluginRegistry registry = Platform.getPluginRegistry();
			if (registry.getPluginDescriptor(pluginName) != null) {
				ClassLoader classLoader = registry.getPluginDescriptor(pluginName).getPluginClassLoader();
				return classLoader.loadClass(className);
			}
		}

		// No plugin name, use plugin classpath of passed in descriptor.
		return (descriptor == null ? CDE_PLUGIN.getDescriptor() : descriptor).getPluginClassLoader().loadClass(className);
	}

	/**
	 * This creates an instance of the class from a classname. If the class is an instanceof IExecutableExtension
	 * then the init data, if any, will be passed as a string to it. The config element and attribute name
	 * will be sent in as nulls because this isn't be created from a configuraton element. The default ctor
	 * will be used to instantiate it.
	 *
	 * This is used for things like CellValidatorClassname to allow passing in additional information. For example
	 * the minmax validator can have "min,max" passed in as initialization data to configure the validator for this.
	 */
	static public Object createInstance(IPluginDescriptor descriptor, String pluginFormatedClassName)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException, CoreException {
		Class c = getClassFromString(descriptor, pluginFormatedClassName);
		Object o = c.newInstance();
		setInitializationData(o, pluginFormatedClassName, null);
		return o;
	}

	/**
	 * Set initialization data on the object if it is IExecutableExtension. If the className is
	 * passed in, it will look for the first ':' and use the string after that as the data.
	 * If initdata is passed in, it will be used in place of the data from the className.
	 * If there is no initdata in the className and the initData is null, then null is used as the class data.
	 */
	static public void setInitializationData(Object o, String className, Object initData) throws CoreException {
		if (o instanceof IExecutableExtension) {
			int colonNdx = className.indexOf(':');
			((IExecutableExtension) o).setInitializationData(
				null,
				null,
				initData == null ? (colonNdx == -1 ? null : className.substring(colonNdx + 1)) : initData);
		}
	}

	/**
	 * Handle getting an image from a string that represents a URL. I.E. A url
	 * will be made of the string and then the image accessed from that.
	 *
	 * NOTE: This method just creates it. It is the responsibility of the
	 * caller to dispose of the image when it is no longer needed.
	 */
	static public Image getImageFromURLString(String url) {
		try {
			return ImageDescriptor.createFromURL(new URL(url)).createImage();
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor().createImage();
		}
	}

	static public ImageDescriptor getImageDescriptorFromURLString(String url) {
		try {
			return ImageDescriptor.createFromURL(new URL(url));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	static public Image getImageFromPlugin(Plugin plugin, String file) {
		try {
			return ImageDescriptor.createFromURL(new URL(plugin.getDescriptor().getInstallURL(), file)).createImage();
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor().createImage();
		}
	}

	static public ImageDescriptor getImageDescriptorFromPlugin(Plugin plugin, String file) {
		try {
			return ImageDescriptor.createFromURL(new URL(plugin.getDescriptor().getInstallURL(), file));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#initializeDefaultPluginPreferences()
	 */
	protected void initializeDefaultPluginPreferences() {
		Preferences prefs = getPluginPreferences();
		prefs.setDefault(PREF_SHOW_OVERVIEW_KEY, false);
		prefs.setDefault(SHOW_XML, false);
		prefs.setDefault(ALIGNMENTWINDOW_X, 0);
		prefs.setDefault(ALIGNMENTWINDOW_Y, 0);
	}

}
