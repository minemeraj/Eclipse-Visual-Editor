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
 *  $RCSfile: ICodeGenFlushController.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import org.eclipse.ve.internal.java.codegen.java.ISynchronizerListener;

/**
 * @author gmendel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface ICodeGenFlushController {

	/**
	 * Request a callback when for when CodeGeneration had finished to generate
	 * code into the Java Editor.  A call back will be issued after a transaction (if in progress)
	 * has completed, and when all changes are flushed into the shared IDocument.
	 * 
	 * 
	 * @param listener implements the call back to be called when the flush occured.
	 * @param marker is an optional designator of the call back.
     */
	public void flushCodeGen(ISynchronizerListener listener, String marker) ;
}
