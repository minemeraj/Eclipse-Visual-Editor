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
package org.eclipse.ve.internal.java.codegen.java.rules;
/*
 *  $RCSfile: IReturnStmtRule.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:48 $ 
 */


import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;

import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.java.ISourceVisitor;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
/**
 *  Enable overides to the default visits to a method.  The implementor of
 *  this interface is responsible to provide an ISourceVisitor if a special
 *  visit is required for the given method.
 */
public interface IReturnStmtRule extends IRule {

	
static String 	RULE_ID = "ruleReturnStatement" ;	 //$NON-NLS-1$

          


/**
 * Unique method visits (like VAJ's "initialize()" method) can be used
 * @return ISourceVisitor - the object which will overide the method's visit
 */
public ISourceVisitor   overideReturnVisit(MethodDeclaration method, ReturnStatement stmt, IBeanDeclModel model) ;
	

}


