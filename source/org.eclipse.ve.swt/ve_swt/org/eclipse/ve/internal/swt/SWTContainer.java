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
			JavaVEPlugin.log(e, Level.INFO);
		}
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
