package org.eclipse.ve.internal.swt;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.*;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.swt.SWTREMProxyRegistration;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class SWTContainer implements IClasspathContainer, IConfigurationContributor {
	
	private IClasspathEntry[] fClasspathEntries;
	private IPath containerPath;	// path for container, NOT path for resolved entry
	
	public SWTContainer(IPath containerPath){
		this.containerPath = containerPath;
		// This is a container that resolves to the path of whenever SWT is installed in the workbench
		Path path = new Path("$ws$/swt.jar");
		URL location = Platform.getPlugin("org.eclipse.swt").find(path);
		try {
			path = new Path(Platform.resolve(location).getFile());
			fClasspathEntries =
				new IClasspathEntry[] {JavaCore.newLibraryEntry(path, null, null)};
		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
	}

	public IClasspathEntry[] getClasspathEntries() {
		return fClasspathEntries;
	}

	public String getDescription() {
		return "Standard Widget Toolkit (SWT)";
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return containerPath;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeClasspaths(org.eclipse.jem.internal.proxy.core.IConfigurationContributionController)
	 */
	public void contributeClasspaths(IConfigurationContributionController controller) throws CoreException {
		// Add the jar file with the supporting classes required for the JVE into the classpath
		// In development model the file proxy.jars redirects this to the plugin development project
		controller.contributeClasspath(SwtPlugin.getDefault(), "jbcfswtvm.jar", IConfigurationContributionController.APPEND_USER_CLASSPATH, false); //$NON-NLS-1$
		
		// Get the location of the swt dll in the workbench path and add it.
		// we're assuming they are all under the same path, ie. some under os while others unders os/arch is not valid for us. current swt looks like all under one directory.
		controller.contributeClasspath(Platform.getPlugin("org.eclipse.swt"), "$os$", IConfigurationContributionController.APPEND_JAVA_LIBRARY_PATH, false);
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
