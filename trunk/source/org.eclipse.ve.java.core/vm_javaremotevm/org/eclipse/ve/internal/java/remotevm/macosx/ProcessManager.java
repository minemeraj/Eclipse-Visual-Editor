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
 *  $Revision: 1.1 $  $Date: 2005-12-12 22:52:44 $ 
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
			if (currentProcess[0] != 0) {
				// Set the java remote vm to be the front process
				SetFrontProcess(currentProcess);
				inFront = true;
			}
		}
	}
	
	public static void processToBack() {
		if (inFront) {
			if (originalFrontProcess != null && originalFrontProcess[0] != 0) {
				// Restore the original front process
				SetFrontProcess(originalFrontProcess);
				originalFrontProcess = null;
			}
			inFront = false;
		}
	}
}
