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
 *  $RCSfile: EmployeeTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:42:30 $ 
 */

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cdm.DiagramFigure;
import org.eclipse.ve.examples.cdm.dept.Company;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
/**
 * Edit Part for the Entree
 */
public class EmployeeTreeEditPart extends AbstractTreeEditPart {

	public EmployeeTreeEditPart(DiagramFigure model) {
		setModel(model);
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new TreePrimaryDragRoleEditPolicy());
	}

	public void activate() {
		super.activate();
		((DiagramFigure) getModel()).eAdapters().add(getModelAdapter());
	}

	public void deactivate() {
		super.deactivate();
		((DiagramFigure) getModel()).eAdapters().remove(getModelAdapter());
	}

	private Adapter diagramAdapter;

	protected Adapter getModelAdapter() {
		if (diagramAdapter == null) {
			diagramAdapter = new AdapterImpl() {
				public void notifyChanged(Notification msg) {
					Notification kmsg = KeyedValueNotificationHelper.notifyChanged(msg, DinnerConstants.EMPLOYEE);
					if (kmsg != null) {
						switch (kmsg.getEventType()) {
							case Notification.SET :
								refreshEmployee((String) ((BasicEMap.Entry) kmsg.getNewValue()).getValue());
								break;
							case Notification.UNSET :
								refreshEmployee(null);
						}
					}
				}
			};
		}
		return diagramAdapter;
	}

	protected void refreshVisuals() {
		Object emp = ((DiagramFigure) getModel()).getKeyedValues().get(DinnerConstants.EMPLOYEE);
		if (emp == null || emp instanceof String)
			refreshEmployee((String) emp);
	}

	private void refreshEmployee(String employee) {
		// Let's verify if valid employee (if not, make it red).
		// Get Company from the views data.
		EditPartViewer v = getRoot().getViewer();
		EditDomain dom = (EditDomain) v.getEditDomain();
		Company cmp = (Company) dom.getData(getRoot().getContents().getModel());
		boolean good = employee != null && cmp != null;
		if (good)
			good = (cmp.getEmployee(employee) != null);
		String emp = employee != null ? employee : "?";
		if (!good)
			emp = "* " + emp;
		setWidgetText(emp);
	}
	
	public Object getAdapter(Class key) {
		if (key.equals(IPropertySource.class)) {
			// Return the property source adapter for this employee
			// We're always newing one up here, but a better way is to
			// have them cached somewhere. These are light-weight here
			// so it's not so bad.
			return new EmployeePropertySource((DiagramFigure) getModel());
		}
		return super.getAdapter(key);
	}	

}
