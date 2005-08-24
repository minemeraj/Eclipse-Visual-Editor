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
 *  $RCSfile: GridLayoutPolicyFactory.java,v $
 *  $Revision: 1.12 $  $Date: 2005-08-24 23:38:10 $ 
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

public class GridLayoutPolicyFactory implements ILayoutPolicyFactory {
	/**
	 * GridLayoutPolicyFactory constructor comment.
	 */
	public GridLayoutPolicyFactory() {
		super();
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new GridLayoutEditPolicy(containerPolicy);
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
	public IJavaInstance getLayoutManagerInstance(IJavaObjectInstance container, JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
	}

	public JavaClass getConstraintClass(ResourceSet rSet) {
		return null;
	}
}
