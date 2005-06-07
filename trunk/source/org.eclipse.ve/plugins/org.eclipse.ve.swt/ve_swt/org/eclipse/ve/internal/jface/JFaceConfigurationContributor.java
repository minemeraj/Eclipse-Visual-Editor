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
 *  $Revision: 1.3 $  $Date: 2005-06-07 20:12:15 $ 
 */

package org.eclipse.ve.internal.jface;

import org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter;
import org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry;

public class JFaceConfigurationContributor extends ConfigurationContributorAdapter {

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.ConfigurationContributorAdapter#contributeToRegistry(org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry)
	 */
	public void contributeToRegistry(ProxyFactoryRegistry registry) {
		JFaceColorProxyRegistration.initialize(registry);	// Prime the JFace ColorRegistry in the remote VM
		JFaceFontProxyRegistration.initialize(registry);	// Prime the JFace FontRegistry in the remote VM
	}

}