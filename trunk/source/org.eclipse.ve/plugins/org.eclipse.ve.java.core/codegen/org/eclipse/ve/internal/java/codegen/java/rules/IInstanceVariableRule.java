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
 *  $RCSfile: IInstanceVariableRule.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;


public interface IInstanceVariableRule extends IRule {
	
static String RULE_ID = "ruleInstanceVariable" ;	 //$NON-NLS-1$

/**
 * Give the parser a hint if an instance variable is to be ignored.
 */
public boolean ignoreVariable(AbstractVariableDeclaration field, ITypeResolver resolver, IDiagramModelInstance di) ;
/**
 * Forces the parser to consider the returned method as the initialization method.
 * 
 * @param field
 * @param resolver
 * @return  init method's name, or null for no specific overide
 */
public String   getDefaultInitializationMethod(AbstractVariableDeclaration field, ITypeResolver resolver, TypeDeclaration typeDec) ;
	

}
	
