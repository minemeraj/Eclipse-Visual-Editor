/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodegenLockManager.java,v $
 *  $Revision: 1.3 $  $Date: 2004-04-02 16:34:23 $ 
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
			readonlyRequestCount --;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager#isGUIReadonly()
	 */
	public boolean isGUIReadonly() {
		return readonlyRequestCount > 0;
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
