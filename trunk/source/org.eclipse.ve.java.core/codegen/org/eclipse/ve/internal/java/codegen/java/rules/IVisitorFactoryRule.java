/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IVisitorFactoryRule.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.java.MethodVisitor;
import org.eclipse.ve.internal.java.codegen.java.TypeVisitor;
 
/**
 * This rule allows a codegen style to determine which visitor to use 
 * for the set class. Before calling any of the getters the rule must be
 * told which class to use, so that the appropriate visitor can be returned.
 * If no class is set, the default visitors will be returned.
 * 
 * 
 * 
 * @since 1.1
 */
public interface IVisitorFactoryRule extends IRule {
	
	public static final String RULE_ID = "ruleVisitorFactory"; //$NON-NLS-1$
	
	public TypeVisitor getTypeVisitor();
	public MethodVisitor getMethodVisitor();
	public EventMethodCallBackVisitor getEventMethodCallBackVisitor();
	public EventMethodVisitor getEventMethodVisitor();
	public EventHandlerVisitor getEventHandlerVisitor();
	public ReturnStmtVisitor getReturnStmtVisitor();
	public ExpressionVisitor getExpressionVisitor();
	public EventExpressionVisitor getEventExpressionVisitor();
	public EventCallBackExpressionVisitor getEventCallBackExpressionVisitor();
	
	public void setClassifier(EClassifier eclassifier);
	public EClassifier getClassifier();
}
