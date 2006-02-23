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
 *  $RCSfile: FormGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2006-02-23 18:05:54 $ 
 */
package org.eclipse.ve.internal.forms;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.ContentPaneFigure;
import org.eclipse.ve.internal.cde.core.VisualComponentsLayoutPolicy;

import org.eclipse.ve.internal.swt.ControlGraphicalEditPart;
 

/**
 * Form graphical edit part.
 * @since 1.2.0
 */
public class FormGraphicalEditPart extends ControlGraphicalEditPart {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public FormGraphicalEditPart(Object model) {
		super(model);
	}
	
	protected IFigure createFigure() {
		ContentPaneFigure cf = (ContentPaneFigure) super.createFigure();
		cf.getContentPane().setLayoutManager(new XYLayout());
		return cf;
	}
	
	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		try {
			ControlGraphicalEditPart controlep = (ControlGraphicalEditPart) ep;
			controlep.setTransparent(true); // So that it doesn't create an image, we subsume it here.
		} catch (ClassCastException e) {
			// For the rare times that it is not a ControlGraphicalEditPart (e.g. undefined).
		}
		return ep;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.ControlGraphicalEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy(false));
//		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new UnknownLayoutInputPolicy(getContainerPolicy());		
	}

	protected List getModelChildren() {
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
