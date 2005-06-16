package org.eclipse.ve.examples.cdm.dept;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: Department.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 19:27:21 $ 
 */

import java.util.*;
import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
/**
 * A Department,
 */
public class Department implements Serializable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 4935480078853335548L;
	protected Company company;	// This is only set by company.
	protected String deptName;
	protected Employee manager;
	protected List employees = new ArrayList(1);
	transient protected PropertyChangeSupport propChange;
	
	public String getDepartmentName() {
		return deptName;
	}
	
	public void setDepartmentName(String name) {
		String old = deptName;
		deptName = name;
		firePropertyChangeEvent(DEPARTMENT_NAME, old, deptName);		
	}
	
	public Employee getManager() {
		return manager;
	}
	
	public void setManager(Employee manager) {
		Employee old = this.manager;
		this.manager = manager;
		if (manager != null)
			manager.setManages(this);
		if (old != null)
			old.setManages(null);
		firePropertyChangeEvent(MANAGER, old, manager);		
	}
	
	/**
	 * This list is not modifiable, use the accessors to do this.
	 */
	public List getEmployees() {
		return Collections.unmodifiableList(employees);
	}
	
	public void addEmployee(Employee employee) {
		Department oldDept = employee.getDepartment();
		if (oldDept != null) 
			oldDept.removeEmployee(employee);
		employees.add(employee);
		employee.dept = this;
		firePropertyChangeEvent(EMPLOYEES, null, employee);
	}
	
	public void addEmployee(Employee employee, int pos) {
		Department oldDept = employee.getDepartment();
		if (oldDept != null) 
			oldDept.removeEmployee(employee);
		employees.add(pos, employee);
		employee.dept = this;
		firePropertyChangeEvent(EMPLOYEES, null, employee);
	}
	
	public void removeEmployee(Employee employee) {
		if (employees.remove(employee)) {
			employee.dept = null;
			firePropertyChangeEvent(EMPLOYEES, employee, null);
		}
	}
	
	public Company getCompany() {
		return company;
	}
	
	/**
	 * Property change support routines.
	 */
	public static final String
		DEPARTMENT_NAME = "deptname",
		MANAGER = "manager",
		EMPLOYEES = "employees";
				
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (propChange == null)
			propChange = new PropertyChangeSupport(this);
		propChange.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (propChange == null)
			propChange = new PropertyChangeSupport(this);
		propChange.removePropertyChangeListener(listener);
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (propChange == null)
			propChange = new PropertyChangeSupport(this);
		propChange.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (propChange == null)
			propChange = new PropertyChangeSupport(this);
		propChange.removePropertyChangeListener(propertyName, listener);
	}
	
	protected void firePropertyChangeEvent(String propertyName, Object oldValue, Object newValue) {
		if (propChange != null)
			propChange.firePropertyChange(propertyName, oldValue, newValue);
	}
	
}