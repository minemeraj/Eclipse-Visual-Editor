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
 *  $RCSfile: DepartmentFactory.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:42:30 $ 
 */

import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.examples.cdm.dept.Department;
/**
 * Factory to create new Departments.
 */

public class DepartmentFactory implements CreationFactory {
	public Object getNewObject(){
		return new Department();
	}
	
	public Object getObjectType(){
		return Department.class;
	}
}