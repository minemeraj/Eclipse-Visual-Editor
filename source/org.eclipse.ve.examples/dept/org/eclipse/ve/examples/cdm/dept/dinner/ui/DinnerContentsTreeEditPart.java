package org.eclipse.ve.examples.cdm.dept.dinner.ui;
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
 *  $RCSfile: DinnerContentsTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:42:30 $ 
 */

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramFigure;
/**
 * Christmas Dinner Edit Part for the Tree Viewer.
 */
public class DinnerContentsTreeEditPart extends DiagramContentsTreeEditPart {

	public DinnerContentsTreeEditPart(Diagram model) {
		setModel(model);
	}

	protected void createEditPolicies() {
		installEditPolicy(
			EditPolicy.TREE_CONTAINER_ROLE,
			new TreeContainerEditPolicy(new DinnerContentsContainerPolicy(EditDomain.getEditDomain(this))));
	}

	/**
	 * Create the child edit part. It will be a DiagramFigure in our case. Also in
	 * our specific case the children will represent entree's (Chicken, etc.).
	 */
	protected EditPart createChild(Object child) {
		DiagramFigure childModel = (DiagramFigure) child;
		return new EntreeTreeEditPart(childModel);
	}
	
	protected Adapter modelAdapter = new AdapterImpl() {
		/**
		 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(Notification)
		 */
		public void notifyChanged(Notification notification) {
			Notification kvMsg = KeyedValueNotificationHelper.notifyChanged(notification, DinnerConstants.COMPANY_URL);
			if (kvMsg != null) {
				refreshChildren();
			}
		}
	};
	
	
	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();
		((Diagram) getModel()).eAdapters().add(modelAdapter);
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		((Diagram) getModel()).eAdapters().remove(modelAdapter);		
		super.deactivate();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class)
			return new MealPropertySource((Diagram) getModel());
		else
			return super.getAdapter(adapter);
	}

}