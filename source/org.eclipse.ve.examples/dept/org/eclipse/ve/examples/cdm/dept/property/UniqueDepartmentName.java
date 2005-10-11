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
 *  $Revision: 1.4 $  $Date: 2005-10-11 21:23:51 $ 
 */

import java.util.Iterator;
import java.util.List;

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
	protected List departments;
	
	public UniqueDepartmentName(Company company, List departments) {
		this.company = company;
		this.departments = departments;
	}
	
	protected boolean prepare() {
		return true;
	}
	
	public void execute() {
		CompoundCommand c = new CompoundCommand();

		for (Iterator deptItr = departments.iterator(); deptItr.hasNext();) {
			Department dept = (Department) deptItr.next();
			String newName = PropertySupport.getUniqueDepartmentName(company, dept.getDepartmentName());
			if (!newName.equals(dept.getDepartmentName())) {
				// Rather than come up with a new Command to specifically change the name, we will use the 
				// PropertySheet support to do it.
				SetPropertyValueCommand pcmd = new SetPropertyValueCommand();
				pcmd.setTarget(new DepartmentPropertySource(dept));
				pcmd.setPropertyId(Department.DEPARTMENT_NAME);
				pcmd.setPropertyValue(newName);
				c.append(pcmd);
			}
			
			// Now do the same for each employee.
			c.append(new UniqueEmployeeName(company, dept.getEmployees()));
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


