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
package org.eclipse.ve.examples.cdm.dept.test;
/*
 *  $RCSfile: CreateTestCompany.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:44 $ 
 */

import org.eclipse.ve.examples.cdm.dept.*;
import java.io.*;
/**
 * Create the test company.
 */
public class CreateTestCompany {

/**
 * Just creates a simple test company
 */
public static void main(java.lang.String[] args) {
	Company company = new Company();
	
	company.setName("IBM");
	
	Department dept1 = new Department();
	dept1.setDepartmentName("Websphere");
	company.addDepartment(dept1);
	
	Employee emp1 = new Employee();
	emp1.setName("Tom Wolfe");
	emp1.setPhone("919-555-1212");
	
	dept1.setManager(emp1);
	
	Employee emp2 = new Employee();
	emp2.setName("Fred the first");
	emp2.setPhone("919-555-2222");
	dept1.addEmployee(emp2);

	
	Employee emp3 = new Employee();
	emp3.setName("Samuel Clemens");
	emp3.setPhone("919-555-4444");
	dept1.addEmployee(emp3);
	
	Department dept2 = new Department();
	dept2.setDepartmentName("EJB");	
	dept2.setManager(emp2);
	company.addDepartment(dept2);
	
	Employee emp4 = new Employee();
	emp4.setName("Margaret Thatcher");
	emp4.setPhone("0333-333-4444");
	dept2.addEmployee(emp4);
	
	Department dept3 = new Department();
	dept3.setDepartmentName("VCE");	
	dept3.setManager(emp3);
	company.addDepartment(dept3);
	
	Employee emp5 = new Employee();
	emp5.setName("Peter Stalker");
	emp5.setPhone("919-555-6666");
	dept3.addEmployee(emp5);
	
	
	try {
		File file = new File("ibm.cdmcmp");
		System.out.println("Being written to: \""+file.getAbsolutePath()+"\"");
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
		os.writeObject(company);
		os.close();
		System.out.println("Completed writing");
	} catch (IOException e) {
		e.printStackTrace();
	}
}

}
