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
 *  $RCSfile: IDiagramSourceDecoder.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 *   Implementors will build a Composition Model from a given source file. Also
 *   the model could be saved back.
 */

public interface IDiagramSourceDecoder {

	public final static String JAVAExt =   "java" ;     // JAVA file ; //$NON-NLS-1$
	

	/**
     *  Decode the input source file
     * 
	 * @param sourceFile
	 * @param pm
	 * @return  true, if decode is completed, false, if the decode was spawned into a different
	 *          thread.
	 * @throws CodeGenException
	 */
	public boolean	decodeDocument (IFile sourceFile, IProgressMonitor pm) throws CodeGenException ;
	
	// What file extention does this decoder works with
	public String  getFileExt() ;
	
	public void dispose() ;

	/**
	 * @param delay will set up the delay time for JavaSourceSynchronizer
	 *         value less than JavaSourceSynchronizer.DEFAULT_SYNC_DELAY will be ignored.
	 */
	public void setSynchronizerSyncDelay(int delay) ;
	    
    // Disconnect from external resources (e.g., Java Working Copy)
	// if clearModel is true, the VCE model will be cleared
	public void disconnect(boolean clearModel) ;  
	// Will re-load a previously disconnected model.
	public void reconnect(IFileEditorInput input,IProgressMonitor pm) throws CodeGenException;		
	
}


