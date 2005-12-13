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
package org.eclipse.ve.internal.java.remotevm.macosx;
/*
 *  $RCSfile: ProcessManager.java,v $
 *  $Revision: 1.2 $  $Date: 2005-12-13 01:05:41 $ 
 */

/**
 * This class manages the showing and hiding of the remote vm windows on Mac OS X.
 * This is accomplished by manipulating the front process via calls through a native library to
 * Carbon's ProcessManager functions.
 */
public class ProcessManager {
	
	static {
		System.loadLibrary("carbon-process-manager");
	}

	public static final native int GetCurrentProcess(int[] psn);
	public static final native int GetFrontProcess(int[] psn);
	public static final native int SetFrontProcess(int[] psn);
	
	private static int[] originalFrontProcess = null;
	private static boolean inFront = false;
	
	/**
	 * Bring the current process to the front.  This method will save the
	 * current front process, that will be restored with a call to processToBack();
	 * 
	 * This function must be followed with a call to processToBack() before being called again.
	 *
	 */
	public static void processToFront()	{
		if (!inFront) {
			// Get the process serial number of the current front process
			// This should be Eclipse's process id
			originalFrontProcess = new int[2];
			GetFrontProcess(originalFrontProcess);
			
			// Get the process serial number for the current process
			// This should be the java remote vm's process id
			int[] currentProcess = new int[2];		
			GetCurrentProcess(currentProcess);
			if (currentProcess[1] != 0) {
				// Set the java remote vm to be the front process
				SetFrontProcess(currentProcess);
				inFront = true;
			}
		}
	}
	
	/**
	 * Restore the old front process to the front, sending the current process to the back.
	 * 
	 * This function must follow a call to processToFront()
	 *
	 */
	public static void processToBack() {
		if (inFront) {
			if (originalFrontProcess != null && originalFrontProcess[1] != 0) {
				// Restore the original front process
				SetFrontProcess(originalFrontProcess);
				originalFrontProcess = null;
			}
			inFront = false;
		}
	}
}
