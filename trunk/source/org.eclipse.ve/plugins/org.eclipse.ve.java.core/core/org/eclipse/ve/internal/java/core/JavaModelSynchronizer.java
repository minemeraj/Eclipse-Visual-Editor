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
 *  $Revision: 1.3 $  $Date: 2004-06-09 22:47:02 $ 
 */

import java.util.Iterator;

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
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.adapters.jdom.JavaModelListener#getJavaProject()
	 */
	protected IJavaProject getJavaProject() {
		return fProject;
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
		switch (delta.getKind()) {
			case IJavaElementDelta.CHANGED : {
				// A file save had occurred. It doesn't matter if currently working copy or not.
				// It means something has changed to the file on disk, but don't know what.
				if ((delta.getFlags() & IJavaElementDelta.F_PRIMARY_RESOURCE) != 0) {
					testForInnerClasses(getFullNameFromElement(element));	//Including inner classes, we don't know if it was just them that changed or not at all. no way of knowing.
				}						
				
				break;
			}
			case IJavaElementDelta.ADDED :
			case IJavaElementDelta.REMOVED : {
				// It doesn't matter if totally removed or just moved somewhere else, recycle
				// because there could be a rename which would be a different class.
				// Currently the element is already deleted and there is no way to find the types in the unit to remove.
				// So instead we ask factory to see if any registered that start with it plus for inner classes.
				testForInnerClasses(getFullNameFromElement(element));	//Including inner classes, we don't know if it was just them that changed or not at all. no way of knowing.
				break;
			}
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
	
	protected void processJavaElementChanged(IPackageFragment element, IJavaElementDelta delta) {
		switch (delta.getKind()) {
			case IJavaElementDelta.ADDED:
				break;	// Don't need to do anything on a new package. If this was from a new fragroot, we would recycle already. Otherwise, it will find this package on the first use.
			case IJavaElementDelta.REMOVED:
				if (delta.getAffectedChildren().length == 0)
					recycleVM = true;	// Since package was removed, we should recyle to get a clean classloader.
				break;
			default :
				super.processJavaElementChanged(element, delta);
		}
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
		if (proxyDomain.getProxyFactoryRegistry() == null || !proxyDomain.getProxyFactoryRegistry().isValid())
			return;	// Don't even bother, our factory is already terminated or we don't have one yet.
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