/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TemplateUtil.java,v $
 *  $Revision: 1.3 $  $Date: 2004-01-24 01:08:29 $ 
 */
package org.eclipse.ve.internal.java.vce.templates;

import java.io.File;
import java.util.*;

import org.eclipse.core.boot.BootLoader;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.launching.*;
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
	
	static public boolean isDevMode(IPluginDescriptor desc) {
		
		// For now, just check that the bin directory is not empty
		File d = new File (getCorrectPath(ProxyPlugin.getPlugin().localizeFromPluginDescriptor(desc, "bin"))) ; //$NON-NLS-1$
		if (d.canRead() && d.isDirectory())
		    if (d.list().length > 0)  return true ;
		return false ;		
		
//		if (fDevMode==null) 
//		    fDevMode = new Boolean ("true".equalsIgnoreCase( //$NON-NLS-1$
//					   Platform.getDebugOption (ProxyPlugin.getPlugin().
//			           getDescriptor().getUniqueIdentifier() + "/dev")));
//        return fDevMode.booleanValue() ;			           
	}
	
	/**
	 * This will return the absolute class path associated with a Plugin
	 */
	static public String getPluginInstallPath (String plugin, String relativePath) {
		return getPluginInstallPath(Platform.getPlugin(plugin).getDescriptor(), relativePath);
	}
	
	/**
	 * This will return the absolute class path associated with a Plugin
	 */
	static public String getPluginInstallPath (IPluginDescriptor pluginDescriptor, String relativePath) {
	    return getCorrectPath(ProxyPlugin.getPlugin().localizeFromPluginDescriptor(pluginDescriptor, relativePath));
	}	
	
	static public String getPluginSrcPath (String plugin, String relativePath) {
		ILibrary[] lib = Platform.getPlugin(plugin).getDescriptor().getRuntimeLibraries();
		if (lib == null || lib.length==0) return null ;
		
		Path src = new Path (lib[0].getPath().removeFileExtension().lastSegment()) ;
		IPath p = src.append(relativePath)  ;
		
		return getPluginInstallPath(plugin,p.toString()) ;
		
	}
	
	/**
	 * This will return the absolute class path associated with run time jars, and
	 * dev. time directories associated with a give plugin.  It does not include
	 * nested (required) plugin path.
	 */
	static public List getPluginJarPath (String plugin) {
		List l = (List) fClassPathMap.get(plugin) ;
		if (l != null) return l ;
				
		IPluginDescriptor desc = Platform.getPlugin(plugin).getDescriptor();
		ArrayList list = new ArrayList();
		if (!isDevMode(desc)) {
			// Pick up the Jars
			ILibrary[] lib = desc.getRuntimeLibraries();			
			for (int i = 0; i < lib.length; i++) {
				String names[] = ProxyPlugin.getPlugin().localizeFromPluginDescriptorAndFragments(desc, lib[i].getPath().toString());
				if (names == null || names.length == 0)
					continue;
				// pickup the first one in line				
				list.add(getCorrectPath(names[0]));
			}
		}
		else
			list.add(getCorrectPath(ProxyPlugin.getPlugin().localizeFromPluginDescriptor(desc, "bin"))); //$NON-NLS-1$
	    fClassPathMap.put(plugin,list) ;
		return list;
	}
	/**
	 * This will return the absolute class path associated with run time jars, and
	 * dev. time directories associated with a give plugin as well as its preReq
	 * plugins
	 * 
	 * Note: a call to this method will activate all preReq plugins.
	 */	
	static public List getPluginAndPreReqJarPath (String plugin) {
		List lst = (List) fClassPathPreReqMap.get(plugin) ;
		if (lst != null) return lst ;
		
		IPluginDescriptor desc = null ;		
		try  {
		   desc = Platform.getPlugin(plugin).getDescriptor();
		}
		catch (Exception e) {
			return new ArrayList() ;
		}
		IPluginPrerequisite[] preReq = desc.getPluginPrerequisites() ;
		List l = getPluginJarPath(plugin) ;
		java.util.HashMap beenThere = new java.util.HashMap() ;
		beenThere.put(plugin,plugin) ;
		for (int i = 0; i < preReq.length; i++) {
			String plgn = preReq[i].getUniqueIdentifier() ;
			if (beenThere.get(plgn) != null) continue ;			
			Iterator itr = getPluginAndPreReqJarPath(plgn,beenThere).iterator() ;
			while (itr.hasNext()) {
			   String path = (String) itr.next() ;
			   if (l.contains(path)) continue ;
			   l.add(path) ;
			}
		}
		fClassPathPreReqMap.put(plugin,l) ;
		return l ;
	}
	
	static private List getPluginAndPreReqJarPath (String plugin, HashMap beenThere) {
		
		List lst = (List) fClassPathPreReqMap.get(plugin) ;
		if (lst != null) return lst ;
		
		IPluginDescriptor desc = null ;
		try  {
		   Plugin p = Platform.getPlugin(plugin) ;
		   if (p != null)
		     desc = p.getDescriptor();
		}
		catch (Exception e) {}
		if (desc == null) 
			return new ArrayList() ;
		
		IPluginPrerequisite[] preReq = desc.getPluginPrerequisites() ;
		List l = getPluginJarPath(plugin) ;
		beenThere.put(plugin,plugin) ;
		for (int i = 0; i < preReq.length; i++) {
			String plgn = preReq[i].getUniqueIdentifier() ;
			if (beenThere.get(plgn) != null) continue ;			
			Iterator itr = getPluginAndPreReqJarPath(plgn,beenThere).iterator() ;
			while (itr.hasNext())
			   l.add(itr.next()) ;
		}
		fClassPathPreReqMap.put(plugin,l) ;
		return l ;
	}
	
	private static String getCorrectPath(String path) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < path.length(); i++) {
			char c = path.charAt(i);
			if (BootLoader.getOS().equals("win32")) { //$NON-NLS-1$
				if (i == 0 && c == '/')
					continue;
			}
			// Some VMs may return %20 instead of a space
			if (c == '%' && i + 2 < path.length()) {
				char c1 = path.charAt(i + 1);
				char c2 = path.charAt(i + 2);
				if (c1 == '2' && c2 == '0') {
					i += 2;
					continue;
				}
			}
			buf.append(c);
		}
		return buf.toString();
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
				String vmID = String.valueOf("1");
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
	  
	  String path = url.getFile();          // string representing location of this
											   // class (if it were a resource file)
										
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
	   } else
	       // No a format we support
	       return null ;
	       
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
	
	public static ClassLoader getClassLoader(String plugin) {
		Plugin p = Platform.getPlugin(plugin) ;
		if (p != null) {
			return p.getDescriptor().getPluginClassLoader() ;
		}
		return null ;		
	}
}
