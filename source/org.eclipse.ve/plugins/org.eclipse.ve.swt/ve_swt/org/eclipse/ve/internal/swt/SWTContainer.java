/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.*;
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
	
	public final static int SWT_CONTAINER_JFACE =			0x10 ;
	
	public static final String SWT_CONTAINER_SIGNITURE = 				"SWT_CONTAINER" ; //$NON-NLS-1$
	public static final String SWT_CONTAINER_SIGNITURE_JFACE = 			"JFACE";			 //$NON-NLS-1$
	public static final String SWT_CONTAINER_SIGNITURE_PATH_PLATFORM = 	"PLATFORM"; //$NON-NLS-1$
	public static final String SWT_CONTAINER_SIGNITURE_PATH_PDE = 		"PDE"; //$NON-NLS-1$
	public static final String SWT_CONTAINER_SIGNITURE_PATH_CUSTOM = 	"CUSTOM";	 //$NON-NLS-1$
	
	
	// see FindSupport.findXXX for more info ... may need to use the Internal TargetPlatform
	public final static IPath  SWT_CONTAINER_WS  = new Path("ws").append(Platform.getWS()); //$NON-NLS-1$
	public final static IPath  SWT_CONTAINER_OS = new Path("os").append(Platform.getOS()).append(Platform.getOSArch()); //$NON-NLS-1$
	public final static String SWT_CONTAINER_OS_PLUGIN_EXT = "."+Platform.getOS()+"."+Platform.getWS()+"."+Platform.getOSArch(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public final static String SWT_CONTAINER_WS_PLUGIN_EXT = "."+Platform.getWS()+"."+Platform.getOS()+"."+Platform.getOSArch(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public final static String SWT_CONTAINER_SRC_PLUGIN = "org.eclipse.rcp.source";	 //$NON-NLS-1$
	
	
	public static class ContainerType {
		private int pathType = SWT_CONTAINER_PATH_PLATFORM;
		private String customPath = null;
		static String platformPath = null;
		static String platformVersion=null;
		String pdePath = null;
		String pdeVersion = null;
	
	   public ContainerType (IPath containerPath) {
		   this();
		   parsePath(containerPath);
	   }
	   
	   public ContainerType () {
		   super();		
		   pathType=SWT_CONTAINER_PATH_PLATFORM; 
	   }	   
	   /*
	    * containerPath may be null, when it is first created
	    */
	   protected void parsePath (IPath containerPath) {
			if (containerPath!=null && !containerPath.segment(0).equals(SWT_CONTAINER_SIGNITURE))
				throw new IllegalStateException(SWTMessages.SWTContainer_11+containerPath); 
						
			pathType=SWT_CONTAINER_PATH_PLATFORM;
			
			if (containerPath!=null){
				for (int i=1; i<containerPath.segmentCount(); i++) {
					if (containerPath.segment(i).equals(SWT_CONTAINER_SIGNITURE_JFACE))
						setPathType(SWT_CONTAINER_JFACE, true);
					else if (containerPath.segment(i).equals(SWT_CONTAINER_SIGNITURE_PATH_PLATFORM))
						setPathType(SWT_CONTAINER_PATH_PLATFORM, true);
					else if (containerPath.segment(i).equals(SWT_CONTAINER_SIGNITURE_PATH_PDE))
						setPathType(SWT_CONTAINER_PATH_PDE, true);
					else if (containerPath.segment(i).equals(SWT_CONTAINER_SIGNITURE_PATH_CUSTOM)) {
						setPathType(SWT_CONTAINER_PATH_CUSTOM, true);
						if (containerPath.segmentCount()>=i+2)
							customPath = containerPath.removeFirstSegments(i+1).toString();	
						break;
					}
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
			   return (""); //$NON-NLS-1$
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
				   platformPath = SWTConfigurationContributor.getFilePath(Platform.resolve(Platform.getBundle(swtLibraries[0].getPluginID()).getEntry("/"))).toOSString(); //$NON-NLS-1$
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
				pdeVersion=PDECore.getDefault().getModelManager().findModel("org.eclipse.swt").getBundleDescription().getVersion().toString(); //$NON-NLS-1$
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
		public String getLegacySrcPluginID() {
			return legacySrcPluginID;
		}		
		public String toString() {
			StringBuffer sb = new StringBuffer("pluginID="); //$NON-NLS-1$
			sb.append(pluginID);
			if (includesLibraries)
				sb.append(" [With Libraries]"); //$NON-NLS-1$
			else
				sb.append(" [No Libraries]"); //$NON-NLS-1$
			sb.append(", legacyID="); //$NON-NLS-1$
			sb.append(legacyID);
			sb.append(", legacyJarPath="); //$NON-NLS-1$
			sb.append(legacyJarPath);
			sb.append(", legacyLibPath="); //$NON-NLS-1$
			sb.append(legacyLibPath);
			sb.append(", srcPluginID="); //$NON-NLS-1$
			sb.append(srcPluginID);				
			return sb.toString();
		}
	}



	
	private IClasspathEntry[] fClasspathEntries = new IClasspathEntry[0];
	
	private IPath containerPath;	// path for container, NOT path for resolved entry
	private IJavaProject project;
	boolean initialized = false;
		
	private ContainerType containerType = null;
	
	// If the PDE target had changed, we may neet to contribute entries... so
	// signal the project.
	private IPluginModelListener pdeModelListener = new IPluginModelListener() {			
		public void modelsChanged(PluginModelDelta delta) {
			// TODO can be more efficient here; for now anyting is a refresh
			try {
				// Clean up, and create a new container instance
				removelisteners();
				ClasspathContainerInitializer initializer = JavaCore.getClasspathContainerInitializer(getPath().segment(0));
				initializer.initialize(getPath(),project);
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e);
			} catch (CoreException e) {
				JavaVEPlugin.log(e);
			}
		}			
	};
	
	
	private boolean isClassPathChanged(IJavaElementDelta[] deltas) {
		if (deltas==null)
			return false;
		for (int i = 0; i < deltas.length; i++) {
			IResourceDelta[] rdeltas = deltas[i].getResourceDeltas();			
			if (rdeltas!=null && rdeltas.length>0) {
				for (int j = 0; j < rdeltas.length; j++) {
					if (rdeltas[j].getFullPath().lastSegment().equals(".classpath")) {						 //$NON-NLS-1$
						return true;
					}					
				}
			}
			if (isClassPathChanged(deltas[i].getChangedChildren()))
				return true;
		}
		return false;
	}
	
	// listen to changes to changes the class path; if we are removed, clean up
	private IElementChangedListener javaModelListener = new IElementChangedListener() {	
		public void elementChanged(ElementChangedEvent event) {
			if (isClassPathChanged(new IJavaElementDelta[] {event.getDelta()})) {
				synchronized (this) {
						// Check to see if we are still "the" SWT container for this project
					try {
						// Container could be cached on the projet even if it is not on the classpath.
						IClasspathContainer c = JavaCore.getClasspathContainer(containerType.getContainerPath(), project);
						if (c==null || c!=SWTContainer.this)						
							removelisteners();
						clear();						
					} catch (JavaModelException e) {
						JavaVEPlugin.log(e);
					}
				}
			}
		}	
	};
	
	
	public static boolean isGTK = Platform.WS_GTK.equals(Platform.getWS());
			
	/**
	 * The following path info. provides both pre and post 3.1 locations
	 * so that we can work with pre leveled 3.1 targets
	 *   
	 */
		 			
	public final static JarInfo[] swtLibraries = new JarInfo[] {
			 new JarInfo(	"org.eclipse.swt"+SWT_CONTAINER_WS_PLUGIN_EXT, //$NON-NLS-1$
					 		true,
							"org.eclipse.swt"+"."+Platform.getWS(), //$NON-NLS-1$ //$NON-NLS-2$
							SWT_CONTAINER_WS.append("swt.jar").toPortableString(),  //$NON-NLS-1$
							SWT_CONTAINER_OS.toPortableString(),
							SWT_CONTAINER_SRC_PLUGIN,
//							"org.eclipse.platform."+Platform.getWS()+".source"
							SWT_CONTAINER_SRC_PLUGIN+SWT_CONTAINER_OS_PLUGIN_EXT
						)};
					
	
	
	private final static JarInfo[] swtGTKLibraries = new JarInfo[]{
		    // GTK libraries are all part of the swt fragment included in the swtLibraries in 3.1
			new JarInfo(	"org.eclipse.swt.gtk",  //$NON-NLS-1$
							SWT_CONTAINER_WS.append("swt-pi.jar").toPortableString(), //$NON-NLS-1$
							null),
			new JarInfo(	"org.eclipse.swt.gtk",  //$NON-NLS-1$
							SWT_CONTAINER_WS.append("swt-mozilla.jar").toPortableString(),  //$NON-NLS-1$
							null)
			};
	
	private final static JarInfo[] jfaceLibraries = new JarInfo[] {
			new JarInfo("org.eclipse.jface", "jface.jar", SWT_CONTAINER_SRC_PLUGIN),  //$NON-NLS-1$ //$NON-NLS-2$
			new JarInfo("org.eclipse.jface.text", "jfacetext.jar", SWT_CONTAINER_SRC_PLUGIN),  //$NON-NLS-1$ //$NON-NLS-2$
			new JarInfo("org.eclipse.core.runtime", "runtime.jar", SWT_CONTAINER_SRC_PLUGIN),  //$NON-NLS-1$ //$NON-NLS-2$
			new JarInfo("org.eclipse.core.runtime.compatibility", "compatibility.jar", SWT_CONTAINER_SRC_PLUGIN), 			 //$NON-NLS-1$ //$NON-NLS-2$
			new JarInfo("org.eclipse.osgi", "core.jar", SWT_CONTAINER_SRC_PLUGIN), 					 //$NON-NLS-1$ //$NON-NLS-2$
			new JarInfo("org.eclipse.core.commands", ".", SWT_CONTAINER_SRC_PLUGIN)  //$NON-NLS-1$ //$NON-NLS-2$

	};
	
	
	protected static boolean isLegacy (String version) {		
		StringTokenizer tk = new StringTokenizer(version,"."); //$NON-NLS-1$
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
		removeProblems();
		ArrayList entries = new ArrayList() ;		
		for (int i = 0; i < swtLibraries.length; i++) {
			IClasspathEntry e = null;
			Bundle b = Platform.getBundle(swtLibraries[i].getPluginID());	
			if (b!=null) {
					e = SWTConfigurationContributor.getPlatformPath(swtLibraries[i].getPluginID(),swtLibraries[i].isIncludesLibraries(), swtLibraries[i].getSrcPluginID());
				 if (e!=null)
					entries.add(e);
				 else {					 
					 addProblem(MessageFormat.format(SWTMessages.SWTContainer_44, new Object[] {swtLibraries[i].getPluginID()})); 
				 }
			}
			else
				JavaVEPlugin.log(SWTMessages.SWTContainer_45+swtLibraries[i].getPluginID()); 
		}

		if (containerType.includeJFace()) {
			for (int i = 0; i < jfaceLibraries.length; i++) {
				IClasspathEntry e = SWTConfigurationContributor.getPlatformPath(jfaceLibraries[i].getPluginID(), jfaceLibraries[i].isIncludesLibraries(), jfaceLibraries[i].getSrcPluginID());
				if (e!=null)
					entries.add(e);
				 else {					 
					 addProblem(MessageFormat.format(SWTMessages.SWTContainer_46, new Object[] {jfaceLibraries[i].getPluginID()})); 
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
			if (JavaVEPlugin.isLoggingLevel(Level.INFO))
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
			if (JavaVEPlugin.isLoggingLevel(Level.INFO))
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
					 addProblem(MessageFormat.format(SWTMessages.SWTContainer_47, new Object[] {swtLibraries[i].getLegacyID()})); 
				 }
			}
			else
				JavaVEPlugin.log("SWTContainer: Could not find a plugin for:"+swtLibraries[i].getLegacyID());
		}

		if (containerType.includeJFace()) {
			for (int i = 0; i < jfaceLibraries.length; i++) {
				IPluginModelBase pluginBase = PDECore.getDefault().getModelManager().findModel(jfaceLibraries[i].getLegacyID());
				if (pluginBase!=null) {
					IClasspathEntry e = SWTConfigurationContributor.getLegacyPDEPath(jfaceLibraries[i].getLegacyID(), jfaceLibraries[i].getLegacyJarPath(), jfaceLibraries[i].getLegacyLibPath(), jfaceLibraries[i].getLegacySrcPluginID());
					if (e!=null)
						entries.add(e);
					 else {					 
						 addProblem(MessageFormat.format(SWTMessages.SWTContainer_49, new Object[] {jfaceLibraries[i].getLegacyID()})); 
					 }
				}
				else
					JavaVEPlugin.log("SWTContainer: Could not find a plugin for: "+jfaceLibraries[i].getLegacyID());	 //$NON-NLS-1$
			}
		}

		// GTK libraries are included in seperate SWT fragments
		if (isGTK) {
			for (int i = 0; i < swtGTKLibraries.length; i++) {
				IPluginModelBase pluginBase = PDECore.getDefault().getModelManager().findModel(swtGTKLibraries[i].getLegacyID());
				if (pluginBase!=null) {
					IClasspathEntry e = SWTConfigurationContributor.getLegacyPDEPath(swtGTKLibraries[i].getLegacyID(), swtGTKLibraries[i].getLegacyJarPath(), swtGTKLibraries[i].getLegacyLibPath(), swtGTKLibraries[i].getLegacySrcPluginID());
					if (e!=null)
						entries.add(e);
					 else {					 
						 addProblem(MessageFormat.format(SWTMessages.SWTContainer_51, new Object[] {swtGTKLibraries[i].getLegacyID()})); 
					 }
				}
				else
					JavaVEPlugin.log("SWTContainer: Could not find a plugin for: "+swtGTKLibraries[i].getLegacyID()); //$NON-NLS-1$
			}
			
			
		}
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
					 addProblem(MessageFormat.format(SWTMessages.SWTContainer_53, new Object[] {swtLibraries[i].getLegacyID()})); 
				 }
			}
			else
				JavaVEPlugin.log("SWTContainer: Could not find a plugin for:"+swtLibraries[i].getLegacyID());	
		}

		if (containerType.includeJFace()) {
			for (int i = 0; i < jfaceLibraries.length; i++) {
				IPluginModelBase pluginBase = PDECore.getDefault().getModelManager().findModel(jfaceLibraries[i].getLegacyID());
				if (pluginBase!=null) {
					IClasspathEntry e = SWTConfigurationContributor.getPDEPath(jfaceLibraries[i].getPluginID(), jfaceLibraries[i].getSrcPluginID(), jfaceLibraries[i].isIncludesLibraries());
					if (e!=null)
						entries.add(e);
					 else {					 
						 addProblem(MessageFormat.format(SWTMessages.SWTContainer_55, new Object[] {jfaceLibraries[i].getLegacyID()})); 
					 }
				}
				else {
					// In a PDE environment, it may be a valid problem.  e.g., RCP only imaged does not contain jfacetext
					if (JavaVEPlugin.isLoggingLevel(Level.INFO))
					   JavaVEPlugin.log("SWTContainer: plugin "+jfaceLibraries[i].getLegacyID()+" is not available on the PDE target", Level.INFO);	 //$NON-NLS-1$ $NON-NLS-2$
				}
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
		if (currentPDEPath!=null && currentPDEPath.equals(ct.getPdePath())) 
			return ;
		
		currentPDEPath = ct.getPdePath();
		containerType=ct;
		removeProblems();
		if (isLegacy(PDECore.getDefault().getModelManager().getTargetVersion())) 
			initLegacyPDE(containerPath);
		else
			initPDE(containerPath);
	}
	
	protected void addListeners() {
		if (containerType.isPDEPath()) {
			PDECore.getDefault().getModelManager().addPluginModelListener(pdeModelListener);
		}
		JavaCore.addElementChangedListener(javaModelListener, ElementChangedEvent.POST_CHANGE);
		
	}
	
	protected void removelisteners() {
		if (containerType.isPDEPath()) {
			PDECore.getDefault().getModelManager().removePluginModelListener(pdeModelListener);
		}
		JavaCore.removeElementChangedListener(javaModelListener);
	}
	
	private String currentCustomPath = null;
	protected void initCustom(IPath containerPath) throws IOException {
		 
		IPath resolvedPath= JavaCore.getResolvedVariablePath(new Path(containerType.getCustomPath()));
		if (resolvedPath==null)  {
			removeProblems();
			addProblem(MessageFormat.format(SWTMessages.SWTContainer_57, new Object[] {containerType.getCustomPath()})); 
			fClasspathEntries = new IClasspathEntry[0];
			return;
		}
		if (currentCustomPath!=null && currentCustomPath.equals(resolvedPath.toPortableString()))
			return ;
		currentCustomPath=resolvedPath.toPortableString();
		removeProblems();
		
		ArrayList entries = new ArrayList() ;		
		for (int i = 0; i < swtLibraries.length; i++) {			
			Bundle b = Platform.getBundle(swtLibraries[i].getPluginID());	
			if (b!=null) {
				Bundle[] hosts = Platform.getHosts(b);
				if(hosts!=null && hosts.length>0)
					b = hosts[0];

				String jarName = b.getSymbolicName();
				int index = jarName.lastIndexOf('.');
				jarName = jarName.substring(index+1,jarName.length())+".jar"; //$NON-NLS-1$
				String jarSrc = "src.zip"; //$NON-NLS-1$
				
				IClasspathAttribute[] attr=null;
				if (swtLibraries[i].isIncludesLibraries()) {				
					attr = new IClasspathAttribute[]{ JavaCore.newClasspathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY, 
                        resolvedPath.toPortableString())};
					if (!resolvedPath.toFile().exists())						
							addProblem(MessageFormat.format(SWTMessages.SWTContainer_60, new Object[] {resolvedPath.toPortableString()})); 
				}
				
				IPath jarPath = resolvedPath.append(jarName);
				if (!jarPath.toFile().exists())
					addProblem(MessageFormat.format(SWTMessages.SWTContainer_61, new Object[] {jarPath.toPortableString()})); 
				IClasspathEntry entry = JavaCore.newLibraryEntry(resolvedPath.append(jarName), resolvedPath.append(jarSrc), null, new IAccessRule [0], attr, false);
				if (entry!=null)
				    entries.add(entry);
			}
			else {
				JavaVEPlugin.log("Could not location class path for: "+swtLibraries[i].getPluginID()); //$NON-NLS-1$
			}
		}
		
		if (containerType.includeJFace()) {
			//TODO: nothing at this time
		}
		fClasspathEntries = (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]);
	}

	protected void computeEntries() {
		try {
			// the run method is dependent on job locks
			synchronized (this) {
				if (initialized)
					return ;
				
				if (containerType.isPlatformPath())
					initPlatformPath(containerPath);
				else if (containerType.isPDEPath()) {
					initPDEPath(containerPath);
				} else if (containerType.isCustomPath())
					initCustom(containerPath);
				
				addListeners();
				initialized=true;	
				try {
					JavaCore.setClasspathContainer(containerType.getContainerPath() , new IJavaProject[] {project}, new IClasspathContainer[] {SWTContainer.this}, null);
				} catch (JavaModelException e) {
					JavaVEPlugin.log(e, Level.INFO);
				}
			}
		} catch (IOException e1) {
			JavaVEPlugin.log(e1, Level.INFO);
		}		
	}
	protected void init() {
				
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		if (ws.isTreeLocked())  //TODO: Can not run under a runable
			computeEntries();
		else {
			try {
				// Wrap this in a runnable so that we can add errors to the problems view
				ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor) throws CoreException {
						computeEntries();
					}
				}, null, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
			} catch (CoreException e) {
				JavaVEPlugin.log(e, Level.INFO);
			}
		}
	}
	
	public SWTContainer(final IPath containerPath, IJavaProject project) {
		this.containerPath = containerPath;
		this.project = project;
		containerType = new ContainerType(containerPath);		
	}
	
	private void clear() {		
		initialized=false;
		fClasspathEntries = new IClasspathEntry[0];
		currentCustomPath=null;
		currentPDEPath=null;
	}
	
	protected boolean hasChanged() {
		if (containerType.isPDEPath()) {
			ContainerType ct = new ContainerType(containerPath);
			if (currentPDEPath==null || !currentPDEPath.equals(ct.getPdePath())) 
				return true;
		}
		else if (containerType.isCustomPath()) {
			IPath resolvedPath= JavaCore.getResolvedVariablePath(new Path(containerType.getCustomPath()));
			if (currentCustomPath==null || !currentCustomPath.equals(resolvedPath.toPortableString()))
				return true ;
		}
		return false; // platform path will "never" change
	}
	
	public  IClasspathEntry[] getClasspathEntries() {
		if (!initialized)
			init();
		else  if (hasChanged()) {
			clear();
			init();
		}		
		return fClasspathEntries;
	}

	public String getDescription() {
		return SWTMessages.SWTContainer_Description; 
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
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(project.getElementName());
		sb.append("\ninitialized="); //$NON-NLS-1$
		sb.append(initialized+"\n"); //$NON-NLS-1$
		for (int i = 0; i < fClasspathEntries.length; i++) {
			sb.append(fClasspathEntries[i].getPath().toPortableString());			
		}
		return sb.toString();
	}
}
