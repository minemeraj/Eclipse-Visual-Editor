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

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;

public class SWTContainerInitializer extends ClasspathContainerInitializer{
	
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {		
			int size = containerPath.segmentCount();
			if (size > 0) {
				if (containerPath.segment(0).equals("SWT_CONTAINER")) {
					SWTContainer container = new SWTContainer(containerPath);
					JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project}, new IClasspathContainer[] {container}, null);
				}
			}
		}	

}
