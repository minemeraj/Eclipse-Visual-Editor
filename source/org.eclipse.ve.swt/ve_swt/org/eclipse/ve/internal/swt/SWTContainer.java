package org.eclipse.ve.internal.swt;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;


public class SWTContainer implements IClasspathContainer {
	
	private IPath fPath;
	private IClasspathEntry[] fClasspathEntries;
	
	public SWTContainer(){
		// This is a container that resolves to the path of whever SWT is installed in the workbench
		Path path = new Path("ws/win32/swt.jar");
		URL location = Platform.getPlugin("org.eclipse.swt").find(path);
		fPath = new Path(location.getFile());
		fClasspathEntries = new IClasspathEntry[] {
			new ClasspathEntry(IPackageFragmentRoot.K_BINARY,
				IClasspathEntry.CPE_LIBRARY,
				JavaProject.canonicalizedPath(fPath),
				ClasspathEntry.NO_EXCLUSION_PATTERNS,
				null,
				null,
				null,
				true)
		};
	}

	public IClasspathEntry[] getClasspathEntries() {
		return fClasspathEntries;
	}

	public String getDescription() {
		return "Standard Widget Toolkit (SWT)";
	}

	public int getKind() {
		return IClasspathContainer.K_SYSTEM;
	}

	public IPath getPath() {
		return fPath;
	}
	
}
