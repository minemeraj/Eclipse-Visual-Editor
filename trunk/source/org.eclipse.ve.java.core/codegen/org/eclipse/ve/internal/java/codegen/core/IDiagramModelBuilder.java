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
 *  $Revision: 1.4 $  $Date: 2004-03-17 13:46:40 $ 
 */


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.cdm.Diagram;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;
import org.eclipse.ve.internal.java.codegen.java.ISynchronizerListener;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 *   A JVE Compostion Model Builder provides an interface to an object
 *   that creates or store a composition model from/to an input file.  
 */
public interface IDiagramModelBuilder  {
	

	/**
	 * A call to this API will reParse/reCreate a new JVE model
	 * 
	 * @param file to parse
	 * @param pm  
	 * @since 1.0.0
	 */
	public void loadModel(IFileEditorInput file, IProgressMonitor pm) throws CodeGenException ;
	
	public BeanSubclassComposition getModelRoot() ;
	public Diagram getDiagram();
	

	/**
	 * If buttom up processing is about to, or is in progress a true
	 * will be returned
	 */
	public boolean isBusy();
	/**
	 * Editor is about to shut down - cleanup
	 */
	public void dispose() ;
 
	
    /**
     * Denotes that a compound operation is completed.  This
     * will actually remove source code of deleted components, if needed,
     * and resume listening to source code changes.
     * 
     * This operation is synchroneous.
     */
    public void commit() ;
    /** 
     * @deprecated  use commit()
     */
    public void commitAndFlush(ISynchronizerListener listener, String marker) ;
    
    /**
	 * @param delay will set up the delay time for JavaSourceSynchronizer
	 *         value less than JavaSourceSynchronizer.DEFAULT_SYNC_DELAY will be ignored.
	 */
	public void setSynchronizerSyncDelay(int delay) ;
			
	
	/**
	 * @deprecated
	 */    
    public void setMsgRenderer (IJVEStatus mr) ;
	/**
	 * Disconnect, and stop synchronization
	 * .. leave the JVE model intact
	 */    
    public boolean pause() ;
    
    /**
     * A diagram model builder listener can register for the following
     * notifications
     */
    public interface IBuilderListener {
    	/**
    	 * Parsing status had changed, and the msg
    	 * should be placed on the status bar
    	 */
    	void statusChanged (String msg);
    	/**
    	 * Can not perform snippet update; a reload is needed
    	 */
		void reloadIsNeeded(boolean flag);
		/**
		 * Change status from/to can code be parsed 
		 */
		void parsingStatus (boolean error);  
		/**
		 * Builder parsing was paused 
		 */
		void parsingPaused(boolean paused) ;
		/**
		 * Buttom up has updated the model
		 */
		void modelUpdated() ;
    }    
    public void addIBuilderListener(IBuilderListener l);
    public void removeIBuilderListener(IBuilderListener l);
    
    /**
     * @deprecated
     */
    public void pauseRoundTripping(boolean flag);
    
    
    
}


