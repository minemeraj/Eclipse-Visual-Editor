/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventCallBackExpressionVisitor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-11 16:03:22 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.ve.internal.java.codegen.model.*;

/**
 * @author Gili Mendel
 * 
 */
public class EventCallBackExpressionVisitor extends SourceVisitor {
	
	
CodeMethodRef				fMethod  ;
CodeCallBackRef				fExpression  ;
BeanPart					fBean ;

public EventCallBackExpressionVisitor(BeanPart b, CodeMethodRef mref, Statement stmt,IBeanDeclModel model) {
	super((ASTNode)stmt,model,null) ;	
	fMethod = mref ;
	fBean = b ;
	fExpression = new CodeCallBackRef (stmt,fMethod) ;
}
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ISourceVisitor#visit()
	 */
	public void visit() {
		// We can parse the callback here to see that it makes actual callbacks here before adding it in.
		fExpression.setBean(fBean) ;
		fMethod.setModel(fBean.getModel()) ;
	    
	
	}

}
