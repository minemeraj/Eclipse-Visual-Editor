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
 *  $RCSfile: Employee.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 19:27:21 $ 
 */

import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
/**
 * An employee
 */
public class Employee implements Serializable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -5233053134098189144L;
	protected String name;
	protected String phone;
	protected Department dept;	// Dept is only settable by Department, so it is protected.
	protected Department managesDept; // ditto
	transient protected PropertyChangeSupport propChange;	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		String old = this.name;
		this.name = name;
		firePropertyChangeEvent(NAME, old, name);	
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		String old = this.phone;
		this.phone = phone;
		firePropertyChangeEvent(PHONE, old, phone);
	}

	public Department getDepartment() {
		return dept;
	}
	
	public Department getManages() {
		return managesDept;
	}
	
	// This is called by Department to maintain the linkage.
	protected void setManages(Department department) {
		Department oldDept = managesDept;
		managesDept = department;
		firePropertyChangeEvent(MANAGES, oldDept, managesDept);
	}
	
	// So that something shows up in the property sheet.
	public String toString() {
		return name != null ? name : "Employee: No name";
	}
	
	public Company getCompany() {
		return (dept != null) ? dept.getCompany() : null;
	}
	
	/**
	 * Property change support routines.
	 */
	public static final String
		NAME = "name",
		PHONE = "phone",
		MANAGES = "manages";
				
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