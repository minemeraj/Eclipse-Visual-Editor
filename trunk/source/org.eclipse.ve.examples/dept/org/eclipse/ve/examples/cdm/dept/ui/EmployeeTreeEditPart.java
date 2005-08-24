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
package org.eclipse.ve.examples.cdm.dept.ui;
/*
 *  $RCSfile: EmployeeTreeEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */


import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.ve.examples.cdm.dept.*;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ve.internal.cde.core.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ve.examples.cdm.dept.property.PropertySupport;
/**
 * Edit Part for the Employee
 */
public class EmployeeTreeEditPart extends AbstractTreeEditPart {
	
	protected PropertyChangeListener employeeListener;
	
	public EmployeeTreeEditPart(Employee model) {
		setModel(model);
	}
	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new TreePrimaryDragRoleEditPolicy());
	}
	
	
	public void activate() {
		super.activate();
		Employee employee = (Employee) getModel();
		employeeListener = new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
		    	if (Employee.NAME.equals(evt.getPropertyName()))
		    		refreshEmployee((String) evt.getNewValue());
		    }
		};
		employee.addPropertyChangeListener(Employee.NAME, employeeListener);
	}
	
	public void deactivate() {
		super.deactivate();
		((Employee) getModel()).removePropertyChangeListener(Employee.NAME, employeeListener);
	}
	
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshEmployee(((Employee) getModel()).getName());
	}
	
	private void refreshEmployee(String employeeName) {
		String emp = employeeName != null ? employeeName : "?";
		setWidgetText(emp);
	}
	
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class)
			return PropertySupport.getPropertySource(getModel());
		else
			return super.getAdapter(key);
	}
	
}


