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
 *  $RCSfile: BorderLayoutPolicyFactory.java,v $
 *  $Revision: 1.8 $  $Date: 2004-03-07 16:45:54 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.*;
/**
 * Layout Policy Factory for java.awt.BorderLayout
 * Creation date: (10/24/00 11:50:13 AM)
 * @author: Peter Walker
 */
public class BorderLayoutPolicyFactory implements ILayoutPolicyFactory {
	/**
	 * BorderLayoutPolicyFactory constructor comment.
	 */
	public BorderLayoutPolicyFactory() {
		super();
	}
	/**
	 * getConstraintConverter method comment.
	 */
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy policy) {
		return new BorderLayoutSwitcher(policy);
	}
	
	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new BorderLayoutEditPolicy(containerPolicy);
	}	
	/**
	 * getLayoutPolicyHelperClass method comment.
	 */
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy aContainerPolicy) {
		return new BorderLayoutPolicyHelper(aContainerPolicy);
	}

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		
		return new AbstractConstraintPropertyDescriptor(sfConstraint) {
			private ILabelProvider labelProvider;  // Performance cache because property sheets asks for this twice always
			public CellEditor createPropertyEditor(Composite parent) {
				return new BorderLayoutConstraintsPropertyEditor(parent);
			}

			public ILabelProvider getLabelProvider() {
				if(labelProvider == null){
					labelProvider = new BorderLayoutConstraintsPropertyEditor.BorderLayoutConstraintsLabelProvider();
				}
				return labelProvider;
			}
		};
	}
	/**
	 * @see ILayoutPolicyFactory#getLayoutManagerInstance(JavaHelpers, ResourceSet)
	 */
	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		return BeanUtilities.createJavaObject(javaClass, rset, (String)null);
	}


}