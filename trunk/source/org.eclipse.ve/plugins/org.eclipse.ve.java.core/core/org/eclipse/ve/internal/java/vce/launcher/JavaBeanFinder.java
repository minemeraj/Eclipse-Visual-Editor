/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce.launcher;
/*
 *  $RCSfile: JavaBeanFinder.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-22 13:00:22 $ 
 */

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * This finds Java Beans within a set of selected elements.
 * The current definition we are using is there is a default ctor (or an implicit default ctor).
 */
public class JavaBeanFinder {
	
	private JavaBeanFinder() {
	}

	public static IType[] findTargets(IRunnableContext context, final Object[] elements) throws InvocationTargetException, InterruptedException{
		final Set result= new HashSet();
	
		if (elements.length > 0) {
			IRunnableWithProgress runnable= new IRunnableWithProgress() {
				public void run(IProgressMonitor pm) throws InterruptedException {
					int nElements= elements.length;
					pm.beginTask(VCELauncherMessages.JavaBeansFinder_SearchMessage, nElements); 
					try {
						for (int i= 0; i < nElements; i++) {
							try {
								collectTypes(elements[i], new SubProgressMonitor(pm, 1), result);
							} catch (JavaModelException e) {
								JavaVEPlugin.log(e, Level.WARNING);
							}
							if (pm.isCanceled()) {
								throw new InterruptedException();
							}
						}
					} finally {
						pm.done();
					}
				}
			};
			context.run(true, true, runnable);			
		}
		return (IType[]) result.toArray(new IType[result.size()]) ;
	}
			
	private static void collectTypes(Object element, IProgressMonitor monitor, Set result) throws JavaModelException {
		if (element instanceof IProcess) {
			element= ((IProcess)element).getLaunch();
		} else if (element instanceof IDebugTarget) {
			element= ((IDebugTarget)element).getLaunch();
		}
		
		if (element instanceof IAdaptable) {
			IJavaElement jelem= (IJavaElement) ((IAdaptable) element).getAdapter(IJavaElement.class);
			if (jelem != null) {
				IType parentType= (IType)jelem.getAncestor(IJavaElement.TYPE);
				if (parentType != null && JavaBeanSearchEngine.typeIsABean(parentType)) {
					result.add(parentType);
					monitor.done();
					return;
				}
				IJavaElement openable= (IJavaElement) jelem.getOpenable();
				if (openable != null) {
					if (openable.getElementType() == IJavaElement.COMPILATION_UNIT) {
						ICompilationUnit cu= (ICompilationUnit) openable;
						IType mainType= cu.getType(Signature.getQualifier(cu.getElementName()));
						if (mainType.exists() && JavaBeanSearchEngine.typeIsABean(mainType)) {
							result.add(mainType);
						}
						monitor.done();
						return;
					} else if (openable.getElementType() == IJavaElement.CLASS_FILE) {
						IType mainType= ((IClassFile)openable).getType();
						if (JavaBeanSearchEngine.typeIsABean(mainType)) {
							result.add(mainType);
						}
						monitor.done();
						return;	
					}
				}
				IType[] types= searchForBeans(jelem, monitor);
				for (int i= 0; i < types.length; i++) {
					result.add(types[i]);
				}				
			}
		}
	}
	
	private static IType[] searchForBeans(IJavaElement elem, IProgressMonitor monitor) throws JavaModelException {
		JavaBeanSearchEngine searchEngine= new JavaBeanSearchEngine();
		return searchEngine.searchJavaBeans(monitor, elem, IJavaElementSearchConstants.CONSIDER_BINARIES);
	}
}
	
