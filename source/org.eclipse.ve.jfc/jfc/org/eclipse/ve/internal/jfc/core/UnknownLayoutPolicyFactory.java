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
 *  $RCSfile: UnknownLayoutPolicyFactory.java,v $
 *  $Revision: 1.7 $  $Date: 2004-04-20 09:04:47 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.*;
/**
 * Unknown Layout Policy Factory for LayoutManager (not LayoutManager2
 * types).
 * Creation date: (10/24/00 2:51:35 PM)
 * @author: Peter Walker
 */
public class UnknownLayoutPolicyFactory implements ILayoutPolicyFactory {
	/**
	 * UnknownayoutPolicyFactory constructor comment.
	 */
	public UnknownLayoutPolicyFactory() {
		super();
	}
	/**
	 * getConstraintConverter method comment.
	 */
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy cp) {
		return new UnknownLayoutSwitcher(cp);
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new UnknownLayoutInputPolicy(containerPolicy);
	}

	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy cp) {
		return new UnknownLayoutPolicyHelper(cp);
	}

	/**
	 * Return a default constraint property descriptor.
	 */
	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		// Return one where the constraint is a string.
		return new AbstractConstraintPropertyDescriptor(sfConstraint) {
			private ILabelProvider labelProvider;  // Performance cache because property sheets asks for this twice always
			public CellEditor createPropertyEditor(Composite parent) {
				return new StringJavaClassCellEditor(parent);
			}

			public ILabelProvider getLabelProvider() {
				if(labelProvider == null){
					return new StringJavaClassLabelProvider();
				} 
				return labelProvider;
			}
		};
	}

	/**
	 * @see ILayoutPolicyFactory#getLayoutManagerInstance(EditDomain)
	 */
	public IJavaInstance getLayoutManagerInstance(IJavaObjectInstance container, JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
	}
}