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
 *  $RCSfile: JMenuContainerPolicy.java,v $
 *  $Revision: 1.7 $  $Date: 2005-02-15 23:42:05 $ 
 */
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.ve.internal.java.core.JavaContainerPolicy;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * @author pwalker
 *
 * Container policy for JMenu. 
 * Allow subclasses of Component, and Action, or a String to be dropped.
 */
public class JMenuContainerPolicy extends JavaContainerPolicy {
	protected JavaClass classComponent,
							classAction,
							classString;

	/**
	 * Constructor for JMenuContainerPolicy.
	 * @param domain
	 */
	public JMenuContainerPolicy(EditDomain domain) {
		super(null, domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		classComponent = Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
		classAction = Utilities.getJavaClass("javax.swing.Action", rset); //$NON-NLS-1$
		classString = Utilities.getJavaClass("java.lang.String", rset); //$NON-NLS-1$
	}

	protected boolean isValidChild(Object component, EStructuralFeature containmentSF) {
		// Also need to verify that the component is valid 
		return classComponent.isInstance(component)	|| 
				classAction.isInstance(component)	||
				classString.isInstance(component);
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
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.JavaContainerPolicy#isValidBeanLocation(java.lang.Object)
	 */
	protected boolean isValidBeanLocation(Object child) {
		return child instanceof EObject && BeanAwtUtilities.isValidBeanLocation(domain, (EObject)child);
	}
}
