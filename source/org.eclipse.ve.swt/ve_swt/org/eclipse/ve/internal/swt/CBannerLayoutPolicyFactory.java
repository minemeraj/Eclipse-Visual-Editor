/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CBannerLayoutPolicyFactory.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-17 18:10:29 $ 
 */
package org.eclipse.ve.internal.swt;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.java.visual.*;
 
/**
 * 
 * @since 1.1.0
 */
public class CBannerLayoutPolicyFactory implements ILayoutPolicyFactory {

	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep) {
		return null;
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new CBannerLayoutEditPolicy(containerPolicy.getEditDomain());
	}

	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new CBannerLayoutPolicyHelper(ep);
	}	

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null;
	}

	public IJavaInstance getLayoutManagerInstance(IJavaObjectInstance container, JavaHelpers javaClass, ResourceSet rset) {
		return null;
	}
	
	public JavaClass getConstraintClass(ResourceSet rSet) {
		return null;
	}

}
