/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: RegisteredClasspathContainerInitializer.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-30 00:21:08 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IJavaProject;
 

/**
 * Classpath container initializer for simple java ve containers.
 * 
 * @since 1.0.0
 */
public class RegisteredClasspathContainerInitializer extends ClasspathContainerInitializer {

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.ClasspathContainerInitializer#initialize(org.eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		int size = containerPath.segmentCount();
		if (size > 0) {
			IClasspathContainer container = new RegisteredClasspathContainer(containerPath);
			JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project}, new IClasspathContainer[] {container}, null);
		}
	}
}
