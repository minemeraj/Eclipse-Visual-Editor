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
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.internal.java.JavaHelpers;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.*;
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
	public ILayoutSwitcher getLayoutSwitcher(ContainerPolicy cp) {
		return new UnknownLayoutSwitcher(cp);
	}
	/**
	 * getLayoutInputPolicyClass method comment.
	 */
	public Class getLayoutInputPolicyClass() {
		return UnknownLayoutInputPolicy.class;
	}

	public ILayoutPolicyHelper getLayoutPolicyHelper(ContainerPolicy cp) {
		return new UnknownLayoutPolicyHelper(cp);
	}

	/**
	 * Return a default constraint property descriptor.
	 */
	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		// Return one where the constraint is a string.
		return new AbstractConstraintPropertyDescriptor(sfConstraint) {

			public CellEditor createPropertyEditor(Composite parent) {
				return new StringJavaClassCellEditor(parent);
			}

			public ILabelProvider getLabelProvider() {
				return new StringJavaClassLabelProvider();
			}
		};
	}

	/**
	 * @see ILayoutPolicyFactory#getLayoutManagerInstance(EditDomain)
	 */
	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, null);
	}
}