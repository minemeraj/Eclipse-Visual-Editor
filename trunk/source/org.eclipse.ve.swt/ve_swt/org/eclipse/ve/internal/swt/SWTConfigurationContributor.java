/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTConfigurationContributor.java,v $
 *  $Revision: 1.9 $  $Date: 2004-07-30 15:20:00 $ 
 */
package org.eclipse.ve.internal.swt;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.swt.SWTREMProxyRegistration;



/**
 * SWT Configuration Contributor.
 * 
 * This is a stateless class, so the static can be used for any programatic usage of this.
 * 
 * @since 1.0.0
 */
public class SWTConfigurationContributor extends ConfigurationContributorAdapter {
	
	/**
	 * A singleton instance that can be used programtically.
	 */
	public static final SWTConfigurationContributor INSTANCE = new SWTConfigurationContributor();
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeClasspaths(org.eclipse.jem.internal.proxy.core.IConfigurationContributionController)
	 */
	public void contributeClasspaths(IConfigurationContributionController controller) throws CoreException {
		// Add the jar file with the supporting classes required for the JVE into the classpath
		// In development model the file proxy.jars redirects this to the plugin development project
		controller.contributeClasspath(SwtPlugin.getDefault().getBundle(), "jbcfswtvm.jar", IConfigurationContributionController.APPEND_USER_CLASSPATH, false); //$NON-NLS-1$
		
		// Get the location of the swt dll in the workbench path and add it.
		// we're assuming they are all under the same path, ie. some under os while others unders os/arch is not valid for us. current swt looks like all under one directory.
		controller.contributeClasspath(Platform.getBundle("org.eclipse.swt"), "$os$", IConfigurationContributionController.APPEND_JAVA_LIBRARY_PATH, false);
		
		// If GTK is the platform, then contribute the native library which does the offscreen screen-scrape
		if(Platform.WS_GTK.equals(Platform.getWS())){
			controller.contributeClasspath(Platform.getBundle("org.eclipse.ve.swt"), "$os$", IConfigurationContributionController.APPEND_JAVA_LIBRARY_PATH, false);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeToConfiguration(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void contributeToConfiguration(ILaunchConfigurationWorkingCopy config) throws CoreException {
		// For Linux/MOTIF we need to export some stuff using an environment variable
		// TODO This is not finished yet.  It should check to see that we're on Linux using
		// some kind of system property and also put the correct environment variables value
		if(false){
			// TODO Not quite sure what this should be. I'm guessing what it should be below
			// String environmentVariableToExportDisplay = "DISPLAY:0:0";
			Map map = config.getAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES, (Map) null);
			if(map == null) 
				map = new HashMap(1);
			map.put("DISPLAY", "DISPLAY:0:0");				
			config.setAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES, map);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry registry) {
		// TODO the problem with this here is that it is hard-coded REM stuff. Need a better way to do this.
		SWTREMProxyRegistration.initialize(registry);	// Set the registry up with SWT REM stuff.
	}
}
