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
 *  $RCSfile: CardLayoutPolicyFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2004-03-04 12:17:19 $ 
 */
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.*;

/**
 * @version 	1.0
 * @author
 */
public class CardLayoutPolicyFactory implements ILayoutPolicyFactory {

	/*
	 * @see ILayoutPolicyFactory#getLayoutSwitcher(ContainerPolicy)
	 */
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep) {
		return new CardLayoutSwitcher(ep);
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new CardLayoutEditPolicy(containerPolicy);
	}

	/*
	 * @see ILayoutPolicyFactory#getLayoutPolicyHelper(ContainerPolicy)
	 */
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new CardLayoutPolicyHelper(ep);
	}

	/*
	 * @see ILayoutPolicyFactory#getConstraintPropertyDescriptor(EStructuralFeature)
	 */
	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return new AbstractConstraintPropertyDescriptor(sfConstraint) {
			{
				setNullInvalid(true);	// nulls are invalid.
			}
			public CellEditor createPropertyEditor(Composite parent) {
				return new StringJavaClassCellEditor(parent);
			}
			public ILabelProvider getLabelProvider() {
				return new StringJavaClassLabelProvider(); // It is a string for display purposes.
			}
		};
	}

	/**
	 * @see ILayoutPolicyFactory#getLayoutManagerInstance(EditDomain)
	 */
	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
	}
}