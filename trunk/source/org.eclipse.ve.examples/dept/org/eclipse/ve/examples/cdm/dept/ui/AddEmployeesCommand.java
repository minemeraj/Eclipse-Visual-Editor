package org.eclipse.ve.examples.cdm.dept.ui;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AddEmployeesCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:42:30 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.ve.examples.cdm.dept.Department;
import org.eclipse.ve.examples.cdm.dept.Employee;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
/**
 * Command to add employees to department.
 */
public class AddEmployeesCommand extends AbstractCommand {
	protected Department department;
	protected List employees;
	protected Employee before;
	
	public AddEmployeesCommand(Department department, List employees, Employee before) {
		this.department = department;
		this.employees = employees;
		this.before = before;
	}
	
	public boolean canExecute() {
		return true;
	}
	
	public void execute() {		
		int pos = department.getEmployees().indexOf(before);
		Iterator itr = employees.iterator();
		while(itr.hasNext()) {
			Employee d = (Employee) itr.next();
			if (pos != -1)
				department.addEmployee(d, pos++);
			else
				department.addEmployee(d);
		}
	}
	
	public void redo() {
		execute();
	}
	
	public void undo() {
		Iterator itr = employees.iterator();
		for (int i = 0; itr.hasNext(); i++) {
			Employee e = (Employee) itr.next();
			department.removeEmployee(e);
		}
	}	

}