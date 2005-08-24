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
 *  $RCSfile: UnknownLayoutPolicyFactory.java,v $
 *  $Revision: 1.13 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;

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
	
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy cp) {
		return new UnknownLayoutSwitcher(cp);
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new UnknownLayoutInputPolicy(containerPolicy);
	}

	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy cp) {
		return new UnknownLayoutPolicyHelper(cp);
	}

	
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

	
	public IJavaInstance getLayoutManagerInstance(IJavaObjectInstance container, JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
	}
	
	public JavaClass getConstraintClass(ResourceSet rSet) {
		return null;
	}	
}
