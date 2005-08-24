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
 *  $RCSfile: UniqueDepartmentName.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.util.Iterator;

import org.eclipse.ve.examples.cdm.dept.*;

import org.eclipse.ve.internal.propertysheet.command.SetPropertyValueCommand;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 * A command that will change the Department name to an unique one,
 * do the same for each of its employees.
 */
public class UniqueDepartmentName extends CommandWrapper {
	protected Company company;
	protected Department department;
	
	public UniqueDepartmentName(Company company, Department department) {
		this.company = company;
		this.department = department;
	}
	
	protected boolean prepare() {
		return true;
	}
	
	public void execute() {
		CompoundCommand c = new CompoundCommand();
		
		String newName = PropertySupport.getUniqueDepartmentName(company, department.getDepartmentName());
		if (!newName.equals(department.getDepartmentName())) {
			// Rather than come up with a new Command to specifically change the name, we will use the 
			// PropertySheet support to do it.
			SetPropertyValueCommand pcmd = new SetPropertyValueCommand();
			pcmd.setTarget(new DepartmentPropertySource(department));
			pcmd.setPropertyId(Department.DEPARTMENT_NAME);
			pcmd.setPropertyValue(newName);
			c.append(pcmd);
		}
		
		// Now do the same for each employee.
		Iterator itr = department.getEmployees().iterator();
		while (itr.hasNext()) {
			c.append(new UniqueEmployeeName(company, (Employee) itr.next()));
		}
		
		if (!c.isEmpty()) {
			command = c.unwrap();
			command.execute();
		}
	}
	
	public void undo() {
		if (command != null)
			super.undo();	// Bug in CommandWrapper, it doesn't test if null before doing undo
	}		
}


