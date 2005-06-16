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
 *  $RCSfile: Company.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 19:27:21 $ 
 */

import java.util.*;
import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
/**
 * A company
 */
public class Company implements Serializable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -5394792597004813925L;
	protected String name;
	protected List departments = new ArrayList(1);
	transient protected PropertyChangeSupport propChange;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		String old = this.name;
		this.name = name;
		firePropertyChangeEvent(COMPANY_NAME, old, name);
	}
	
	/**
	 * This list is not modifiable, use the accessors to do this.
	 */
	public List getDepartments() {
		return Collections.unmodifiableList(departments);
	}
	
	public void addDepartment(Department dept) {
		departments.add(dept);
		dept.company = this;
		firePropertyChangeEvent(DEPARTMENTS, null, dept);
	}
	
	public void addDepartment(Department dept, int pos) {
		departments.add(pos, dept);
		dept.company = this;
		firePropertyChangeEvent(DEPARTMENTS, null, dept);
	}
	
	public void removeDepartment(Department dept) {
		departments.remove(dept);
		dept.company = null;
		firePropertyChangeEvent(DEPARTMENTS, dept, null);
	}
	
	/**
	 * Return iterator over employees. This is not
	 * an efficient example. A true model of this
	 * type would be a database with efficient
	 * query mechanisms.
	 */
	public Iterator getEmployeesIterator() {
		return new Iterator() {
			Iterator deptItr;
			Iterator empItr;
			Employee next;
			{
				deptItr = departments.iterator();
				empItr = null;
				next = null;
				setNext();
			}
			
			protected void setNext() {
				if (empItr == null) {
					if (deptItr.hasNext()) {
						Department dept = (Department) deptItr.next();
						empItr = dept.getEmployees().iterator();
						if (dept.getManager() != null && dept.getManager().getDepartment() == null)
							next = dept.getManager();	// Top-level manager, not in a dept.
						else
							setNext();
					} else
						next = null;	// There are no more
				} else if (empItr.hasNext())
					next = (Employee) empItr.next();
				else {
					empItr = null;
					setNext();
				}
			}
			
			public boolean hasNext() {
				return next != null;
			}
			
			public Object next() {
				Object n = next;
				setNext();
				return n;
			}
			
			public void remove() {
			}
		};
	}
	
	/**
	 * API to get a specific employee. Again, not
	 * an efficient mechanism.
	 */
	public Employee getEmployee(String name) {
		Iterator itr = getEmployeesIterator();
		while (itr.hasNext()) {
			Employee e = (Employee) itr.next();
			if (e.getName().equals(name))
				return e;
		}
		return null;
	}
	
	/**
	 * Property change support routines.
	 */
	public static final String
		COMPANY_NAME = "name",
		DEPARTMENTS = "departments";
				
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