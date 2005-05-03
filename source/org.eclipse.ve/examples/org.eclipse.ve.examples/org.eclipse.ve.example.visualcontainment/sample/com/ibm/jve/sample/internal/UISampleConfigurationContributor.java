package com.ibm.jve.sample.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter;
import org.eclipse.jem.internal.proxy.core.IConfigurationContributionController;

import com.ibm.jve.sample.SamplePlugin;

public class UISampleConfigurationContributor extends ConfigurationContributorAdapter {

	public void contributeClasspaths(IConfigurationContributionController controller) throws CoreException {
		// Add the jar file with the supporting classes required for the JVE into the classpath
		// In development model the file proxy.jars redirects this to the plugin development project
		controller.contributeClasspath(SamplePlugin.getDefault().getBundle(), "vm/jvesamplevm.jar", IConfigurationContributionController.APPEND_USER_CLASSPATH, true); //$NON-NLS-1$		
	}

}