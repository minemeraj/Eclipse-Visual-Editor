package org.eclipse.ve.internal.swt;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;

import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class SWTContainer implements IClasspathContainer {
	
	private IClasspathEntry[] fClasspathEntries;
	private IPath containerPath;	// path for container, NOT path for resolved entry
	
	public SWTContainer(IPath containerPath){
		this.containerPath = containerPath;
		// This is a container that resolves to the path of whenever SWT is installed in the workbench
		Path path = new Path("$ws$/swt.jar");
		URL location = Platform.getPlugin("org.eclipse.swt").find(path);
		try {
			path = new Path(Platform.resolve(location).getFile());
			fClasspathEntries =
				new IClasspathEntry[] {JavaCore.newLibraryEntry(path, null, null)};
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
// TODO uncomment when can figure out how to get dll path into beaninfo too.		return IClasspathContainer.K_APPLICATION;	// Goes on regular classpath, not bootpath.
		return IClasspathContainer.K_SYSTEM;	// this mean it won't show on classpaths started by proxy. so we need configurator to do it.
	}

	public IPath getPath() {
		return containerPath;
	}
	
}
