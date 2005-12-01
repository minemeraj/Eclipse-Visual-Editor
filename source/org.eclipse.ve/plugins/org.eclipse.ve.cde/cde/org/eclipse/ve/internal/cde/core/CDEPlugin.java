/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: CDEPlugin.java,v $
 *  $Revision: 1.18 $  $Date: 2005-12-01 20:19:41 $ 
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jem.util.logger.proxyrender.EclipseLogger;

/**
 * This is the top-level class of the Common Diagram Editor plugin
 */
public final class CDEPlugin extends AbstractUIPlugin {
	
	// Preference store keys
	public static final String SHOW_XML = "SHOW_XML"; //$NON-NLS-1$
	public static final String PREF_SHOW_OVERVIEW_KEY = "ShowOver"; //$NON-NLS-1$	

	public static String CUSTOMIZELAYOUTWINDOW_X = "CUSTOMIZELAYOUTWINDOW_X"; //$NON-NLS-1$
	public static String CUSTOMIZELAYOUTWINDOW_Y = "CUSTOMIZELAYOUTWINDOW_Y"; //$NON-NLS-1$	

	// Strings needed for the "Adapter by type" extension point.

	public static final String ADAPTER_BY_TYPE_ID = "adapter"; // Extension point id //$NON-NLS-1$
	public static final String ADAPTER_ELEMENT = "adapter"; // Adapter element name //$NON-NLS-1$
	public static final String DECORATOR_TYPE_CLASS = "typeclass"; // decorator type class key //$NON-NLS-1$
	public static final String ADAPTER_CLASS = "adapterclass"; // adapter class key		 //$NON-NLS-1$

	private static CDEPlugin CDE_PLUGIN = null;
	public static final String SHOW_GRID_WHEN_SELECTED = "SHOW_GRID_WHEN_SELECTED"; //$NON-NLS-1$	
	public static final String XY_GRID_SPACING = "XY_GRID_SPACING"; //$NON-NLS-1$	

	public CDEPlugin() {
		CDE_PLUGIN = this;
	}

	/**
	 * Accessor to return the plugin as the OCM plugin.
	 */
	public static CDEPlugin getPlugin() {
		return CDE_PLUGIN;
	}

	public String getPluginID() {
		return getBundle().getSymbolicName();
	}
	
	private Logger logger;
	
	/**
	 * Get the logger.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Logger getLogger() {
		if (logger == null)
			logger = EclipseLogger.getEclipseLogger(this);
		return logger;
	}

	/**
	 * Handle getting a class from the string. The format of the string is either:
	 *   a) pluginName/className - Find class within the plugin
	 *   b) className - Find class within EMFPlugin classpath.
	 * 
	 * @param pluginFormatedClassName
	 * @return
	 * @throws ClassNotFoundException
	 * 
	 * @since 1.0.0
	 */
	static public Class getClassFromString(String pluginFormatedClassName) throws ClassNotFoundException {
		return getClassFromString(null, pluginFormatedClassName);
	}
	
	/**
	 * Answers whether there is any initialization data in the string.
	 * @param pluginFormatedClassName
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static public boolean hasInitializationData(String pluginFormatedClassName) {
		return pluginFormatedClassName.indexOf(':') != -1;
	}

	/**
	 * Handle getting a class from the string. The format of the string is either:
	 *   a) pluginName/className - Find class within the plugin
	 *   b) className - Find class within the plugin classpath of the bundle passed in.
	 *
	 *   If of the form "className:data" or "pluginName/className:data" the ":data" portion will be stripped off.
	 * 
	 * @param bundle
	 * @param pluginFormatedClassName
	 * @return
	 * @throws ClassNotFoundException
	 * 
	 * @since 1.0.0
	 */
	static public Class getClassFromString(Bundle bundle, String pluginFormatedClassName) throws ClassNotFoundException {

		int slashNdx = pluginFormatedClassName.indexOf('/');	// Separator for bundle id
		int colonNdx = pluginFormatedClassName.indexOf(':', slashNdx + 1); // Separator for data
		String className = colonNdx == -1 ? pluginFormatedClassName : pluginFormatedClassName.substring(0, colonNdx);
		if (slashNdx != -1) {
			String pluginName = className.substring(0, slashNdx);
			className = className.substring(slashNdx + 1);
			Bundle abundle = Platform.getBundle(pluginName);
			if (abundle != null) {
				return abundle.loadClass(className);
			}
		}

		// No plugin name, use plugin classpath of passed in bundle.
		return (bundle == null ? CDE_PLUGIN.getBundle() : bundle).loadClass(className);
	}

	/**
	 * This creates an instance of the class from a classname. If the class is an instanceof IExecutableExtension
	 * then the init data, if any, will be passed as a string to it. The config element and attribute name
	 * will be sent in as nulls because this isn't be created from a configuraton element. The default ctor
	 * will be used to instantiate it.
	 * <p>
	 * This is used for things like CellValidatorClassname to allow passing in additional information. For example
	 * the minmax validator can have "min,max" passed in as initialization data to configure the validator for this.
	 * 
	 * @param bundle
	 * @param pluginFormatedClassName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws CoreException
	 * 
	 * @since 1.0.0
	 */
	static public Object createInstance(Bundle bundle, String pluginFormatedClassName)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException, CoreException {
		Class c = getClassFromString(bundle, pluginFormatedClassName);
		Object o = c.newInstance();
		setInitializationData(o, pluginFormatedClassName, null);
		return o;
	}

	/**
	 * Set initialization data on the object if it is IExecutableExtension. If the className is
	 * passed in, it will look for the first ':' and use the string after that as the data.
	 * If initdata is passed in, it will be used in place of the data from the className.
	 * If there is no initdata in the className and the initData is null, then null is used as the class data.
	 * @return the object sent in. This allows <code>return setInitializationData(o, cn, initdata);</code> so that
	 * you can take the object, send it in, and then return it.
	 */
	static public Object setInitializationData(Object o, String className, Object initData) throws CoreException {
		if (o instanceof IExecutableExtension) {
			int colonNdx = className.indexOf(':');
			((IExecutableExtension) o).setInitializationData(
				null,
				null,
				initData == null ? (colonNdx == -1 ? null : className.substring(colonNdx + 1)) : initData);
		}
		return o;
	}
	
	/**
	 * This is used to parse the init data for the key. This is because we don't have the capablity
	 * of creating Maps for the initdata since it comes from just a string, so instead we have the
	 * initdata capable of being like a map. It will be a series of key/value pairs. In the form
	 * <code>key="value";key='value'</code>. The "key" cannot contain a ';' or '='. The value
	 * cannot contain a "'". We will not be doing a fancy parse that allows escaped single-quotes.
	 * This is used for parameterization data, which doesn't usually require single-quotes.
	 * <p>
	 * Though to allow us to work with a map too, if the initData is a Map, then it will look in the map instead.
	 *  
	 * @param initData data or <code>null</code> if no data. If must be either a Map or a String or <code>null</code>.
	 * @param key
	 * @return value or <code>null</code> if the key is not in the data, or the data is <code>null</code>, or the data is not valid. 
	 * 
	 * @since 1.1.0.1
	 */
	public static String parseInitializationData(Object initData, String key) {
		if (initData instanceof CharSequence) {
			Pattern p = Pattern.compile("\\s*"+key+"='(.*)';*");
			java.util.regex.Matcher m = p.matcher((CharSequence) initData);
			if (m.find()) {
				return m.group(1);
			} else
				return null;
		} else if (initData instanceof Map) {
			return (String) ((Map) initData).get(key);
		} else
			return null;
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

	/**
	 * Get image from the bundle
	 * @param bundle
	 * @param file
	 * @return the image. Callers must dispose of image when no longer needed.
	 * 
	 * @since 1.0.0
	 */
	static public Image getImageFromBundle(Bundle bundle, String file) {
		URL url = Platform.find(bundle, new Path(file));
		if (url != null)
			return ImageDescriptor.createFromURL(url).createImage();
		else
			return ImageDescriptor.getMissingImageDescriptor().createImage();		
	}
	
	/**
	 * Create an Image from a plugin. 
	 * @param plugin
	 * @param file
	 * @return the image. It is caller's responsibility to dispose of this image when not needed.
	 * 
	 * @since 1.0.0
	 */
	static public Image getImageFromPlugin(Plugin plugin, String file) {
		return getImageFromBundle(plugin.getBundle(), file);
	}

	/**
	 * Return image descriptor from a file in the plugin.
	 * @param plugin
	 * @param file
	 * @return
	 * 
	 * @since 1.0.0
	 */
	static public ImageDescriptor getImageDescriptorFromPlugin(Plugin plugin, String file) {
		URL url = Platform.find(plugin.getBundle(), new Path(file));
		if (url != null)
			return ImageDescriptor.createFromURL(url);
		else
			return ImageDescriptor.getMissingImageDescriptor();
	}
	
	protected void initializeDefaultPreferences(IPreferenceStore aStore) {
		aStore.setDefault(SHOW_GRID_WHEN_SELECTED,true);		
		aStore.setDefault(XY_GRID_SPACING, 15);		
		aStore.setDefault(PREF_SHOW_OVERVIEW_KEY, false);
		aStore.setDefault(SHOW_XML, false);
		aStore.setDefault(CUSTOMIZELAYOUTWINDOW_X, 0);
		aStore.setDefault(CUSTOMIZELAYOUTWINDOW_Y, 0);
	}
}
