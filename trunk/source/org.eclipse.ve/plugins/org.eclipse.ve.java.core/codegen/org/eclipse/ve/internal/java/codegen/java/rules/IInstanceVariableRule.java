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
 *  $Revision: 1.3 $  $Date: 2004-03-16 20:55:59 $ 
 */


import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.ITypeResolver;


public interface IInstanceVariableRule extends IRule {
	
static String RULE_ID = "ruleInstanceVariable" ;	 //$NON-NLS-1$

/**
 * Give the parser a hint if an instance variable is to be ignored.
 */
public boolean ignoreVariable(FieldDeclaration field, ITypeResolver resolver, IVEModelInstance di) ;
/**
 * Forces the parser to consider the returned method as the initialization method.
 * 
 * @param field
 * @param resolver
 * @return  init method's name, or null for no specific overide
 */
public String   getDefaultInitializationMethod(FieldDeclaration field, ITypeResolver resolver, TypeDeclaration typeDec) ;
	

}
	
