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
 *  $Revision: 1.1 $  $Date: 2004-02-23 19:55:30 $ 
 */
package org.eclipse.ve.internal.java.codegen.core;

/**
 * 
 * @since 1.0.0
 */
public class CodegenLockManager implements ICodegenLockManager {

	private int readonlyRequestCount = 0;
	private boolean threadScheduled = false;

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
	public boolean isThreadScheduled() {
		return threadScheduled;
	}

}
