/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 *  $Revision: 1.2 $  $Date: 2004-03-15 22:31:11 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper;
import org.eclipse.ve.internal.java.visual.ILayoutSwitcher;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 
/**
 * 
 * @since 1.0.0
 */
public class NullLayoutPolicyFactory implements ILayoutPolicyFactory {
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory#getLayoutSwitcher(org.eclipse.ve.internal.java.visual.VisualContainerPolicy)
	 */
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep) {
		return new NullLayoutSwitcher(ep);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory#getLayoutEditPolicy(org.eclipse.ve.internal.java.visual.VisualContainerPolicy)
	 */
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		// The NullLayoutEditPolicy is the one used here but requires a special ctor in 
		// addition to the containerPolicy so it's instantiated in the CompositeGraphicalEditPart for now
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory#getLayoutPolicyHelper(org.eclipse.ve.internal.java.visual.VisualContainerPolicy)
	 */
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new NullLayoutPolicyHelper(ep);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory#getConstraintPropertyDescriptor(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory#getLayoutManagerInstance(org.eclipse.jem.java.JavaHelpers, org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		return null;
	}
}
