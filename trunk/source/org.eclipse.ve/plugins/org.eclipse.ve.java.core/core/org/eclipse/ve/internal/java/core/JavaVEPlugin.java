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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jem.internal.core.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

public class JavaVEPlugin extends AbstractUIPlugin {

	public static final String PI_JBCF = "org.eclipse.ve.internal.java.core"; // Plugin ID, used for QualifiedName. //$NON-NLS-1$
	public static final String PI_JBCF_REGISTRATIONS = "registrations"; //$NON-NLS-1$
	// ID of the registrations extension point.

	public static final String PI_VARIABLE = "variable"; // <variable> in extension point. //$NON-NLS-1$
	public static final String PI_PATH = "path"; // <path="..."> in extension point.	 //$NON-NLS-1$
	public static final String PI_CONTRIBUTOR = "contributor"; //$NON-NLS-1$
	public static final String PI_PALETTECATS = "palettecats"; //$NON-NLS-1$
	public static final String PI_RUNTIME = "runtime"; //$NON-NLS-1$
	public static final String PI_SOURCE = "source"; //$NON-NLS-1$	
	public static final String PI_SOURCEPREFIX = "prefix"; //$NON-NLS-1$
	public static final String PI_EXTEND = "extend"; //$NON-NLS-1$			
	// <contributor ...> or contributor="..." in extension point

	private static JavaVEPlugin PLUGIN;
	private static MsgLogger msgLogger = null;
	public static Image fJavaBeanImage;
	public static Image fAppletImage;
	public static Image CHECK_IMAGE;

	// Map of registered variable contributors, mapped key is path, value is IConfigurationElement[].
	// It is allowed to have more than one. They will be concatenated together when used.
	private HashMap variableContributors = new HashMap();

	private static ImageDescriptor WIZARD_TITLE_DESC;

	public static final String SHOW_EVENTS = "SHOW_EVENTS"; // Plugin preferences key //$NON-NLS-1$
	public static final int EVENTS_NONE = 0; // Plugin preferences value
	public static final int EVENTS_BASIC = 1; // Plugin preferences value
	public static final int EVENTS_EXPERT = 2; // Plugin preferences value

	public JavaVEPlugin(IPluginDescriptor pd) {
		super(pd);
		PLUGIN = this;
	}

	public static JavaVEPlugin getPlugin() {
		return PLUGIN;
	}

	/*
	 * @see Plugin#startup()
	 */
	public void startup() throws CoreException {
		super.startup();

		processRegistrationExtensionPoint();
	}

	protected void processRegistrationExtensionPoint() {
		// Read in the registration information from the extensions.
		// We'll first gather together in Lists, and then send as arrays at one time to register them.
		HashMap registrations = new HashMap();
		IConfigurationElement[] configs = getDescriptor().getExtensionPoint(PI_JBCF_REGISTRATIONS).getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement iConfigurationElement = configs[i];
			if (PI_VARIABLE.equals(iConfigurationElement.getName())) {
				if (iConfigurationElement.getAttributeAsIs(PI_CONTRIBUTOR) != null
					|| iConfigurationElement.getChildren(PI_CONTRIBUTOR).length > 0
					|| iConfigurationElement.getAttributeAsIs(PI_PALETTECATS) != null) {
					String varpathstr = iConfigurationElement.getAttributeAsIs(PI_PATH);
					if (varpathstr == null)
						continue; // Not proper format.
					IPath varpath = new Path(varpathstr);
					List varentry = (List) registrations.get(varpath);
					if (varentry == null) {
						varentry = new ArrayList(1);
						registrations.put(varpath, varentry);
					}
					varentry.add(iConfigurationElement);
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

	/**
	 * Register one registration for the path.
	 * The path must be a classpath variable for the first segment. It won't be looked for otherwise.
	 * If it is only one segment long, then it is for the variable itself, and it will be used
	 * for all paths that start with that variable. This allows several different jars within 
	 * the variable's path to share the same registration information.
	 */
	public void registerRegistration(IPath path, IConfigurationElement registration) {
		IConfigurationElement[] registered = (IConfigurationElement[]) variableContributors.get(path);
		if (registered == null)
			registered = new IConfigurationElement[] { registration };
		else {
			IConfigurationElement[] old = registered;
			registered = new IConfigurationElement[old.length + 1];
			System.arraycopy(old, 0, registered, 0, old.length);
			registered[old.length] = registration;
		}

		variableContributors.put(path, registered);
	}

	/**
	 * Register multiple registrations for the path.
	 * The path must be a classpath variable for the first segment. It won't be looked for otherwise.
	 * If it is only one segment long, then it is for the variable itself, and it will be used
	 * for all paths that start with that variable. This allows several different jars within 
	 * the variable's path to share the same beaninfo registration information.
	 */
	public void registerRegistration(IPath path, IConfigurationElement[] registrations) {
		IConfigurationElement[] registered = (IConfigurationElement[]) variableContributors.get(path);
		if (registered == null) {
			registered = new IConfigurationElement[registrations.length];
			System.arraycopy(registrations, 0, registered, 0, registrations.length);
		} else {
			IConfigurationElement[] old = registered;
			registered = new IConfigurationElement[old.length + registrations.length];
			System.arraycopy(old, 0, registered, 0, old.length);
			System.arraycopy(registrations, 0, registered, old.length, registrations.length);
		}

		variableContributors.put(path, registered);
	}

	/**
	 * Return the registrations for a specified path. Return null if not registered.
	 */
	public IConfigurationElement[] getRegistrations(IPath path) {
		return (IConfigurationElement[]) variableContributors.get(path);
	}

	public MsgLogger getMsgLogger() {
		if (msgLogger == null)
			msgLogger = EclipseLogMsgLogger.createLogger(this);
		return msgLogger;
	}

	public static void log(Object obj) {
		getPlugin().getMsgLogger().log(obj);
	}

	public static void log(Object obj, int level) {
		getPlugin().getMsgLogger().log(obj, level);
	}

	public static void log(Throwable e) {
		getPlugin().getMsgLogger().log(e);
	}

	public static void log(Throwable e, int level) {
		getPlugin().getMsgLogger().log(e, level);
	}

	public static void log(IStatus status) {
		getPlugin().getMsgLogger().log(status);
	}

	public static void log(IStatus status, int logLevel) {
		getPlugin().getMsgLogger().log(status, logLevel);
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

}