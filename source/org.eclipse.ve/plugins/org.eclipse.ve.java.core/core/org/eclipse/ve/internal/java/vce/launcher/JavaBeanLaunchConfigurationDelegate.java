package org.eclipse.ve.internal.java.vce.launcher;
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
 *  $RCSfile: JavaBeanLaunchConfigurationDelegate.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */



/*
 * Launcher that lets JavaBeans be tested by creating them and hosting
 * them inside a frame, applet viewer, etc... as required by the JavaBean
 */

import java.io.File;
import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.*;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

public class JavaBeanLaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate {
	
	static String LAUNCHER_TYPE_NAME = "org.eclipse.ve.internal.java.vce.launcher.remotevm.JavaBeansLauncher"; //$NON-NLS-1$
	static String PACK = " PACK"; //$NON-NLS-1$
	static String LOCALE = "LOCALE"; //$NON-NLS-1$
	static String APPLET_PARMS_NUMBER = "APPLET_PARMS_NUMBER"; //$NON-NLS-1$
	static String APPLET_PARM_NAME = "APPLET_PARM_NAME"; //$NON-NLS-1$
	static String APPLET_PARM_VALUE = "APPLET_PARM_VALUE"; //$NON-NLS-1$
	
public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
	
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		
		monitor.beginTask(VCELauncherMessages.getString("LaunchConfigurationDelegate.Msg.Launching"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}
			
		String javaBeanName = verifyMainTypeName(configuration);

		IVMInstall vm = verifyVMInstall(configuration);

		IVMRunner runner = vm.getVMRunner(mode);
		if (runner == null) {
			abort(MessageFormat.format(VCELauncherMessages.getString("Launcher.jreerror.msg_ERROR_"), new String[]{vm.getId()}), null, IJavaLaunchConfigurationConstants.ERR_VM_RUNNER_DOES_NOT_EXIST); //$NON-NLS-1$
		}

		File workingDir = verifyWorkingDirectory(configuration);
		String workingDirName = null;
		if (workingDir != null) {
			workingDirName = workingDir.getAbsolutePath();
		}
		
		// Program & VM args
		String pgmArgs = getProgramArguments(configuration);
		String vmArgs = getVMArguments(configuration,javaBeanName);
		// The vmArgs contain specific arguments that let the launcher know
		// which program to run - the look and feel mode to use, etc...
		
		ExecutionArguments execArgs = new ExecutionArguments(vmArgs, pgmArgs);
		
		// VM-specific attributes
		Map vmAttributesMap = getVMSpecificAttributesMap(configuration);
				
		// Classpath - Make sure it has the launcher added to it
		String[] classpath = getClasspath(configuration);
		String[] remoteVMLocations = ProxyPlugin.getPlugin().localizeFromPluginDescriptorAndFragments(JavaVEPlugin.getPlugin().getDescriptor(), "vm/vcelauncher.jar");	 //$NON-NLS-1$
		String[] newClassPath = new String[classpath.length + remoteVMLocations.length];
		System.arraycopy(remoteVMLocations, 0, newClassPath, 0, remoteVMLocations.length);
		System.arraycopy(classpath,0,newClassPath,remoteVMLocations.length,classpath.length);
		
		// Create VM config
		VMRunnerConfiguration runConfig = new VMRunnerConfiguration(LAUNCHER_TYPE_NAME, newClassPath);
		runConfig.setProgramArguments(execArgs.getProgramArgumentsArray());
		runConfig.setVMArguments(execArgs.getVMArgumentsArray());
		runConfig.setWorkingDirectory(workingDirName);
		runConfig.setVMSpecificAttributesMap(vmAttributesMap);		

		// Bootpath
		String[] bootpath = getBootpath(configuration);
		runConfig.setBootClassPath(bootpath);
		
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}	
				
		// Launch the configuration
		runner.run(runConfig, launch, monitor);	
		
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}				
		
		// set the default source locator if required
		setDefaultSourceLocator(launch, configuration);
		
		monitor.done();		
	}	
public String getVMArguments(ILaunchConfiguration configuration, String javaBeanName) throws CoreException {
	// First get any of the user requested ones.
	String args = super.getVMArguments(configuration);
	
	// Now add in ours.
	
	// We always launch the program javaBeansLauncher on the target VM and we give it details about which JavaBean to test,
	// which look and feel to use, etc... in arguments, e.g. to test the java.awt.Button the args would be
	// java javaBeansLauncher  -Dvce.launcher.class=java.awt.Button 

	// Put the name of the class to launch
	args = args + " -Dvce.launcher.class=" + javaBeanName; //$NON-NLS-1$
	// Set the look and feel to the one the user specified in the configuration
	String lookAndFeelClass = configuration.getAttribute(VCEPreferences.SWING_LOOKANDFEEL, ""); //$NON-NLS-1$
	if ( !lookAndFeelClass.equals("") ) { //$NON-NLS-1$
		args = args + " -Dvce.launcher.lookandfeel=" + lookAndFeelClass; //$NON-NLS-1$
	}			
	String locale = configuration.getAttribute(LOCALE, ""); //$NON-NLS-1$
	if ( !locale.equals("")) { //$NON-NLS-1$
		args = args + " -Dlocale=" + locale; //$NON-NLS-1$
	}
	
	// Pass the size and whether to pack or not
	boolean pack = configuration.getAttribute(PACK, false);
	if ( pack ) {
		args = args + " -Dpack=true"; //$NON-NLS-1$
	}
	// Pass the applet parameters
	// These are passed as several strings -Dappletparmsnumber= is the total number of parms
	// and each one is passed as a separate argument, e.g.
	// -Dappletparmname1= and -Dappletparmvalue1=
	String numberOfAppletParmsString = configuration.getAttribute(APPLET_PARMS_NUMBER, ""); //$NON-NLS-1$
	if ( !numberOfAppletParmsString.equals("") ) { //$NON-NLS-1$
		int numberOfAppletParms = Integer.parseInt(numberOfAppletParmsString);
		args = args + " -Dappletparmsnumber=" + numberOfAppletParmsString; //$NON-NLS-1$
		for ( int i=1 ; i <= numberOfAppletParms ; i++){
			String name = configuration.getAttribute(APPLET_PARM_NAME+i, ""); //$NON-NLS-1$
			args = args + " -Dappletparmname" + i + "=\"" + name + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			String value = configuration.getAttribute(APPLET_PARM_VALUE+i, ""); //$NON-NLS-1$
			args = args + " -Dappletparmvalue" + i + "=\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	};
	
	return args;
}
}
