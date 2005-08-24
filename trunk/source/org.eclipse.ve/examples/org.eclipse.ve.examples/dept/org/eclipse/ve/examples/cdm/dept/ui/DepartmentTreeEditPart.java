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
 *  $RCSfile: DepartmentTreeEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.examples.cdm.dept.Department;
import org.eclipse.ve.examples.cdm.dept.Employee;
import org.eclipse.ve.examples.cdm.dept.property.PropertySupport;
/**
 * Department Edit Part for the Tree Viewer.
 */
public class DepartmentTreeEditPart extends AbstractTreeEditPart {

	protected PropertyChangeListener departmentListener;
			
	public DepartmentTreeEditPart(Department model) {
		setModel(model);
	}
	
	protected List getModelChildren() {
		Department dept = (Department) getModel();
		return dept.getEmployees();
	}
	
	public void activate() {
		super.activate();
		Department dept = (Department) getModel();
		departmentListener = new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
		    	if (Department.EMPLOYEES.equals(evt.getPropertyName()))
		    		refreshChildren();
		    	else if (Department.DEPARTMENT_NAME.equals(evt.getPropertyName()))
		    		refreshName((String) evt.getNewValue());
		    }
		};
		dept.addPropertyChangeListener(departmentListener);
	}
	
	public void deactivate() {
		super.deactivate();
		((Department) getModel()).removePropertyChangeListener(departmentListener);
	}
	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new TreeContainerEditPolicy(new DepartmentContainerPolicy(EditDomain.getEditDomain(this))));
	}
	
	protected void refreshChildren() {
		// This method is here so that it is visible to the departmentListener.
		super.refreshChildren();
	}
	
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class)
			return PropertySupport.getPropertySource(getModel());
		else
			return super.getAdapter(key);
	}
	
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshName(((Department) getModel()).getDepartmentName());
	}
	
	private void refreshName(String deptName) {
		String dname = deptName != null ? deptName : "?";
		setWidgetText(dname);
	}	
					
	/**
	 * Create the child edit part. It will be a Department in our case.
	 */
	protected EditPart createChild(Object child) {
		Employee childModel = (Employee) child;
		return new EmployeeTreeEditPart(childModel);
	}

}
