package org.eclipse.ve.examples.cdm.dept.dinner.ui;
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
 *  $RCSfile: DinnerConstants.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:42:30 $ 
 */

/**
 * This contains constants used by the ui.
 */
public interface DinnerConstants {

	public static final String
		COMPANY_URL = "companyurl",	// Key on Dinner Diagram for the company file url.
		EMPLOYEE = "employee",		// Key on Employee DiagramFigure for the employee in the company.
		FOOD = "food",			// Key op Entree DiagramFigure for the entree food.
		
		ENTREE_CHILD_TYPE = "entree",	// The type of children valid for Dinner Diagram. Only entrees are allowed.
		EMPLOYEE_CHILD_TYPE = "employee";	// The type of children valid Entree DiagramFigure. Only employees are allowed.
}