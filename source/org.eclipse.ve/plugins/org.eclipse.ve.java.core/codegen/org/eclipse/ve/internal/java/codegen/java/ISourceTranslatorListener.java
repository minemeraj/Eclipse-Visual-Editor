package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: ISourceTranslatorListener.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
/**
 * @version 	1.0
 * @author
 * 
 * 
 */
public interface ISourceTranslatorListener {
    
    // Denotes that a source delta updated 
    // the JVE model.  This callback will be called on the
    // UI thread, after a call to snippetProcessingCompleted()
    void  modelUpdated() ;
    // Snippet work has been queued. will not be called on UI thread.
    // Note: multiple calls may be made before a single complete is called.
    void  snippetProcessingStart() ;
    // Snippet work has been completed, may not be called on UI thread.
    void  snippetProcessingCompleted () ;

}
