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
 *  $RCSfile: JToolBarTreeEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:18:06 $ 
 */

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaClass;

/**
 * TreeEditPart for javax.swing.JToolBar.
 */
public class JToolBarTreeEditPart extends ComponentTreeEditPart {
	protected EStructuralFeature sfItems;

	/**
	 * Constructor for JPopupMenuTreeEditPart.
	 * @param model
	 */
	public JToolBarTreeEditPart(Object model) {
		super(model);
	}

	protected void createEditPolicies() {
		// The TreeContainerEditPolicy is the CDE one
		super.createEditPolicies();
		installEditPolicy(
			EditPolicy.TREE_CONTAINER_ROLE,
			new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(
				new JToolBarContainerPolicy(EditDomain.getEditDomain(this))));
	}

	private Adapter containerAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfItems)
				refreshChildren();
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(containerAdapter);
	}


	public List getChildJavaBeans() {
		return (List) ((EObject) getModel()).eGet(sfItems);
	}

	protected EditPart createChildEditPart(Object model) {
		EditPart ep = super.createChildEditPart(model);
		if (ep instanceof ComponentTreeEditPart)
			((ComponentTreeEditPart) ep).setPropertySource(new NonBoundsBeanPropertySource((EObject)model));
		return ep;
	}
	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sfItems = modelType.getEStructuralFeature("items"); //$NON-NLS-1$
	}
}