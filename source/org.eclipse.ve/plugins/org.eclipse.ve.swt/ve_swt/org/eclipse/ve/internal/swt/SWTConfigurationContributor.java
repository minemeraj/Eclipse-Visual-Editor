/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SWTConfigurationContributor.java,v $
 *  $Revision: 1.11 $  $Date: 2005-02-15 23:51:47 $ 
 */
package org.eclipse.ve.internal.swt;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.core.resources.*;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;

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
	
	protected IJavaProject javaProject;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#initialize(org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo)
	 */
	public void initialize(IConfigurationContributionInfo info) {
		super.initialize(info);
		this.javaProject = info.getJavaProject();
	}
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

	public static final String SWT_BUILD_PATH_MARKER = "org.eclipse.ve.swt.buildpath";
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry registry) {
		// TODO the problem with this here is that it is hard-coded REM stuff. Need a better way to do this.
		SWTREMProxyRegistration.initialize(registry);	// Set the registry up with SWT REM stuff.
		
		// [70275] Need a marker if VM is less than 1.4.2 because of a bug with beaninfo.
		if (javaProject != null) {
			boolean versOk = true;	// Default is true, and if for some reason can't parse the version, then it will still be true because we don't know.
			IBeanProxy version = registry.getMethodProxyFactory().getInvokable("java.lang.System", "getProperty", new String[] {"java.lang.String"}).invokeCatchThrowableExceptions(null, registry.getBeanProxyFactory().createBeanProxyWith("java.version"));
			if (version instanceof IStringBeanProxy) {
				// We got the version
				StringTokenizer versTokens = new StringTokenizer(((IStringBeanProxy) version).stringValue(), "._");
				if (versTokens.hasMoreTokens()) {
					try {
						Integer v = Integer.valueOf(versTokens.nextToken());
						if (v.intValue() == 1 && versTokens.hasMoreTokens()) {
							Integer r = Integer.valueOf(versTokens.nextToken());
							if (r.intValue() < 4)
								versOk = false; // Can't support 1.3 on SWT.
							else if (r.intValue() == 4) {
								if (versTokens.hasMoreTokens()) {
									// Need to have mod 2 or greater.
									Integer m = Integer.valueOf(versTokens.nextToken());
									if (m.intValue() < 2)
										versOk = false; // Not 1.4.2 or greater.
								} else
									versOk = false; // Just 1.4, probably shouldn't get this, but be safe, this is not good.
							}
						}
					} catch (NumberFormatException e) {
					}
				}
			}
			final boolean fversok = versOk;
			try {
				ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				
					/* (non-Javadoc)
					* @see org.eclipse.core.resources.IWorkspaceRunnable#run(org.eclipse.core.runtime.IProgressMonitor)
					*/
					public void run(IProgressMonitor monitor) throws CoreException {
						if (fversok) {
							try {
								// It is ok, remove any outstanding marker. We are doing one kind, so if any there it is gone. 
								IMarker[] markers = javaProject.getProject().findMarkers(SWT_BUILD_PATH_MARKER, false, IResource.DEPTH_ZERO);
								for (int i = 0; i < markers.length; i++) {
									markers[i].delete();
								}
							} catch (CoreException e) {
								SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
							}			
						} else {
							try {
								// It is bad, if there is a marker of this type, just leave it there, it 
								// is already for this message. else add in the new marker. 
								IMarker[] markers = javaProject.getProject().findMarkers(SWT_BUILD_PATH_MARKER, false, IResource.DEPTH_ZERO);
								if (markers.length == 1) 
									;	// Do nothing we have it already
								else {
									if (markers.length > 1) {
										// We have more than one, not valid, so get rid of them.
										for (int i = 0; i < markers.length; i++) {
											markers[i].delete();
										}
									}
									IMarker marker = javaProject.getProject().createMarker(SWT_BUILD_PATH_MARKER);
									marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
									marker.setAttribute(IMarker.MESSAGE, SWTMessages.getString("Marker.BuildPathNot142"));
								}
							} catch (CoreException e) {
								SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
							}				
						}
					}
				}, null, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
			} catch (CoreException e) {
				SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
			}
		}
	}
}
