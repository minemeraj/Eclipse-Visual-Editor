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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osgi.framework.Bundle;

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
		String pdePath = null;
	
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
			pathType=SWT_CONTAINER_PATH_PLATFORM;
			
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
		/**
		 * 
		 * @param pathType  [SWT_CONTAINER_TARGET_PATH, SWT_CONTAINER_CUSTOMED_PATH, SWT_CONTAINER_PLATFORM_PATH] | SWT_CONTAINER_JFACE_PATH 
		 * @param customPath In the case of SWT_CONTAINER_CUSTOMED_PATH, target path
		 * @return
		 * 
		 * @since 1.1.0
		 */
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
				pathType &= SWT_CONTAINER_PATH_PLATFORM; // Forec a default
		}		
		public boolean isPlatformPath() {
			return (pathType&SWT_CONTAINER_PATH_PLATFORM)>0;
		}		
		public boolean isTargetPath() {
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
			if (pdePath==null)
				pdePath = SWTConfigurationContributor.getPDEPath();
			return pdePath;
		}		
		public String getPlatformPath() {
			if (platformPath==null)
				platformPath = SWTConfigurationContributor.getPlatformPath(swtLibraries[0][0], swtLibraries[0][3], swtLibraries[0][4]).getPath().toOSString();
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
	
    }



	
	private IClasspathEntry[] fClasspathEntries;
	
	private IPath containerPath;	// path for container, NOT path for resolved entry
		
	private ContainerType containerType = null;
	
	
	private boolean isGTK = Platform.WS_GTK.equals(Platform.getWS());
			
	/**
	 * The following path info. provides both pre and post 3.1 locations
	 * so that we can work with pre leveled 3.1 targets
	 * [ <pluginID>,  
	 *        <pre 3.1 jar name>, <pre 3.1 lib path>,
	 *        <post 3.1 fragment name> <post 3.1 lib Path> ]
	 *   <pluginID> required
	 *   <pre 3.1 jar name> .jar inside the plugin
	 *   <pre 3.1 lib Path> optional library that is required by this jar
	 *   <post 3.1 fragment name> optional jared fragment name, if not the plugin itself  
	 *   <post 3.1 lib Path> optional required library. path is within the jared fragement
	 *   
	 */
	
	public final static String SWT_CONTAINER_LIB_FRAGMENT_NAME = "org.eclipse.swt"+"."+Platform.getWS()+"."+Platform.getOS()+"."+Platform.getOSArch();
	 			
	private final static String[][] swtLibraries = new String[][] {
			{ "org.eclipse.swt", 
					"$ws$/swt.jar", null,
					SWT_CONTAINER_LIB_FRAGMENT_NAME, ""  } //$NON-NLS-1$ //$NON-NLS-2$
	};
	
	private final static String[][] swtGTKLibraries = new String[][]{
		    // GTK libraries are all part of the swt fragment included in the swtLibraries in 3.1
			{"org.eclipse.swt.gtk", 
					"$ws$/swt-pi.jar", null, 
					null, null },  //$NON-NLS-1$ //$NON-NLS-2$
			{"org.eclipse.swt.gtk", 
					"$ws$/swt-mozilla.jar", null, 
					null, null } //$NON-NLS-1$ //$NON-NLS-2$
	};
	
	private final static String[][] jfaceLibraries = new String[][] {
			{ "org.eclipse.jface", 
					"jface.jar", null, 
					null, null }, //$NON-NLS-1$ //$NON-NLS-2$
			{ "org.eclipse.jface.text", 
					"jfacetext.jar", null, 
					null, null }, //$NON-NLS-1$ //$NON-NLS-2$
			{ "org.eclipse.core.runtime", 
					"runtime.jar", null, 
					null, null  }, //$NON-NLS-1$ //$NON-NLS-2$
			{ "org.eclipse.core.runtime.compatibility", 
					"compatibility.jar", null, 
					null, null }, //$NON-NLS-1$ //$NON-NLS-2$			
			{ "org.eclipse.osgi", 
					"core.jar", null, 
					null, null }, //$NON-NLS-1$ //$NON-NLS-2$
			{ "org.eclipse.core.commands", 
						".", null, 
						null, null } //$NON-NLS-1$ //$NON-NLS-2$
	};
	

	

	
	/**
	 * Return the class path entry for the .jar resource inside a plugin/fragment.  This is 
	 * the way jars were packaged before Eclipse 3.1
	 * @param pluginID
	 * @param jarPath
	 * @return entry, null if not.
	 * 
	 * @since 1.1.0
	 */
	private IClasspathEntry getPlatformLegacyPath (String pluginID, String jarPath, String libPath) {
		URL[] locSrc = ProxyPlugin.getPlugin().findPluginJarAndAttachedSource(Platform.getBundle(pluginID), new Path(jarPath));		 				
		Path  path;
		Path srcPath;
		try {
			path = new Path(Platform.resolve(locSrc[0]).getFile());
			srcPath = null;
			if (locSrc.length>1 && locSrc[1] != null)
				srcPath = new Path(Platform.resolve(locSrc[1]).getFile());
			if (libPath==null)
			   return JavaCore.newLibraryEntry(path, srcPath, null);
			else {
				IClasspathAttribute[] attr = getPlatformLegacyLibraryPath(pluginID, libPath);
				return JavaCore.newLibraryEntry(path, srcPath, null, new IAccessRule [0], attr, false);
			}
		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
		return null;				
	}
	
	private IClasspathAttribute [] getPlatformLegacyLibraryPath(String pluginID, String libPath) {
		URL[] loc = ProxyPlugin.getPlugin().findPluginJarAndAttachedSource(Platform.getBundle(pluginID), new Path(libPath));		 				
		try {
			String path = Platform.resolve(loc[0]).getFile(); 	
			return new IClasspathAttribute[]{JavaCore.newClasspathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY,path)};			
		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
		return null;				
		
	}
		
	private boolean isLegacy (Bundle b, String[] pathInfo) {
		if (pathInfo[2]==null) return true;  // No bundle information
		StringTokenizer tk = new StringTokenizer((String) b.getHeaders().get("Bundle-Version"),".");
		try {
			int Major = Integer.parseInt(tk.nextToken());
			int Minor = Integer.parseInt(tk.nextToken());
			if ((Major>3) || (Major==3 && Minor>=1))
				return false;
		} catch (NumberFormatException e) {}
		return true;
	}
	
	protected void initPlatformPath(IPath containerPath) throws IOException {

		ArrayList entries = new ArrayList() ;		
		for (int i = 0; i < swtLibraries.length; i++) {
			IClasspathEntry e = null;
			Bundle b = Platform.getBundle(swtLibraries[i][0]);	
			if (b!=null) {
//				 if (!isLegacy(b,swtLibraries[i])) 
					e = SWTConfigurationContributor.getPlatformPath(swtLibraries[i][0], swtLibraries[i][3], swtLibraries[i][4]);
//				 if (e==null)
//				 e = getPlatformLegacyPath(swtLibraries[i][0],swtLibraries[i][1]);				
				 if (e!=null)
					entries.add(e);
			}
			else
				JavaVEPlugin.log("Could not location class path for: "+swtLibraries[i][0]);				
		}

		if (containerType.includeJFace()) {
			for (int i = 0; i < jfaceLibraries.length; i++) {
				IClasspathEntry e = SWTConfigurationContributor.getPlatformPath(jfaceLibraries[i][0], jfaceLibraries[i][3], jfaceLibraries[i][4]);
				if (e!=null)
					entries.add(e);
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
	
	

	
	public SWTContainer(IPath containerPath) {
		this.containerPath = containerPath;
		containerType = new ContainerType(containerPath);		
		try {
			
		    if (containerType.isPlatformPath()) 
				initPlatformPath(containerPath);
			


		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
	}
	public IClasspathEntry[] getClasspathEntries() {
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
