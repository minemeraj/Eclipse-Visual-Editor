/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: SourceVisitor.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:09 $ 
 */

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;


/**
 *  Source Visitor is an abstract class for java JDOM analyzor
 */
public abstract class SourceVisitor implements ISourceVisitor {
	
	protected ASTNode 		fVisitedNode = null ;
	protected IBeanDeclModel 	fModel ; 
	protected List			fReTryLater = null ;
	
		
public SourceVisitor (ASTNode node, IBeanDeclModel model, List reTryList) {
	fVisitedNode = node ;
	fReTryLater = reTryList ;
	fModel = model ;
}

/**
 *  Go for it.
 */
public abstract void visit() ;

/**
 *  Do not allow a visitor to postpone execution
 */
public void setNoRetry () {
  fReTryLater = null ;
} 


}
