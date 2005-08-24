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
 *  $RCSfile: PropertySupport.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.ve.examples.cdm.dept.*;
import java.util.Iterator;
import org.eclipse.ui.views.properties.IPropertySource;
/**
 * Some helper methods for supporting Company model properties.
 */
public class PropertySupport {
	
	/**
	 * Return the correct property source for the object.
	 */
	public static IPropertySource getPropertySource(Object model) {
		if (model instanceof Company)
			return new CompanyPropertySource((Company) model);
		else if (model instanceof Department)
			return new DepartmentPropertySource((Department) model);
		else if (model instanceof Employee)
			return new EmployeePropertySource((Employee) model);
		else
			return null;
	}

	
	/**
	 * See if the Department name is unique in the company, and if not, then return an
	 * unique one.
	 */
	public static String getUniqueDepartmentName(Company company, String deptName) {
		
		String baseDeptName = null;
		if (deptName != null)
			baseDeptName = deptName;
		else
			baseDeptName = "Dept XYZ";	// Use a default.
		String componentName = baseDeptName;
		int incr = 0;
		main : while (true) {
			Iterator departments = company.getDepartments().iterator();
			while (departments.hasNext()) {
				Department aDept = (Department) departments.next();
				if (componentName.equals(aDept.getDepartmentName())) {
					componentName = baseDeptName + ++incr;
					continue main;
				}
			}
			break;
		}
		
		return componentName;
	}
	
	/**
	 * See if the Employee name is unique in the company, and if not, then return an
	 * unique one.
	 */
	public static String getUniqueEmployeeName(Company company, String empName) {
		
		String baseEmpName = null;
		if (empName != null)
			baseEmpName = empName;
		else
			baseEmpName = "Employee XYZ";	// Use a default.
		String componentName = baseEmpName;
		int incr = 0;
		main : while (true) {
			Iterator employees = company.getEmployeesIterator();
			while (employees.hasNext()) {
				Employee anEmp = (Employee) employees.next();
				if (componentName.equals(anEmp.getName())) {
					componentName = baseEmpName + ++incr;
					continue main;
				}
			}
			break;
		}
		
		return componentName;
	}			

}
