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
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/*
 *  $RCSfile: CompositePropertySourceAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-04-14 13:59:32 $ 
 */

public class CompositePropertySourceAdapter extends ControlPropertySourceAdapter {
	
	protected boolean includeFeature(EStructuralFeature sfeature) {
		// Do not include the "layout" feature if we are implicit
		// This is to allow for the Composite argument in something like the method
		// createPartControl(Composite aComposite)
		if(sfeature.getName().equals("layout")){
			if (getBean().getAllocation() instanceof ImplicitAllocation) {
				return false;
			}
		} 
		return super.includeFeature(sfeature);
	}
}
