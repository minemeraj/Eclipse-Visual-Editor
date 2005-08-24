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
 *  $RCSfile: RegisteredClasspathContainer.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.osgi.framework.Bundle;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;
 

/**
 * Classpath container that works with the RegisteredClasspathContainerInitializer.
 * <package-protected> because only the RegisteredClasspathContainerInitializer should use it.
 * @since 1.0.0
 */
class RegisteredClasspathContainer implements IClasspathContainer {

	private IPath containerpath;
	private String description;
	private IClasspathEntry[] classpath;
	
	public RegisteredClasspathContainer(IPath containerpath) {
		this.containerpath = containerpath;
		String containerid = containerpath.segment(0);
		// TODO This is just hard-coded against this point. Future would have these retrieved by the plugin, plus more in there.
		IConfigurationElement[] configs = Platform.getExtensionRegistry().getConfigurationElementsFor(JavaVEPlugin.getPlugin().getBundle().getSymbolicName(),JavaVEPlugin.PI_JBCF_REGISTRATIONS); 
		for (int i = 0; i < configs.length; i++) {
			String ctrid = configs[i].getAttributeAsIs(JavaVEPlugin.PI_CONTAINER);
			if (containerid.equals(ctrid)) {
				description = configs[i].getAttribute(JavaVEPlugin.PI_DESCRIPTION);
				if (description == null)
					description = containerid;
				IConfigurationElement[] libraries = configs[i].getChildren(JavaVEPlugin.PI_LIBRARY);
				List libs = new ArrayList(libraries.length);
				for (int j = 0; j < libraries.length; j++) {
					String lib = libraries[j].getAttributeAsIs(JavaVEPlugin.PI_RUNTIME);
					IPath libpath = getPath(lib, libraries[j]);
					if (libpath == null)
						continue;
					String src = libraries[j].getAttributeAsIs(JavaVEPlugin.PI_SOURCE);
					IPath srcpath = getPath(src, libraries[j]);
					String prefix = libraries[j].getAttributeAsIs(JavaVEPlugin.PI_SOURCEPREFIX);
					IPath srcattachpath = prefix != null ? new Path(prefix) : null;
					libs.add(JavaCore.newLibraryEntry(libpath, srcpath, srcattachpath));
				}
				classpath = (IClasspathEntry[]) libs.toArray(new IClasspathEntry[libs.size()]);
				break;
			}
		}
	}
	
	private IPath getPath(String lib, IConfigurationElement ce) {
		if (lib == null || lib.length() == 0)
			return null;
		Bundle bundle = null;
		IPath libpath =  null;
		if (lib.charAt(0) != '/') {
			// Relative to declaring bundle.
			bundle = Platform.getBundle(ce.getDeclaringExtension().getNamespace());
			libpath = new Path(lib);
		} else {
			int pathNdx = lib.indexOf('/', 1);
			if (pathNdx == -1 || pathNdx >= lib.length())
				return null;	// Invalid, no path after plugin id.
			bundle = Platform.getBundle(lib.substring(0, pathNdx));
			if (bundle == null)
				return null;	// Invalid, plugin not found.
			libpath = new Path(lib.substring(pathNdx+1));
		}
		// Unfortunately, you can't return class folders from a container, only jars, so in workbench dev mode
		// we can't reference the bin directory. Therefore we have to have an actual jar sitting in the development
		// image for it to be found. localize can't use the bin directory.
		URL url = ProxyPlugin.getPlugin().urlLocalizeFromBundleOnly(bundle, libpath);
		if (url != null)
			return new Path(url.getFile());
		else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getClasspathEntries()
	 */
	public IClasspathEntry[] getClasspathEntries() {
		return classpath;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getKind()
	 */
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getPath()
	 */
	public IPath getPath() {
		return containerpath;
	}

}
