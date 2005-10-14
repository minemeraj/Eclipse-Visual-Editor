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
package org.eclipse.ve.internal.java.vce.launcher;
/*
 *  $RCSfile: JavaBeanLaunchConfigurationDelegate.java,v $
 *  $Revision: 1.22 $  $Date: 2005-10-14 17:45:07 $ 
 */



/*
 * Launcher that lets JavaBeans be tested by creating them and hosting
 * them inside a frame, applet viewer, etc... as required by the JavaBean
 */

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.*;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ProxyLaunchSupport.LaunchInfo;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin.FoundIDs;
import org.eclipse.jem.internal.proxy.remote.LocalFileConfigurationContributorController;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.PDEUtilities;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

public class JavaBeanLaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate {
	
	/**
	 * Comment for <code>IS_SWT</code>
	 * 
	 * @since 1.2.0
	 */
	public static final String IS_SWT = "isSWT";
	static String LAUNCHER_TYPE_NAME = "org.eclipse.ve.internal.java.vce.launcher.remotevm.JavaBeansLauncher"; //$NON-NLS-1$
	static String JFC_LAUNCHER_TYPE_NAME = "org.eclipse.ve.internal.java.vce.launcher.remotevm.JFCLauncher"; //$NON-NLS-1$
	static String SWT_LAUNCHER_TYPE_NAME = "org.eclipse.ve.internal.java.vce.launcher.remotevm.SWTLauncher"; //$NON-NLS-1$
	static String RCP_LAUNCHER_TYPE_NAME = "org.eclipse.ve.internal.java.vce.launcher.remotevm.RCPLauncher"; //$NON-NLS-1$
	static String PACK = " PACK"; //$NON-NLS-1$
	static String LOCALE = "LOCALE"; //$NON-NLS-1$
	static String APPLET_PARMS_NUMBER = "APPLET_PARMS_NUMBER"; //$NON-NLS-1$
	static String APPLET_PARM_NAME = "APPLET_PARM_NAME"; //$NON-NLS-1$
	static String APPLET_PARM_VALUE = "APPLET_PARM_VALUE"; //$NON-NLS-1$
	static String QUOTE_DELIM = "\"";
	
public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
	
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		
		monitor.beginTask(VCELauncherMessages.LaunchConfigurationDelegate_Msg_Launching, IProgressMonitor.UNKNOWN); 
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}
			
		String javaBeanName = verifyMainTypeName(configuration);

		IVMInstall vm = verifyVMInstall(configuration);

		IVMRunner runner = vm.getVMRunner(mode);
		if (runner == null) {
			abort(MessageFormat.format(VCELauncherMessages.Launcher_jreerror_msg_ERROR_, new String[]{vm.getId()}), null, IJavaLaunchConfigurationConstants.ERR_VM_RUNNER_DOES_NOT_EXIST); 
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
		String[] remoteVMLocations = ProxyPlugin.getPlugin().localizeAllFromBundleAndFragments(JavaVEPlugin.getPlugin().getBundle(), "vm/vcelauncher.jar");	 //$NON-NLS-1$
		String[] newClassPath = new String[classpath.length + remoteVMLocations.length];
		System.arraycopy(remoteVMLocations, 0, newClassPath, 0, remoteVMLocations.length);
		System.arraycopy(classpath,0,newClassPath,remoteVMLocations.length,classpath.length);
		
		VMRunnerConfiguration runConfig;
		// Create VM config
		runConfig = new VMRunnerConfiguration(LAUNCHER_TYPE_NAME, newClassPath);

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
	StringBuffer args = new StringBuffer(super.getVMArguments(configuration));
	
	String launchersList = JFC_LAUNCHER_TYPE_NAME;	
	
	// Now add in ours.
	// TODO: refactor launchers into appropriate packages
	
	IJavaProject project = verifyJavaProject(configuration);
	if ( isRCPPlugin(project) ){
		launchersList = launchersList + "," + RCP_LAUNCHER_TYPE_NAME; //$NON-NLS-1$
		
		// Ensure necessary classes are available for the launcher.
		setupClasspath(configuration, args);
		
		// Add IDE preferences to the argument buffer for use on the VM.
		int fTabPosition = WorkbenchPlugin.getDefault().getPreferenceStore().getInt(IPreferenceConstants.VIEW_TAB_POSITION);
		boolean fTraditionalTabs = PlatformUI.getPreferenceStore().getBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS);
		String activeLinkColor = JFaceResources.getColorRegistry().getRGB(JFacePreferences.ACTIVE_HYPERLINK_COLOR).toString();
		String errorColor = JFaceResources.getColorRegistry().getRGB(JFacePreferences.ERROR_COLOR).toString();
		String linkColor = JFaceResources.getColorRegistry().getRGB(JFacePreferences.HYPERLINK_COLOR).toString();
		String defaultFont = JFaceResources.getDefaultFont().getFontData()[0].toString();
		String dialogFont = JFaceResources.getDialogFont().getFontData()[0].toString();
		String bannerFont = JFaceResources.getBannerFont().getFontData()[0].toString();
		String headerFont = JFaceResources.getHeaderFont().getFontData()[0].toString();
		String textFont = JFaceResources.getTextFont().getFontData()[0].toString();
		
		args.append(" -Drcp.launcher.tabPosition=" + fTabPosition); //$NON-NLS-1$
		args.append(" -Drcp.launcher.traditionalTabs=" + fTraditionalTabs); //$NON-NLS-1$
		args.append(" -Drcp.launcher.activeLink=\"" + activeLinkColor + QUOTE_DELIM); //$NON-NLS-1$
		args.append(" -Drcp.launcher.error=\"" + errorColor + QUOTE_DELIM); //$NON-NLS-1$
		args.append(" -Drcp.launcher.link=\"" + linkColor + QUOTE_DELIM); //$NON-NLS-1$
		args.append(" -Drcp.launcher.defaultFont=\"" + defaultFont + QUOTE_DELIM); //$NON-NLS-1$
		args.append(" -Drcp.launcher.dialogFont=\"" + dialogFont + QUOTE_DELIM); //$NON-NLS-1$
		args.append(" -Drcp.launcher.bannerFont=\"" + bannerFont + QUOTE_DELIM); //$NON-NLS-1$
		args.append(" -Drcp.launcher.headerFont=\"" + headerFont + QUOTE_DELIM); //$NON-NLS-1$
		args.append(" -Drcp.launcher.textFont=\"" + textFont + QUOTE_DELIM); //$NON-NLS-1$
		
		// If we are launching a WorkbenchPart then we want to read the plugin.xml to see if it is defined as a view
		// or editor and if so get its name and the location of its icon so that the target VM can render its title bar correctly
		PDEUtilities pdeUtilities = PDEUtilities.getUtilities(project.getProject());
		String viewName = pdeUtilities.getViewName(javaBeanName);
		if(viewName != null){
			args.append(" -Drcp.launcher.viewName=\"" + viewName + QUOTE_DELIM); //$NON-NLS-1$			
		}
		String iconPath = pdeUtilities.getIconPath(javaBeanName);		
		if(iconPath != null){
			args.append(" -Drcp.launcher.iconPath=\"" + iconPath + QUOTE_DELIM); //$NON-NLS-1$			
		}		
	}
	
	if (configuration.getAttribute(IS_SWT, false)) { //$NON-NLS-1$
		// SWT support is provided by the included library, so just add the launcher.
		launchersList = launchersList + "," + SWT_LAUNCHER_TYPE_NAME; //$NON-NLS-1$
	}
	
	args.append(" -Dvce.launchers=\"" + launchersList + "\""); //$NON-NLS-1$ //$NON-NLS-2$
	
	// We always launch the program javaBeansLauncher on the target VM and we give it details about which JavaBean to test,
	// which look and feel to use, etc... in arguments, e.g. to test the java.awt.Button the args would be
	// java javaBeansLauncher  -Dvce.launcher.class=java.awt.Button 

	// Put the name of the class to launch
	args.append(" -Dvce.launcher.class=" + javaBeanName); //$NON-NLS-1$
	// Set the look and feel to the one the user specified in the configuration
	String lookAndFeelClass = configuration.getAttribute(VCEPreferences.SWING_LOOKANDFEEL, ""); //$NON-NLS-1$
	if(lookAndFeelClass.equals("")){ //$NON-NLS-1$
		lookAndFeelClass = VCEPreferences.getPlugin().getPluginPreferences().getString(VCEPreferences.SWING_LOOKANDFEEL);		
	}
	if ( !lookAndFeelClass.equals("") ) { //$NON-NLS-1$
		args.append(" -Dvce.launcher.lookandfeel=" + lookAndFeelClass); //$NON-NLS-1$
	} 
	String locale = configuration.getAttribute(LOCALE, ""); //$NON-NLS-1$
	if ( !locale.equals("")) { //$NON-NLS-1$
		args.append(" -Dlocale=" + locale); //$NON-NLS-1$
	}
	
	// Pass the size and whether to pack or not
	boolean pack = configuration.getAttribute(PACK, false);
	if ( pack ) {
		args.append(" -Dpack=true"); //$NON-NLS-1$
	}
	// Pass the applet parameters
	// These are passed as several strings -Dappletparmsnumber= is the total number of parms
	// and each one is passed as a separate argument, e.g.
	// -Dappletparmname1= and -Dappletparmvalue1=
	String numberOfAppletParmsString = configuration.getAttribute(APPLET_PARMS_NUMBER, ""); //$NON-NLS-1$
	if ( !numberOfAppletParmsString.equals("") ) { //$NON-NLS-1$
		int numberOfAppletParms = Integer.parseInt(numberOfAppletParmsString);
		args.append(" -Dappletparmsnumber=" + numberOfAppletParmsString); //$NON-NLS-1$
		for ( int i=1 ; i <= numberOfAppletParms ; i++){
			String name = configuration.getAttribute(APPLET_PARM_NAME+i, ""); //$NON-NLS-1$
			args.append(" -Dappletparmname" + i + "=\"" + name + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			String value = configuration.getAttribute(APPLET_PARM_VALUE+i, ""); //$NON-NLS-1$
			args.append(" -Dappletparmvalue" + i + "=\"" + value + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	};
		
	return args.toString();
}

private boolean isRCPPlugin(IJavaProject proj) {
	try {
		FoundIDs foundids = ProxyPlugin.getPlugin().getIDsFound(proj);
		return foundids.pluginIds.get("org.eclipse.ui.workbench") == Boolean.TRUE;
	} catch (JavaModelException e) {
	}
	return false;
}

protected void setupClasspath(ILaunchConfiguration configuration, StringBuffer args){
	
	try{
		
		// Now let's get the classpaths created through the contributors.
		URL[] classpath = ProxyLaunchSupport.convertStringPathsToURL(getClasspath(configuration));
		URL[][] bootpathInfo = new URL[][]{	};
				
		IConfigurationContributor[] emptyList = {};
		LaunchInfo launchInfo = new LaunchInfo();
		
		final IConfigurationContributor[] contributors = 
			ProxyLaunchSupport.fillInLaunchInfo(emptyList, launchInfo, verifyJavaProject(configuration).getElementName());
		final LocalFileConfigurationContributorController controller = 
			new LocalFileConfigurationContributorController(classpath, bootpathInfo, launchInfo);
		
		if (contributors != null) {		
			for (int i = 0; i < contributors.length; i++) {
				contributors[i].initialize(launchInfo.getConfigInfo());
				contributors[i].contributeClasspaths(controller);
			}
		}
		Iterator libPaths = controller.getFinalJavaLibraryPath().iterator();
		StringBuffer javaPath = new StringBuffer("");
		while(libPaths.hasNext()){
			String curLib = ((URL)libPaths.next()).getPath();
			if(curLib.startsWith("/") && Platform.getOS().equals(Platform.OS_WIN32))
				curLib = curLib.substring(1);
			if (javaPath.length()>0)
				javaPath.append(File.pathSeparatorChar);
			javaPath.append(curLib);
		}
		if (!args.toString().matches(".*java.library.path=")) //$NON-NLS-1$
			args.append(" -Djava.library.path=\"" + javaPath + "\""); //$NON-NLS-1$ //$NON-NLS-2$
	} catch(Exception e){
		e.printStackTrace();
	}
}
}
