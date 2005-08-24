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
 *  $RCSfile: IfStatementRule.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:48 $ 
 */


import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.java.ISourceVisitor;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;

public class IfStatementRule implements IIfStatementRule {

	/**
	 *  No Overide
	 */
	public ISourceVisitor overideIFVisit(MethodDeclaration method, IfStatement stmt, IBeanDeclModel model) {
		return null;
	}

	/**
	 *  We can try to use some pattern analysys.  e.g., VAJ's initialization as 
	 *  if (ivjFoo == null) {  initialize block }
	 */
	public int whichPartToProcess(MethodDeclaration method, IfStatement stmt) {
		// This will do for now,
		return PROCESS_IF;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}
}
