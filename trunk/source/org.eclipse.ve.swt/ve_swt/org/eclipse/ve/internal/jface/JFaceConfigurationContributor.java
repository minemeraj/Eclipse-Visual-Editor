/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JFaceConfigurationContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-04-03 06:04:11 $ 
 */

package org.eclipse.ve.internal.jface;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter;
import org.eclipse.jem.internal.proxy.core.IConfigurationContributionController;
import org.eclipse.ve.internal.swt.SwtPlugin;

public class JFaceConfigurationContributor extends ConfigurationContributorAdapter {
	
public void contributeClasspaths(final IConfigurationContributionController controller) throws CoreException {	
	// Add the jar file with the supporting classes required for the JVE into the classpath
	// In development model the file proxy.jars redirects this to the plugin development project
	controller.contributeClasspath(SwtPlugin.getDefault().getBundle(), "jbcfjfacevm.jar", IConfigurationContributionController.APPEND_USER_CLASSPATH, false); //$NON-NLS-1$
	
}
}