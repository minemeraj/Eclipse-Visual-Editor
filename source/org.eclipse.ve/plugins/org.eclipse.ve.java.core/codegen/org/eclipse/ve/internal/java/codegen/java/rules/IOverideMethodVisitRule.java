/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java.rules;
/*
 *  $RCSfile: IOverideMethodVisitRule.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:10 $ 
 */

import org.eclipse.jdt.core.dom.MethodDeclaration;

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
public ISourceVisitor overideMethodVisit(MethodDeclaration method, IBeanDeclModel model) ;
	



}
