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
 *  $RCSfile: IDiagramModelBuilder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.codegen.java.ISourceTranslatorListener;
import org.eclipse.ve.internal.java.codegen.java.ISynchronizerListener;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;

/**
 *   A VCE Compostion Model Builder provides an interface to an object
 *   that creates or store a composition model from/to an input file.  
 */
public interface IDiagramModelBuilder extends IDiagramModelInstance {
	
	
	// Add Transient error listeners
	public void addTransientErrorListener(ITransientErrorListener listener);
	// Initialize a new Resource Set for a new file
	public void setInputResource(IFile file) throws CodeGenException ;
	// Create a new Composition model from the input file
	public void     loadModel (IProgressMonitor pm) throws CodeGenException ;  			
      // Save the model  
	public void	  saveModel  (IProgressMonitor pm) throws CodeGenException ; 
	
	// Is the model loaded? NOTE: This is under synchronization, so be careful of deadlocks.
	public boolean isModelLoaded();
	
	// Get the Load lock. Need to lock this on a NON-UI thread before calling loadModel/decodemodel ON UI Thread.
	public Object getLoadLock();
	
	// Disconnect, and bring down all threads/resources used by the builder
	public void dispose() ;
	// Disconnect from external resources (e.g., Java Working Copy)
	// if clearModel is true, the VCE model will be cleared
	public void disconnect(boolean clearModel) ;  
	// Will re-load a previously disconnected model.
	public void reconnect(IFileEditorInput input, IProgressMonitor pm) throws CodeGenException ;
	
    /**
     * Denotes that a compound operation is completed.  This
     * will actually remove source code of deleted components, if needed,
     * and will flush the changes to the Java Editor.
     * 
     * This operation is synchroneous.
     */
    public void commitAndFlush(boolean canWait) ;
    /**
     * Async. version of commitAndFlush
     * 
     * @param listener implements the call back
     * @param marker is an optional origin designator for the call back.
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

	// Remove Transient error listeners
	public void removeTransientErrorListener(ITransientErrorListener listener);
    
    public void setMsgRenderer (IJVEStatus mr) ;
    public void setEditDomain (EditDomain d) ;
    public void pauseRoundTripping(boolean flag) throws CodeGenException ;
}


