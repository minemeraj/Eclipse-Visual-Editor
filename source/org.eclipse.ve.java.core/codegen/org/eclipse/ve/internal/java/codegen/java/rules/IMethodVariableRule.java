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
 *  $RCSfile: IMethodVariableRule.java,v $
 *  $Revision: 1.2 $  $Date: 2004-03-05 23:18:38 $ 
 */

import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;



public interface IMethodVariableRule extends IRule {
	
static String RULE_ID = "ruleMethodVariable" ;	 //$NON-NLS-1$


/**
 * Give the parser a hint if a local variable is to be ignored.
 */
public boolean ignoreVariable(VariableDeclarationStatement localField, ITypeResolver resolver,IDiagramModelInstance di) ;
	

}
	

