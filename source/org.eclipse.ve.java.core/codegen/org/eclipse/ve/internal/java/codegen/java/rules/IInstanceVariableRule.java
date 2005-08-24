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
 *  $RCSfile: IInstanceVariableRule.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:48 $ 
 */


import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver;


public interface IInstanceVariableRule extends IRule {
	
static String RULE_ID = "ruleInstanceVariable" ;	 //$NON-NLS-1$

/**
 * Give the parser a hint if an instance variable is to be ignored. It is not ignored if any of the following are true 
 * <li>The type of the declaration is 'modelled' in the overrides</li> 
 * <li>The variable name is starting with the default prefix defined in Window>Preferences>Java>Visual Editor>Code Generation tab>Default prefix</li>
 * <li>The field declaration has a Coegen Annotation comment at the end</li>
 * Note: the passed in AST node field will find its codegen annotation comment from the source 
 * present in its parent CU node's 'org.eclipse.ve.codegen.source' property.
 */
public boolean ignoreVariable(FieldDeclaration field, TypeResolver resolver, IVEModelInstance di) ;
/**
 * Forces the parser to consider the returned method as the initialization method.
 * 
 * @param field
 * @param resolver
 * @return  init method's name, or null for no specific overide
 */
public String   getDefaultInitializationMethod(FieldDeclaration field, TypeResolver resolver, TypeDeclaration typeDec) ;
	

}
	
