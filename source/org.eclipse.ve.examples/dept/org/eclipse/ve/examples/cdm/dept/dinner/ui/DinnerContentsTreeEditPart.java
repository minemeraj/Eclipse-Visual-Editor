/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.cdm.dept.dinner.ui;
/*
 *  $RCSfile: DinnerContentsTreeEditPart.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramFigure;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.DiagramContentsTreeEditPart;
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
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.emf.DiagramContentsTreeEditPart#createModelAdapter()
	 */
	protected DiagramAdapter createModelAdapter() {
		return new DiagramAdapter() {
			public void notifyChanged(Notification notification) {
				Notification kvMsg = KeyedValueNotificationHelper.notifyChanged(notification, DinnerConstants.COMPANY_URL);
				if (kvMsg != null)
					queueExec(DinnerContentsTreeEditPart.this, "COMPANYS"); //$NON-NLS-1$
				else
					super.notifyChanged(notification);
			}
		};
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
