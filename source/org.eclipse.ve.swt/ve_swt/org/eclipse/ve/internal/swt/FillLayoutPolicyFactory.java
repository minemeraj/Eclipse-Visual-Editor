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
 * $RCSfile: FillLayoutPolicyFactory.java,v $ $Revision: 1.1 $ $Date: 2004-03-05 20:57:04 $
 */
package org.eclipse.ve.internal.swt;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.ve.internal.java.visual.*;
/**
 * 
 * Defines all the policy classes, switcher, etc., for a SWT FillLayout
 */
public class FillLayoutPolicyFactory extends Object implements ILayoutPolicyFactory {
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * Use the RowLayoutEditPolicy since it determines 'type' and provides adequate feedback for
	 * FillLayout
	 */
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new RowLayoutEditPolicy(containerPolicy);
	}
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		// Nothing that special about FillLayout so we can use the default policy helper
		return new UnknownLayoutPolicyHelper(ep);
	}
	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null;
	}
	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		return null;
	}
}
