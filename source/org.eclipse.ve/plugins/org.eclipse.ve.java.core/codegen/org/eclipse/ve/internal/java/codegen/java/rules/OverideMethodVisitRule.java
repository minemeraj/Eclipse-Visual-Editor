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
 *  $RCSfile: OverideMethodVisitRule.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.java.ISourceVisitor;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;

public class OverideMethodVisitRule implements IOverideMethodVisitRule {

	/**
	 *
	 */
	protected boolean sameContent(String st, char[] cArray) {
		if (st == null || st.length() != cArray.length)
			return false;
		return st.equals(new String(cArray));
	}

	/**
	 *
	 */
	public ISourceVisitor overideMethodVisit(AbstractMethodDeclaration method, IBeanDeclModel model) {
		if (sameContent("initialize", method.selector)) { // Maybe do something different here, same for connFooFoo() //$NON-NLS-1$
			return null;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.rules.IRule#setRegistry(org.eclipse.ve.internal.cde.rules.IRuleRegistry)
	 */
	public void setRegistry(IRuleRegistry registry) {
		// no-op. We don't care.
	}
}