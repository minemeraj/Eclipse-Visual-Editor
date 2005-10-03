/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BaseJavaContainerPolicy;

 

/**
 * This is a standard SWT Widget container policy that is for 
 * a SWT Widget that takes children, but IS not for standard
 * Composites (i.e. not using the "controls" feature and not
 * having layout data). Such as for Table. 
 * <p>
 * This is used where the children have a constructor of the 
 * form <code>childWidget(Widget parentWidget, ...)</code>. It makes
 * sure that the parent is in the child's allocation.
 * @since 1.1.0.1
 */
public class SWTWidgetContainerPolicy extends BaseJavaContainerPolicy {

	public SWTWidgetContainerPolicy(EStructuralFeature feature, EditDomain domain) {
		super(feature, domain);
	}

	public SWTWidgetContainerPolicy(EditDomain domain) {
		super(domain);
	}
	
	protected boolean isValidBeanLocation(Object child) {
		return BeanSWTUtilities.isValidBeanLocation(getEditDomain(), (IJavaObjectInstance)child, (EObject) getContainer());
	}
}
