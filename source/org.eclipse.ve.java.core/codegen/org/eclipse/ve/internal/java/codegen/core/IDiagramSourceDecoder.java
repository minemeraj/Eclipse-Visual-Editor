package org.eclipse.ve.internal.java.codegen.core;
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
 *  $RCSfile: IDiagramSourceDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;
import org.eclipse.ve.internal.java.codegen.java.ISourceTranslatorListener;
import org.eclipse.ve.internal.java.codegen.java.ISynchronizerListener;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 *   Implementors will build a Composition Model from a given source file. Also
 *   the model could be saved back.
 */

public interface IDiagramSourceDecoder {

	public final static String JAVAExt =   "java" ;     // JAVA file ; //$NON-NLS-1$
	
	// Add Transient error listeners
	public void addTransientErrorListener(ITransientErrorListener listener);
	
	// Remove Transient error listeners
	public void removeTransientErrorListener(ITransientErrorListener listener);

    // Decode the imput source
	public void	decodeDocument (IDiagramModelInstance cm, String uri, IFile sourceFile, IProgressMonitor pm) throws CodeGenException ;
	// Save a document 
	public void		saveDocument (IProgressMonitor pm) throws CodeGenException ;
	
	// What file extention does this decoder works with
	public String  getFileExt() ;
	
	public void dispose() ;

	/**
	 * Denote that a compound operation is completed.  This
	 * will actually remove code for deleted components, if needed,
	 * and will flush the changes to the Java Editor.
	 * A call to this API is synchroneous.
	 * 
	 * @param canWait denotes if the call can wait for the next sync.
	 *         cycle.  if false, a flush will be instigated with no delay.
	 */
	public void commitAndFlush(boolean canWait) ;
	/**
	 * Async. version of commitAndFlush
	 *        The flush will occur on the next cycle of the synchronizer
	 * 
	 * @param listener implements the call back to be called when the flush occured.
	 * @param marker is an optional designator of the call back.
     */
	public void commitAndFlush(ISynchronizerListener listener, String marker) ;
	
	/**
	 * @param delay will set up the delay time for JavaSourceSynchronizer
	 *         value less than JavaSourceSynchronizer.DEFAULT_SYNC_DELAY will be ignored.
	 */
	public void setSynchronizerSyncDelay(int delay) ;
	
	/**
	 * @param listener for Source Deocder events
	 */	
	public void addTranslatorListener (ISourceTranslatorListener listener) ;
	
	public void removeTranslatorListener (ISourceTranslatorListener listener) ;
    
    public void setMsgRenderer (IJVEStatus mr)  ;
    
    // Disconnect from external resources (e.g., Java Working Copy)
	// if clearModel is true, the VCE model will be cleared
	public void disconnect(boolean clearModel) ;  
	// Will re-load a previously disconnected model.
	public void reconnect(IFileEditorInput input,IProgressMonitor pm) throws CodeGenException;
	public void setEditDomain (EditDomain d) ;
	public void pauseRoundTripping(boolean flag) throws CodeGenException ;
	
}


