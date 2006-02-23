/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FormTreeEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2006-02-23 18:05:54 $ 
 */
package org.eclipse.ve.internal.forms;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.swt.ControlTreeEditPart;
 

/**
 * Form Tree editpart.
 * @since 1.2.0
 */
public class FormTreeEditPart extends ControlTreeEditPart {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public FormTreeEditPart(Object model) {
		super(model);
	}
	
	protected List getChildJavaBeans() {
		return Collections.singletonList(((EObject) getModel()).eGet(sf_body));
	}
	
	protected EStructuralFeature sf_body;
	public void setModel(Object model) {
		super.setModel(model);
		IJavaObjectInstance javaModel = (IJavaObjectInstance) model;
		if (javaModel.eResource() != null && javaModel.eResource().getResourceSet() != null) {
			sf_body = javaModel.eClass().getEStructuralFeature("body");
		}
	}

}
