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
 *  $RCSfile: GridBagLayoutPolicyFactory.java,v $
 *  $Revision: 1.8 $  $Date: 2004-04-20 09:04:47 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.*;

public class GridBagLayoutPolicyFactory implements ILayoutPolicyFactory {
	public GridBagLayoutPolicyFactory() {
		super();
	}

	public EditPolicy getLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		return new GridBagLayoutEditPolicy(containerPolicy);
	}
	
	
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new GridBagLayoutPolicyHelper(ep);
	}
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy ep) {
		return new GridBagLayoutSwitcher(ep);
	}

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return new AbstractConstraintPropertyDescriptor(sfConstraint) {
			private ILabelProvider labelProvider;  // Performance cache because property sheets asks for this twice always			
			public ILabelProvider getLabelProvider() {
				if(labelProvider == null){
					labelProvider = new GridBagConstraintsJavaClassLabelProvider();
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