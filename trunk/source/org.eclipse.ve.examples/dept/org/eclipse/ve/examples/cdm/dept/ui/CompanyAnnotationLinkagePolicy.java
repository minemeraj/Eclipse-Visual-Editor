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
 *  $RCSfile: CompanyAnnotationLinkagePolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.ve.internal.cde.core.GenericAnnotationLinkagePolicy;
import org.eclipse.ve.examples.cdm.dept.*;
import org.eclipse.ve.internal.cdm.AnnotationGeneric;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
/**
 * The linkage helper for working with a company model.
 */
public class CompanyAnnotationLinkagePolicy extends GenericAnnotationLinkagePolicy {

	protected Company company;
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	public static final String
		EMPLOYEE_PROTOCOL = "emp:",
		DEPARTMENT_PROTOCOL = "dept:",
		MANAGED_PROTOCOL = "managed:";	// Used to identify the manager->department connection.
		
	/**
	 * The model object for a Manager connection. It is only
	 * used within the viewer.
	 */
	public static class ManagedConnection {
		public Department department;
		
		public ManagedConnection(Department department) {
			this.department = department;
		}
		
		public boolean equals(Object other) {
			if (this == other)
				return true;
			
			if (!(other instanceof ManagedConnection))
				return false;
				
			return (this.department.equals(((ManagedConnection) other).department));
		}
		
		public int hashCode() {
			return department.hashCode();
		}
	}
		
		
	/**
	 * Get the model from the id. The ID has the form:
	 *   emp:employee-name
	 *   dept:department-name
	 *   managed: department-name
	 */
	public Object getModelFromID(String id){
		if (id.startsWith(EMPLOYEE_PROTOCOL)) {
			String employeeid = id.substring(EMPLOYEE_PROTOCOL.length());
			return company.getEmployee(employeeid);
		} else if (id.startsWith(DEPARTMENT_PROTOCOL)) {
			String deptid = id.substring(DEPARTMENT_PROTOCOL.length());
			Iterator depts = company.getDepartments().iterator();
			while (depts.hasNext()) {
				Department dept = (Department) depts.next();
				if (deptid.equals(dept.getDepartmentName()))
					return dept;
			}
		} else if (id.startsWith(MANAGED_PROTOCOL)) {
			String deptid = id.substring(MANAGED_PROTOCOL.length());
			Iterator depts = company.getDepartments().iterator();
			while (depts.hasNext()) {
				Department dept = (Department) depts.next();
				if (deptid.equals(dept.getDepartmentName()))
					return new ManagedConnection(dept);
			}
		}

		return null;
	}
	
	// Check if this is a valid annotation. It is valid if the ID exists in the
	// model.
	protected boolean isAnnotationValidGeneric(AnnotationGeneric annotation) {
		if (!annotation.isSetAnnotatesID())
			return false;
		String id = annotation.getAnnotatesID();
		Object model = getModelFromID(id);
		if (model != null) 
			if (model instanceof ManagedConnection) {
				// Need a further test, is there a manager.
				Department d = ((ManagedConnection) model).department;
				return (d.getManager() != null);
			} else
				return true;
		else
			return false;
	}
	
	/**
	 * Get the id from the model object.
	 */
	public String getIDFromModel(Object model) {
		if (model instanceof Employee)
			return EMPLOYEE_PROTOCOL+((Employee) model).getName();
		else if (model instanceof Department)
			return DEPARTMENT_PROTOCOL+((Department) model).getDepartmentName();
		else if (model instanceof ManagedConnection)
			return MANAGED_PROTOCOL+((ManagedConnection) model).department.getDepartmentName();
		else
			return null;
	}
	
	public List getContainedChildren(Object model) {
		if (model instanceof Department)
			return ((Department) model).getEmployees();
		else if (model instanceof Company)
			return ((Company) model).getDepartments();
		else
			return	Collections.EMPTY_LIST;
	}
	
	
	protected PropertyChangeListener departmentListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (Department.DEPARTMENT_NAME.equals(evt.getPropertyName())) {
				// The id has changed.
				changeID(DEPARTMENT_PROTOCOL+((String) evt.getOldValue()), DEPARTMENT_PROTOCOL+((String) evt.getNewValue()));
				// Also change the managed connection id, if there is anyone listening.
				changeID(MANAGED_PROTOCOL+((String) evt.getOldValue()), MANAGED_PROTOCOL+((String) evt.getNewValue()));
			}
		}
	};

	protected PropertyChangeListener employeeListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (Employee.NAME.equals(evt.getPropertyName())) {
				// The id has changed.
				changeID(EMPLOYEE_PROTOCOL+((String) evt.getOldValue()), EMPLOYEE_PROTOCOL+((String) evt.getNewValue()));
			}
		}
	};
	protected Object linkingToID(String id) {
		// Someone is interested in this ID. Get the Model object for that ID and
		// listen on it for any changes that signal that the ID has changed.
		Object model = getModelFromID(id);
		if (id != null) {
			if (model instanceof Employee)
				((Employee) model).addPropertyChangeListener(Employee.NAME, employeeListener);
			else if (model instanceof Department) 
				((Department) model).addPropertyChangeListener(Department.DEPARTMENT_NAME, departmentListener);
			else
				return null;	// Managed connection has no aux data to add to the linkage.
			return model;	// This will then be sent back on unlinkingFromID to allow unlinking.
		} else
			return null;
	}
	
	protected void unlinkingFromID(String id, Object model) {
		// No one is interested in this ID anymore. Remove the listener from the mode.
		if (model != null) {
			if (model instanceof Employee)
				((Employee) model).removePropertyChangeListener(Employee.NAME, employeeListener);
			else if (model instanceof Department) 
				((Department) model).removePropertyChangeListener(Department.DEPARTMENT_NAME, departmentListener);				
		}

	}

}
