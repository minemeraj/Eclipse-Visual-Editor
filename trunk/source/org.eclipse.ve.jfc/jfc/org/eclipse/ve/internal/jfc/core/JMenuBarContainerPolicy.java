package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JMenuBarContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-18 21:54:37 $ 
 */

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BaseJavaContainerPolicy;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * @author pwalker
 *
 * Container policy for JMenuBar. 
 * Allow only JMenu's to be dropped.
 */
public class JMenuBarContainerPolicy extends BaseJavaContainerPolicy {

	/**
	 * Constructor for JMenuBarContainerPolicy.
	 * @param domain
	 */
	public JMenuBarContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), JFCConstants.SF_JMENUBAR_MENUS), domain);
	}

}
