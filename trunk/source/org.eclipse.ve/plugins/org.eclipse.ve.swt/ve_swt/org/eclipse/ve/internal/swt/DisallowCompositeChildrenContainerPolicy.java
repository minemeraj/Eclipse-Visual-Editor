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
/*
 * $RCSfile: DisallowCompositeChildrenContainerPolicy.java,v $ $Revision: 1.1 $ $Date: 2005-06-07 13:52:41 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;

public class DisallowCompositeChildrenContainerPolicy extends CompositeContainerPolicy {
	
	private JavaClass COMPOSITE_CLASS;
	public DisallowCompositeChildrenContainerPolicy(EditDomain anEditDomain){
		super(anEditDomain);
	}
	protected boolean isValidChild(Object child,EStructuralFeature containmentSF) {
		// Do not allow composites to be dropped onto us 
		try {
			IJavaInstance javaChild = (IJavaInstance)child;
			if(COMPOSITE_CLASS == null){
				COMPOSITE_CLASS = Utilities.getJavaClass("org.eclipse.swt.widgets.Composite",getEditDomain().getDiagramData().eResource().getResourceSet());
			}
			// If we are a subclass of composite return false
			return !COMPOSITE_CLASS.isAssignableFrom(javaChild.getJavaType());
		} catch (ClassCastException e){
			return false;
		}
	}
}	