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
 *  $RCSfile: UniqueEmployeeName.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.ve.examples.cdm.dept.Company;
import org.eclipse.ve.examples.cdm.dept.Employee;

import org.eclipse.ve.internal.propertysheet.command.SetPropertyValueCommand;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * A command that will change the Employee name to an unique one,
 */
public class UniqueEmployeeName extends CommandWrapper {
	protected Company company;
	protected Employee employee;
	
	public UniqueEmployeeName(Company company, Employee employee) {
		this.company = company;
		this.employee = employee;
	}
	
	protected boolean prepare() {
		return true;
	}
	
	public void execute() {
		
		String newName = PropertySupport.getUniqueEmployeeName(company, employee.getName());
		if (!newName.equals(employee.getName())) {
			// Rather than come up with a new Command to specifically change the name, we will use the 
			// PropertySheet support to do it.
			SetPropertyValueCommand pcmd = new SetPropertyValueCommand();
			pcmd.setTarget(PropertySupport.getPropertySource(employee));
			pcmd.setPropertyId(Employee.NAME);
			pcmd.setPropertyValue(newName);
			command = pcmd;
			command.execute();
		}
	}
	
	public void undo() {
		if (command != null)
			super.undo();	// Bug in CommandWrapper, it doesn't test if null before doing undo
	}		
}


