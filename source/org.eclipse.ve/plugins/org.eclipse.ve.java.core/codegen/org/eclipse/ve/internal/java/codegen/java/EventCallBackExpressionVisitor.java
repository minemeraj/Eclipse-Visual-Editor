/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventCallBackExpressionVisitor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-03-30 17:34:23 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.model.*;

/**
 * @author Gili Mendel
 * 
 */
public class EventCallBackExpressionVisitor extends SourceVisitor {
	
	
CodeMethodRef				fMethod  ;
CodeCallBackRef				fExpression  ;
BeanPart					fBean ;

public void initialize(BeanPart b, CodeMethodRef mref, Statement stmt,IBeanDeclModel model) {
	super.initialize((ASTNode)stmt,model,null) ;	
	fMethod = mref ;
	fBean = b ;
	fExpression = new CodeCallBackRef (stmt,fMethod) ;
}
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ISourceVisitor#visit()
	 */
	public void visit() {
		// We can parse the callback here to see that it makes actual callbacks here before adding it in.
		getProgressMonitor().subTask(fMethod.getTypeRef().getSimpleName()+" : "+fMethod.getMethodName()+"()"+" : "+fExpression.getCodeContent());
		fExpression.setBean(fBean) ;
		fMethod.setModel(fBean.getModel()) ;
	    
	
	}

}
