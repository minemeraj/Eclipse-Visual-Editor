package org.eclipse.ve.internal.java.codegen.java.rules;
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
 *  $RCSfile: IOverideMethodVisitRule.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.java.ISourceVisitor;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;

/**
 *  Enable overides to the default visits to a method.  The implementor of
 *  this interface is responsible to provide an ISourceVisitor if a special
 *  visit is required for the given method.
 */
public interface IOverideMethodVisitRule extends IRule {

	
static String RULE_ID = "ruleOverideMethod" ;	 //$NON-NLS-1$


/**
 * Unique method visits (like VAJ's "initialize()" method) can be used
 * @return ISourceVisitor - the object which will overide the method's visit
 *
 * May be used with connection type methods
 */
public ISourceVisitor overideMethodVisit(AbstractMethodDeclaration method, IBeanDeclModel model) ;
	



}