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
 *  $RCSfile: JMenuBarGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.List;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

/**
 * @author pwalker
 *
 * Graphical editpart for JMenuBar
 */
public class JMenuBarGraphicalEditPart extends ContainerGraphicalEditPart {
	protected EStructuralFeature sfMenus;

	/**
	 * Constructor for JMenuBarGraphicalEditPart.
	 * @param model
	 */
	public JMenuBarGraphicalEditPart(Object model) {
		super(model);
	}

	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		((ComponentGraphicalEditPart) ep).setTransparent(true); // So that it doesn't create an image, we subsume it here.
		return ep;
	}

	/**
	 * Use a JMenuBarLayoutPolicy which is a FlowLayout and only allows JMenus to be added
	 */
	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JMenuBarLayoutEditPolicy(this));
	}

	public List getModelChildren() {
		return (List) ((EObject) getModel()).eGet(sfMenus);
	}

public void activate() {
	super.activate();	
	((EObject) getModel()).eAdapters().add(menubarAdapter);
}

public void deactivate() {
	super.deactivate();
	((EObject) getModel()).eAdapters().remove(menubarAdapter);
}

private Adapter menubarAdapter = new Adapter() {
	public void notifyChanged(Notification notification) {
		if (notification.getFeature() == sfMenus)
			refreshChildren();
	}

	public Notifier getTarget() {
		return null;
	}

	public void setTarget(Notifier newTarget) {
	}

	public boolean isAdapterForType(Object type) {
		return false;
	}
};

	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sfMenus = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JMENUBAR_MENUS);
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ContainerGraphicalEditPart#setPropertySource(ComponentGraphicalEditPart)
	 */
	protected void setPropertySource(ComponentGraphicalEditPart childEP, EObject child) {
		childEP.setPropertySource(new NonBoundsBeanPropertySource(child));
	}

}
