/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: FillLayoutPolicyFactory.java,v $ $Revision: 1.7 $ $Date: 2005-08-24 23:52:55 $
 */
package org.eclipse.ve.internal.swt;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.ve.internal.java.visual.*;
/**
 * 
 * Defines all the policy classes, switcher, etc., for a SWT FillLayout
 */
public class FillLayoutPolicyFactory extends Object implements ILayoutPolicyFactory {
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep) {
		return new UnknownLayoutSwitcher(ep);
	}
	
	/*
	 * Use the RowLayoutEditPolicy since it determines 'type' and provides adequate feedback for
	 * FillLayout
	 */
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new FillLayoutEditPolicy(containerPolicy);
	}
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		// Nothing that special about FillLayout so we can use the default policy helper
		return new UnknownLayoutPolicyHelper(ep);
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
