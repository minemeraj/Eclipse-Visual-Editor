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
/*
 *  $RCSfile: TemplateUtil.java,v $
 *  $Revision: 1.21 $  $Date: 2006-02-21 17:16:35 $ 
 */
package org.eclipse.ve.internal.java.vce.templates;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.launching.*;
import org.osgi.framework.Bundle;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

/**
 * @author Gili Mendel
 *
 */
public class TemplateUtil {
	
//	static Boolean fDevMode = null ;

    private final static HashMap fClassPathMap = new HashMap() ;  // cache class path
    private final static HashMap fClassPathPreReqMap = new HashMap() ;  // cache PreReq class path
    private static List fPlatformJRE = null ;
    private static HashMap fFilePath = new HashMap();
    private static HashMap fClassLoaders = new HashMap();
	
    /**
     * Get the path for file within the given bundle.
     * @param bundleName
     * @param relativePath
     * @return
     * 
     * @since 1.0.0
     */
	static public String getPathForBundleFile (String bundleName, String relativePath) {
		String key = bundleName+":"+relativePath; //$NON-NLS-1$
		String result;
		if ((result=(String)fFilePath.get(key))==null) {
		 result =  getPathForBundleFile(Platform.getBundle(bundleName), relativePath);
		 fFilePath.put(key,result) ;
		}
		return result;
	}
	
	/**
	 * This will return the absolute class path associated with a Plugin
	 */
	static public String getPathForBundleFile (Bundle bundle, String relativePath) {
	    return getCorrectPath(ProxyPlugin.getPlugin().localizeFromBundleOnly(bundle, relativePath));
	}	
	
	/**
	 * This will return the absolute class path associated with run time jars, and
	 * dev. time directories associated with a give plugin.  It does not include
	 * nested (required) plugin path. It will include fragments.
	 */
	static public List getPluginJarPath (String plugin) {
		List l = (List) fClassPathMap.get(plugin) ;
		if (l != null) return l ;
				
		Bundle bundle = Platform.getBundle(plugin);
		List list = null;
		if (bundle != null) {
			URL[] urls = ProxyPlugin.getPlugin().urlLocalizeBundleAndFragments(bundle);
			list = new ArrayList(urls.length);
			for (int i = 0; i < urls.length; i++) {
				list.add(ProxyPlugin.getFileFromURL(urls[i]));
			}
		} else
			list = Collections.EMPTY_LIST;
	    fClassPathMap.put(plugin,list) ;
		return list;
	}
	
	/**
	 * This will return the absolute class path associated with run time jars, and
	 * dev. time directories associated with a give plugin as well as its preReq
	 * plugins
	 */	
	static public List getPluginAndPreReqJarPath (String plugin) {
		List lst = (List) fClassPathPreReqMap.get(plugin) ;
		if (lst != null) return lst ;
		Bundle bundle = Platform.getBundle(plugin);
		List classPath;
		if (bundle != null) {
			classPath = new ArrayList();
			classPath.addAll(getPluginJarPath(bundle.getSymbolicName()));
			List allReqs = ProxyPlugin.getAllPrereqs(bundle);
			for (int i = 0; i < allReqs.size(); i++) {
				classPath.addAll(getPluginJarPath(((Bundle) allReqs.get(i)).getSymbolicName()));
			}
			((ArrayList) classPath).trimToSize();
		} else
			classPath = Collections.EMPTY_LIST;
		fClassPathPreReqMap.put(plugin,classPath) ;
		return classPath ;
	}
		
	private static String getCorrectPath(String path) {
		if (path.length() == 0)
			return path;
		boolean skipLeading = path.charAt(0) == '/' && Platform.getOS().equals(Platform.OS_WIN32);
		if (skipLeading || path.indexOf('%') != -1) {
			StringBuffer buf = new StringBuffer(path.length());
			for (int i = skipLeading ? 1 : 0; i < path.length(); i++) {
				char c = path.charAt(i);
	
				// Some VMs may return %20 instead of a space
				if (c == '%' && i + 2 < path.length()) {
					char c1 = path.charAt(i + 1);
					char c2 = path.charAt(i + 2);
					if (c1 == '2' && c2 == '0') {
						i += 2;
						buf.append(' ');
						continue;
					}
				}
				buf.append(c);
			}
			return buf.toString();
		}
		return path;
	}
	
	static public List getPlatformJREPath() throws TemplatesException {

		if (fPlatformJRE != null)
			return fPlatformJRE;

		VMStandin detectedVMStandin = null;
		// Try to detect a VM for each declared VM type
		IVMInstallType[] vmTypes = JavaRuntime.getVMInstallTypes();
		for (int i = 0; i < vmTypes.length; i++) {

			File detectedLocation = vmTypes[i].detectInstallLocation();
			if (detectedLocation != null) {

				// Create a standin for the detected VM and add it to the result collector
				String vmID = String.valueOf("1"); //$NON-NLS-1$
				detectedVMStandin = new VMStandin(vmTypes[i], vmID);
				if (detectedVMStandin != null) {
					detectedVMStandin.setInstallLocation(detectedLocation);
					detectedVMStandin.setName(detectedVMStandin.getInstallLocation().getName());
					LibraryLocation[] locations = JavaRuntime.getLibraryLocations(detectedVMStandin);
					fPlatformJRE = new ArrayList(locations.length);
					for (int j = 0; j < locations.length; j++) {
						IPath path = locations[j].getSystemLibraryPath();
						if (!Path.EMPTY.equals(path))
							fPlatformJRE.add(path.toString());
					}
					return fPlatformJRE;
				}
			}
		}
		fPlatformJRE = Collections.EMPTY_LIST;
		return fPlatformJRE;
	}
	
	/**
     *  This method will return the classpath associated with the class was used to create
     *  the instance of o.  If the class is in a Jar file, the path to the jar will be returned.
     * 
     *  @param o  object
     *  @return the class path.  A null will be returned if the class path is not local, or
     *           could not be parsed.
     */
   public static String getClassPath (Object o) {    	
      if (o == null) return null ;     
      return getClassPath(o.getClass()) ; 	
   } 
   public static String getClassPath (Class clazz) {
    	
      if (clazz == null) return null ;
      	
	  String name = "/"+clazz.getName().replace('.','/')+".class" ; //$NON-NLS-1$ //$NON-NLS-2$
	  java.net.URL url = clazz.getResource(name) ;
    
	  if (url == null) return null ;
	  
	  try {
		// class (if it were a resource file)
		  url = FileLocator.resolve(url);
	  } catch (IOException e) {}
	  
	  String path = url.getFile();          // string representing location of this

										
	  if (path.startsWith("/") && path.charAt(2) == ':') { //$NON-NLS-1$
			 // could be something like /C:/com/ibm/etools/TheClass.class
			 path = path.substring(1);     // delete the first char
	   } 
	   else if (path.toLowerCase().startsWith("file:")) {  //$NON-NLS-1$
		    // could be something like file:/C:/IBM/TheJar.jar!/com/ibm/..../TheClass.class
			path = path.substring(5);
			// is it a jar file ?
		    int excMarkIndex = path.indexOf("!"); //$NON-NLS-1$
			if (excMarkIndex != -1) {
					 path = path.substring(0,excMarkIndex); // C:/IBM/TheJar.jar
			}
	   } else if (path.charAt(1) == ':') {
	   	   // C:/com/ibm...
	   } else {
	       // most likely a vanilla unix path
		   // is it a jar file ?
	       int excMarkIndex = path.indexOf("!"); //$NON-NLS-1$
		    if (excMarkIndex != -1) {
				 path = path.substring(0,excMarkIndex); // C:/IBM/TheJar.jar
		    }
	   }
	       
	  // URL sometimes encodes spaces in a path with %20
	  return getCorrectPath(path) ;
	  
   } 
   	/**
	 * This method will return the time stamp of the .class associated with o, or
	 * the time stamp of the .jar file if o is inside a .jar file.
	 * 
	 * @param o  object 
	 * @return time stamp, or -1 if the time stamp could not be determined
	 */
	public static long getTimeStamp(Object o) {
		return getTimeStamp(o.getClass()) ;
	}
	public static long getTimeStamp(Class clazz) {

		String path = getClassPath(clazz) ;
		if (path == null) return -1 ;
		
		File f = new File(path) ;
		if (!f.canRead()) return -1 ;
		return f.lastModified() ;		
	}
	
	private static class SpecialClassLoader extends ClassLoader {
		Bundle bundle;
		
		public SpecialClassLoader(Bundle bundle) {
			this.bundle = bundle;
		}
		
		
		/* (non-Javadoc)
		 * @see java.lang.ClassLoader#findClass(java.lang.String)
		 */
		protected Class findClass(String name) throws ClassNotFoundException {
			return bundle.loadClass(name);
		}
	}
	public static ClassLoader getClassLoader(String plugin) {
		// The plugin class loader is now deprecated. Since this method is used
		// extensively in the generator pattern here, we will instead fluff one 
		// up that goes to the bundle to load the classes.
		
		ClassLoader cl = (ClassLoader) fClassLoaders.get(plugin);
		if (cl==null) {
			Bundle b = Platform.getBundle(plugin);
		    if (b != null) {
			    cl = new SpecialClassLoader(b);
			    fClassLoaders.put(plugin, cl);
		    }
		}
		return cl ;		
	}
}
