/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Feb 10, 2005 by Gili Mendel
 * 
 *  $RCSfile: SnippetParseJob.java,v $
 *  $Revision: 1.4 $  $Date: 2005-04-05 22:48:22 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ModelChangeController;

import org.eclipse.ve.internal.java.codegen.core.IEditorUpdateState;
import org.eclipse.ve.internal.java.codegen.util.IBackGroundWorkStrategy;
import org.eclipse.ve.internal.java.codegen.util.ReverseParserJob;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 

/**
 * 
 * @since 1.1.0
 */
public class SnippetParseJob extends ReverseParserJob {	
	
	private IBackGroundWorkStrategy strategyRoutine;
	private Display					curDisplay;
	private ICompilationUnit		cu;
	private IEditorUpdateState		editorState;
	private List 					docEvents;
    private EditDomain fEditDomain;
    public static final String SNIPPET = "SNIPPET"; //$NON-NLS-1$
	
	/**
	 * @param file
	 * 
	 * @since 1.1.0
	 */
	public SnippetParseJob(IFile file, IBackGroundWorkStrategy strategy, Display disp, ICompilationUnit cu, IEditorUpdateState state, List events) {
		super(file);
		strategyRoutine=strategy;
		curDisplay=disp;
		this.cu=cu;

		editorState=state;
		docEvents=events;		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.ReverseParserJob#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus doRun(IProgressMonitor monitor) {		
	    
	    ModelChangeController changeController = (ModelChangeController)fEditDomain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
		try {
		  changeController.transactionBeginning(SNIPPET);
		  strategyRoutine.run(curDisplay,cu,editorState,docEvents,monitor);
		}
		catch (Throwable t) {
			JavaVEPlugin.log(t);
			String msg = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();			
			return new Status(Status.WARNING,CDEPlugin.getPlugin().getPluginID(), 0, msg ,t);
		}
		finally {
			editorState.setBottomUpProcessing(false);
			changeController.transactionEnded(SNIPPET);			
		}
		return Status.OK_STATUS;
	}
    /**
     * @param editDomain
     * Set the edit domain
     * 
     * @since 1.0.2
     */
    public void setEditDomain(EditDomain editDomain) {
        fEditDomain = editDomain;       
    }

}
