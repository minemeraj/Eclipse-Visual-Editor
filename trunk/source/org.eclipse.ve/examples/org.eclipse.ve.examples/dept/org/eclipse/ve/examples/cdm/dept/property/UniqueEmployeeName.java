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
 *  $Revision: 1.4 $  $Date: 2005-10-11 21:23:51 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.CompoundCommand;

import org.eclipse.ve.examples.cdm.dept.Company;
import org.eclipse.ve.examples.cdm.dept.Employee;

import org.eclipse.ve.internal.propertysheet.command.SetPropertyValueCommand;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * A command that will change the Employee name to an unique one,
 */
public class UniqueEmployeeName extends CommandWrapper {
	protected Company company;
	protected List employees;
	
	public UniqueEmployeeName(Company company, List employees) {
		this.company = company;
		this.employees = employees;
	}
	
	protected boolean prepare() {
		return true;
	}
	
	public void execute() {
		CompoundCommand cc = new CompoundCommand();
		for (Iterator itr = employees.iterator(); itr.hasNext();) {
			Employee employee = (Employee) itr.next();
			String newName = PropertySupport.getUniqueEmployeeName(company, employee.getName());
			if (!newName.equals(employee.getName())) {
				// Rather than come up with a new Command to specifically change the name, we will use the 
				// PropertySheet support to do it.
				SetPropertyValueCommand pcmd = new SetPropertyValueCommand();
				pcmd.setTarget(PropertySupport.getPropertySource(employee));
				pcmd.setPropertyId(Employee.NAME);
				pcmd.setPropertyValue(newName);
				cc.add(pcmd);
			}
		}
		command = cc.unwrap();
		command.execute();

	}
	
	public void undo() {
		if (command != null)
			super.undo();	// Bug in CommandWrapper, it doesn't test if null before doing undo
	}		
}


