package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: JFCVisualContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-20 00:43:58 $ 
 */

import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.launching.VMRunnerConfiguration;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

public class JFCVisualContributor implements IConfigurationContributor {

	public void contributeClasspaths(List classPaths, IClasspathContributionController controller) {
		// Add the visualvm.jar and nls jars to the end of the classpath. This jar contains the vm stuff
		// needed for doing JVE editing.
		controller.contributeClasspath(
			ProxyPlugin.getPlugin().urlLocalizeFromPluginDescriptorAndFragments(JFCVisualPlugin.getPlugin().getDescriptor(), "vm/jfcvm.jar"), //$NON-NLS-1$
			classPaths,
			-1);
	}
	/*
	 * @see IConfigurationContributor#contributeToConfiguration(VMRunnerConfiguration)
	 */
	public void contributeToConfiguration(VMRunnerConfiguration config) {
	}

	/*
	 * @see IConfigurationContributor#contributeToRegistry(ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry registry) {
		String lookAndFeelClass =
			VCEPreferences.getPlugin().getPluginPreferences().getString(VCEPreferences.SWING_LOOKANDFEEL);
		if (lookAndFeelClass != null & lookAndFeelClass.length() > 0) {
			try {
				IBeanTypeProxy aUIManagerBeanTypeProxy =
					registry.getBeanTypeProxyFactory().getBeanTypeProxy("javax.swing.UIManager"); //$NON-NLS-1$
				IMethodProxy setLookAndFeelMethodProxy =
					aUIManagerBeanTypeProxy.getMethodProxy("setLookAndFeel", "javax.swing.LookAndFeel"); //$NON-NLS-1$ //$NON-NLS-2$
				IBeanTypeProxy lookAndFeelType = registry.getBeanTypeProxyFactory().getBeanTypeProxy(lookAndFeelClass);
				if (lookAndFeelType == null)
					JavaVEPlugin.log("L&F not found: " + lookAndFeelClass, Level.WARNING); //$NON-NLS-1$
				else {				
					IBeanProxy windowsLookAndFeelProxy = lookAndFeelType.newInstance();
					setLookAndFeelMethodProxy.invoke(aUIManagerBeanTypeProxy, windowsLookAndFeelProxy);
				}
			} catch (Exception exc) {
				JavaVEPlugin.log("Unable to set target VM to L&F" + lookAndFeelClass, Level.WARNING); //$NON-NLS-1$
				JavaVEPlugin.log(exc, Level.WARNING);
			}
		}
	}

}