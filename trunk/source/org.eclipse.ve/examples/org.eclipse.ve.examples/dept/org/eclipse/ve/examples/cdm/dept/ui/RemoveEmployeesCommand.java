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
 *  $RCSfile: RemoveEmployeesCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.ve.examples.cdm.dept.Department;
import org.eclipse.ve.examples.cdm.dept.Employee;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
/**
 * Command to remove employees from department.
 */
public class RemoveEmployeesCommand extends AbstractCommand {
	protected Department department;
	protected List employees;
	protected int oldPos[];	// Position they were in so that they can be put back in the same place on undo.
	
	public RemoveEmployeesCommand(Department department, List employees) {
		this.department = department;
		this.employees = employees;
	}
	
	public boolean canExecute() {
		return true;
	}
	
	public void execute() {
		Iterator itr = employees.iterator();
		oldPos = new int[employees.size()];
		int i = 0;
		while(itr.hasNext()) {
			Employee employee = (Employee) itr.next();
			int pos = department.getEmployees().indexOf(employee);
			oldPos[i++] = pos;
			if (pos != -1)
				department.removeEmployee(employee);
		}
	}

	
	public void redo() {
		execute();
	}
	
	public void undo() {
		Iterator itr = employees.iterator();
		int i = 0;
		while (itr.hasNext()) {
			Employee employee = (Employee) itr.next();
			int pos = oldPos[i++];
			if (pos != -1)
				department.addEmployee(employee, pos);
		}
		
		oldPos = null;
	}	

}
