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
 *  $RCSfile: IThisReferenceRule.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import org.eclipse.ve.internal.cde.rules.IRule;

import org.eclipse.ve.internal.java.codegen.java.ISourceVisitor;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
/**
 *  this.setFoo() (or this.whoKnows()) is visited, - what toDo ?
 */
public interface IThisReferenceRule extends IRule {

	static String RULE_ID = "ruleIThisReference"; //$NON-NLS-1$

	/**
	 * Unique method visits (like VAJ's "initialize()" method) can be used
	 * @return ISourceVisitor - the object which will overide the method's visit
	 */
	public ISourceVisitor overideThisReferenceVisit(MethodDeclaration method, MethodInvocation stmt, IBeanDeclModel model);
	//  Need to process these kind of statments only if the current type extends a bean we care about
	public boolean shouldProcess(MethodDeclaration method, MethodInvocation stmt, ITypeHierarchy h);
	//  Do we need to use inheritance
	public boolean useInheritance(String superClass, ResourceSet rs);
	//  what is the name/modifier of the init metod for the this part
	//  The return is an array of two string { name, modifier }
	public String[] getThisInitMethodName(ITypeHierarchy h);

}
