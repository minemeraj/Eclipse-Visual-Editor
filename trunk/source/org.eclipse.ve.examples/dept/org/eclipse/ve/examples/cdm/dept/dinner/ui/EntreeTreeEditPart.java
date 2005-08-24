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
 *  $RCSfile: EntreeTreeEditPart.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.*;

import org.eclipse.ve.internal.cdm.DiagramFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
/**
 * Edit Part for the Entree
 */
public class EntreeTreeEditPart extends DiagramFigureTreeEditPart {

	public EntreeTreeEditPart(DiagramFigure model) {
		setModel(model);
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		installEditPolicy(
			EditPolicy.TREE_CONTAINER_ROLE,
			new TreeContainerEditPolicy(new EntreeContainerPolicy(EditDomain.getEditDomain(this))));
	}

	/**
	 * Create the child.
	 */
	protected EditPart createChild(Object child) {
		DiagramFigure childModel = (DiagramFigure) child;
		return new EmployeeTreeEditPart(childModel);
	}

	protected void refreshVisuals() {
		Object food = ((DiagramFigure) getModel()).getKeyedValues().get(DinnerConstants.FOOD);
		if (food == null || food instanceof String)
			refreshFood((String) food);
	}

	private void refreshFood(String food) {
		String foodString = food != null ? food : "?";
		setWidgetText(foodString);
	}

	public Object getAdapter(Class key) {
		if (key.equals(IPropertySource.class)) {
			// Return the property source adapter for this entree.
			// We're always newing one up here, but a better way is to
			// have them cached somewhere. These are light-weight here
			// so it's not so bad.
			return new EntreePropertySource((DiagramFigure) getModel());
		}
		return super.getAdapter(key);
	}

	/**
	 * @see org.eclipse.ve.internal.cde.emf.DiagramFigureTreeEditPart#createModelAdapter()
	 */
	protected DiagramFigureAdapter createModelAdapter() {
		return new DiagramFigureAdapter() {
			public void notifyChanged(Notification msg) {
				super.notifyChanged(msg);
				Notification kmsg = KeyedValueNotificationHelper.notifyChanged(msg, DinnerConstants.FOOD);
				if (kmsg != null) {
					switch (kmsg.getEventType()) {
						case Notification.SET :
							refreshFood((String) ((BasicEMap.Entry) kmsg.getNewValue()).getValue());
							break;
						case Notification.UNSET :
							refreshFood(null);
					}
				}
			}

		};
	}

}
