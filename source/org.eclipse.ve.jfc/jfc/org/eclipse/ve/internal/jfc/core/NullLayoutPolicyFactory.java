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
 *  $RCSfile: NullLayoutPolicyFactory.java,v $
 *  $Revision: 1.7 $  $Date: 2004-04-20 09:05:42 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.*;
/**
 * Layout Policy Factory for "null" awt layout. (i.e. no layout manager set on an awt container).
 * Creation date: (10/23/00 3:30:18 PM)
 * @author: Peter Walker
 */
public class NullLayoutPolicyFactory implements ILayoutPolicyFactory {
	/**
	 * NullLayoutPolicyFactory constructor comment.
	 */
	public NullLayoutPolicyFactory() {
		super();
	}
	/**
	 * getConstraintConverter method comment.
	 */
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep) {
		return new NullLayoutSwitcher(ep);
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new NullLayoutEditPolicy(containerPolicy);
	}
	
	
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new NullLayoutPolicyHelper(ep);
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
}