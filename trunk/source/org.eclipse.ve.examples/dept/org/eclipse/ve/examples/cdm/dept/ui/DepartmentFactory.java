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
 *  $RCSfile: DepartmentFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
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
