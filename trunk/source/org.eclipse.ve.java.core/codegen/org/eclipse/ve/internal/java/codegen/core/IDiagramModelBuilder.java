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
package org.eclipse.ve.internal.java.codegen.core;
/*
 *  $RCSfile: IDiagramModelBuilder.java,v $
 *  $Revision: 1.16 $  $Date: 2005-08-24 23:30:48 $ 
 */


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.cdm.Diagram;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.java.ISynchronizerListener;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 *   A JVE Compostion Model Builder provides an interface to an object
 *   that creates or store a composition model from/to an input file.  
 * 
 * @since 1.0.0
 */
public interface IDiagramModelBuilder  {
	

	public final static String MODEL_BUILDER_KEY = "IDiagramModelBuilder"; //$NON-NLS-1$
	/**
	 * A call to this API will reParse/reCreate a new JVE model
	 * 
	 * @param file to parse
	 * @param pm  
	 * @since 1.0.0
	 */
	public void loadModel(IFileEditorInput file, boolean useCache, IProgressMonitor pm) throws CodeGenException ;
	
	/**
	 * Get the root of the model.
	 * @return the model root. It will be <code>null</code> if not loaded, or in process but not yet ready.
	 * 
	 * @since 1.0.0
	 */
	public BeanSubclassComposition getModelRoot() ;
	
	/**
	 * Get the diagram for this model.
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public Diagram getDiagram();
	
	/**
	 * Answer the type name of the "this". For example if editing "xyz.Qxr" return "xyz.Qxr" as the this type name.
	 * @return typename of the "this".
	 * 
	 * @since 1.0.0
	 */
	public String getThisTypeName();
	

	/**
	 * If buttom up processing is about to, or is in progress a true
	 * will be returned

	 * @return
	 * 
	 * @since 1.0.0
	 */
	public boolean isBusy();

	/**
	 * Editor is about to shut down - cleanup
	 * 
	 * @since 1.0.0
	 */
	public void dispose() ;
 
	/**
	 * Signal start of a transaction. The UI thread is doing this. It is required that when the transaction
	 * is completed that commit() is call.ed
	 * 
	 * @see IDiagramModelBuilder#commit();
	 * @since 1.0.0
	 */
	public void startTransaction();
	
	/**
     * Denotes that a compound operation is completed.  This
     * will actually remove source code of deleted components, if needed,
     * and resume listening to source code changes.
     * <p>
     * This operation is synchroneous.
     * <p>
     * This operation MUST be called at end of transaction. 
	 * 
	 * @see IDiagramModelBuilder#startTransaction()
	 * @since 1.0.0
	 */
    public void commit() ;
    
    /** 
     * @deprecated  use commit()
     */
    public void commitAndFlush(ISynchronizerListener listener, String marker) ;
    
    /**
     * Set the delay from when user stops typing to when the user changes will be processed.
     * 
	 * @param delay will set up the delay time for JavaSourceSynchronizer
	 *         value less than <code>JavaSourceSynchronizer.DEFAULT_SYNC_DELAY</code> will be ignored.
	 */
	public void setSynchronizerSyncDelay(int delay) ;
			
	
    /**
	 * Disconnect, and stop synchronization
	 * .. leave the JVE model intact
     * 
     * @return <code>true</code> if pause could be performed.
     * 
     * @since 1.0.0
     */
    public boolean pause() ;

    /**
     * A diagram model builder listener can register for the following
     * notifications
     * 
     * @since 1.0.0
     */
    public interface IBuilderListener {
    	/**
    	 * Parsing status had changed, and the msg
    	 * should be placed on the status bar
     	 * 
    	 * @param msg
    	 * 
    	 * @since 1.0.0
    	 */
    	void statusChanged (String msg);
    	
    	/**
    	 * Can not perform snippet update; a reload is needed. 
    	 * 
    	 * 
    	 * @since 1.0.0
    	 */
		void reloadIsNeeded();
		
		/**
		 * Change status from/to can code be parsed
		 *  
		 * @param error <code>true</code> if parse error, <code>false</code> if parse error cleared.
		 * 
		 * @since 1.0.0
		 */
		void parsingStatus (boolean error);
		
		/**
		 *  Builder parsing was paused or resumed
		 * 
		 * @param paused <code>true</code> if parsing has been paused.
		 * 
		 * @since 1.0.0
		 */
		void parsingPaused(boolean paused) ;
		
		/**
		 * Bottom up has updated the model
		 * 
		 * @since 1.0.0
		 */
		void modelUpdated() ;
    } 
    
    /**
     * Add a listener.
     * 
     * @param l
     * 
     * @since 1.0.0
     */
    public void addIBuilderListener(IBuilderListener l);
    
    /**
     * Remove a listener.
     * 
     * @param l
     * 
     * @since 1.0.0
     */
    public void removeIBuilderListener(IBuilderListener l);

    /**
     * Denotes that a save operation is being executed.  An implementor may descide to 
     * store a processed cache, to improve a consequence load.
     * @param monitor
     * 
     * @since 1.0.0
     */
    public void doSave(IProgressMonitor monitor);
	
	/**
	 * If isBusy() wait until it is not
	 * @param cancelJobs <code>true</code> cancel any waiting parsing jobs because you are about to do a complete reparse. <code>false</code> if
     * any waiting jobs should complete normally.
	 * @since 1.1.0
	 */
	public void waitforNotBusy(boolean cancelJobs) ;
    
}


