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
 *  $RCSfile: DepartmentPropertySource.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ve.examples.cdm.dept.Department;
import org.eclipse.ve.examples.cdm.dept.Employee;
import org.eclipse.ve.internal.propertysheet.*;
/**
 * This is the property source for an Entree.
 */
public class DepartmentPropertySource implements IPropertySource {
	protected Department department;
	protected IPropertyDescriptor[] descriptors;
	
	public DepartmentPropertySource(Department department) {
		this.department = department;
	}

	public Object getEditableValue() {
		return department;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = new IPropertyDescriptor[] {
				new StringPropertyDescriptor(Department.DEPARTMENT_NAME, "department name"),
				new ManagerPropertyDescriptor(Department.MANAGER, "department manager")
			};
			
			StringPropertyDescriptor pd = (StringPropertyDescriptor) descriptors[0];
			pd.setValidator(new NameValidator());
			pd.setAlwaysIncompatible(true);	// This is so that we can't select more than one and change the name. If we did that, then automatically the names would not be unique.
			
			((ManagerPropertyDescriptor) descriptors[1]).setNullInvalid(false);
		};
				
		return descriptors;
	}
	
	public Object getPropertyValue(Object propertyKey) {
		if (Department.DEPARTMENT_NAME.equals(propertyKey))
			return department.getDepartmentName();
		else if (Department.MANAGER.equals(propertyKey))
			return department.getManager();
		else
			return null;
	}
	
	public boolean isPropertySet(Object propertyKey) {
		if (Department.DEPARTMENT_NAME.equals(propertyKey)) {
			return department.getDepartmentName() != null;
		} else if (Department.MANAGER.equals(propertyKey)) {
			return department.getManager() != null;
		} else
			return false;
	}
	
	public void resetPropertyValue(Object propertyKey) {
		if (Department.DEPARTMENT_NAME.equals(propertyKey))
			department.setDepartmentName(null);
		else if (Department.MANAGER.equals(propertyKey))
			department.setManager(null);
	}
	
	public void setPropertyValue(Object propertyKey, Object value) {
		if (value instanceof String && Department.DEPARTMENT_NAME.equals(propertyKey)) {
			department.setDepartmentName((String) value);
		} if ((value == null || value instanceof Employee) && Department.MANAGER.equals(propertyKey)) {
			department.setManager((Employee) value);
		}
	}
	
	private class NameValidator implements ICellEditorValidator {
		// Is the name valid. It is valid if the name is unique in the company.
		// Null is considered valid here because the non-nulls validator takes care of nulls.
		public String isValid(Object value) {
			if (value == null)
				return null;
				
			String name = (String) value;
			if (name.length() == 0)
				return "There must be a name.";
				
			if (name.equals(department.getDepartmentName()))
				return null;	// The current name is considered valid.
				
			String newName = PropertySupport.getUniqueDepartmentName(department.getCompany(), name);
			if (newName.equals(name))
				return null;	// The name didn't change, so it is valid.
				
			return "The name is not unique.";
		}
	}
	
	private static class ManagerCellEditor extends StandardComboBoxCellEditor implements ISourced {
		Department source;
		
		public ManagerCellEditor(Composite parent) {
			super(parent);
		}

		// This will be controlled and will always be only one source.		
		public void setSources(Object[] sources, IPropertySource[] pos, IPropertyDescriptor[] des) {
			source = (Department) sources[0];
			
			// Gather the non-manager employees and their names so that we can put them in the combobox.
			// Don't include any from the current department because a manager can't be in the same department
			// that they are managing.
			// Include the current one so that it is in the list. She is marked as a manager so she wouldn't be picked up for 
			// the list normally.
			// Also allow <null> i.e. no manager. But it last in the list.
			ArrayList employees = new ArrayList();
			ArrayList employeeStrings = new ArrayList();
			Employee manager = source.getManager();
			if (manager != null) {
				employees.add(manager);
				employeeStrings.add(manager.toString());
			}
			Iterator itr = source.getCompany().getEmployeesIterator();
			while(itr.hasNext()) {
				Employee e = (Employee) itr.next();
				if (e.getManages() == null && e.getDepartment() != source) {
					employees.add(e);
					employeeStrings.add(e.toString());
				}
			}
			
			employees.add(null);
			employeeStrings.add("<No manager>");
			
			setItems((String[]) employeeStrings.toArray(new String[employeeStrings.size()]), employees.toArray());
		}
	}
					
		
		
	private static class ManagerPropertyDescriptor extends EToolsPropertyDescriptor {
		
		public ManagerPropertyDescriptor(Object id, String displayName) {
			super(id, displayName);
			
			setAlwaysIncompatible(true);
		}
		
		public CellEditor createPropertyEditor(Composite parent) {
						
			CellEditor editor = new ManagerCellEditor(parent);
			ICellEditorValidator v = getValidator();
			if (v != null)
				editor.setValidator(v);			
			return editor;
		}
	}
	
}


