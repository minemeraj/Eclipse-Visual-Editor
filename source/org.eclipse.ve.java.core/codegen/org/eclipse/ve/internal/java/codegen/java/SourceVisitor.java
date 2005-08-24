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
 *  $RCSfile: SourceVisitor.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ASTNode;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;


/**
 *  Source Visitor is an abstract class for java JDOM analyzor
 */
public abstract class SourceVisitor implements ISourceVisitor {
	
	protected ASTNode 		fVisitedNode = null ;
	protected IBeanDeclModel 	fModel ; 
	protected List			fReTryLater = null ;
	protected IProgressMonitor progressMonitor = null; 
	
public void initialize (ASTNode node, IBeanDeclModel model, List reTryList) {
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


	/**
	 * @return Returns the progressMonitor.
	 * 
	 * @since 1.0.2
	 */
	public IProgressMonitor getProgressMonitor() {
		if(progressMonitor==null)
			progressMonitor = new NullProgressMonitor();
		return progressMonitor;
	}
	/**
	 * @param progressMonitor The progressMonitor to set.
	 * 
	 * @since 1.0.2
	 */
	public void setProgressMonitor(IProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}
}
