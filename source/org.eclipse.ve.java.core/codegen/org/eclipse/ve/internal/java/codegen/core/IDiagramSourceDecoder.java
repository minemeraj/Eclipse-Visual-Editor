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
 *  $Revision: 1.3 $  $Date: 2004-03-16 20:55:59 $ 
 */

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IFileEditorInput;

import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 *   Implementors will build a Composition Model from a given source file. Also
 *   the model could be saved back.
 */

public interface IDiagramSourceDecoder {

	public final static String JAVAExt =   "java" ;     // JAVA file ; //$NON-NLS-1$
	

    // Decode the imput source
	public void	decodeDocument (IFile sourceFile, IProgressMonitor pm) throws CodeGenException ;
	
	// What file extention does this decoder works with
	public String  getFileExt() ;
	
	public void dispose() ;

	/**
	 * @param delay will set up the delay time for JavaSourceSynchronizer
	 *         value less than JavaSourceSynchronizer.DEFAULT_SYNC_DELAY will be ignored.
	 */
	public void setSynchronizerSyncDelay(int delay) ;
	

    /**
     * @deprecated
     */
    public void setMsgRenderer (IJVEStatus mr)  ;
    
    // Disconnect from external resources (e.g., Java Working Copy)
	// if clearModel is true, the VCE model will be cleared
	public void disconnect(boolean clearModel) ;  
	// Will re-load a previously disconnected model.
	public void reconnect(IFileEditorInput input,IProgressMonitor pm) throws CodeGenException;		
	
}


