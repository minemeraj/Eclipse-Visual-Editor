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
/*
 *  $RCSfile: JFaceConfigurationContributor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-11-10 16:07:01 $ 
 */

package org.eclipse.ve.internal.jface;

import java.util.logging.Level;

import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class JFaceConfigurationContributor extends ConfigurationContributorAdapter {

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(final ProxyFactoryRegistry registry) {
		// These must run on the UI thread. But we can't do syncExec because we may have a lock that
		// the UI thread requires. The only problem is that this may not execute soon enough for 
		// when they are needed later. So a race condition could occur.
		Runnable jfaceInit = new Runnable() {

			public void run() {
				if (registry.isValid()) {
					IExpression expression = registry.getBeanProxyFactory().createExpression();
					try {
						JFaceColorProxyRegistration.initialize(expression); // Prime the JFace ColorRegistry in the remote VM
						JFaceFontProxyRegistration.initialize(expression); // Prime the JFace FontRegistry in the remote VM
						expression.invokeExpression();
					} catch (IllegalStateException e) {
						JavaVEPlugin.log(e, Level.WARNING);
					} catch (ThrowableProxy e) {
						JavaVEPlugin.log(e, Level.WARNING);
					} catch (NoExpressionValueException e) {
						JavaVEPlugin.log(e, Level.WARNING);
					}
				}
			}
		};
		Display display = Display.getCurrent();
		if (display != null)
			jfaceInit.run();	// Go ahead and run it, we are the UI thread.
		else
			Display.getDefault().asyncExec(jfaceInit);
	}

}
