/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce.launcher;
/*
 *  $RCSfile: JavaBeanSearchEngine.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:10 $ 
 */

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.Assert;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * Search engine for a java bean. The current definition is a type
 * that is public and has a default ctor, or no ctors at all (no ctors at all 
 * means it has a default ctor). Actually the no ctors at all
 * will only be a true test if there are no errors in the type.
 * If the type has errors, it may be that it has no default ctor,
 * but we can't tell. At the moment, I don't know how to determine
 * if there are no errors.
 */
public class JavaBeanSearchEngine {

	public static boolean typeIsABean(IType type) {

		boolean anyCtors = false;
		try {
			if (!type.isClass())
				return false;

			IMethod[] methods = type.getMethods();
			for (int i = 0; i < methods.length; i++) {
				IMethod method = methods[i];
				if (method.isConstructor()) {
					anyCtors = true;
					if (method.getNumberOfParameters() == 0)
						return true;
				}
			}
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e.getStatus(), Level.FINE); // Not really a bad error
			return false;
		}

		return !anyCtors; // If we got this far and no ctors at all, then still a bean.
	}

	private static class JavaBeanCollector implements IJavaSearchResultCollector {
		private List fResult;
		private IProgressMonitor fProgressMonitor;

		public JavaBeanCollector(List result, int style, IProgressMonitor progressMonitor) {
			Assert.isNotNull(result);
			fResult = result;
			fProgressMonitor = progressMonitor;
		}

		public void accept(IResource resource, int start, int end, IJavaElement enclosingElement, int accuracy) {
			if (enclosingElement instanceof IType) { // defensive code
				IType curr = (IType) enclosingElement;
				// Do the test for consider... before typeISABean because those faster than typeISABean.
				if (typeIsABean(curr)) {
					fResult.add(curr);
				}
			} else if (enclosingElement instanceof IMethod) {
				try {
					if (Flags.isPublic(((IMethod) enclosingElement).getFlags())) {
						IType type = (IType) enclosingElement.getAncestor(IJavaElement.TYPE);
						if (Flags.isPublic(type.getFlags()))
							fResult.add(type);
					}
				} catch (JavaModelException e) {
					JavaVEPlugin.log(e.getStatus(), Level.FINE); // Not really a bad error						
				}
			}
		}

		public IProgressMonitor getProgressMonitor() {
			return fProgressMonitor;
		}

		public void aboutToStart() {
		}

		public void done() {
		}
	}

	/**
	 * Searches for all java beans in the given scope.
	 * Valid styles are IJavaElementSearchConstants.CONSIDER_BINARIES and
	 * IJavaElementSearchConstants.CONSIDER_EXTERNAL_JARS
	 */
	public IType[] searchJavaBeans(IProgressMonitor pm, IJavaElement elem, int style) throws JavaModelException {
		List typesFound = new ArrayList(200);

		// The type of search we need to do is very expensive (no quick way to narrow down to what is bean).
		// The following heuristic tries to do some narrowing down. At the moment don't know of any more efficient way.
		List binaryRoots = new ArrayList(0);
		List sourceRoots = new ArrayList(0);
		boolean binaries = (style & IJavaElementSearchConstants.CONSIDER_BINARIES) != 0;
		boolean externals = (style & IJavaElementSearchConstants.CONSIDER_EXTERNAL_JARS) != 0;
		IJavaSearchResultCollector collector = new JavaBeanCollector(typesFound, style, pm);
		if (elem != null) {
			if (elem instanceof IJavaProject || elem instanceof IPackageFragmentRoot)
				gatherRoots(elem, binaryRoots, sourceRoots, binaries, externals);
			else {
				// Do the expensive search because we can't narrow it down anymore.
				IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] { elem }, false);
				new SearchEngine().search(ResourcesPlugin.getWorkspace(), "*", IJavaSearchConstants.TYPE, //$NON-NLS-1$
				IJavaSearchConstants.DECLARATIONS, scope, collector);
			}
		} else {
			IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
			IJavaProject[] projects = model.getJavaProjects();
			for (int i = 0; i < projects.length; i++) {
				gatherRoots(projects[i], binaryRoots, sourceRoots, binaries, externals);
			}
		}

		// Now two separate searches if needed.
		if (!sourceRoots.isEmpty()) {
			// Source roots we need a more complicated test. We need to look at classes and see if there is a default ctor, or there are no ctors. In source that
			// can still signal an implicit default ctor.
			IJavaSearchScope scope =
				SearchEngine.createJavaSearchScope((IJavaElement[]) sourceRoots.toArray(new IJavaElement[sourceRoots.size()]), false);
			new SearchEngine().search(ResourcesPlugin.getWorkspace(), "*", IJavaSearchConstants.TYPE, //$NON-NLS-1$
			IJavaSearchConstants.DECLARATIONS, scope, collector);
		}
		if (!binaryRoots.isEmpty()) {
			// Binary roots simply need default public ctors. Implicit ones will still show up.
			IJavaSearchScope scope =
				SearchEngine.createJavaSearchScope((IJavaElement[]) binaryRoots.toArray(new IJavaElement[binaryRoots.size()]), false);
			new SearchEngine().search(ResourcesPlugin.getWorkspace(), "*()", IJavaSearchConstants.CONSTRUCTOR, //$NON-NLS-1$
			IJavaSearchConstants.DECLARATIONS, scope, collector);
		}

		return (IType[]) typesFound.toArray(new IType[typesFound.size()]);
	}

	protected void gatherRoots(IJavaElement elem, List binaryRoots, List sourceRoots, boolean binaries, boolean externals)
		throws JavaModelException {
		{
			if (elem instanceof IJavaProject) {
				IPackageFragmentRoot[] roots = ((IJavaProject) elem).getPackageFragmentRoots();
				for (int i = 0; i < roots.length; i++) {
					IPackageFragmentRoot root = roots[i];
					if (root.isArchive())
						if (binaries)
							if (root.isExternal())
								if (externals)
									if (!binaryRoots.contains(root))
										binaryRoots.add(root);
					// Binary roots could be in more than one project, so don't add more than once
					else
						;
			else
				;
else if (!binaryRoots.contains(root))
	binaryRoots.add(root); // Binary roots could be in more than one project, so don't add more than once
else
	;
else
	;
else
	sourceRoots.add(root);
}
} else if (elem instanceof IPackageFragmentRoot) {
	// If the selected element is a package fragment root, then do it no matter what.
	binaries = externals = true;
	IPackageFragmentRoot root = (IPackageFragmentRoot) elem;
	if (root.isArchive())
		if (binaries)
			if (root.isExternal())
				if (externals)
					if (!binaryRoots.contains(root))
						binaryRoots.add(root); // Binary roots could be in more than one project, so don't add more than once
	else
		;
else
	;
else if (!binaryRoots.contains(root))
	binaryRoots.add(root); // Binary roots could be in more than one project, so don't add more than once
else
	;
else
	;
else
	sourceRoots.add(root);
}
}
}

/**
 * Searches for all bean types in the given scope.
 * Valid styles are IJavaElementSearchConstants.CONSIDER_BINARIES and
 * IJavaElementSearchConstants.CONSIDER_EXTERNAL_JARS
 */
public IType[] searchJavaBeans(IRunnableContext context, final IJavaElement elem, final int style)
	throws InvocationTargetException, InterruptedException {
	int allFlags = IJavaElementSearchConstants.CONSIDER_EXTERNAL_JARS | IJavaElementSearchConstants.CONSIDER_BINARIES;
	Assert.isTrue((style | allFlags) == allFlags);

	final IType[][] res = new IType[1][];

	IRunnableWithProgress runnable = new IRunnableWithProgress() {
		public void run(IProgressMonitor pm) throws InvocationTargetException {
			try {
				res[0] = searchJavaBeans(pm, elem, style);
			} catch (JavaModelException e) {
				throw new InvocationTargetException(e);
			}
		}
	};
	context.run(true, true, runnable);

	return res[0];
}

}
