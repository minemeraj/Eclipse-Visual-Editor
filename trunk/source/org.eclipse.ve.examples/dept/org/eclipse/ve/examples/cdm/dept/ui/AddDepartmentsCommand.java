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
 *  $RCSfile: AddDepartmentsCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.ve.examples.cdm.dept.Company;
import org.eclipse.ve.examples.cdm.dept.Department;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
/**
 * Command to add departments to company.
 */
public class AddDepartmentsCommand extends AbstractCommand {
	protected Company company;
	protected List departments;
	protected Department before;
	
	public AddDepartmentsCommand(Company company, List departments, Department before) {
		this.company = company;
		this.departments = departments;
		this.before = before;
	}
	
	public boolean canExecute() {
		return true;
	}
	
	public void execute() {	

		int pos = company.getDepartments().indexOf(before);
		Iterator itr = departments.iterator();
		while(itr.hasNext()) {
			Department d = (Department) itr.next();
			if (pos != -1)
				company.addDepartment(d, pos++);
			else
				company.addDepartment(d);
		}
	}
	
	public void redo() {
		execute();
	}
	
	public void undo() {
		Iterator itr = departments.iterator();
		for (int i = 0; itr.hasNext(); i++) {
			Department d = (Department) itr.next();
			company.removeDepartment(d);
		}
	}	

}
