package org.eclipse.ve.internal.java.remotevm;
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
 *  $RCSfile: WindowListener.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jem.internal.proxy.common.*;

public class WindowListener implements ICallback {
	
	IVMServer fServer;
	int fCallbackID;
	
/**
 * The listener initialize for callback server.
 */
public void initializeCallback(IVMServer server, int callbackID){
	fServer = server;
	fCallbackID = callbackID;
}	
/**
 * Invoked when the window is closed
 */
public void windowClosed() {
	if (fServer != null) {
		try {
			fServer.doCallback(new ICallbackRunnable() {
				public Object run(ICallbackHandler handler) throws CommandException {
					return handler.callbackWithParms(fCallbackID, 1 , null);
				}
			});
		} catch (CommandException exp) {
		}
	}
}
}