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
 *  $RCSfile: ReturnStmtRule.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:10 $ 
 */


import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.java.ISourceVisitor;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;

public class ReturnStmtRule implements IReturnStmtRule {

	/**
	 *   Use the default ReturnVisitor
	 */
	public ISourceVisitor overideReturnVisit(MethodDeclaration method, ReturnStatement stmt, IBeanDeclModel model) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}
}
