package org.eclipse.ve.examples.java;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaExampleContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2004-03-30 00:21:12 $ 
 */

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;

import org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter;
import org.eclipse.jem.internal.proxy.core.IConfigurationContributionController;


public class JavaExampleContributor extends ConfigurationContributorAdapter {

	/**
	 * To demonstrate the JSR-57 persistence of beans we use this as the mechanism to add
	 * archiver.jar and crimson.jar to the bootclasspath
	 * This CANNOT be shipped to non-IBM folks and anyone who wants to use this must
	 * make sure they can have a license with Hursley to ship this and use this code as
	 * an example of how to manipulate the bootclasspath ( which is only required for 1.3 ).
	 * 1.4 includes this in the JDK.
	 */
	public void contributeClasspaths(IConfigurationContributionController controller) {
		
		IPluginDescriptor pluginDescriptor = Platform.getPluginRegistry().getPluginDescriptor("org.eclipse.ve.examples");
		controller.contributeClasspath(pluginDescriptor, "archiver.jar", IConfigurationContributionController.PREPEND_BOOT_CLASSPATH, false);
		controller.contributeClasspath(pluginDescriptor, "crimson.jar", IConfigurationContributionController.PREPEND_BOOT_CLASSPATH, false);		
	}	
}