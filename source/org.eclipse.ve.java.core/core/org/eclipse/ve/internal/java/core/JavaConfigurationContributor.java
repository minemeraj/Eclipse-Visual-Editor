package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaConfigurationContributor.java,v $
 *  $Revision: 1.3 $  $Date: 2004-02-20 00:44:29 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

import org.eclipse.jem.internal.proxy.core.*;

/**
 * Classpath contributor for JCM. It will add in the required contributions, plus
 * it will go through the registered extension points for variables in the class path
 * and will add in the contributors that they refer to.
 */
public class JavaConfigurationContributor implements IConfigurationContributor {

	List variableContributors = new ArrayList(0); // Variable contributors that were found.
	public List paletteCats;	// Temporary API to get palette categories on a per-variable basis. This is currently just a kludge attempt.
	private int firstInsert = 0;	// Location to insert when "first" received for paletteloc
	
	IProject project;
	
	/**
	 * Create with the project that the editor is opening within.
	 */
	public JavaConfigurationContributor(IProject project) {
		this.project = project;
	}

	/**
	 * Method to update any class paths with any
	 * paths that need to be added to a VM. In this case, it is
	 * the proxyvm.jar that needs to be added. This jar contains
	 * the common code that is required by any VM for proxy
	 * support.
	 */
	public void contributeClasspaths(List classPaths, IClasspathContributionController controller)
		throws CoreException {
		// Add in the remote vm jar and any nls jars that is required for JBCF itself.
		controller.contributeClasspath(
			ProxyPlugin.getPlugin().urlLocalizeFromPluginDescriptorAndFragments(JavaVEPlugin.getPlugin().getDescriptor(), "vm/javaremotevm.jar"), //$NON-NLS-1$
			classPaths,
			-1);

		// Need to find any additional contributors.
		HashSet visitedProjects = new HashSet();
		HashSet visitedVariablepaths = new HashSet();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		findContributorsForProject(project, root, visitedProjects, visitedVariablepaths);
		// Now turn the var elements into contributors.
		for (ListIterator itr = variableContributors.listIterator(); itr.hasNext();) {
			IConfigurationElement v = (IConfigurationElement) itr.next();
			IConfigurationContributor contrib = null;
			if (v.getAttribute(JavaVEPlugin.PI_CONTRIBUTOR) != null || v.getChildren(JavaVEPlugin.PI_CONTRIBUTOR).length > 0) {
				try {
					contrib = (IConfigurationContributor) v.createExecutableExtension(JavaVEPlugin.PI_CONTRIBUTOR);
				} catch (ClassCastException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (CoreException e) {
					// Couldn't find or instantiate it. The exception was probably logged, so we just ignore it.
				}
			}

			itr.set(contrib); // Set to what should be used, null is valid for not found.
			if (contrib != null)
				contrib.contributeClasspaths(classPaths, controller);
			String pcat = v.getAttributeAsIs(JavaVEPlugin.PI_PALETTECATS); //$NON-NLS-1$
			if (pcat != null && pcat.length() > 0) {
				if (paletteCats == null)
					paletteCats = new ArrayList(3);
				String ploc = v.getAttributeAsIs("paletteloc"); //$NON-NLS-1$
				if (ploc == null || ploc.equals("first")) //$NON-NLS-1$
					paletteCats.add(firstInsert++,pcat);
				else
					paletteCats.add(pcat);
			}
		}
	}

	/*
	 * Contribute classpaths for the specified project. If doc is passed in, then this is the top level and
	 * all should be added. If no doc, then this is pre-req'd project, and then we will handle exported entries only.
	 */
	protected void findContributorsForProject(
		IProject project,
		IWorkspaceRoot root,
		HashSet visitedProjects,
		HashSet visitedVariablePaths)
		throws CoreException {
		if (visitedProjects.contains(project))
			return;
		visitedProjects.add(project);

		IJavaProject jProject = JavaCore.create(project);
		IClasspathEntry[] rawPath = jProject.getRawClasspath();

		// Find contributors for variable entries
		for (int i = 0; i < rawPath.length; i++) {
			IClasspathEntry entry = rawPath[i];
			processBuildPath(
				visitedProjects,
				visitedVariablePaths,
				root,
				entry.getEntryKind(),
				entry.getPath());
		}

	}
	
	private static final IPath JRE_LIB_VARIABLE_PATH = new Path(JavaRuntime.JRELIB_VARIABLE);	// TODO Remove when we handle containers.
	protected void processBuildPath(
		HashSet visitedProjects,
		HashSet visitedVariablePaths,
		IWorkspaceRoot root,
		int kind,
		IPath path)
		throws CoreException {
		if (kind == IClasspathEntry.CPE_PROJECT) {
			IProject reqProject = (IProject) root.findMember(path.lastSegment());
			// Project entries only have one segment.
			if (reqProject != null && reqProject.isOpen())
				findContributorsForProject(reqProject, root, visitedProjects, visitedVariablePaths);
		} else if (kind == IClasspathEntry.CPE_VARIABLE) {
			// We only handle variables as being registered. 
			if (path == null || path.segmentCount() == 0)
				return; // No path information to process.
			// First we handle the generic kind of for just the variable itself (which is segment 0).
			IPath varpath = path.segmentCount() == 1 ? path : path.removeLastSegments(path.segmentCount() - 1);
			if (!visitedVariablePaths.contains(varpath)) {
				visitedVariablePaths.add(varpath);
				IConfigurationElement[] registrations = JavaVEPlugin.getPlugin().getRegistrations(varpath);
				if (registrations != null)
					processConfigurationRegistrations(registrations);
			}

			// Now process for the specific path (which would be variable followed by some subpaths).
			if (path.segmentCount() > 1 && !visitedVariablePaths.contains(path)) {
				visitedVariablePaths.add(path);
				IConfigurationElement[] registrations = JavaVEPlugin.getPlugin().getRegistrations(path);
				if (registrations != null)
					processConfigurationRegistrations(registrations);
			}
		} else if (kind == IClasspathEntry.CPE_CONTAINER) {
			// KLUDGE TODO For now we can't really handle containers, we will simply hard-code and only handle JRE container to JRE_LIB stuff.
			if (path == null || path.segmentCount() == 0)
				return; // No path information to process.
			if (path.segment(0).equals(JavaRuntime.JRE_CONTAINER)) {
				if (!visitedVariablePaths.contains(JRE_LIB_VARIABLE_PATH)) {
					visitedVariablePaths.add(JRE_LIB_VARIABLE_PATH);
					IConfigurationElement[] registrations = JavaVEPlugin.getPlugin().getRegistrations(JRE_LIB_VARIABLE_PATH);
					if (registrations != null)
						processConfigurationRegistrations(registrations);
				}
			} else {
				// See whether the container is registered
				if(!visitedVariablePaths.contains(path)){
					visitedVariablePaths.add(path);
					IConfigurationElement[] registrations = JavaVEPlugin.getPlugin().getRegistrations(path);
					if (registrations != null)
						processConfigurationRegistrations(registrations);					
				}
			}
		}
	}

	protected void processConfigurationRegistrations(IConfigurationElement[] registrations) {
		for (int i = 0; i < registrations.length; i++)
			variableContributors.add(registrations[i]);
	}

	public void contributeToConfiguration(VMRunnerConfiguration config) {
		for (int i = 0; i < variableContributors.size(); i++) {
			IConfigurationContributor contrib = (IConfigurationContributor) variableContributors.get(i);
			if (contrib != null)
				contrib.contributeToConfiguration(config);
		}
	}

	public void contributeToRegistry(ProxyFactoryRegistry registry) {
		
		// Call the setup method in the target VM to initialize statics
		// and other environment variables. First do the one specific for JBCF, then do any variable contributors.
		IBeanTypeProxy aSetupBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.java.remotevm.Setup"); //$NON-NLS-1$
		IMethodProxy setupMethodProxy = aSetupBeanTypeProxy.getMethodProxy("setup"); //$NON-NLS-1$
		setupMethodProxy.invokeCatchThrowableExceptions(aSetupBeanTypeProxy);
		
		// Now do variable path contributions.
		for (int i = 0; i < variableContributors.size(); i++) {
			IConfigurationContributor contrib = (IConfigurationContributor) variableContributors.get(i);
			if (contrib != null)
				contrib.contributeToRegistry(registry);
		}
	}
}