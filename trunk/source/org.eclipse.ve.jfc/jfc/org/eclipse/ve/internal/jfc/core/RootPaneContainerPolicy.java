/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: RootPaneContainerPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:42:05 $ 
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
