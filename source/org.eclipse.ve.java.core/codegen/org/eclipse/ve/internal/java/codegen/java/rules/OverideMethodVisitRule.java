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
 *  $Revision: 1.2 $  $Date: 2004-03-05 23:18:38 $ 
 */

import org.eclipse.jdt.core.dom.MethodDeclaration;

import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.java.codegen.java.ISourceVisitor;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;

public class OverideMethodVisitRule implements IOverideMethodVisitRule {

	/**
	 *
	 */
	public ISourceVisitor overideMethodVisit(MethodDeclaration method, IBeanDeclModel model) {
		if ("initialize".equals(method.getName().getIdentifier())) { // Maybe do something different here, same for connFooFoo() //$NON-NLS-1$
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