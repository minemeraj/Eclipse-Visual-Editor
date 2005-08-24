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
 *  $RCSfile: CardLayoutPolicyFactory.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:38:09 $ 
 */
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.internal.instantiation.base.*;
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
			private ILabelProvider labelProvider;  // Performance cache because property sheets asks for this twice always			
			{
				setNullInvalid(true);	// nulls are invalid.
			}
			public CellEditor createPropertyEditor(Composite parent) {
				return new StringJavaClassCellEditor(parent);
			}
			public ILabelProvider getLabelProvider() {
				if(labelProvider == null){
					labelProvider = new StringJavaClassLabelProvider(); // It is a string for display purposes.
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

	public JavaClass getConstraintClass(ResourceSet rSet) {
		return null;
	}
}
