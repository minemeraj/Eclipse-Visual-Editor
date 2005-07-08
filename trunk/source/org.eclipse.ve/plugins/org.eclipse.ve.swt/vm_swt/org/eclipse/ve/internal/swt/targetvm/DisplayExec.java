/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DisplayExec.java,v $
 *  $Revision: 1.3 $  $Date: 2005-07-08 17:51:50 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.proxy.common.*;
import org.eclipse.jem.internal.proxy.common.ICallback;
import org.eclipse.jem.internal.proxy.common.IVMCallbackServer;
 
/**
 * This is the callback for doing Display.asyncExec or Display.syncExec from the 
 * IDE side so that any calls to the remote side during callback will run in the
 * UI thread.
 * 
 * @since 1.0.0
 */
public class DisplayExec implements ICallback {

	private IVMCallbackServer vmServer;
	private int callbackID;
	
	/**
	 * Callback message ids to tell the the exec should run, or that the exec is finished and
	 * that the callback can be released.
	 * 
	 * TODO These need to go into a Common interface class that is available to both IDE and VM.
	 */
	public static final int RUN_EXEC = 0;
	
	/**
	 * Create the DisplayExec.
	 * 
	 * @since 1.0.0
	 */
	public DisplayExec() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.common.ICallback#initializeCallback(org.eclipse.jem.internal.proxy.common.IVMServer, int)
	 */
	public void initializeCallback(IVMCallbackServer vmServer, int callbackID) {
		this.vmServer = vmServer;
		this.callbackID = callbackID;
	}
	
	/**
	 * Do a display.syncExec on the given display. This will not return until
	 * the runnable has completed.
	 * <p>
	 * Unlike true syncExec, this will allow a value to be returned. This will make
	 * this very convienent to use.
	 * 
	 * @param display The display or <code>null</code> to use default display.
	 * @return Object returned from runnable.
	 * 
	 * @since 1.0.0
	 */
	public Object syncExec(Display display) {
		if (display == null)
			display = Display.getDefault();
		ExecRunnable runnable = new ExecRunnable();
		display.syncExec(runnable);
		return runnable.getResult();
	}

	/**
	 * Do a display.asyncExec on the given display. This will return immediately,
	 * even if runnable has not yet run.
	 * 
	 * @param display The display or <code>null</code> to use default display.
	 * 
	 * @since 1.0.0
	 */
	public void asyncExec(Display display) {
		display.asyncExec(new ExecRunnable());
	}	

	/*
	 * The syncExec or asyncExec runnable which calls back to the IDE to let it process proxy commands
	 * in the UI thread.
	 * 
	 * @since 1.0.0
	 */
	private class ExecRunnable implements Runnable {
		
		private Object result;
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				result = vmServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(callbackID, RUN_EXEC, new Object[] {Display.getCurrent()});
					}
				});
			} catch (CommandException e) {
				e.printStackTrace();
			}
		}

		public Object getResult() {
			return result;
		}
	}
}
