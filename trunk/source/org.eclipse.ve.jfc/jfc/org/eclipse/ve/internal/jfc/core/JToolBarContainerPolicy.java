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
 *  $RCSfile: JToolBarContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.ve.internal.java.core.JavaContainerPolicy;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * @author pwalker
 *
 * Container policy for JToolBar. Children can be components or Actions.
 */
public class JToolBarContainerPolicy extends JavaContainerPolicy {
	protected JavaClass classComponent, classAction;

	/**
	 * Constructor for JToolBarContainerPolicy.
	 * @param domain
	 */
	public JToolBarContainerPolicy(EditDomain domain) {
		super(null, domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		classComponent = (JavaClass) Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
		classAction = (JavaClass) Utilities.getJavaClass("javax.swing.Action", rset); //$NON-NLS-1$
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
			containmentSF = modelType.getEStructuralFeature("items"); //$NON-NLS-1$
		}
	}

}
