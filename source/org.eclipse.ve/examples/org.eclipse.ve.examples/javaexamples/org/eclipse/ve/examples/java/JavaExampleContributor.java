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
 *  $Revision: 1.2 $  $Date: 2004-03-04 16:14:17 $ 
 */

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import org.eclipse.jem.internal.proxy.core.*;

public class JavaExampleContributor implements IConfigurationContributor {

	/**
	 * To demonstrate the JSR-57 persistence of beans we use this as the mechanism to add
	 * archiver.jar and crimson.jar to the bootclasspath
	 * This CANNOT be shipped to non-IBM folks and anyone who wants to use this must
	 * make sure they can have a license with Hursley to ship this and use this code as
	 * an example of how to manipulate the bootclasspath ( which is only required for 1.3 ).
	 * 1.4 includes this in the JDK.
	 */
	public void contributeClasspaths(IConfigurationContributionController controller) {
		controller.contributeClasspath(JavaExamplePlugin.getPlugin(), "archiver.jar", IConfigurationContributionController.PREPEND_BOOT_CLASSPATH, false);
		controller.contributeClasspath(JavaExamplePlugin.getPlugin(), "crimson.jar", IConfigurationContributionController.PREPEND_BOOT_CLASSPATH, false);		
	}
	
	public void contributeToConfiguration(ILaunchConfigurationWorkingCopy aConfig) {
	}
	
	/*
	 * @see IConfigurationContributor#contributeToRegistry(ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry aRegistry) {
	}

}