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
 *  $RCSfile: JMenuContainerPolicy.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:38:09 $ 
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
 * Container policy for JMenu. 
 * Allow subclasses of Component, and Action, or a String to be dropped.
 */
public class JMenuContainerPolicy extends BaseJavaContainerPolicy {
	protected JavaClass classComponent,
							classAction,
							classString;

	/**
	 * Constructor for JMenuContainerPolicy.
	 * @param domain
	 */
	public JMenuContainerPolicy(EditDomain domain) {
		super(null, domain);
		// Can't set the containment SF until after we get the container. This is because
		// this class is used both for JMenu and JPopupMenu, and the "items" feature
		// is not the same physical feature.
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		classComponent = Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
		classAction = Utilities.getJavaClass("javax.swing.Action", rset); //$NON-NLS-1$
		classString = Utilities.getJavaClass("java.lang.String", rset); //$NON-NLS-1$
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#isValidChild(java.lang.Object, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected boolean isValidChild(Object component, EStructuralFeature containmentSF) {
		return classComponent.isInstance(component)	|| 
				classAction.isInstance(component)	||
				classString.isInstance(component);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.ContainerPolicy#setContainer(java.lang.Object)
	 */
	public void setContainer(Object container) {
		super.setContainer(container);
		if (container != null) {
			JavaClass modelType = (JavaClass) ((EObject) container).eClass();
			setContainerFeature(modelType.getEStructuralFeature("items")); //$NON-NLS-1$
		}
	}
}
