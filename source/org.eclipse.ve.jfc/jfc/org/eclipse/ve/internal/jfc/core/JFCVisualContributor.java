/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JFCVisualContributor.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.logging.Level;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

public class JFCVisualContributor extends ConfigurationContributorAdapter {

	public void contributeClasspaths(IConfigurationContributionController controller) {
		// Add the visualvm.jar and nls jars to the end of the classpath. This jar contains the vm stuff
		// needed for doing JVE editing.
		controller.contributeClasspath(JFCVisualPlugin.getPlugin().getBundle(), "vm/jfcvm.jar", IConfigurationContributionController.APPEND_USER_CLASSPATH, true); //$NON-NLS-1$
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
				if (lookAndFeelType == null) {
					if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
						JavaVEPlugin.log("L&F not found: " + lookAndFeelClass, Level.WARNING); //$NON-NLS-1$
				} else {				
					IBeanProxy windowsLookAndFeelProxy = lookAndFeelType.newInstance();
					setLookAndFeelMethodProxy.invoke(aUIManagerBeanTypeProxy, windowsLookAndFeelProxy);
				}
			} catch (Exception exc) {
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
					JavaVEPlugin.log("Unable to set target VM to L&F" + lookAndFeelClass, Level.WARNING); //$NON-NLS-1$
					JavaVEPlugin.log(exc, Level.WARNING);
				}
			}
		}
	}

}
