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
 *  $RCSfile: JToolBarContainerPolicy.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-18 21:54:37 $ 
 */
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.core.*;

/**
 * @author pwalker
 *
 * Container policy for JToolBar. Children can be components or Actions.
 */
public class JToolBarContainerPolicy extends BaseJavaContainerPolicy {
	protected JavaClass classComponent, classAction;

	/**
	 * Constructor for JToolBarContainerPolicy.
	 * @param domain
	 */
	public JToolBarContainerPolicy(EditDomain domain) {
		super(null, domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		classComponent = Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
		classAction = Utilities.getJavaClass("javax.swing.Action", rset); //$NON-NLS-1$
	}

	protected boolean isValidChild(Object component, EStructuralFeature containmentSF) {
		// Also need to verify that the component is valid 
		return classComponent.isInstance(component) || classAction.isInstance(component);
	}

	/**
	 * @see org.eclipse.ve.internal.cde.core.ContainerPolicy#setContainer(Object)
	 */
	public void setContainer(Object container) {
		super.setContainer(container);
		if (container != null) {
			JavaClass modelType = (JavaClass) ((EObject) container).eClass();
			setContainerFeature(modelType.getEStructuralFeature("items")); //$NON-NLS-1$
		}
	}

}
