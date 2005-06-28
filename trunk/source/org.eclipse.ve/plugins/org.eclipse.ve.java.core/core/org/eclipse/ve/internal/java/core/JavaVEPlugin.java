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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: JavaVEPlugin.java,v $
 *  $Revision: 1.33 $  $Date: 2005-06-28 20:13:15 $ 
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jem.util.logger.proxyrender.EclipseLogger;
import org.eclipse.jem.util.plugin.JEMUtilPlugin;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.vce.VCEPreferences;


public class JavaVEPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.ve.java.core"; //$NON-NLS-1$
	
	public static final String PI_JBCF_REGISTRATIONS = "registrations"; //$NON-NLS-1$
	public static final String PI_CONTRIBUTION_EXTENSION_POINT = "org.eclipse.ve.java.core.contributors"; //$NON-NLS-1$
		
	public static final IPath VE_CACHE_ROOT_NAME = new Path(".cache"); //$NON-NLS-1$
	public static final IPath  VE_PLUGIN_CACHE_DESTINATION = Platform.getStateLocation(Platform.getBundle(PLUGIN_ID)).append(VE_CACHE_ROOT_NAME);  
	public static final IPath  VE_GENERATED_OBJECTs_DESTINATION = VE_PLUGIN_CACHE_DESTINATION.append("javajetObjects"); //$NON-NLS-1$ 
	public static final IPath  VE_GENERATED_LIBRARIES_CACHE = VE_PLUGIN_CACHE_DESTINATION.append(".libCache"); //$NON-NLS-1$
	// ID of the registrations extension point.

	public static final String TRANSFER_HEADER = "{ *** VE HEADER ***}";	 //$NON-NLS-1$
	public static final String PI_DESCRIPTION = "description";	 //$NON-NLS-1$
	public static final String PI_LIBRARY = "library"; // <library> in extension point. //$NON-NLS-1$	
	public static final String PI_CONTAINER = "container"; // <container> in extension point. //$NON-NLS-1$	
	public static final String PI_PATH = "path"; // <path="..."> in extension point.	 //$NON-NLS-1$
	
	public static final String PI_CONTRIBUTOR = "contributor"; //$NON-NLS-1$
	public static final String PI_PALETTECATS = "palettecats"; //$NON-NLS-1$
	public static final String PI_RUNTIME = "runtime"; //$NON-NLS-1$
	public static final String PI_SOURCE = "source"; //$NON-NLS-1$	
	public static final String PI_SOURCEPREFIX = "prefix"; //$NON-NLS-1$	
	
	public static final URL URL_HOMEPAGE;
	public static final URL URL_NEWSGROUP;
	
	static {
		URL temp = null;
		try {
			temp = new URL("http://www.eclipse.org/vep"); //$NON-NLS-1$
		} catch (MalformedURLException e) {
		}
		URL_HOMEPAGE = temp;
		temp = null;
		try {
			temp = new URL("news://news.eclipse.org/eclipse.tools.ve");	 //$NON-NLS-1$
		} catch (MalformedURLException e) {
		}
		URL_NEWSGROUP = temp;
	}
	// <contributor ...> or contributor="..." in extension point

	private static JavaVEPlugin PLUGIN;
	private static Logger logger = null;
	public static Image fJavaBeanImage;
	public static Image fAppletImage;
	public static Image CHECK_IMAGE;
	private static ImageDescriptor WIZARD_TITLE_DESC;

	public static final String SHOW_EVENTS = "SHOW_EVENTS"; // Plugin preferences key //$NON-NLS-1$
	public static final int EVENTS_NONE = 0; // Plugin preferences value
	public static final int EVENTS_BASIC = 1; // Plugin preferences value
	public static final int EVENTS_EXPERT = 2; // Plugin preferences value

	public JavaVEPlugin() {
		PLUGIN = this;
	}

	public static JavaVEPlugin getPlugin() {
		return PLUGIN;
	}

	
	public Logger getLogger() {
		if (logger == null)
			logger = EclipseLogger.getEclipseLogger(this);
		return logger;
	}

	public static boolean isLoggingLevel(Level level) {
		return getPlugin().getLogger().isLoggingLevel(level);
	}
	
	public static void log(Object obj) {
		getPlugin().getLogger().log(obj);
	}

	public static void log(Object obj, Level level) {
		getPlugin().getLogger().log(obj, level);
	}

	public static void log(Throwable e) {
		getPlugin().getLogger().log(e);
	}

	public static void log(Throwable e, Level level) {
		getPlugin().getLogger().log(e, level);
	}

	public static void log(IStatus status) {
		getPlugin().getLogger().log(status);
	}

	public static void log(IStatus status, Level logLevel) {
		getPlugin().getLogger().log(status, logLevel);
	}

	public static Image getJavaBeanImage() {
		if (fJavaBeanImage == null) {
			fJavaBeanImage = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/javabean.gif"); //$NON-NLS-1$
		}
		return fJavaBeanImage;
	}
	public static Image getAppletImage() {
		if (fAppletImage == null) {
			fAppletImage = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/applet.gif"); //$NON-NLS-1$
		}
		return fAppletImage;
	}

	public static ImageDescriptor getWizardTitleImageDescriptor() {
		if (WIZARD_TITLE_DESC == null) {
			WIZARD_TITLE_DESC = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/wizban/javabean_wiz.gif"); //$NON-NLS-1$
		}
		return WIZARD_TITLE_DESC;
	}

	public static Image getCheckImage() {
		if (CHECK_IMAGE == null) {
			CHECK_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/booleantrue.gif"); //$NON-NLS-1$
		}
		return CHECK_IMAGE;
	}

	public void start(BundleContext bc) throws Exception {
		super.start(bc);
		getPluginPreferences().setDefault(SHOW_EVENTS, EVENTS_BASIC);
		VCEPreferences.initializeDefaultPluginPreferences(getPluginPreferences());
		JEMUtilPlugin.addCleanResourceChangeListener(cleanResourceListener = new JEMUtilPlugin.CleanResourceChangeListener() {
		
			protected void cleanProject(IProject project) {
			}
		
			protected void cleanAll() {
				// Clean Jet Directory.
				JEMUtilPlugin.deleteDirectoryContent(VE_GENERATED_OBJECTs_DESTINATION.toFile(), true, new NullProgressMonitor());
				// Clean extracted libraries directory
				// TODO This will go away from here when we move this stuff to base JEM.
				JEMUtilPlugin.deleteDirectoryContent(VE_GENERATED_LIBRARIES_CACHE.toFile(), true, new NullProgressMonitor());
			}
		
		}, 0);
	}
	
	private JEMUtilPlugin.CleanResourceChangeListener cleanResourceListener;
	
	/**
	 * Called by JavaVmController. It is public only because this class is in another package.
	 * It is not meant to be called by any other classes, either internal or customers.
	 * 
	 * @param disposer when java vm controller is active it will give the plugin a dispose runnable to call.
	 * @since 1.0.0
	 */
	public void setJavaVMControllerDisposer(Runnable disposer) {
		javaVMControllerDisposer = disposer;
	}
	
	/*
	 * If the javaVMController ever started, then we will be given a runnable to call on shutdown.
	 */
	private Runnable javaVMControllerDisposer;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (javaVMControllerDisposer != null) {
			javaVMControllerDisposer.run();
			javaVMControllerDisposer = null;
		}
		if (cleanResourceListener != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(cleanResourceListener);
			cleanResourceListener = null;
		}
		super.stop(context);
	}
	
	/*
	 * Map of container id's to their ordered array of contribution config elements.
	 */
	protected Map containerToContributions = null;
	/*
	 * Map of plugin id's to their ordered array of contribution config elements.
	 */
	protected Map pluginToContributions = null;
	
	/**
	 * Return the plugin ordered array of configuration elements for the given container, or <code>null</code> if not contributed.
	 * 
	 * @param containerid
	 * @return Array of configuration elements or <code>null</code> if this container has no contributions.
	 * 
	 * @since 1.0.0
	 */
	public synchronized IConfigurationElement[] getContainerConfigurations(String containerid) {
		if (containerToContributions == null)
			processProxyContributionExtensionPoint();
		return (IConfigurationElement[]) containerToContributions.get(containerid);
	}

	/**
	 * Return the plugin ordered array of configuration elements for the given plugin, or <code>null</code> if not contributed.
	 * 
	 * @param pluginid
	 * @return Array of configuration elements or <code>null</code> if this plugin has no contributions.
	 * 
	 * @since 1.0.0
	 */
	public synchronized IConfigurationElement[] getPluginConfigurations(String pluginid) {
		if (pluginToContributions == null)
			processProxyContributionExtensionPoint();
		return (IConfigurationElement[]) pluginToContributions.get(pluginid);
	}
	
	protected synchronized void processProxyContributionExtensionPoint() {
		ProxyPlugin.ContributorExtensionPointInfo info = ProxyPlugin.processContributionExtensionPoint(PI_CONTRIBUTION_EXTENSION_POINT);
		containerToContributions = info.containerToContributions;
		pluginToContributions = info.pluginToContributions;
	}

}
