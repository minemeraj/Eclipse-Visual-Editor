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
 *  $RCSfile: JScrollPaneContainerPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-07-15 22:36:56 $ 
 */

import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.*;
/**
 * Container Edit Policy for Bean Compositions.
 */
public class JScrollPaneContainerPolicy extends JavaContainerPolicy {
	
	public JScrollPaneContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), JFCConstants.SF_JSCROLLPANE_VIEWPORTVIEW ), domain);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.JavaContainerPolicy#isValidBeanLocation(java.lang.Object)
	 */
	protected boolean isValidBeanLocation(Object child) {
		return child instanceof EObject && BeanUtilities.isValidBeanLocation(domain, (EObject)child);
	}
}


