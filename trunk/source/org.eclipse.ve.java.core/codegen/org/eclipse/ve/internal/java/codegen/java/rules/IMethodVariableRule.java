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
 *  $Revision: 1.4 $  $Date: 2004-05-14 19:54:05 $ 
 */

import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;



public interface IMethodVariableRule extends IRule {
	
static String RULE_ID = "ruleMethodVariable" ;	 //$NON-NLS-1$


/**
 * Give the parser a hint if a local variable is to be ignored. It is not ignored if any of the following are true 
 * <li>The type of the declaration is 'modelled' in the overrides</li> 
 * <li>The variable name is starting with the default prefix defined in Window>Preferences>Java>Visual Editor>Code Generation tab>Default prefix</li>
 * <li>The field declaration has a Coegen Annotation comment at the end</li>
 * Note: the passed in AST node field will find its codegen annotation comment from the source 
 * present in its parent CU node's 'org.eclipse.ve.codegen.source' property.
 */
public boolean ignoreVariable(VariableDeclarationStatement localField, ITypeResolver resolver,IVEModelInstance di) ;
	

}
	

