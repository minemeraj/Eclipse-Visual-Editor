package org.eclipse.ve.internal.swt;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.*;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class SWTContainer implements IClasspathContainer, IConfigurationContributor {
	
	private IClasspathEntry[] fClasspathEntries;
	
	private IPath containerPath;	// path for container, NOT path for resolved entry
	
	private final static String[][] swtLibraries = new String[][] {
			{ "org.eclipse.swt", "$ws$/swt.jar" }
	};
	
	private final static String[][] jfaceLibraries = new String[][] {
			{ "org.eclipse.jface", "jface.jar" },
			{ "org.eclipse.core.runtime", "runtime.jar" },
			{ "org.eclipse.core.runtime.compatibility", "compatibility.jar" },
			{ "org.eclipse.osgi", "osgi.jar" }			
	};
	
	public SWTContainer(IPath containerPath){
		this.containerPath = containerPath;

		try {
			boolean isJFace = isJFace(containerPath);
			
			if (isJFace) {
				fClasspathEntries = new IClasspathEntry[swtLibraries.length + jfaceLibraries.length];
			} else {
				fClasspathEntries = new IClasspathEntry[swtLibraries.length];
			}
			
			int i;
			for (i = 0; i < swtLibraries.length; i++) {
				Path path = new Path(swtLibraries[i][1]);
				URL location = Platform.getPlugin(swtLibraries[i][0]).find(path);
				path = new Path(Platform.resolve(location).getFile());
				fClasspathEntries[i] = JavaCore.newLibraryEntry(path, null, null);	
			}
			
			if (isJFace) {
				for (int j = 0; j < jfaceLibraries.length; j++) {
					Path path = new Path(jfaceLibraries[j][1]);
					URL location = Platform.getPlugin(jfaceLibraries[j][0]).find(path);
					path = new Path(Platform.resolve(location).getFile());
					fClasspathEntries[j + i] = JavaCore.newLibraryEntry(path, null, null);
				}
			}
		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.INFO);
		}
	}
	
	private boolean isJFace(IPath containerPath) {
		// The first segment is the SWT_CONTAINER name
		for (int i = 1; i < containerPath.segmentCount(); i++) {
			if (containerPath.segment(i).equals("JFACE")) {
				return true;
			}
		}
		return false;
	}

	public IClasspathEntry[] getClasspathEntries() {
		return fClasspathEntries;
	}

	public String getDescription() {
		return "Standard Widget Toolkit (SWT)";
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return containerPath;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#initialize(org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo)
	 */
	public void initialize(IConfigurationContributionInfo info) {
		SWTConfigurationContributor.INSTANCE.initialize(info);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeClasspaths(org.eclipse.jem.internal.proxy.core.IConfigurationContributionController)
	 */
	public void contributeClasspaths(IConfigurationContributionController controller) throws CoreException {
		SWTConfigurationContributor.INSTANCE.contributeClasspaths(controller);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeToConfiguration(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void contributeToConfiguration(ILaunchConfigurationWorkingCopy config) throws CoreException {
		SWTConfigurationContributor.INSTANCE.contributeToConfiguration(config);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IConfigurationContributor#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry registry) {
		SWTConfigurationContributor.INSTANCE.contributeToRegistry(registry);
	}
}
