package org.eclipse.ve.internal.swt;

import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.swt.SWTREMProxyRegistration;

public class SWTConfigurationContributor implements IConfigurationContributor {

	public void contributeClasspaths(List classpaths, IClasspathContributionController controller) throws CoreException {
				
		// Add the SWT jar to the classpath of the target VM
		// TODO needs thought about how to cope with SWTContainer being the one in the build pathh
  		SWTContainer swtContainer = new SWTContainer();
		IPath swtJarPath = swtContainer.getPath();
		String[] swtJarLocation = new String[] {swtJarPath.toString()};
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
		// TODO SWT DLL comes from there + we need to think about Linux where the folder name is different
		Path path = new Path("os/win32/x86");
		URL localURL = Platform.getPlugin("org.eclipse.swt").find(path);		
		String dllLocation = localURL.getFile();
		dllLocation = dllLocation.replace('/',java.io.File.separatorChar);		

		String programArg = "-Djava.library.path=" + dllLocation;		
		String[] existingVMArguments = aConfig.getVMArguments();
		// Create a new array of VM arguments so we can add in the two new entries
		String[] newVMArguments = new String[existingVMArguments.length + 1];
		System.arraycopy(existingVMArguments,0,newVMArguments,1,existingVMArguments.length);
		newVMArguments[0] = programArg;
		aConfig.setVMArguments(newVMArguments);
		
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
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
		}	
		
		// Register the SWT proxy factory that allows easy manipulation of things like rectangles, points, etc...
		//TODO Big hack to get the proxies registered - needs much more thought
		SWTREMProxyRegistration.initialize(registry);
		
		// Force focus back to the IDE
//		Display.getDefault().getActiveShell().forceActive();
		
			
	}
}
