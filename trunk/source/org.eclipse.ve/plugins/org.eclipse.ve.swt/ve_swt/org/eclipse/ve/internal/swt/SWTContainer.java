/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.*;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class SWTContainer implements IClasspathContainer, IConfigurationContributor {
	
	public final static int SWT_CONTAINER_PATH_CUSTOM = 	0x01 ;
	public final static int SWT_CONTAINER_PATH_PDE = 		0x02 ;
	public final static int SWT_CONTAINER_PATH_PLATFORM = 	0x04 ;
	
	final static int SWT_CONTAINER_PATH = 	SWT_CONTAINER_PATH_CUSTOM|
											SWT_CONTAINER_PATH_PDE|
											SWT_CONTAINER_PATH_PLATFORM;	
	
	public final static int SWT_CONTAINER_JFACE =		0x10 ;
	
	public static final String SWT_CONTAINER_SIGNITURE = 				"SWT_CONTAINER" ;
	public static final String SWT_CONTAINER_SIGNITURE_JFACE = 			"JFACE";			
	public static final String SWT_CONTAINER_SIGNITURE_PATH_PLATFORM = 	"PLATFORM";
	public static final String SWT_CONTAINER_SIGNITURE_PATH_PDE = 		"PDE";
	public static final String SWT_CONTAINER_SIGNITURE_PATH_CUSTOM = 	"CUSTOM";	
	
	
	public static class ContainerType {
		private int pathType = SWT_CONTAINER_PATH_PLATFORM;
		private String customPath = null;
		static String platformPath = null;
		static String platformVersion=null;
		String pdePath = null;
		String pdeVersion = null;
	
	   public ContainerType (IPath containerPath) {
		   super();
		   parsePath(containerPath);
	   }
	   
	   public ContainerType () {
		   super();		   
	   }	   
	   protected void parsePath (IPath containerPath) {
			if (!containerPath.segment(0).equals(SWT_CONTAINER_SIGNITURE))
				throw new IllegalStateException("Invalid Container ID: "+containerPath);
			
			pathType=SWT_CONTAINER_PATH_PLATFORM; // default
			
			for (int i=1; i<containerPath.segmentCount(); i++) {
				if (containerPath.segment(i).equals(SWT_CONTAINER_SIGNITURE_JFACE))
					setPathType(SWT_CONTAINER_JFACE, true);
				else if (containerPath.segment(i).equals(SWT_CONTAINER_SIGNITURE_PATH_PLATFORM))
					setPathType(SWT_CONTAINER_PATH_PLATFORM, true);
				else if (containerPath.segment(i).equals(SWT_CONTAINER_SIGNITURE_PATH_PDE))
					setPathType(SWT_CONTAINER_PATH_PDE, true);
				else if (containerPath.segment(i).equals(SWT_CONTAINER_SIGNITURE_PATH_CUSTOM)) {
					setPathType(SWT_CONTAINER_PATH_PDE, true);
					customPath = containerPath.removeFirstSegments(i).toString();	
					break;
				}
			}
			
		}

	   
		public  IPath getContainerPath () {
			IPath result = new Path (SWT_CONTAINER_SIGNITURE);
			
			if ((pathType&SWT_CONTAINER_JFACE)>0)
				result = result.append(SWT_CONTAINER_SIGNITURE_JFACE);
			
			int path = pathType & SWT_CONTAINER_PATH;
			switch (path) {
				case SWT_CONTAINER_PATH_PLATFORM:
					result = result.append(SWT_CONTAINER_SIGNITURE_PATH_PLATFORM);
					break;
				case SWT_CONTAINER_PATH_PDE:				
					result = result.append(SWT_CONTAINER_SIGNITURE_PATH_PDE);
					break;
				case SWT_CONTAINER_PATH_CUSTOM:
					result = result.append(SWT_CONTAINER_SIGNITURE_PATH_CUSTOM).append(customPath);
					break;
			}
			return result;		
		}
		
		
		public void setPathType (int type, boolean flag) {
			if ((type&SWT_CONTAINER_PATH)>0 && flag)
				pathType &= ~SWT_CONTAINER_PATH;  // only allow one path type to be set
			
			if (flag)
				pathType |= type;
			else
				pathType &= ~type;
			
			if ((pathType&SWT_CONTAINER_PATH)==0)
				pathType &= SWT_CONTAINER_PATH_PLATFORM; // Force a default
		}		
		public boolean isPlatformPath() {
			return (pathType&SWT_CONTAINER_PATH_PLATFORM)>0;
		}		
		public boolean isPDEPath() {
			return (pathType&SWT_CONTAINER_PATH_PDE)>0;
		}		
		public boolean isCustomPath() {
			return (pathType&SWT_CONTAINER_PATH_CUSTOM)>0;
		}		
		public boolean includeJFace() {
			return (pathType&SWT_CONTAINER_JFACE)>0;
		}		
		public String getCustomPath() {
			if (customPath!=null)
			   return customPath;
			else
			   return ("");
		}		
		public void setCustomPath(String path) {
			IPath p = new Path (path);
			customPath = p.toPortableString();
		}		
		public String getPdePath() {
			if (pdePath==null) {				
				if (isLegacy(PDECore.getDefault().getModelManager().getTargetVersion()))
					pdePath = PDECore.getDefault().getModelManager().findModel(swtLibraries[0].getLegacyID()).getInstallLocation();
				else 
				   pdePath = PDECore.getDefault().getModelManager().findModel(swtLibraries[0].getPluginID()).getInstallLocation();
			}
			return pdePath;
		}		
		public String getPlatformPath() {
			if (platformPath==null)
				try {
				   platformPath = SWTConfigurationContributor.getFilePath(Platform.resolve(Platform.getBundle(swtLibraries[0].getPluginID()).getEntry("/"))).toOSString();
				} catch (IOException e) {}
			return platformPath;
		}
		public String toString() {
			return getContainerPath().toPortableString();
		}
		
		public boolean equals(Object o) {
			if (o ==null || !(o instanceof SWTContainer.ContainerType))
				return false;
			
			// Deal with migration issues
			SWTContainer.ContainerType ct = (SWTContainer.ContainerType)o;			
			return (ct.getContainerPath().equals(getContainerPath()));
		}
		public  String getPlatformVersion() {
			if (platformVersion==null)
				platformVersion=(String)Platform.getBundle(swtLibraries[0].getPluginID()).getHeaders().get(Constants.BUNDLE_VERSION);
			return platformVersion;
		}
		public String getPdeVersion() {			
			if (pdeVersion==null)
				pdeVersion=PDECore.getDefault().getModelManager().findModel("org.eclipse.swt").getBundleDescription().getVersion().toString();
			return pdeVersion;
		}
	
    }
	
	/**
	 * The idea is to specify exactly where things are, so to not spend time
	 * looking for jars/src on the file system/src Contribution
	 * @since 1.1.0
	 */
	public static class JarInfo  {
		String pluginID;		// The Plugin/Fragment where the .jar is in
		boolean includesLibraries = false;
		String legacyID;    	// Pre 3.1
		String legacyJarPath;	// Relative to the plugin (Pre 3.1) 
		String legacyLibPath;	// Relative to the plubin (Pre 3.1)
		String srcPluginID;		// Search for src. also in this plugin
		String legacySrcPluginID;
		
		/**
		 * 
		 * @param pluginID same id for both legacy/current
		 * @param legacyJarPath  legacy path
		 * @param srcPluginID
		 * 
		 * @since 1.1.0
		 */
		public JarInfo (String pluginID, String jarPath, String srcPluginID) {		
			this.pluginID=pluginID;			
			legacyID=pluginID;			
			this.legacyJarPath=jarPath;
			this.srcPluginID=srcPluginID;
			this.legacySrcPluginID=srcPluginID;
			legacyLibPath=null;
		}
		
		public JarInfo (String pluginID, boolean includeLibraries, String legacyID, String jarPath, String libPath, String srcPluginID, String legacySrcPluginID) {
			this.pluginID=pluginID;
			this.legacyID=legacyID;
			this.legacyJarPath=jarPath;
			this.srcPluginID=srcPluginID;								
			this.legacyLibPath=libPath;			
			this.includesLibraries=includeLibraries;
			this.legacySrcPluginID=legacySrcPluginID;
		}
		
		public String getLegacyJarPath() {
			return legacyJarPath;
		}
		public String getLegacyLibPath() {
			return legacyLibPath;
		}
		public String getPluginID() {		
			return pluginID;
		}		
		public String getSrcPluginID() {
			return srcPluginID;
		}		
		public String getLegacyID() {
			return legacyID;
		}		
		public boolean isIncludesLibraries() {
			return includesLibraries;
		}
		public String toString() {
			StringBuffer sb = new StringBuffer("pluginID=");
			sb.append(pluginID);
			if (includesLibraries)
				sb.append(" [With Libraries]");
			else
				sb.append(" [No Libraries]");
			sb.append(", legacyID=");
			sb.append(legacyID);
			sb.append(", legacyJarPath=");
			sb.append(legacyJarPath);
			sb.append(", legacyLibPath=");
			sb.append(legacyLibPath);
			sb.append(", srcPluginID=");
			sb.append(srcPluginID);				
			return sb.toString();
		}

		
		public String getLegacySrcPluginID() {
			return legacySrcPluginID;
		}
	}



	
	private IClasspathEntry[] fClasspathEntries = new IClasspathEntry[0];
	
	private IPath containerPath;	// path for container, NOT path for resolved entry
	private IJavaProject project;
		
	private ContainerType containerType = null;
	
	
	private boolean isGTK = Platform.WS_GTK.equals(Platform.getWS());
			
	/**
	 * The following path info. provides both pre and post 3.1 locations
	 * so that we can work with pre leveled 3.1 targets
	 * [ <pluginID>,  
	 *        <pre 3.1 jar name>, <pre 3.1 lib path>,
	 *        <post 3.1 fragment name> <post 3.1 lib Path> ]
	 * 0  <pluginID> required
	 * 1  <pre 3.1 fragment name> optional fragment plugin name, if not in the plugin itself
	 * 2  <pre 3.1 jar name> .jar inside the plugin	    
	 * 3  <pre 3.1 lib Path> optional library that is required by this jar
	 * 
	 * 4  <post 3.1 fragment name> optional jared fragment name, if not the plugin itself  
	 * 5  <post 3.1 lib Path> optional required library. path is within the jared fragement
	 *   
	 */
	
	// see FindSupport.findXXX for more info ... may need to use the Internal TargetPlatform
	public final static IPath  SWT_CONTAINER_WS  = new Path("ws").append(Platform.getWS());
	public final static IPath  SWT_CONTAINER_OS = new Path("os").append(Platform.getOS()).append(Platform.getOSArch());
	public final static String SWT_CONTAINER_OS_PLUGIN_EXT = "."+Platform.getWS()+"."+Platform.getOS()+"."+Platform.getOSArch();
	public final static String SWT_CONTAINER_SRC_PLUGIN = "org.eclipse.platform.source";
	 			
	public final static JarInfo[] swtLibraries = new JarInfo[] {
			 new JarInfo(	"org.eclipse.swt"+SWT_CONTAINER_OS_PLUGIN_EXT,
					 		true,
							"org.eclipse.swt"+"."+Platform.getOS(),
							SWT_CONTAINER_WS.append("swt.jar").toPortableString(), 
							SWT_CONTAINER_OS.toPortableString(),
							SWT_CONTAINER_SRC_PLUGIN,
//							"org.eclipse.platform."+Platform.getWS()+".source"
							SWT_CONTAINER_SRC_PLUGIN+SWT_CONTAINER_OS_PLUGIN_EXT
						)};
					
	
	
	private final static JarInfo[] swtGTKLibraries = new JarInfo[]{
		    // GTK libraries are all part of the swt fragment included in the swtLibraries in 3.1
			new JarInfo(	"org.eclipse.swt.gtk", 
							SWT_CONTAINER_WS.append("swt-pi.jar").toPortableString(),
							null),
			new JarInfo(	"org.eclipse.swt.gtk", 
							SWT_CONTAINER_WS.append("swt-mozilla.jar").toPortableString(), 
							null)
			};
	
	private final static JarInfo[] jfaceLibraries = new JarInfo[] {
			new JarInfo("org.eclipse.jface", "jface.jar", SWT_CONTAINER_SRC_PLUGIN), 
			new JarInfo("org.eclipse.jface.text", "jfacetext.jar", SWT_CONTAINER_SRC_PLUGIN), 
			new JarInfo("org.eclipse.core.runtime", "runtime.jar", SWT_CONTAINER_SRC_PLUGIN), 
			new JarInfo("org.eclipse.core.runtime.compatibility", "compatibility.jar", SWT_CONTAINER_SRC_PLUGIN), 			
			new JarInfo("org.eclipse.osgi", "core.jar", SWT_CONTAINER_SRC_PLUGIN), 					
			new JarInfo("org.eclipse.core.commands", ".", SWT_CONTAINER_SRC_PLUGIN) 

	};
	

	

	
	public static boolean isLegacy (String version) {		
		StringTokenizer tk = new StringTokenizer(version,".");
		try {
			int Major = Integer.parseInt(tk.nextToken());
			int Minor = Integer.parseInt(tk.nextToken());
			if ((Major>3) || (Major==3 && Minor>=1))
				return false;
		} catch (NumberFormatException e) {}
		return true;
	}
	
	/**
	 * Platform is 3.1
	 * @param containerPath current signiture
	 * @throws IOException
	 * 
	 * @since 1.1.0
	 */
	protected void initPlatformPath(IPath containerPath) throws IOException {

		ArrayList entries = new ArrayList() ;		
		for (int i = 0; i < swtLibraries.length; i++) {
			IClasspathEntry e = null;
			Bundle b = Platform.getBundle(swtLibraries[i].getPluginID());	
			if (b!=null) {
					e = SWTConfigurationContributor.getPlatformPath(swtLibraries[i].getPluginID(),swtLibraries[i].isIncludesLibraries(), swtLibraries[i].getSrcPluginID());
				 if (e!=null)
					entries.add(e);
				 else {					 
					 addProblem(MessageFormat.format("Could not add {0} JAR to the class path", new Object[] {swtLibraries[i].getPluginID()})); //$NON-NLS-1)
				 }
			}
			else
				JavaVEPlugin.log("Could not location class path for: "+swtLibraries[i].getPluginID());				
		}

		if (containerType.includeJFace()) {
			for (int i = 0; i < jfaceLibraries.length; i++) {
				IClasspathEntry e = SWTConfigurationContributor.getPlatformPath(jfaceLibraries[i].getPluginID(), jfaceLibraries[i].isIncludesLibraries(), jfaceLibraries[i].getSrcPluginID());
				if (e!=null)
					entries.add(e);
				 else {					 
					 addProblem(MessageFormat.format("Could not add {0} JAR to the class path", new Object[] {jfaceLibraries[i].getPluginID()})); //$NON-NLS-1)
				 }
			}
		}
		fClasspathEntries = (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]);
		
	}
	
	
	protected void removeProblems() {
		try {
			IMarker[] markers = project.getProject().findMarkers(SWTConfigurationContributor.SWT_BUILD_PATH_MARKER, false, IResource.DEPTH_ZERO );
			for (int i = 0; i < markers.length; i++) {
				markers[i].delete();				
			}
		} catch (CoreException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}				
	}
	
	protected void addProblem (final String msg) {
		IMarker marker;
		try {
			marker = project.getProject().createMarker(SWTConfigurationContributor.SWT_BUILD_PATH_MARKER);		
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.MESSAGE, msg);
		} catch (CoreException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}	
	}
	
	protected void initLegacyPDE(IPath containerPath) throws IOException {		
		ArrayList entries = new ArrayList() ;		
		for (int i = 0; i < swtLibraries.length; i++) {
			IClasspathEntry e = null;
			IPluginModelBase pluginBase = PDECore.getDefault().getModelManager().findModel(swtLibraries[i].getLegacyID());	
			if (pluginBase!=null) {
				e = SWTConfigurationContributor.getLegacyPDEPath(swtLibraries[i].getLegacyID(), swtLibraries[i].getLegacyJarPath(), swtLibraries[i].getLegacyLibPath(), swtLibraries[i].getLegacySrcPluginID());
				 if (e!=null)
					entries.add(e);
				 else {					 
					 addProblem(MessageFormat.format("Could not add {0} JAR to the class path", new Object[] {swtLibraries[i].getLegacyID()})); //$NON-NLS-1)
				 }
			}
			else
				JavaVEPlugin.log("SWTContainer: Could not find a plugin for: "+swtLibraries[i].getLegacyID());				
		}

		if (containerType.includeJFace()) {
			for (int i = 0; i < jfaceLibraries.length; i++) {
				IPluginModelBase pluginBase = PDECore.getDefault().getModelManager().findModel(jfaceLibraries[i].getLegacyID());
				if (pluginBase!=null) {
					IClasspathEntry e = SWTConfigurationContributor.getLegacyPDEPath(jfaceLibraries[i].getLegacyID(), jfaceLibraries[i].getLegacyJarPath(), jfaceLibraries[i].getLegacyLibPath(), jfaceLibraries[i].getLegacySrcPluginID());
					if (e!=null)
						entries.add(e);
					 else {					 
						 addProblem(MessageFormat.format("Could not add {0} JAR to the class path", new Object[] {jfaceLibraries[i].getLegacyID()})); //$NON-NLS-1)
					 }
				}
				else
					JavaVEPlugin.log("SWTContainer: Could not find a plugin for: "+jfaceLibraries[i].getLegacyID());	
			}
		}

		// GTK libraries are included in the SWT fragment.
//		if (isGTK) {
//			for (int j = 0; j < swtGTKLibraries.length; j++) {
//				Path path = new Path(swtGTKLibraries[j][1]);
//				URL[] locSrc = ProxyPlugin.getPlugin().findPluginJarAndAttachedSource(Platform.getBundle(swtGTKLibraries[j][0]), path);
//				if (locSrc[0] == null)
//					continue;
//				path = new Path(Platform.resolve(locSrc[0]).getFile());
//				Path srcPath = null;
//				if (locSrc[1] != null)
//					srcPath = new Path(Platform.resolve(locSrc[1]).getFile());
//				entries.add(JavaCore.newLibraryEntry(path, srcPath, null));
//			}
//			
//			
//		}
		fClasspathEntries = (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]);
	}
	protected void initPDE(IPath containerPath) throws IOException {
		ArrayList entries = new ArrayList() ;		
		for (int i = 0; i < swtLibraries.length; i++) {
			IClasspathEntry e = null;
			IPluginModelBase pluginBase = PDECore.getDefault().getModelManager().findModel(swtLibraries[i].getPluginID());	
			if (pluginBase!=null) {
				e = SWTConfigurationContributor.getPDEPath(swtLibraries[i].getPluginID(), swtLibraries[i].getSrcPluginID(), swtLibraries[i].isIncludesLibraries());
				 if (e!=null)
					entries.add(e);
				 else {					 
					 addProblem(MessageFormat.format("Could not add {0} JAR to the class path", new Object[] {swtLibraries[i].getLegacyID()})); //$NON-NLS-1)
				 }
			}
			else
				JavaVEPlugin.log("SWTContainer: Could not find a plugin for: "+swtLibraries[i].getLegacyID());				
		}

		if (containerType.includeJFace()) {
			for (int i = 0; i < jfaceLibraries.length; i++) {
				IPluginModelBase pluginBase = PDECore.getDefault().getModelManager().findModel(jfaceLibraries[i].getLegacyID());
				if (pluginBase!=null) {
					IClasspathEntry e = SWTConfigurationContributor.getPDEPath(jfaceLibraries[i].getPluginID(), jfaceLibraries[i].getSrcPluginID(), jfaceLibraries[i].isIncludesLibraries());
					if (e!=null)
						entries.add(e);
					 else {					 
						 addProblem(MessageFormat.format("Could not add {0} JAR to the class path", new Object[] {jfaceLibraries[i].getLegacyID()})); //$NON-NLS-1)
					 }
				}
				else
					JavaVEPlugin.log("SWTContainer: Could not find a plugin for: "+jfaceLibraries[i].getLegacyID());	
			}
		}
		fClasspathEntries = (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]);
	}
	/**
	 * PDE may be 3.1 or earlier
	 * @param containerPath
	 * @throws IOException
	 * 
	 * @since 1.1.0
	 */
	private String currentPDEPath = null;
	protected void initPDEPath(IPath containerPath) throws IOException {
		ContainerType ct = new ContainerType(containerPath);
		if (currentPDEPath!=null && currentPDEPath.equals(ct.getPdePath())) return ;		
		currentPDEPath = ct.getPdePath();
		containerType=ct;
				
		if (isLegacy(PDECore.getDefault().getModelManager().getTargetVersion())) 
			initLegacyPDE(containerPath);
		else
			initPDE(containerPath);
	}
	
	public SWTContainer(final IPath containerPath, IJavaProject project) {
		this.containerPath = containerPath;
		this.project = project;
		containerType = new ContainerType(containerPath);
					    
		try {
			// Wrap this in a runnable so that we can add errors to the problems view
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
					 removeProblems();	
					 if (containerType.isPlatformPath())						
							initPlatformPath(containerPath);
					 else if (containerType.isPDEPath()) {
						    initPDEPath(containerPath);
					 }
					 
					} catch (IOException e) {
						JavaVEPlugin.log(e, Level.INFO);
					}							
				}
			}, null, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());						
		} catch (CoreException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
	}
	public IClasspathEntry[] getClasspathEntries() {
		if (containerType.isPDEPath()) // Target may have changed
			try {
				// noOp if target did not change
				initPDEPath(containerPath);
			} catch (IOException e) {
				JavaVEPlugin.log(e, Level.INFO);
			}
		return fClasspathEntries;
	}

	public String getDescription() {
		return SWTMessages.getString("SWTContainer.Description"); //$NON-NLS-1$
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return containerPath;
	}


	private SWTConfigurationContributor configContribute = new SWTConfigurationContributor();
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#initialize(org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo)
	 */
	public void initialize(IConfigurationContributionInfo info) {
		configContribute.initialize(info);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeClasspaths(org.eclipse.jem.internal.proxy.core.IConfigurationContributionController)
	 */
	public void contributeClasspaths(IConfigurationContributionController controller) throws CoreException {
		configContribute.contributeClasspaths(controller);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeToConfiguration(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void contributeToConfiguration(ILaunchConfigurationWorkingCopy config) throws CoreException {
		configContribute.contributeToConfiguration(config);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry registry) {
		configContribute.contributeToRegistry(registry);
	}
}
