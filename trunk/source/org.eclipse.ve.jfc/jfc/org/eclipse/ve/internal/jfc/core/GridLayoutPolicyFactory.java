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
 *  $RCSfile: GridLayoutPolicyFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2004-01-21 00:00:29 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.*;

public class GridLayoutPolicyFactory implements ILayoutPolicyFactory {
	/**
	 * GridLayoutPolicyFactory constructor comment.
	 */
	public GridLayoutPolicyFactory() {
		super();
	}
	public Class getLayoutInputPolicyClass() {
		return FlowLayoutEditPolicy.class;
	}
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new FlowLayoutPolicyHelper(ep);
	}
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy cp) {
		return new GridLayoutSwitcher(cp);
	}

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null; // No constraint, so no property descriptor.
	}

	/**
	 * @see ILayoutPolicyFactory#getLayoutManagerInstance(EditDomain)
	 */
	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
	}
}