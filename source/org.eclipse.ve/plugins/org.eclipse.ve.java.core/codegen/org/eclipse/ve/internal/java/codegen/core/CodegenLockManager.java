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
 *  $RCSfile: CodegenLockManager.java,v $
 *  $Revision: 1.5 $  $Date: 2005-02-15 23:28:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.core;

/**
 * 
 * @since 1.0.0
 */
public class CodegenLockManager implements ICodegenLockManager {

	private int readonlyRequestCount = 0;
	private boolean threadScheduled = false;
	private boolean isGUIUpdating = false;

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager#setGUIReadonly(boolean)
	 */
	public void setGUIReadonly(boolean flag) {
		if(flag){
			readonlyRequestCount ++;
		}else{
			if(readonlyRequestCount>0)
				readonlyRequestCount --;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager#isGUIReadonly()
	 */
	public boolean isGUIReadonly() {
		return readonlyRequestCount > 0;
	}
	
	/**
	 * Should be called by the one API which cleans 
	 * all maintainance of the snippet update process.
	 * This will the UI again, as it depends on the 
	 * #isGUIReadonly() API.
	 * 
	 * @since 1.0.0
	 */
	public void resetGUIReadOnly(){
		readonlyRequestCount = 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager#setThreadScheduled(boolean)
	 */
	public synchronized void setThreadScheduled(boolean flag) {
		threadScheduled = flag;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager#isThreadScheduled()
	 */
	public synchronized boolean isThreadScheduled() {
		return threadScheduled;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager#isGUIUpdating()
	 */
	public boolean isGUIUpdating() {
		return isGUIUpdating;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager#setGUIUpdating(boolean)
	 */
	public void setGUIUpdating(boolean updating) {
		isGUIUpdating = updating;
	}
}
