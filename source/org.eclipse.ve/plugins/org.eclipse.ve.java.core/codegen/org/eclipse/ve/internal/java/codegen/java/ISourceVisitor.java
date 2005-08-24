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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: ISourceVisitor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:44 $ 
 */

/**
 *  To build a IBeanDeclModel, the JavaDecoder visits (parse) various
 *  parts of the code (Methods, Statements etc.)
 *  A ISourceVisitor may update the IBeanDeclModel and may call other Visitors.
 *  A ISourceVisitor may posponed its action to a second pass, if it can not resolve
 *  elements that have not been parsed yet.
 */
public interface ISourceVisitor {	
	public void visit () ;
	// Do not allow the visitor to postpone its excecution.
	public void setNoRetry () ;
}
