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
 *  $RCSfile: RootPaneContainerPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:18:06 $ 
 */

import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.ve.internal.java.core.JavaContainerPolicy;

/**
 * Container policy for rootpanes whose only child is a contentpane...
 */
public class RootPaneContainerPolicy extends JavaContainerPolicy {

	/**
	 * Constructor for RootPaneContainerPolicy.
	 * @param domain
	 */
	public RootPaneContainerPolicy(EditDomain domain) {
		super(null, domain);
	}

	/**
	 * @see org.eclipse.ve.internal.cde.core.ContainerPolicy#setContainer(Object)
	 */
	public void setContainer(Object container) {
		super.setContainer(container);
		if (container != null) {
			JavaClass modelType = (JavaClass) ((EObject) container).eClass();
			containmentSF = modelType.getEStructuralFeature("contentPane"); //$NON-NLS-1$
		}
	}

}