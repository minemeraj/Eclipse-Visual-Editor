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
