/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: ToolBarGraphicalEditPart.java,v $ $Revision: 1.7 $ $Date: 2005-05-18 16:48:00 $
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

public class ToolBarGraphicalEditPart extends CompositeGraphicalEditPart {

	private EReference sf_items;

	public ToolBarGraphicalEditPart(Object model) {
		super(model);
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sf_items = JavaInstantiation.getReference(rset, SWTConstants.SF_TOOLBAR_ITEMS);
	}

	/*
	 * Model children is the items feature. However, this returns the CoolItems, but we want to return instead the controls themselves. They are the
	 * "model" that gets sent to the createChild and control edit part.
	 */
	protected List getModelChildren() {
		return (List) ((EObject) getModel()).eGet(sf_items);
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		super.deactivate();
	}

	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ToolBarLayoutEditPolicy(this));
	}

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
	}

	private Adapter containerAdapter = new EditPartAdapterRunnable() {

		public void run() {
			if (isActive()) {
				refreshChildren();
			}
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sf_items)
				queueExec(ToolBarGraphicalEditPart.this, "ITEMS"); //$NON-NLS-1$
		}
	};

	/**
	 * ToolItemGraphicalEditPart is not allowed on the free form as it is specially designed for a ToolBar and is subclassed from Item. Therefore
	 * instead of it being defined in the .override for the ToolItem it is instantiated directly by the ToolBarGraphicalEditPart
	 */
	protected EditPart createChild(Object child) {
		ToolItemGraphicalEditPart result = new ToolItemGraphicalEditPart();
		result.setModel(child);
		//result.setBounds(getColumnBounds(getChildren().size()));
		return result;
	}
	
	protected void refreshItems() {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			((ToolItemGraphicalEditPart) children.get(i)).refreshVisuals();
		}
	}
}