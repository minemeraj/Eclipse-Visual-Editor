package org.eclipse.ve.internal.swt;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;

import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class SWTContainer implements IClasspathContainer {
	
	private IPath fPath;
	private IClasspathEntry[] fClasspathEntries;
	
	public SWTContainer(){
		// This is a container that resolves to the path of whever SWT is installed in the workbench
		Path path = new Path("$ws$/swt.jar");
		URL location = Platform.getPlugin("org.eclipse.swt").find(path);
		try {
			fPath = new Path(Platform.resolve(location).getFile());
			fClasspathEntries =
				new IClasspathEntry[] {
					 new ClasspathEntry(
						IPackageFragmentRoot.K_BINARY,
						IClasspathEntry.CPE_LIBRARY,
						JavaProject.canonicalizedPath(fPath),
						ClasspathEntry.NO_EXCLUSION_PATTERNS,
						null,
						null,
						null,
						true)};
		} catch (IOException e) {
			JavaVEPlugin.log(e, MsgLogger.LOG_INFO);
		}
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
