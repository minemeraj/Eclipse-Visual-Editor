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
 *  Created Aug 15, 2005 by Gili Mendel
 * 
 *  $RCSfile: DecorationsTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-17 12:30:36 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;


public class DecorationsTreeEditPart extends CompositeTreeEditPart {

	protected EReference sf_menuBar;
	
	public DecorationsTreeEditPart(Object model) {
		super(model);
	}
	
	public void setModel(Object model) {
		super.setModel(model);

		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_menuBar = JavaInstantiation.getReference(rset, SWTConstants.SF_MENU_BAR);
	}	
	
	protected List getChildJavaBeans() {
		List controls = super.getChildJavaBeans();
		if (((EObject) getModel()).eIsSet(sf_menuBar)) {
			if (controls.isEmpty())
				return Collections.singletonList(((EObject) getModel()).eGet(sf_menuBar));
			else {
				List children = new ArrayList(controls.size()+1);
				children.add(((EObject) getModel()).eGet(sf_menuBar));
				children.addAll(controls);
				return children;
			}
		}
		
		return controls;
	}

}
