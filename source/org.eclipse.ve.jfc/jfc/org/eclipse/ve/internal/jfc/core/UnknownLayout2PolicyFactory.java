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
 *  $RCSfile: UnknownLayout2PolicyFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
/**
 * Layout policy factory for an unknown layout manager of type LayoutManager2.
 */
public class UnknownLayout2PolicyFactory extends UnknownLayoutPolicyFactory {
	
	/**
	 * Return a default constraint property descriptor.
	 */
	public IPropertyDescriptor getConstraintPropertyDescriptor(EStructuralFeature sfConstraint) {
		// This one is a read-only constraint descriptor.
		return new AbstractConstraintPropertyDescriptor(sfConstraint) {
			{
				setReadOnly(true);
			}		
		};
	}
}
