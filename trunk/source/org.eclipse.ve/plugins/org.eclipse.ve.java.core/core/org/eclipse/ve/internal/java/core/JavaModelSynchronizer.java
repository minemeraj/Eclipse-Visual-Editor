package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaModelSynchronizer.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.*;

import org.eclipse.jem.internal.adapters.jdom.JavaModelListener;
import org.eclipse.jem.internal.proxy.core.IStandardBeanTypeProxyFactory;
/**
 * This class listens for changes to the java model and terminates
 * the proxy registry if any of the changes affect classes that
 * the registry has loaded.
 */

public class JavaModelSynchronizer extends JavaModelListener {
	protected IBeanProxyDomain proxyDomain;
	protected IJavaProject fProject; // The project this listener is opened on.
	protected IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	private static final IPath CLASSPATH_PATH = new Path(".classpath"); //$NON-NLS-1$	
	private boolean recycleVM = false;
	private Runnable terminateRun;		

	public JavaModelSynchronizer(IBeanProxyDomain proxyDomain, IJavaProject aProject, Runnable terminateRun) {
		super();
		this.proxyDomain = proxyDomain;
		this.terminateRun = terminateRun;
		
		fProject = aProject;
		
		// We're really only interested in Post_Change, not the others, so we will
		// remove ourself (since super ctor added ourself) and then add ourself
		// back with only post change. (Post change is after everything has been
		// reconciled and build).
		JavaCore.removeElementChangedListener(this);
		JavaCore.addElementChangedListener(this, ElementChangedEvent.POST_CHANGE);	
	}
	
	public void setProject(IJavaProject project) {
		fProject = project;
	}

	/**
	 * Stop the synchronizer from listening to any more changes.
	 */
	public void stopSynchronizer() {
		JavaCore.removeElementChangedListener(this);
	}

	protected IJavaProject getJavaProject(IClasspathEntry entry) {
		IProject proj = workspaceRoot.getProject(entry.getPath().segment(0));
		if (proj != null)
			return (IJavaProject) JavaCore.create(proj);
		return null;
	}
	
	private boolean isClassPathChange(IJavaElementDelta delta) {
		int flags = delta.getFlags();
		return (
			delta.getKind() == IJavaElementDelta.CHANGED
				&& ((flags & IJavaElementDelta.F_ADDED_TO_CLASSPATH) != 0)
				|| ((flags & IJavaElementDelta.F_REMOVED_FROM_CLASSPATH) != 0)
				|| ((flags & IJavaElementDelta.F_REORDER) != 0));
	}

	/**
	 * This method will check to see if a <code>javaProject</code> is a project in the
	 * classpath of the adapterFactory java project.
	 */
	protected boolean isInClasspath(IJavaProject javaProject) {
		IJavaProject adapterJavaProject = fProject;
		if (javaProject.equals(adapterJavaProject))
			return true;
		return isInClasspath(javaProject, adapterJavaProject, true, new HashSet());
	}

	protected boolean isInClasspath(IJavaProject testProject, IJavaProject targetProject, boolean isFirstLevel, Set visited) {
		if (visited.contains(targetProject))
			return false;
		visited.add(targetProject);
		IClasspathEntry[] entries = null;
		try {
			entries = targetProject.getRawClasspath();
		} catch (JavaModelException e) {
			return false;
		}
		IClasspathEntry entry, resEntry;
		IJavaProject proj = null;
		List projects = null;
		for (int i = 0; i < entries.length; i++) {
			entry = entries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				resEntry = JavaCore.getResolvedClasspathEntry(entry);
				proj = getJavaProject(resEntry);
				if (isFirstLevel || resEntry.isExported()) {
					if (proj.equals(testProject))
						return true;
					else {
						if (projects == null)
							projects = new ArrayList();
						projects.add(proj);
					}
				}
			}
		}
		return isInClasspath(testProject, projects, false, visited);
	}
	
	protected boolean isInClasspath(IJavaProject testProject, List someJavaProjects, boolean isFirstLevel, Set visited) {
		if (someJavaProjects == null)
			return false;
		int size = someJavaProjects.size();
		IJavaProject javaProj = null;
		for (int i = 0; i < size; i++) {
			javaProj = (IJavaProject) someJavaProjects.get(i);
			return isInClasspath(testProject, javaProj, isFirstLevel, visited);
		}
		return false;
	}

	/*
	 * Test if .classpath is part of the change. This is necessary
	 * because .classpath changes of DEPENDENT PROJECTS are not signaled
	 * through classpath change mechanisms of the top project.
	 */
	private boolean isClasspathResourceChange(IJavaElementDelta delta) {
		IResourceDelta[] resources = delta.getResourceDeltas();
		if (resources == null)
			return false;
		IPath path = null;
		for (int i = 0; i < resources.length; i++) {
			if (resources[i].getKind() == IResourceDelta.CHANGED) {
				path = resources[i].getProjectRelativePath();
				if (path.equals(CLASSPATH_PATH))
					return true;
			}
		}
		return false;
	}

	protected void processJavaElementChanged(IJavaProject element, IJavaElementDelta delta) {
		if (isInClasspath(element)) {
			if (delta.getKind() == IJavaElementDelta.REMOVED || delta.getKind() == IJavaElementDelta.ADDED) {
				// Don't need to do anything for delete/close/add/open of main project because Eclipse will close us down
				// anyway for this.			
				if (!element.equals(fProject)) {
					// However, all other projects are required projects and if they are deleted/closed/added/opened when need to do
					// a recycle because we don't know any of the state, whether they are still there or not.
					recycleVM = true;
					return;	// Don't need to process further since we will recycle.
				}
				return;
			} else if (isClasspathResourceChange(delta)) {
				recycleVM = true; // The .classpath file itself in SOME DEPENDENT PROJECT has changed. 
				return;	// Don't need to process further since we will recycle.
			}
			processChildren(element, delta);
		}
	}

	/**
	 * Handle the change for a single element, children will be handled separately.
	 * If a working copy, then ignore it because we don't care about changes until
	 * they are committed. Else, if the CU has changed content then mark all of the
	 * types in this CU (such as inner classes) as stale.
	 * If it is not a content change then process the children.
	 */
	protected void processJavaElementChanged(ICompilationUnit element, IJavaElementDelta delta) {
		if (!element.isWorkingCopy()) {
			if ((delta.getKind() == IJavaElementDelta.CHANGED || delta.getKind() == IJavaElementDelta.ADDED)) {
				try {
					IType[] flushTypes = element.getAllTypes();
					IStandardBeanTypeProxyFactory btypeFactory = proxyDomain.getProxyFactoryRegistry().getBeanTypeProxyFactory();
					for (int i = 0; i < flushTypes.length; i++) {
						String typeName = flushTypes[i].getFullyQualifiedName();
						if (btypeFactory.isBeanTypeRegistered(typeName) || btypeFactory.isBeanTypeNotFound(typeName)) {
							recycleVM = true;
							return;	// Don't need to process further since we will recycle.
						}
					}
				} catch (JavaModelException e) {
				}
			}
			if (delta.getKind() == IJavaElementDelta.REMOVED) {
				// It doesn't matter if totally removed or just moved somewhere else, recycle
				// because there could be a rename which would be a different class.
				// Currently the element is already deleted and there is no way to find the types in the unit to remove.
				// So instead we ask factory to see if any registered that start with it plus for inner classes.
				testForInnerClasses(getFullNameFromElement(element));
				return;
				// Since the compilation unit was removed we don't need to process the children (actually the children list will be empty
			}
			processChildren(element, delta);
		}
	}

	protected void testForInnerClasses(String sourceName) {
		String sourceNameForInner = sourceName + '$';
		IStandardBeanTypeProxyFactory btypeFactory = proxyDomain.getProxyFactoryRegistry().getBeanTypeProxyFactory();				
		Iterator itr = btypeFactory.registeredTypes().iterator();
		while (itr.hasNext()) {
			String entryName = (String) itr.next();
			if (entryName.equals(sourceName) || entryName.startsWith(sourceNameForInner)) {
				recycleVM = true;
				return;	// Don't need to process further since we will recycle.						
			}
		}
		return;
	}

	/**
	 * Handle the change for a single element, children will be handled separately.
	 */
	protected void processJavaElementChanged(IClassFile element, IJavaElementDelta delta) {
		if (delta.getKind() == IJavaElementDelta.REMOVED) {
			// It doesn't matter if totally removed or just moved somewhere else, we will clear out and remove the
			// adapter because there could be a rename which would be a different class.
			// Currently the element is already deleted and there is no way to find the types in the unit to remove.
			// So instead we ask factory to remove all it any that start with it plus for inner classes.
			testForInnerClasses(getFullNameFromElement(element));
			return; // Since the classfile was removed we don't need to process the children (actually the children list will be empty
		}
		processChildren(element, delta);
	}

	protected String getFullNameFromElement(IJavaElement element) {
		String name = element.getElementName();		
		if (!(element instanceof ICompilationUnit || element instanceof IClassFile))
			return name;	// Shouldn't be here
		
		// remove extension.
		int periodNdx = name.lastIndexOf('.');
		if (periodNdx == -1)
			return name;	// Shouldn't be here. There should be an extension
					
		String typeName = null;
		String parentName = element.getParent().getElementName();
		if (parentName == null || parentName.length() == 0)
			typeName = name.substring(0, periodNdx); // In default package
		else
			typeName = parentName + "." + name.substring(0, periodNdx); //$NON-NLS-1$
								
		return typeName;
	}

	/**
	 * Handle the change for a single element, children will be handled separately.
	 * If the classpath has changed, recycle because we don't know what
	 * has changed. Things that were in the path may no longer be in the path, or
	 * the order was changed, which could affect the introspection.
	 */
	protected void processJavaElementChanged(IPackageFragmentRoot element, IJavaElementDelta delta) {
		if (isClassPathChange(delta))
			recycleVM = true;
		else
			super.processJavaElementChanged(element, delta);
	}

	/**
	 * Handle the change for a single element, children will be handled separately.
	 * Something about the type has changed. If it was removed (not a move), then recycle.
	 */
	protected void processJavaElementChanged(IType element, IJavaElementDelta delta) {
		if (!recycleVM) {
			IStandardBeanTypeProxyFactory btypeFactory = proxyDomain.getProxyFactoryRegistry().getBeanTypeProxyFactory();
			String typeName = element.getFullyQualifiedName();		
			if (btypeFactory.isBeanTypeRegistered(typeName) || btypeFactory.isBeanTypeNotFound(typeName))
				recycleVM = true;
		}
	}

	public String toString() {
		return super.toString()+" "+fProject.getElementName(); //$NON-NLS-1$
	}
	/**
	 * @see org.eclipse.jdt.core.IElementChangedListener#elementChanged(ElementChangedEvent)
	 */
	public void elementChanged(ElementChangedEvent event) {
		if (!proxyDomain.getProxyFactoryRegistry().isValid())
			return;	// Don't even bother, our factory is already terminated.
		// We want to know when we start process all deltas, and then we end. At the end if
		// the recycle flag is set, we will only do one recycle.
		recycleVM = false;
		super.elementChanged(event);
		if (recycleVM) {
			recycleVM = false;
			terminateRun.run();
		}
	}

}