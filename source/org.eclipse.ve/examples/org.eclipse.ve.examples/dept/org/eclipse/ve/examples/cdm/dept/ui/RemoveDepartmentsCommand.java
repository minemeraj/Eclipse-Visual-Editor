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
 *  $RCSfile: RemoveDepartmentsCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.ve.examples.cdm.dept.Company;
import org.eclipse.ve.examples.cdm.dept.Department;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
/**
 * Command to remove departments from company.
 */
public class RemoveDepartmentsCommand extends AbstractCommand {
	protected Company company;
	protected List departments;
	protected int oldPos[];	// Position they were in so that they can be put back in the same place on undo.
	
	public RemoveDepartmentsCommand(Company company, List departments) {
		this.company = company;
		this.departments = departments;
	}
	
	public boolean canExecute() {
		return true;
	}
	
	public void execute() {
		Iterator itr = departments.iterator();
		oldPos = new int[departments.size()];
		int i = -1;
		while(itr.hasNext()) {
			Department department = (Department) itr.next();
			int pos = company.getDepartments().indexOf(department);
			oldPos[++i] = pos;
			if (pos != -1) {
				company.removeDepartment(department);
			}
				
		}
	}

	
	public void redo() {
		execute();
	}
	
	public void undo() {
		Iterator itr = departments.iterator();
		int i = -1;
		while (itr.hasNext()) {
			Department department = (Department) itr.next();
			int pos = oldPos[++i];
			if (pos != -1) {
				company.addDepartment(department, pos);
			}
		}
		oldPos = null;
	}	

}
