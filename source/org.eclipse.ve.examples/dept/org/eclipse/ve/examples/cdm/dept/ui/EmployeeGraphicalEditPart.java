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
 *  $RCSfile: EmployeeGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ve.examples.cdm.dept.*;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ve.internal.cde.core.DefaultComponentEditPolicy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.ve.examples.cdm.dept.property.PropertySupport;
/**
 * Edit Part for the Employee
 */
public class EmployeeGraphicalEditPart extends AbstractGraphicalEditPart {
	
	protected PropertyChangeListener employeeListener;
	
	public EmployeeGraphicalEditPart(Employee model) {
		setModel(model);
	}
	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
	}
	
	protected IFigure createFigure() {
		IFigure f = new Label("?");	
		return f;
	}
	
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshEmployee(((Employee) getModel()).getName());
	}
	
	private void refreshEmployee(String employeeName) {
		String emp = employeeName != null ? employeeName : "?";
		((Label) getFigure()).setText(emp);
	}
	
	public void activate() {
		super.activate();
		Employee employee = (Employee) getModel();
		employeeListener = new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
		    	if (Employee.NAME.equals(evt.getPropertyName()))
		    		refreshEmployee((String) evt.getNewValue());
		    	else if (Employee.MANAGES.equals(evt.getPropertyName())) {
		    		// Need to have the container refresh its source connections because 
		    		// the management has changed of one of its employees.
				refreshParentSourceConnections();
		    	}
		    }
		};
		employee.addPropertyChangeListener(employeeListener);
	}
	
	protected void refreshParentSourceConnections() {
		((DepartmentGraphicalEditPart) getParent()).refreshSourceConnections();
	}
	
	public void deactivate() {
		super.deactivate();
		((Employee) getModel()).removePropertyChangeListener(employeeListener);
	}
	
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class)
			return PropertySupport.getPropertySource(getModel());
		else
			return super.getAdapter(key);
	}
	
		
}
