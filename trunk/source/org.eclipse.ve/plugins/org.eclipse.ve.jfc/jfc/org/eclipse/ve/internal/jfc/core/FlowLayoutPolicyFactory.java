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
 *  $RCSfile: FlowLayoutPolicyFactory.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.internal.java.JavaHelpers;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.BeanUtilities;
/**
 * Layout Policy Factory for the java.awt.FlowLayout.
 * Creation date: (10/23/00 4:47:12 PM)
 * @author: Peter Walker
 */
public class FlowLayoutPolicyFactory implements ILayoutPolicyFactory {

	/**
	 * FlowLayoutPolicyFactory constructor comment.
	 */
	public FlowLayoutPolicyFactory() {
		super();
	}
	/**
	 * getLayoutSwitcher method comment.
	 */
	public ILayoutSwitcher getLayoutSwitcher(ContainerPolicy cp) {
		return new FlowLayoutSwitcher(cp);
	}
	/**
	 * getLayoutInputPolicyClass method comment.
	 */
	public Class getLayoutInputPolicyClass() {
		return FlowLayoutEditPolicy.class;
	}
	public ILayoutPolicyHelper getLayoutPolicyHelper(ContainerPolicy ep) {
		return new FlowLayoutPolicyHelper(ep);
	}

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null; // No constraint, so no property descriptor.
	}

	/**
	 * @see ILayoutPolicyFactory#getLayoutManagerInstance(EditDomain)
	 */
	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, null);
	}
}