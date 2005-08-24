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
package org.eclipse.ve.examples.cdm.dept.property;
/*
 *  $RCSfile: EmployeePropertySource.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ve.examples.cdm.dept.Employee;
import org.eclipse.ve.internal.propertysheet.StringPropertyDescriptor;
/**
 * This is the property source for an Employee.
 */
public class EmployeePropertySource implements IPropertySource {
	protected Employee employee;
	protected IPropertyDescriptor[] descriptors;
	
	public EmployeePropertySource(Employee employee) {
		this.employee = employee;
	}

	public Object getEditableValue() {
		return employee;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = new IPropertyDescriptor[] {
				new StringPropertyDescriptor(Employee.NAME, "name"),
				new StringPropertyDescriptor(Employee.PHONE, "phone number")				
			};
			StringPropertyDescriptor pd = (StringPropertyDescriptor) descriptors[0];
			pd.setValidator(new NameValidator());
			pd.setAlwaysIncompatible(true);	// This is so that we can't select more than one and change the name. If we did that, then automatically the names would not be unique.
		};
				
		return descriptors;
	}
	
	public Object getPropertyValue(Object propertyKey) {
		if (Employee.NAME.equals(propertyKey))
			return employee.getName();
		else if (Employee.PHONE.equals(propertyKey))
			return employee.getPhone();
		else
			return null;
	}
	
	public boolean isPropertySet(Object propertyKey) {
		if (Employee.NAME.equals(propertyKey))
			return employee.getName() != null;
		else if (Employee.PHONE.equals(propertyKey))
			return employee.getPhone() != null;
		else
			return false;
	}
	
	public void resetPropertyValue(Object propertyKey) {
		if (Employee.NAME.equals(propertyKey)) {
			// There is no default name really so it will always appear to be set. We will choose a unique name
			employee.setName(PropertySupport.getUniqueEmployeeName(employee.getCompany(), null));
		} else if (Employee.PHONE.equals(propertyKey))
			employee.setPhone(null);
	}
	
	public void setPropertyValue(Object propertyKey, Object value) {
		if (Employee.NAME.equals(propertyKey))
			employee.setName((String) value);
		else if (Employee.PHONE.equals(propertyKey))
			employee.setPhone((String) value);
	}
	
	private class NameValidator implements ICellEditorValidator {
		protected Object[] sources;
		
		// Is the name valid. It is valid if the name is unique in the company.
		// Null is considered valid here because the non-nulls validator takes care of nulls.
		public String isValid(Object value) {
			if (value == null)
				return null;
				
			String name = (String) value;
			if (name.length() == 0)
				return "There must be a name.";

			if (name.equals(employee.getName()))
				return null;	// The current name is considered valid.
				
			// There should be at least one source, and it should be a department.
			String newName = PropertySupport.getUniqueEmployeeName(employee.getCompany(), name);
			if (newName.equals(name))
				return null;	// The name didn't change, so it is valid.
				
			return "The name is not unique.";
		}
		
		public void setSources(Object[] sources) {
			this.sources = sources;
		}
	}
	
}


