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
 *  $RCSfile: DefaultTransientErrorHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import java.text.MessageFormat;
import java.util.Hashtable;

import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;

/**
 * @version 	1.0
 * @author
 */
public class DefaultTransientErrorHandler implements ITransientErrorListener {

   IJVEStatus fMr = null ;
   Hashtable    fEvents = new Hashtable () ;

   public DefaultTransientErrorHandler(IJVEStatus mr) {
       this.fMr = mr ;
   }

	public void display(ITransientErrorEvent event) {
		JavaVEPlugin.log("DefaultTransientError:", MsgLogger.LOG_FINE); //$NON-NLS-1$
		JavaVEPlugin.log("\t\tMessage:"+event.getMessage(), MsgLogger.LOG_FINE); //$NON-NLS-1$
		JavaVEPlugin.log("\t\tErrorType:"+event.getErrorType(), MsgLogger.LOG_FINE); //$NON-NLS-1$
		JavaVEPlugin.log("\t\tRefObject:"+event.getRefObject(), MsgLogger.LOG_FINE); //$NON-NLS-1$
	}

	/*
	 * @see ITransientErrorListener#correctionOccured(TransientErrorEvent)
	 */
	public void correctionOccured(ITransientErrorEvent event) {
        if (fMr != null)
           fMr.showMsg(null,IJVEStatus.NORMAL_MSG) ;        
		display(event);
	}

	/*
	 * @see ITransientErrorListener#errorOccured(TransientErrorEvent)
	 */
	public void errorOccured(ITransientErrorEvent event) {
        if (fMr != null)
        fMr.showMsg(MessageFormat.format(CodegenMessages.getString("TransientErrorHandler.MsgRenderer.TransientError"), new Object[]{event.getMessage()}),IJVEStatus.ERROR_MSG) ; //$NON-NLS-1$
    //    fEvents.put(event.getElementHandle(), event)
		display(event);
	}

}
