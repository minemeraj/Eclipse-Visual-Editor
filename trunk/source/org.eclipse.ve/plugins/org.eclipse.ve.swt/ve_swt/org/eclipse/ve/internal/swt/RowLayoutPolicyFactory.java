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
 *  $RCSfile: RowLayoutPolicyFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2004-03-04 02:13:17 $ 
 */
package org.eclipse.ve.internal.swt;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.ve.internal.java.visual.*;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;
 
/**
 * 
 * @since 1.0.0
 */
public class RowLayoutPolicyFactory implements ILayoutPolicyFactory {

	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep) {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getLayoutInputPolicyClass() {
		return RowLayoutEditPolicy.class;
	}

	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		// Nothing that special about RowLayout so we can use the default policy helper
		return new UnknownLayoutPolicyHelper(ep);
	}	

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null;
	}

	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		return null;
	}

}
