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
 *  $Revision: 1.22 $  $Date: 2005-05-05 13:02:27 $ 
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
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.osgi.framework.adaptor.core.AbstractFrameworkAdaptor;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.pde.core.plugin.IFragmentModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.PluginModelManager;
import org.osgi.framework.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.swt.SWTREMProxyRegistration;

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
	public static final IPath SWT_OS_PATH =  new Path("os/"+Platform.getOS()+"/"+Platform.getOSArch());
	
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
	
protected static boolean isInterestingLibFile(String name) {
	return name.endsWith(".dll")
			|| name.endsWith(".jnilib")
			|| name.endsWith(".sl")
			|| name.endsWith(".a")
			|| name.indexOf(".so") != -1;
}

static public URL generateLibCacheIfNeeded (IFragmentModel frag, String relativePath) {
	return generateLibCacheIfNeeded(frag.getInstallLocation(), relativePath);
}
static public URL generateLibCacheIfNeeded (String srcJarFile, String relativePath) {		
		if (srcJarFile == null)
			return null;
		// Create a root path for each version of .dlls
		IPath root = JavaVEPlugin.VE_GENERATED_LIBRARIES_CACHE.append(Integer.toString(srcJarFile.hashCode()));
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
		URL url = null;  // result
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
										Runtime.getRuntime().exec(new String[] {"chmod", "755", dest.getAbsolutePath()}).waitFor();
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
		return url;		
}
		
    public static String getPDEPath() {
		
		PluginModelManager pm = PDECore.getDefault().getModelManager();
		IPluginModelBase swtEntry = pm.findModel("org.eclipse.swt");
		return swtEntry.getBundleDescription().getLocation();
    }
	
	
	
    protected void contributePluginLibrary(final IConfigurationContributionController controller) throws CoreException {
		
		PluginModelManager pm = PDECore.getDefault().getModelManager();
		final IPluginModelBase swtModel = pm.findModel(SWTContainer.SWT_CONTAINER_LIB_FRAGMENT_NAME);
		// Run it as a runnable, so that we can update the problems view
	    ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					final String msg = SWTMessages.getString("SWTConfigurationContributor.CouldntResolveDLLInPDE_ERROR_"); //$NON-NLS-1$
					if (swtModel!=null && swtModel.isEnabled()) {			
						final URL librarylocation = generateLibCacheIfNeeded(swtModel.getBundleDescription().getLocation(),"");			
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
			if (frags[i].getBundleDescription().getHost().getName().equals("org.eclipse.swt")) { //$NON-NLS-1$
				// swt fragment					
				if (frags[i].getBundleDescription().getSymbolicName().startsWith("org.eclipse.swt.nl"))  //$NON-NLS-1$
					continue; // skip the nl ones
				os = getResourceURL(frags[i], SWT_OS_PATH.toPortableString());
				if (os!=null){	
				  return os;
				}
			}
		}	
		return null;
	}
		
    protected void contributeLegacyPluginLibrary(final IConfigurationContributionController controller) throws CoreException {		
		// Assume target's have the same platform/architecture/overrides
				
		URL	os = getSWTLegacyOSPath();
		if (os!=null){	
			// if our DLL is inside a .jar extract it out to out private cache.
		   if (os.toString().startsWith("jar"))
				os = generateLibCacheIfNeeded(getFilePath(os).toPortableString(), SWT_OS_PATH.toPortableString());
		}

        final String msg = SWTMessages.getString("SWTConfigurationContributor.CouldntResolveDLLInPDE_ERROR_"); //$NON-NLS-1$
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
		
		// If GTK is the platform, then contribute the native library which does the offscreen screen-scrape
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
		
		if (!ProxyPlugin.isPDEProject(javaProject)) {
			// SWT container would have contributed all the proper jars, and DLLs... if it is
			// not in the class path, we may have many other issues.
		}
		else {
			PluginModelManager pm = PDECore.getDefault().getModelManager();
			IPluginModelBase swtEntry = pm.findModel("org.eclipse.swt");
			Version version = swtEntry.getBundleDescription().getVersion();
			// TODO: this is a temporary stop gap measure... we should generalize plugin projects into the LocalFileConfigurationContributorController			
			if (version.getMajor() < 3 || version.getMinor() < 1)
				contributeLegacyPluginLibrary(controller);
			else
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
		// TODO the problem with this here is that it is hard-coded REM stuff. Need a better way to do this.
		SWTREMProxyRegistration.initialize(registry);	// Set the registry up with SWT REM stuff.
		
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
							removeMarker(SWTMessages.getString("Marker.BuildPathNot142")); //$NON-NLS-1$
						} else {
							createMarker(SWTMessages.getString("Marker.BuildPathNot142")); //$NON-NLS-1$
						}
					}
				}, null, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
			} catch (CoreException e) {
				SwtPlugin.getDefault().getLogger().log(e, Level.WARNING);
			}
		}
	}
	private static URL getResourceURL(IFragmentModel frag, String relativePath) {
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
	
	private static IPath getFilePath(URL l) {
		if (l.getProtocol().equals("file"))
		     return  new Path (l.getFile());
		else if (l.getProtocol().equals("jar")) {
			String f = l.getFile();
			int idx = f.lastIndexOf('!');
			if (idx>=0)
				f = f.substring(0,idx);
			try {
				return getFilePath (new URL(f));
			} catch (MalformedURLException e) {}
		}
		return null;
	}	
	
	// cache bundle to src.zip path
	private static HashMap platformSrcPath = new HashMap(10);
	/**
	 * 
	 * @param bundle 
	 * @param name the pkged name of the bundle (id, version etc)
	 * @return path for src.zip, null if can not find it
	 * 
	 * @since 1.1.0
	 */
	public static IPath getPlatformSrcLocationFor(Bundle bundle, String name) {
		
		IPath result = (IPath) platformSrcPath.get(bundle);
		if (result != null) return result;
		
		String srcFile = "src.zip";		
		try {
			URL u = bundle.getEntry(srcFile);
			if (u!=null) {
			    result = getFilePath(Platform.resolve(u));
				platformSrcPath.put(bundle,result);
				return result;
			}
		} catch (IOException e) {}
		
		if (name.endsWith(".jar")) {
			name = name.substring(0,name.lastIndexOf(".jar"));
		}
		

		IConfigurationElement[] ces = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.pde.core.source");
		
		for (int i = 0; i < ces.length; i++) {
			IPath srcsrch = new Path(ces[i].getAttributeAsIs("path"));			
			Bundle srcBundle = Platform.getBundle(ces[i].getDeclaringExtension().getNamespace());
			Bundle[] fragments = Platform.getFragments(srcBundle);
			List bundles;
			if (fragments!=null)
				bundles = new ArrayList(Arrays.asList(Platform.getFragments(srcBundle)));
			else
				bundles = new ArrayList(1);
			bundles.add(srcBundle);
			for (int j = 0; j < bundles.size(); j++) {
				Bundle b = (Bundle) bundles.get(j);
				URL srcUrl = null;
				if (b==bundle)
					srcUrl = Platform.find(b, srcsrch.append(srcFile));
				else
					srcUrl = Platform.find(b, srcsrch.append(name).append(srcFile));				
				if (srcUrl != null) {
					try {
						result = getFilePath(Platform.resolve(srcUrl));
						platformSrcPath.put(bundle,result);
						return result;
					} catch (IOException e) {}
				}
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
	public static IClasspathEntry getPlatformPath (String pluginID, String fragmentName, String libPath) {
		Bundle pBundle = Platform.getBundle(pluginID);
		Bundle bundle = null;
		if (fragmentName!=null) 
			bundle = Platform.getBundle(fragmentName);		
		else
			bundle = pBundle;
		
		if (bundle.getState()!=Bundle.ACTIVE &&
		    bundle.getState()!=Bundle.RESOLVED)
			return null;
		
		IPath location = null;
		try {				
			// .jared fragment
			URL l = Platform.resolve(bundle.getEntry("/"));	
			boolean project = l.getProtocol().equals("file"); // This library is inside the workbench
			location = getFilePath(l).removeTrailingSeparator();
			if (location !=null) {
				IPath src = getPlatformSrcLocationFor(bundle, location.lastSegment());
				IClasspathAttribute[] attr = new IClasspathAttribute[0];
				if (libPath!=null) { // Create a lib path attribute to this entry						
					// jar may be imported into the workbench, use it; if not cache it
					URL libURL = project ? location.toFile().toURL() : 
						         generateLibCacheIfNeeded(location.toPortableString(), libPath);
					attr = new IClasspathAttribute[]{ JavaCore.newClasspathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY, 
							                                                getFilePath(libURL).toPortableString())};
				}					
				// TODO: this will not work if the fragment is not a .jar (imported to the IDE as a project)
				return JavaCore.newLibraryEntry(location, src, null, new IAccessRule [0], attr, false);
			}
		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
		return null;
	}	
}
