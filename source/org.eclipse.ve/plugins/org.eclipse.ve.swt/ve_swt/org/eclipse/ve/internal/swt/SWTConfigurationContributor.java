package org.eclipse.ve.internal.swt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.swt.SWTREMProxyRegistration;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class SWTConfigurationContributor implements IConfigurationContributor {

	public void contributeClasspaths(List classpaths, IClasspathContributionController controller) throws CoreException {
				
		// Add the SWT jar to the classpath of the target VM
		// TODO when we figure out how to get dll path automatically added too for beaninfo, then we can make the
		// swtcontainer by an application type (as it should) so that it shows automatically in classpath and then
		// we can get rid of both of these.
  		IClasspathContainer swtContainer = JavaCore.getClasspathContainer(new Path("SWT_CONTAINER"), controller.getJavaProject());
		IClasspathEntry[] swtJarPath = swtContainer.getClasspathEntries();	// We know for there is only one.
		String[] swtJarLocation = new String[] {JavaCore.getResolvedClasspathEntry(swtJarPath[0]).getPath().toString()};
		controller.contributeClasspath(
			swtJarLocation,
			classpaths,
			-1);
				
		// Add the jar file with the supporting classes required for the JVE into the classpath
		// In development model the file proxy.jars redirects this to the plugin development project
		controller.contributeClasspath(
			ProxyPlugin.getPlugin().urlLocalizeFromPluginDescriptorAndFragments(SwtPlugin.getDefault().getDescriptor(), "jbcfswtvm.jar"), //$NON-NLS-1$
			classpaths,
			-1);
							
	}
	/**
	 * The program argument for the target VM must include the swt dll
	 * -Djava.library.path=FOLDERLOCATIONOFDLL
	 */
	public void contributeToConfiguration(VMRunnerConfiguration aConfig) {	

		// Get the location of the swt dll in the workbench path and add it
		// TODO This needs changing so if a build path project is being used then the
		Path path = new Path("$os$");	// we're assuming they are all under the same path, ie. some under os while others unders os/arch is not valid for us. current swt looks like all under one directory.
		URL localURL = Platform.getPlugin("org.eclipse.swt").find(path);
		if (localURL == null)
			return;	// can't find it.
		try {
			localURL = Platform.resolve(localURL);
		} catch (IOException e) {
			return;	// can't find it or resolve it locally.
		}
		String dllLocation = localURL.getFile();
		dllLocation = dllLocation.replace('/',java.io.File.separatorChar);		

		boolean foundit = false;		
		String[] existingVMArguments = aConfig.getVMArguments();
		for (int i = 0; i < existingVMArguments.length; i++) {
			if (existingVMArguments[i].startsWith("-Djava.library.path")) {
				existingVMArguments[i] = existingVMArguments[i]+File.pathSeparatorChar+dllLocation;
				foundit = true;
			}
		}
		
		if (!foundit) {
			// Create a new array of VM arguments so we can add in the two new entries
			String[] newVMArguments = new String[existingVMArguments.length + 1];
			System.arraycopy(existingVMArguments,0,newVMArguments,1,existingVMArguments.length);
			newVMArguments[0] = "-Djava.library.path="+dllLocation;
			aConfig.setVMArguments(newVMArguments);
		} else
			aConfig.setVMArguments(existingVMArguments);	// put them back because we modified one of them.
		
		
		// For Linux/MOTIF we need to export some stuff using an environment variable
		// TODO This is not finished yet.  It should check to see that we're on Linux using
		// some kind of system property and also put the correct environment variables value
		if(false){
			String environmentVariableToExportDisplay = "DISPLAY:0:0";
			Map map = aConfig.getVMSpecificAttributesMap();
			if(map == null) map = new HashMap(1);
			String[] environmentVariables = (String[])map.get(ProxyPlugin.ENVIRONMENT_VARIABLE);
			if(environmentVariables == null){
				map.put(ProxyPlugin.ENVIRONMENT_VARIABLE, new String[] {environmentVariableToExportDisplay});				
			} else {
				String[] newEnvironmentVariables = new String[environmentVariables.length + 1];
				System.arraycopy(environmentVariables,0,newEnvironmentVariables,1,environmentVariables.length);
				newEnvironmentVariables[0] = "DISPLAY:0:0";
				map.put(ProxyPlugin.ENVIRONMENT_VARIABLE, new String[] {environmentVariableToExportDisplay});				
			}
			aConfig.setVMSpecificAttributesMap(map);
		}
		
	}

	public void contributeToRegistry(ProxyFactoryRegistry registry) {
		
		// Run the static initialize() method on the class
		// com.ibm.etools.jbcf.swt.targetvm.Environment
		// This creates the singleton Display 
		try {
			IBeanTypeProxy environmentBeanTypeProxy =
				registry.getBeanTypeProxyFactory().getBeanTypeProxy("com.ibm.etools.jbcf.swt.targetvm.Environment"); //$NON-NLS-1$
				//Old way of doing things involves the Environment creating the display thread
			IMethodProxy initializeMethodProxy =
				environmentBeanTypeProxy.getMethodProxy("initialize"); //$NON-NLS-1$ //$NON-NLS-2$
			initializeMethodProxy.invoke(environmentBeanTypeProxy);				
		} catch (Exception exc) {
			JavaVEPlugin.log("Unable to set initialize the SWT target VM");
			JavaVEPlugin.log(exc, Level.WARNING);
		}	
		
		// Register the SWT proxy factory that allows easy manipulation of things like rectangles, points, etc...
		//TODO Big hack to get the proxies registered - needs much more thought
		SWTREMProxyRegistration.initialize(registry);
		
		// Force focus back to the IDE
//		Display.getDefault().getActiveShell().forceActive();
		
			
	}
}
