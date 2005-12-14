/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
 *  $Revision: 1.38 $  $Date: 2005-12-14 21:44:40 $ 
 */
package org.eclipse.ve.internal.swt;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.osgi.framework.adaptor.core.AbstractFrameworkAdaptor;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.*;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.PluginModelManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;





/**
 * SWT Configuration Contributor.
 * 
 * This is a stateless class, so the static can be used for any programatic usage of this.
 * 
 * @since 1.0.0
 */
public class SWTConfigurationContributor extends ConfigurationContributorAdapter {
	
	public static final String SWT_BUILD_PATH_MARKER = "org.eclipse.ve.swt.buildpath";	 //$NON-NLS-1$	
	
	protected IJavaProject javaProject;
	protected IConfigurationContributionInfo fConfigContributionInfo;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#initialize(org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo)
	 */
	public void initialize(IConfigurationContributionInfo info) {
		super.initialize(info);
		this.javaProject = info.getJavaProject();
		this.fConfigContributionInfo = info;
	}

/**
 * 
 * @param name
 * @return Should this file be extractef from a jar
 * 
 * @since 1.1.0
 */	
protected static boolean isInterestingLibFile(String name) {
	return name.endsWith(".dll") //$NON-NLS-1$
			|| name.endsWith(".jnilib") //$NON-NLS-1$
			|| name.endsWith(".sl") //$NON-NLS-1$
			|| name.endsWith(".a") //$NON-NLS-1$
			|| name.indexOf(".so") != -1; //$NON-NLS-1$
}



static private Set libraryCaches = new HashSet();  // Store the configured caches in this session
static final private String argSeperator = "|"; //$NON-NLS-1$

static public URL generateLibCacheIfNeeded (IFragmentModel frag, String relativePath) {
	return generateLibCacheIfNeeded(frag.getInstallLocation(), relativePath);
}

/*
 * If a cache is not there already, create a cache location from the jar 
 * and return a URL to the cache.
 */
static public URL generateLibCacheIfNeeded (String srcJarFile, String relativePath) {		
		if (srcJarFile == null)
			return null;
		// These are cache locations that we were dependant on at some time this
		// session
		libraryCaches.add((srcJarFile+argSeperator+relativePath).intern());
		File f = new File(srcJarFile);
		if (f.isDirectory())
			try {
				return f.toURL();
			} catch (MalformedURLException e2) {
				JavaVEPlugin.log(e2);
			}
		// Create a root path for each version of .dlls
		IPath root = JavaVEPlugin.VE_GENERATED_LIBRARIES_CACHE.append(Integer.toString(f.getAbsolutePath().hashCode()));
		URL url = null;  // result
		synchronized (root.toString().intern()) {  // Thread safe
			File target = root.append(relativePath).toFile();
			if (target.exists()) {
				try {
					return target.toURL();
				} catch (MalformedURLException e1) {
					JavaVEPlugin.log(e1);
					return null;
				}
			}
			target.mkdirs();
					
			File src = new File(srcJarFile);			
			if (src.isFile() && src.getName().endsWith(".jar")) { //$NON-NLS-1$			
				try {
					ZipFile zip = new ZipFile(src);
					try {				
						for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
							ZipEntry entry = (ZipEntry) entries.nextElement();
							if (entry.getName().startsWith(relativePath) &&
								isInterestingLibFile(entry.getName())) {
								InputStream in = zip.getInputStream(entry);						
								try {
									if (in!=null) {
										File dest = new File (root.toFile(), entry.getName());
										AbstractFrameworkAdaptor.readFile(in, dest);
										if (!Platform.getOS().equals(Constants.OS_WIN32))
											Runtime.getRuntime().exec(new String[] {"chmod", "755", dest.getAbsolutePath()}).waitFor(); //$NON-NLS-1$ //$NON-NLS-2$
									}
								} catch (IOException e) {
									JavaVEPlugin.log(e);
								} catch (InterruptedException e) {
									JavaVEPlugin.log(e);
								}							
								finally {
									try {
								     in.close();
									}
									catch (Exception e) {}
								}
							}
							
						}
						url = target.toURL();
					} catch (IOException e) {				
						JavaVEPlugin.log(e);
					} 
					finally {
						try {
							zip.close();
						} catch (IOException e) {}
					}
				} catch (ZipException e) {
					JavaVEPlugin.log(e);
				} catch (IOException e) {
					JavaVEPlugin.log(e);
				}			
			}
		}
		return url;		
}
		
	
    protected void contributePluginLibrary(final IConfigurationContributionController controller) throws CoreException {
				
		final IPluginModelBase swtModel = PDECore.getDefault().getModelManager().findModel(SWTContainer.swtLibraries[0].getPluginID());
		// Run it as a runnable, so that we can update the problems view
	    ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					final String msg = SWTMessages.SWTConfigurationContributor_CouldntResolveDLLInPDE_ERROR_; 
					if (swtModel!=null && swtModel.isEnabled()) {			
						final URL librarylocation = generateLibCacheIfNeeded(swtModel.getBundleDescription().getLocation(),"");			 //$NON-NLS-1$
						if (librarylocation!=null) {						
							controller.contributeClasspath(librarylocation, IConfigurationContributionController.APPEND_JAVA_LIBRARY_PATH);
							removeMarker(msg);
						}
						else
							createMarker(msg);
					}
					else
						createMarker(msg);
				}
	    }, null, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());		
    }
	
	protected static URL getSWTLegacyOSPath () {
		PluginModelManager pm = PDECore.getDefault().getModelManager();
		IFragmentModel[] frags = pm.getFragments();			
		URL os = null;
		for (int i = 0; i < frags.length; i++) {
			if (frags[i].getBundleDescription().getSymbolicName().startsWith("org.eclipse.swt") && //$NON-NLS-1$
				frags[i].getBundleDescription().getHost().getName().equals("org.eclipse.swt")) { //$NON-NLS-1$
				// swt fragment					
				if (frags[i].getBundleDescription().getSymbolicName().startsWith("org.eclipse.swt.nl"))  //$NON-NLS-1$
					continue; // skip the nl ones
				os = getResourceURL(frags[i], SWTContainer.SWT_CONTAINER_OS.toPortableString());
				if (os!=null){	
				  return os;
				}
			}
		}	
		return null;
	}
	
	
	static  String [] getSrcConfig (IPluginModelBase plugin) {
		ArrayList configs = new ArrayList();
		IPluginExtension[] extensions = plugin.getExtensions().getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IPluginExtension extension = extensions[i];
			if ((PDECore.getPluginId() + ".source").equals(extension.getPoint())) { //$NON-NLS-1$
				IPluginObject[] children = extension.getChildren();
				for (int j = 0; j < children.length; j++) {
					if (children[j].getName().equals("location")) { //$NON-NLS-1$
						IPluginElement element = (IPluginElement) children[j];
						String pathValue = element.getAttribute("path").getValue(); //$NON-NLS-1$b	
						configs.add(pathValue);						
					}
				}
			}
		}	
		return (String[]) configs.toArray(new String[configs.size()]);
	}
	
	protected static IPath getPDESrceLocationFor(IPluginModelBase plugin, String pkgName, String srcPath, String srcPluginID) {
		IPath result = (IPath) platformSrcPath.get(plugin);
		if (result != null) return result;
		String rel[] = getSrcConfig(plugin);			
		if (rel.length==0)
			result = getFilePath(getResourceURL(plugin, srcPath));
		else
			for (int i = 0; i < rel.length; i++) {
				result = getFilePath(getResourceURL(plugin, rel[i]+"/"+srcPath)); //$NON-NLS-1$
				if (result!=null) break;					
			}
		if (result!=null) {			   
			platformSrcPath.put(plugin,result);
			return result;
		}
			
		if (pkgName.endsWith(".jar")) { //$NON-NLS-1$
			pkgName = pkgName.substring(0,pkgName.lastIndexOf(".jar")); //$NON-NLS-1$
		}
		
		if (srcPluginID!=null) {
			IPluginModelBase srcPlugin = PDECore.getDefault().getModelManager().findModel(srcPluginID);
			if (srcPlugin==null) return null;
			BundleDescription[] fragDesc = srcPlugin.getBundleDescription().getFragments();
			ArrayList plugins = new ArrayList();
			plugins.add(srcPlugin);
			for (int i = 0; i < fragDesc.length; i++) 
				plugins.add(PDECore.getDefault().getModelManager().findModel(fragDesc[i].getSymbolicName()));

	        for (int i = 0; i < plugins.size(); i++) {
				IPluginModelBase p = (IPluginModelBase)plugins.get(i);
				rel = getSrcConfig(p);					
				if (rel.length==0)
					result = getFilePath(getResourceURL(p, pkgName+"/"+srcPath)); //$NON-NLS-1$
				else
					for (int j= 0; j < rel.length; j++) {
						Path pre = new Path(rel[j]);
						result = getFilePath(getResourceURL(p, pre.append(pkgName).append(srcPath).toPortableString()));
						if (result!=null) break;					
					}
				if (result!=null) {
					platformSrcPath.put(plugin, result);	
					break;
				}
	        }
		}	
		return result;
	}
	
	public static IClasspathEntry getLegacyPDEPath (String pluginID, String jarPath, String libPath, String srcPluginID) {
							
		IPluginModelBase base = PDECore.getDefault().getModelManager().findModel(pluginID);
		IPath baseLocation = new Path (base.getInstallLocation());
		IPath location = baseLocation.append(jarPath);
		IPath libLocation = libPath!=null ? new Path (base.getInstallLocation()).append(libPath): null;		
		String srcPath = jarPath.substring(0,jarPath.lastIndexOf('.'))+"src.zip"; //$NON-NLS-1$
		IPath srcLocation = getPDESrceLocationFor(base, baseLocation.lastSegment(), srcPath, srcPluginID);
		
		IClasspathAttribute[] attr = new IClasspathAttribute[0];
		if (libLocation!=null)			
		    attr = new IClasspathAttribute[]{ JavaCore.newClasspathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY, libLocation.toPortableString())};
		return JavaCore.newLibraryEntry(location, srcLocation, null, new IAccessRule [0], attr, false);		
	}
	
	public static IClasspathEntry getPDEPath (String pluginID, String srcPluginID, boolean lib) {		
		IPluginModelBase base = PDECore.getDefault().getModelManager().findModel(pluginID);		
		IResource r = ResourcesPlugin.getWorkspace().getRoot().findMember(base.getBundleDescription().getName());
		IPath baseLocation = new Path (base.getInstallLocation());
		IClasspathAttribute[] attr = new IClasspathAttribute[0];
		if (r!=null && r instanceof IProject) { 
			IPath pLocation = r.getLocation();
			if (pLocation.equals(baseLocation)) {
				// Classpath is a project in an IDE workspace
				if (lib)
				    attr = new IClasspathAttribute[]{ JavaCore.newClasspathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY, baseLocation.toPortableString())};
				return JavaCore.newProjectEntry(r.getFullPath() ,new IAccessRule[0], false, attr, false);
			}
		}
		IPath location = baseLocation;			
		IPath libLocation = lib? getFilePath(generateLibCacheIfNeeded(base.getBundleDescription().getLocation(),"")): null;		 //$NON-NLS-1$
		String srcPath = "src.zip"; //$NON-NLS-1$
		IPath srcLocation = getPDESrceLocationFor(base, baseLocation.lastSegment(), srcPath, srcPluginID);
		
		
		if (libLocation!=null)			
			attr = new IClasspathAttribute[]{ JavaCore.newClasspathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY, libLocation.toPortableString())};
		return JavaCore.newLibraryEntry(location, srcLocation, null, new IAccessRule [0], attr, false);		
    }
		
    protected void contributeLegacyPluginLibrary(final IConfigurationContributionController controller) throws CoreException {		
		// Assume target's have the same platform/architecture/overrides
				
		URL	os = getSWTLegacyOSPath();
		if (os!=null){	
			// if our DLL is inside a .jar extract it out to out private cache.
		   if (os.toString().startsWith("jar")) //$NON-NLS-1$
				os = generateLibCacheIfNeeded(getFilePath(os).toPortableString(), SWTContainer.SWT_CONTAINER_OS.toPortableString());
		}

        final String msg = SWTMessages.SWTConfigurationContributor_CouldntResolveDLLInPDE_ERROR_; 
        final URL osURL = os;
        ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				if (osURL!=null) {						
				    controller.contributeClasspath(osURL, IConfigurationContributionController.APPEND_JAVA_LIBRARY_PATH);
				    removeMarker(msg);
				}
				else
					createMarker(msg);				
		}
		}, null, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
		
		if(Platform.WS_GTK.equals(Platform.getWS())){
			controller.contributeClasspath(Platform.getBundle("org.eclipse.ve.swt"), "$os$", IConfigurationContributionController.APPEND_JAVA_LIBRARY_PATH, false); //$NON-NLS-1$ //$NON-NLS-2$
		}		
		
    }

	   /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeClasspaths(org.eclipse.jem.internal.proxy.core.IConfigurationContributionController)
	 */
	public void contributeClasspaths(final IConfigurationContributionController controller) throws CoreException {
		// Add the jar file with the supporting classes required for the JVE into the classpath
		// In development model the file proxy.jars redirects this to the plugin development project
		controller.contributeClasspath(SwtPlugin.getDefault().getBundle(), "vm/jbcfswtvm.jar", IConfigurationContributionController.APPEND_USER_CLASSPATH, false); //$NON-NLS-1$
		// If GTK or Carbon is the platform, then contribute the native library which does the offscreen screen-scrape
		if(SWTContainer.isGTK || SWTContainer.isCarbon)
			controller.contributeClasspath(Platform.getBundle("org.eclipse.ve.swt"), "$os$", IConfigurationContributionController.APPEND_JAVA_LIBRARY_PATH, false); //$NON-NLS-1$ //$NON-NLS-2$
		
		if (!ProxyPlugin.isPDEProject(javaProject)) {
			// SWT container would have contributed all the proper jars, and DLLs... if it is
			// not in the class path, we may have many other issues.
			//
			// But, it is possible that a project clean removed the library cache since
			// the time the container created it.
			// Typically the expectation is 1 (or a few)cache locations
			for (Iterator itr = libraryCaches.iterator(); itr.hasNext();){
				StringTokenizer st = new StringTokenizer((String)itr.next(),argSeperator);
				if (st.countTokens()>1)
					generateLibCacheIfNeeded(st.nextToken(),st.nextToken());
				else
					generateLibCacheIfNeeded(st.nextToken(),""); //$NON-NLS-1$
			}
		}
		else {
			// This is only to suppor the SWT's Libraries
			PluginModelManager pm = PDECore.getDefault().getModelManager();
			final IPluginModelBase swtEntry = pm.findModel("org.eclipse.swt"); //$NON-NLS-1$
			Version version = swtEntry.getBundleDescription().getVersion();
			// TODO: this is a temporary stop gap measure... we should generalize plugin projects into the LocalFileConfigurationContributorController			
			if (version.getMajor() < 3 || version.getMinor() < 1)
				contributeLegacyPluginLibrary(controller);
			else // 3.1 and up
			    contributePluginLibrary(controller);
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
			map.put("DISPLAY", "DISPLAY:0:0");				 //$NON-NLS-1$ //$NON-NLS-2$
			config.setAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES, map);
		}
		
		if (Platform.OS_MACOSX.equals(Platform.getOS()))
		{
			// Need to add the -Djava.awt.headless=true vm arg if running on OS X
			StringBuffer vmArgs = new StringBuffer(config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""));  //$NON-NLS-1$
			vmArgs.append(" -Djava.awt.headless=true");  //$NON-NLS-1$
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmArgs.toString());
		}
	}


	protected IMarker[] getMarker(String msg) {
		List result = new ArrayList();
		
		try {
			IMarker[] markers = javaProject.getProject().findMarkers(SWT_BUILD_PATH_MARKER, false, IResource.DEPTH_ZERO);		
			for (int i = 0; i < markers.length; i++) {
				if (markers[i].getAttribute(IMarker.MESSAGE).equals(msg))
						result.add(markers[i]);
			}
		} catch (CoreException e) {
			SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
		}
		return (IMarker[]) result.toArray(new IMarker[result.size()]);
	}
	/**
	 * 
	 * @param markerID
	 * @param msg  null to delete all msgs.  If not null, will not be deleted
	 * @return true of a maker already exits
	 * 
	 * @since 1.1.0
	 */
	protected void removeMarker(String msg) {
		
		try {
			IMarker[] marks = getMarker(msg);
			for (int i = 0; i < marks.length; i++) {
				marks[i].delete();
			}			
		} catch (CoreException e) {
			SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
		}
	}
	
	protected void createMarker(String msg) {
		try {
			// It is bad, if there is a marker of this type, just leave it there, it 
			// is already for this message. else add in the new marker.
			if (getMarker(msg).length==0) {
				IMarker marker = javaProject.getProject().createMarker(SWT_BUILD_PATH_MARKER);				
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				marker.setAttribute(IMarker.MESSAGE, msg);									
			}
		} catch (CoreException e) {
			SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry registry) {		
		// [70275] Need a marker if VM is less than 1.4.2 because of a bug with beaninfo.
		if (javaProject != null) {
			boolean versOk = true;	// Default is true, and if for some reason can't parse the version, then it will still be true because we don't know.
			IBeanProxy version = registry.getMethodProxyFactory().getInvokable("java.lang.System", "getProperty", new String[] {"java.lang.String"}).invokeCatchThrowableExceptions(null, registry.getBeanProxyFactory().createBeanProxyWith("java.version")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			if (version instanceof IStringBeanProxy) {
				// We got the version
				StringTokenizer versTokens = new StringTokenizer(((IStringBeanProxy) version).stringValue(), "._"); //$NON-NLS-1$
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
							removeMarker(SWTMessages.Marker_BuildPathNot142); 
						} else {
							createMarker(SWTMessages.Marker_BuildPathNot142); 
						}
					}
				}, null, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
			} catch (CoreException e) {
				SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
			}
		}
	}
	private static URL getResourceURL(IPluginModelBase frag, String relativePath) {
		String location = frag.getInstallLocation();
		if (location == null)
			return null;
		
		File file = new File(location);
		URL url = null;
		try {
			if (file.isFile() && file.getName().endsWith(".jar")) { //$NON-NLS-1$
				ZipFile zip = new ZipFile(file);
				if (zip.getEntry(relativePath) != null) {
					url = new URL("jar:file:" + file.getAbsolutePath() + "!/" + relativePath); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else if (new File(file, relativePath).exists()){
				url = new URL("file:" + file.getAbsolutePath() + Path.SEPARATOR + relativePath); //$NON-NLS-1$
			}
		} catch (IOException e) {
		}
		return url;
	}
	
	public static IPath getFilePath(URL l) {
		if (l != null) {
			if (l.getProtocol().equals("file")) //$NON-NLS-1$
			     return  new Path (l.getFile());
			else if (l.getProtocol().equals("jar")) { //$NON-NLS-1$
				String f = l.getFile();
				int idx = f.lastIndexOf('!');
				if (idx>=0)
					f = f.substring(0,idx);
				try {
					return getFilePath (new URL(f));
				} catch (MalformedURLException e) {}
			}
		}
		return null;
	}	
	
	protected static String[] getSrcConfig(Bundle b) {				
		Bundle[] frags = Platform.getFragments(b);		
		if (frags==null)
			frags = new Bundle[0];
		String[] names = new String [frags.length+1];
		names[0] = b.getSymbolicName();
		for (int i = 1; i < names.length; i++) {
			names[i]=frags[i-1].getSymbolicName();			
		}
		List fragNames = Arrays.asList(names);
		
		ArrayList result = new ArrayList();
		IConfigurationElement[] ces = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.pde.core.source"); //$NON-NLS-1$
		for (int i = 0; i < ces.length; i++) {	
            if (!fragNames.contains((ces[i].getDeclaringExtension().getNamespace()))) 
				continue;
			String p = ces[i].getAttributeAsIs("path"); //$NON-NLS-1$
			if (!result.contains(p))
			result.add(p);
		}
		return (String[])result.toArray(new String[result.size()]);
	}
	
	// cache bundle to src.zip path
	private static HashMap platformSrcPath = new HashMap(10);
	/**
	 * 
	 * We will NOT look at all the org.eclipse.pde.core.source contributions, this
	 * is quite expensive on a large configuration
	 * 
	 * @param bundle 
	 * @param pkgName, String srcPluginID the pkged name of the bundle (id, version etc)
	 * @param srcPluginID.. potential plugin that house the src. 
	 * @return path for src.zip, null if can not find it
	 * 
	 * The source may be inside the bundle itself, one of its fragments, 
	 * or in a different bundle (e.g., org.eclipse.platform.source)
	 * 
	 * @since 1.1.0
	 */
	public static IPath getPlatformSrcLocationFor(Bundle bundle, String pkgName, String srcPluginID) {
		
		// src looks on the FS... so cache it for the Platform
		IPath result = (IPath) platformSrcPath.get(bundle);
		if (result != null) return result;
		
		String srcFile = "src.zip";		 //$NON-NLS-1$
		try {						
			String rel[] = getSrcConfig(bundle);
			URL u=null;
			if (rel.length==0)
			   u = bundle.getEntry(srcFile);
			else
				for (int i = 0; i < rel.length; i++) {
					u = bundle.getEntry(rel[i]+"/"+srcFile); //$NON-NLS-1$
					if (u!=null) break;					
				}
			if (u!=null) {
			    result = getFilePath(Platform.resolve(u));
				platformSrcPath.put(bundle,result);
				return result;
			}
		} catch (IOException e) {}
		
		if (pkgName.endsWith(".jar")) { //$NON-NLS-1$
			pkgName = pkgName.substring(0,pkgName.lastIndexOf(".jar")); //$NON-NLS-1$
		}
		
		if (srcPluginID!=null) {
			Bundle srcBundle = Platform.getBundle(srcPluginID);
			String rel[] = getSrcConfig(srcBundle);
			URL u = null;
			if (rel.length==0)
				u = Platform.find(srcBundle, new Path(pkgName).append(srcFile));
			else
				for (int i = 0; i < rel.length; i++) {
					u = Platform.find(srcBundle, new Path(rel[i]).append(pkgName).append(srcFile));
					if (u!=null) break;					
				}
			if (u!=null) {
			   try {
				result = getFilePath(Platform.resolve(u));
				platformSrcPath.put(bundle,result);
				return result;
			   } catch (IOException e) {}			   
			}
		}
		return null;
	}
	
	/**
	 * return the class entry for the fragmentName associated with the pluginID with
	 * a library path as an attribute if libPath is not null
	 * @param pluginID
	 * @param fragmentName
	 * @return entry, or null if none
	 * 
	 * @since 1.1.0
	 */
	public static IClasspathEntry getPlatformPath (String pluginID,  boolean libPath, String srcPluginID) {
		

		Bundle bundle = Platform.getBundle(pluginID);
		
		if (bundle.getState()!=Bundle.ACTIVE &&
		    bundle.getState()!=Bundle.RESOLVED)
			return null;
		
		IPath location = null;
		try {				
			// .jared fragment
			URL l = Platform.resolve(bundle.getEntry("/"));	 //$NON-NLS-1$
			boolean project = l.getProtocol().equals("file"); // This library is inside the workbench... 			 //$NON-NLS-1$
			location = new Path(ProxyPlugin.getFilePath(l).getFile()).removeTrailingSeparator();
			// not supporting a worbench project at this time ... .jar only
			if (location !=null && location.lastSegment().endsWith(".jar")) { //$NON-NLS-1$
				IPath src = getPlatformSrcLocationFor(bundle, location.lastSegment(), srcPluginID);
				IClasspathAttribute[] attr = new IClasspathAttribute[0];
				if (libPath) { // Create a lib path attribute to this entry						
					// jar may be imported into the workbench, use it; if not cache it
					URL libURL = project ? location.toFile().toURL() : 
						         generateLibCacheIfNeeded(location.toPortableString(), ""); //$NON-NLS-1$
					attr = new IClasspathAttribute[]{ JavaCore.newClasspathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY, 
									getFilePath(libURL).toPortableString())};
				}										
				return JavaCore.newLibraryEntry(location, src, null, new IAccessRule [0], attr, false);
			}
		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
		return null;
	}	
}
