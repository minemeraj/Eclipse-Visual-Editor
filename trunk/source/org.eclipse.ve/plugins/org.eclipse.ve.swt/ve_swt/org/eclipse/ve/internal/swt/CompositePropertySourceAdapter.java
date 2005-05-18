/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;

/*
 *  $RCSfile: CompositePropertySourceAdapter.java,v $
 *  $Revision: 1.5 $  $Date: 2005-05-18 16:48:00 $ 
 */

public class CompositePropertySourceAdapter extends ControlPropertySourceAdapter {
	
	protected boolean includeFeature(EStructuralFeature sfeature) {
		// Do not include the "layout" feature if we are implicit
		// This is to allow for the Composite argument in something like the method
		// createPartControl(Composite aComposite)
		if(sfeature.getName().equals("layout")){ //$NON-NLS-1$
			if (getBean().getAllocation() instanceof ImplicitAllocation) {
				return false;
			}
		} 
		return super.includeFeature(sfeature);
	}
}
