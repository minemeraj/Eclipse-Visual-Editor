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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:42:31 $ 
 */

import java.io.File;
import java.util.List;

import org.eclipse.jdt.launching.VMRunnerConfiguration;

import org.eclipse.jem.internal.proxy.core.*;

public class JavaExampleContributor implements IConfigurationContributor {

	public void contributeClasspaths(List classPaths, IClasspathContributionController controller) {
	}
	
	/**
	 * To demonstrate the JSR-57 persistence of beans we use this as the mechanism to add
	 * archiver.jar and crimson.jar to the bootclasspath
	 * This CANNOT be shipped to non-IBM folks and anyone who wants to use this must
	 * make sure they can have a license with Hursley to ship this and use this code as
	 * an example of how to manipulate the bootclasspath ( which is only required for 1.3 ).
	 * 1.4 includes this in the JDK.
	 */
	public void contributeToConfiguration(VMRunnerConfiguration aConfig) {
		String[] existingVMArgs = aConfig.getVMArguments();
		// archiver.jar and crimson.jar are in the plugin root
		String archiverPath =
			ProxyPlugin.getPlugin().localizeFromPlugin(JavaExamplePlugin.getPlugin(), "archiver.jar"); //$NON-NLS-1$
		String crimsonPath =
			ProxyPlugin.getPlugin().localizeFromPlugin(JavaExamplePlugin.getPlugin(), "crimson.jar"); //$NON-NLS-1$
		if (archiverPath != null && crimsonPath != null) {
			// This is done rather than using the getBootClassPath and setBootClassPath
			// of VMRunnerConfiguration because that doesn't put a /p in which means that the 
			// the path is not prepended to but is totally replaced and this causes problems
			String archiverArgs =
				"-Xbootclasspath/p:" + archiverPath + File.pathSeparator + crimsonPath + File.pathSeparator; //$NON-NLS-1$
	
			int length = existingVMArgs.length;
			String[] newVMArgs = new String[length + 1];
			System.arraycopy(existingVMArgs, 0, newVMArgs, 0, length);
			newVMArgs[length] = archiverArgs;
			aConfig.setVMArguments(newVMArgs);
		}
	}
	/*
	 * @see IConfigurationContributor#contributeToRegistry(ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry aRegistry) {
	}

}