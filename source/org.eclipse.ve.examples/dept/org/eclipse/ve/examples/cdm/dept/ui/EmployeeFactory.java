package org.eclipse.ve.examples.cdm.dept.ui;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EmployeeFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-16 00:30:22 $ 
 */

import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.examples.cdm.dept.Employee;
/**
 * Factory to create new Departments.
 */

public class EmployeeFactory implements CreationFactory {
	public Object getNewObject(){
		return new Employee();
	}
	
	public Object getObjectType(){
		return Employee.class;
	}
}