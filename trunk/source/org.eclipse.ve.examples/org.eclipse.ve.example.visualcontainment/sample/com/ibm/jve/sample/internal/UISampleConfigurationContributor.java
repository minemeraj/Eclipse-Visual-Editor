/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
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