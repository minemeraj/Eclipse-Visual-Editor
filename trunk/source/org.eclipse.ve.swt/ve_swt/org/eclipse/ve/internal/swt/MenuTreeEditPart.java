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
 *  $RCSfile: MenuTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-17 12:30:36 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.JavaBeanTreeEditPart;


public class MenuTreeEditPart extends JavaBeanTreeEditPart {

	protected EReference sf_items;
	
	public MenuTreeEditPart(Object model) {
		super(model);
	}
	
	public void setModel(Object model) {
		super.setModel(model);

		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_MENU_ITEMS);
	}	
	
	protected List getChildJavaBeans() {
		if (((EObject) getModel()).eIsSet(sf_items)) {
			return (List)((EObject) getModel()).eGet(sf_items);
		} else 	
			return Collections.EMPTY_LIST;
	}

}
