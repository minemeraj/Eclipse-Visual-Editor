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
import java.net.URL;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.*;
import org.osgi.framework.Bundle;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class SWTContainer implements IClasspathContainer, IConfigurationContributor {
	
	private IClasspathEntry[] fClasspathEntries;
	
	private IPath containerPath;	// path for container, NOT path for resolved entry
	
	private final static String[][] swtLibraries = new String[][] {
			{ "org.eclipse.swt", "$ws$/swt.jar" } //$NON-NLS-1$ //$NON-NLS-2$
	};
	
	private final static String[][] swtGTKLibraries = new String[][]{
			{"org.eclipse.swt.gtk", "$ws$/swt-pi.jar"},  //$NON-NLS-1$ //$NON-NLS-2$
			{"org.eclipse.swt.gtk", "$ws$/swt-mozilla.jar"} //$NON-NLS-1$ //$NON-NLS-2$
	};
	
	private final static String[][] jfaceLibraries = new String[][] {
			{ "org.eclipse.jface", "jface.jar" }, //$NON-NLS-1$ //$NON-NLS-2$
			{ "org.eclipse.jface.text", "jfacetext.jar" }, //$NON-NLS-1$ //$NON-NLS-2$
			{ "org.eclipse.core.runtime", "runtime.jar" }, //$NON-NLS-1$ //$NON-NLS-2$
			{ "org.eclipse.core.runtime.compatibility", "compatibility.jar" }, //$NON-NLS-1$ //$NON-NLS-2$			
			{ "org.eclipse.osgi", "core.jar" } //$NON-NLS-1$ //$NON-NLS-2$
	};
	
	public SWTContainer(IPath containerPath) {
		this.containerPath = containerPath;

		try {
			boolean isJFace = isJFace(containerPath);
			boolean isGTK = isGTK();
			int classpathlength = swtLibraries.length;

			if (isJFace)
				classpathlength += jfaceLibraries.length;
			if (isGTK)
				classpathlength += swtGTKLibraries.length;
			fClasspathEntries = new IClasspathEntry[classpathlength];

			int ci = 0;
			for (int i = 0; i < swtLibraries.length; i++) {
				Path path = new Path(swtLibraries[i][1]);
				URL[] locSrc = ProxyPlugin.getPlugin().findPluginJarAndAttachedSource(Platform.getBundle(swtLibraries[i][0]), path);
				if (locSrc[0] != null) 					
				   path = new Path(Platform.resolve(locSrc[0]).getFile());
				else
				   path = null;
				Path srcPath = null;
				if (locSrc.length>1 && locSrc[1] != null)
					srcPath = new Path(Platform.resolve(locSrc[1]).getFile());
				if (path!=null) 
				   fClasspathEntries[ci++] = JavaCore.newLibraryEntry(path, srcPath, null);
				else {
					//TODO: this is a temporary M6 hack for flatten swt.jar
					Bundle[] frags = Platform.getFragments(Platform.getBundle(swtLibraries[i][0]));
					IPath location = null;
					for (int j = 0; j < frags.length; j++) {
						if (frags[i].getSymbolicName().indexOf(".nl")>=0) continue;
						// TODO: Assume a single real fragment for SWT					
						String filter = (String) frags[i].getHeaders().get("Eclipse-PlatformFilter");
						if (filter.indexOf("osgi.ws")>=0) {
								location = new Path(Platform.getInstallLocation().getURL().getPath());
								String version = "_"+(String) frags[i].getHeaders().get("Bundle-Version");
								location = location.append("plugins").append(frags[i].getSymbolicName()+version+".jar");
					    }						
					}				
					fClasspathEntries[ci++] = JavaCore.newLibraryEntry(location, location, null);						
				}
			}

			if (isJFace) {
				for (int j = 0; j < jfaceLibraries.length; j++) {
					Path path = new Path(jfaceLibraries[j][1]);
					URL[] locSrc = ProxyPlugin.getPlugin().findPluginJarAndAttachedSource(Platform.getBundle(jfaceLibraries[j][0]), path);
					if (locSrc[0] == null)
						continue;
					path = new Path(Platform.resolve(locSrc[0]).getFile());
					Path srcPath = null;
					if (locSrc[1] != null)
						srcPath = new Path(Platform.resolve(locSrc[1]).getFile());
					fClasspathEntries[ci++] = JavaCore.newLibraryEntry(path, srcPath, null);
				}
			}

			if (isGTK) {
				for (int j = 0; j < swtGTKLibraries.length; j++) {
					Path path = new Path(swtGTKLibraries[j][1]);
					URL[] locSrc = ProxyPlugin.getPlugin().findPluginJarAndAttachedSource(Platform.getBundle(swtGTKLibraries[j][0]), path);
					if (locSrc[0] == null)
						continue;
					path = new Path(Platform.resolve(locSrc[0]).getFile());
					Path srcPath = null;
					if (locSrc[1] != null)
						srcPath = new Path(Platform.resolve(locSrc[1]).getFile());
					fClasspathEntries[ci++] = JavaCore.newLibraryEntry(path, srcPath, null);
				}
				
				
			}

		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
	}
	private boolean isGTK(){
		if(Platform.WS_GTK.equals(Platform.getWS()))
			return true;
		return false;
	}

	private boolean isJFace(IPath containerPath) {
		// The first segment is the SWT_CONTAINER name
		return "JFACE".equals(containerPath.segment(1)); //$NON-NLS-1$
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
