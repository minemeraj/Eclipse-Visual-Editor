/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: ScrollPaneContainerPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:48 $ 
 */

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

public class ScrollPaneContainerPolicy extends ContainerPolicy {
	protected JavaClass classComponent;
	protected URI SF_COMPONENT;
	protected EStructuralFeature componentSF;

	public ScrollPaneContainerPolicy(EditDomain domain) {
		super(domain);
		classComponent = (JavaClass) Utilities.getJavaClass("java.awt.Component", JavaEditDomainHelper.getResourceSet(domain));	//$NON-NLS-1$
	}

	/*
	 * Return true if we have no children. A ScrollPane can only have one child.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		if (!super.isValidChild(child, containmentSF))
			return false;
			
		IJavaObjectInstance scrollPaneBean = (IJavaObjectInstance) getContainer();
		List components = (List) scrollPaneBean.eGet(containmentSF);
		return (components.isEmpty());
	}
}
