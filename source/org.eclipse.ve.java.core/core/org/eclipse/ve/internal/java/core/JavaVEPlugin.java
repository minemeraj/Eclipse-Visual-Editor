package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: JavaVEPlugin.java,v $
 *  $Revision: 1.6 $  $Date: 2004-03-22 23:49:37 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import com.ibm.wtp.common.logger.proxy.Logger;
import com.ibm.wtp.logger.proxyrender.EclipseLogger;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.vce.VCEPreferences;

public class JavaVEPlugin extends AbstractUIPlugin {

	public static final String PI_JBCF = "org.eclipse.ve.internal.java.core"; // Plugin ID, used for QualifiedName. //$NON-NLS-1$
	public static final String PI_JBCF_REGISTRATIONS = "registrations"; //$NON-NLS-1$
	public static final String PI_CONTRIBUTION_EXTENSION_POINT = "org.eclipse.ve.java.core.contributors";
	// ID of the registrations extension point.

	public static final String PI_VARIABLE = "variable"; // <variable> in extension point. //$NON-NLS-1$
	public static final String PI_LIBRARY = "library"; // <library> in extension point. //$NON-NLS-1$	
	public static final String PI_CONTAINER = "container"; // <container> in extension point. //$NON-NLS-1$	
	public static final String PI_PATH = "path"; // <path="..."> in extension point.	 //$NON-NLS-1$
	public static final String PI_CONTRIBUTOR = "contributor"; //$NON-NLS-1$
	public static final String PI_PALETTECATS = "palettecats"; //$NON-NLS-1$
	public static final String PI_RUNTIME = "runtime"; //$NON-NLS-1$
	public static final String PI_SOURCE = "source"; //$NON-NLS-1$	
	public static final String PI_SOURCEPREFIX = "prefix"; //$NON-NLS-1$
	public static final String PI_EXTEND = "extend"; //$NON-NLS-1$			
	// <contributor ...> or contributor="..." in extension point

	private static JavaVEPlugin PLUGIN;
	private static Logger logger = null;
	public static Image fJavaBeanImage;
	public static Image fAppletImage;
	public static Image CHECK_IMAGE;

	// Map of registered variable contributors, mapped key is path, value is IConfigurationElement[].
	// It is allowed to have more than one. They will be concatenated together when used.
	private HashMap variableContributors;

	private static ImageDescriptor WIZARD_TITLE_DESC;

	public static final String SHOW_EVENTS = "SHOW_EVENTS"; // Plugin preferences key //$NON-NLS-1$
	public static final int EVENTS_NONE = 0; // Plugin preferences value
	public static final int EVENTS_BASIC = 1; // Plugin preferences value
	public static final int EVENTS_EXPERT = 2; // Plugin preferences value

	public JavaVEPlugin(IPluginDescriptor pd) {
		super(pd);
		PLUGIN = this;
	}

	private Map getVariableContributors() {
		if (variableContributors == null) {
			variableContributors = new HashMap(30);
			processRegistrationExtensionPoint();
		}
		return variableContributors;
	}

	public static JavaVEPlugin getPlugin() {
		return PLUGIN;
	}

	protected void processRegistrationExtensionPoint() {
		// Read in the registration information from the extensions.
		// We'll first gather together in Lists, and then send as arrays at one time to register them.
		HashMap registrations = new HashMap();
		IConfigurationElement[] configs = getDescriptor().getExtensionPoint(PI_JBCF_REGISTRATIONS).getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement iConfigurationElement = configs[i];
			if (PI_VARIABLE.equals(iConfigurationElement.getName())) {
				processLibraryEntry(iConfigurationElement,registrations);
			}
			// This is format for allowing containers or library to be used by a plugin
			// <library 
			//     container="FOO_CONTAINER"  OR  library="FOO_LIB"
			//	   palettecats="platform:/plugin/org.eclipse.ve.swt/swtpalette.xmi"
 			//	   contributor="com.foo.FOOConfigurationContributor">
			// </library>
			if (PI_LIBRARY.equals(iConfigurationElement.getName())) {
				if(iConfigurationElement.getAttributeAsIs(PI_VARIABLE) != null){
					processLibraryEntry(iConfigurationElement,registrations);
				} else if(iConfigurationElement.getAttributeAsIs(PI_CONTAINER) != null){
					processContainerEntry(iConfigurationElement,registrations);
				}
			}			
		}

		// Now we've processed all of the extensions.
		Iterator regItr = registrations.entrySet().iterator();
		while (regItr.hasNext()) {
			Map.Entry entry = (Map.Entry) regItr.next();
			List registrationsList = (List) entry.getValue();
			registerRegistration(
				(IPath) entry.getKey(),
				(IConfigurationElement[]) registrationsList.toArray(new IConfigurationElement[registrationsList.size()]));
		}
	}
	private void processLibraryEntry(IConfigurationElement aConfigurationElement,Map registrations){
		if (aConfigurationElement.getAttributeAsIs(PI_CONTRIBUTOR) != null
			|| aConfigurationElement.getChildren(PI_CONTRIBUTOR).length > 0
			|| aConfigurationElement.getAttributeAsIs(PI_PALETTECATS) != null) {
			String varpathstr = aConfigurationElement.getAttributeAsIs(PI_PATH);
			if (varpathstr == null)
				return; // Not proper format.
			IPath varpath = new Path(varpathstr);
			List varentry = (List) registrations.get(varpath);
			if (varentry == null) {
				varentry = new ArrayList(1);
				registrations.put(varpath, varentry);
			}
			varentry.add(aConfigurationElement);
		}		
	}
/**	
 * Process the extension point for a container registration
 * Example syntax is 
 * <pre>
 * 	<extension point="org.eclipse.ve.java.core.registrations">
 *	  <library
 *		  container="SWT_CONTAINER"
 *		  palettecats="platform:/plugin/org.eclipse.ve.swt/swtpalette.xmi"
 *		  contributor="org.eclipse.ve.internal.swt.SWTConfigurationContributor">
 *	  </library>
 *	</extension>
 * </pre>
 **/
	private void processContainerEntry(IConfigurationElement aConfigurationElement,Map registrations){
		if (aConfigurationElement.getAttributeAsIs(PI_CONTRIBUTOR) != null
			|| aConfigurationElement.getChildren(PI_CONTRIBUTOR).length > 0
			|| aConfigurationElement.getAttributeAsIs(PI_PALETTECATS) != null) {
			String containerName = aConfigurationElement.getAttributeAsIs(PI_CONTAINER);
			if (containerName == null)
				return; // Not proper format.
			IPath containerpath = new Path(containerName);
			List containerentry = (List) registrations.get(containerpath);
			if (containerentry == null) {
				containerentry = new ArrayList(1);
				registrations.put(containerpath, containerentry);
			}
			containerentry.add(aConfigurationElement);
		}		
	}	
	/**
	 * Register one registration for the path.
	 * The path must be a classpath variable for the first segment. It won't be looked for otherwise.
	 * If it is only one segment long, then it is for the variable itself, and it will be used
	 * for all paths that start with that variable. This allows several different jars within 
	 * the variable's path to share the same registration information.
	 */
	public void registerRegistration(IPath path, IConfigurationElement registration) {
		IConfigurationElement[] registered = (IConfigurationElement[]) getVariableContributors().get(path);
		if (registered == null)
			registered = new IConfigurationElement[] { registration };
		else {
			IConfigurationElement[] old = registered;
			registered = new IConfigurationElement[old.length + 1];
			System.arraycopy(old, 0, registered, 0, old.length);
			registered[old.length] = registration;
		}

		getVariableContributors().put(path, registered);
	}

	/**
	 * Register multiple registrations for the path.
	 * The path must be a classpath variable for the first segment. It won't be looked for otherwise.
	 * If it is only one segment long, then it is for the variable itself, and it will be used
	 * for all paths that start with that variable. This allows several different jars within 
	 * the variable's path to share the same beaninfo registration information.
	 */
	public void registerRegistration(IPath path, IConfigurationElement[] registrations) {
		IConfigurationElement[] registered = (IConfigurationElement[]) getVariableContributors().get(path);
		if (registered == null) {
			registered = new IConfigurationElement[registrations.length];
			System.arraycopy(registrations, 0, registered, 0, registrations.length);
		} else {
			IConfigurationElement[] old = registered;
			registered = new IConfigurationElement[old.length + registrations.length];
			System.arraycopy(old, 0, registered, 0, old.length);
			System.arraycopy(registrations, 0, registered, old.length, registrations.length);
		}

		getVariableContributors().put(path, registered);
	}

	/**
	 * Return the registrations for a specified path. Return null if not registered.
	 */
	public IConfigurationElement[] getRegistrations(IPath path) {
		return (IConfigurationElement[]) getVariableContributors().get(path);
	}

	public Logger getLogger() {
		if (logger == null)
			logger = EclipseLogger.getEclipseLogger(this);
		return logger;
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

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#initializeDefaultPluginPreferences()
	 */
	protected void initializeDefaultPluginPreferences() {
		super.initializeDefaultPluginPreferences();
		getPluginPreferences().setDefault(SHOW_EVENTS, EVENTS_BASIC);
		VCEPreferences.initializeDefaultPluginPreferences(getPluginPreferences());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#shutdown()
	 */
	public void shutdown() throws CoreException {
		savePluginPreferences();
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
	public IConfigurationElement[] getContainerConfigurations(String containerid) {
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
	public IConfigurationElement[] getPluginConfigurations(String pluginid) {
		if (pluginToContributions == null)
			processProxyContributionExtensionPoint();
		return (IConfigurationElement[]) pluginToContributions.get(pluginid);
	}
	
	protected void processProxyContributionExtensionPoint() {
		ProxyPlugin.ContributorExtensionPointInfo info = ProxyPlugin.processContributionExtensionPoint(PI_CONTRIBUTION_EXTENSION_POINT);
		containerToContributions = info.containerToContributions;
		pluginToContributions = info.pluginToContributions;
	}
	

}