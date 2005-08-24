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
 *  $RCSfile: FlowLayoutPolicyFactory.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.*;
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
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy cp) {
		return new FlowLayoutSwitcher(cp);
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new FlowLayoutEditPolicy(containerPolicy);
	}
	
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new FlowLayoutPolicyHelper(ep);
	}

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null; // No constraint, so no property descriptor.
	}

	/**
	 * @see ILayoutPolicyFactory#getLayoutManagerInstance(EditDomain)
	 */
	public IJavaInstance getLayoutManagerInstance(IJavaObjectInstance container, JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
	}
	public JavaClass getConstraintClass(ResourceSet rSet) {
		return null;
	}
}
