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
 *  $RCSfile: BoxLayoutPolicyFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2004-01-13 21:12:18 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.*;

public class BoxLayoutPolicyFactory implements ILayoutPolicyFactory {

	public BoxLayoutPolicyFactory() {
		super();
	}
	public Class getLayoutInputPolicyClass() {
		return BoxLayoutEditPolicy.class;
	}
	public ILayoutPolicyHelper getLayoutPolicyHelper(VisualContainerPolicy ep) {
		return new FlowLayoutPolicyHelper(ep);
	}
	public ILayoutSwitcher getLayoutSwitcher(VisualContainerPolicy cp) {
		return new FlowLayoutSwitcher(cp);
	}

	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		return null; // No constraint, so no property descriptor.
	}

	/**
	 * Hack for now since we can't set a property for Axis on a boxlayout. 
	 * There are two possible BoxLayout's in the layout dropdown list. One for
	 * each possible axis orientation. A special object is used for each. 
	 * Use the type of object to determine the initialization string for the
	 * ctor of the BoxLayout.
	 */
	public IJavaInstance getLayoutManagerInstance(JavaHelpers javaClass, ResourceSet rset) {
		JavaHelpers boxLayoutJavaClass = JavaRefFactory.eINSTANCE.reflectType("javax.swing.BoxLayout", rset); //$NON-NLS-1$
		String initString = "new javax.swing.BoxLayout(,javax.swing.BoxLayout."; //$NON-NLS-1$
		String javaClassName = ((JavaClass) javaClass).getName();
		if (javaClassName.equals("BoxLayoutX_Axis")) { //$NON-NLS-1$
			initString += "X_AXIS)"; //$NON-NLS-1$
		} else if (javaClassName.equals("BoxLayoutY_Axis")) { //$NON-NLS-1$
			initString += "Y_AXIS)"; //$NON-NLS-1$
		} else {
			initString += "X_AXIS)";	// default to X_AXIS //$NON-NLS-1$
		}
		return BeanUtilities.createJavaObject(boxLayoutJavaClass, rset, initString);
	}
}