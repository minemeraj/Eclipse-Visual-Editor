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
 *  $RCSfile: ICodegenLockManager.java,v $
 *  $Revision: 1.1 $  $Date: 2004-02-23 19:55:30 $ 
 */
package org.eclipse.ve.internal.java.codegen.core;
 
/**
 * 
 * @since 1.0.0
 */
public interface ICodegenLockManager {
	/**
	 * GUI read-only lock.
	 * The purpose of this lock is to not disable any GUI (Top-down) changes 
	 * while the source changes (Bottom-up) are taking place. Before accepting 
	 * document event changes, this flag is turned on. It is turned off subsequently
	 * for every document event processed in the background. The lock gets 
	 * out of the read-only state when a false flag has been set in the background
	 * for every document event.
	 *  
	 * @param flag
	 * 
	 * @since 1.0.0
	 */
	public void setGUIReadonly(boolean flag);
	
	/**
	 * Returns whether the GUI read-only lock is enabled or not. The lock is enabled
	 * if a document event has set the flag to be true, but hasnt set it back to false.
	 * 
	 * @return  Whether the GUI read-only lock is enabled or not
	 * 
	 * @since 1.0.0
	 */
	public boolean isGUIReadonly();
	
	/**
	 * Thread scheduled lock.
	 * The purpose of this lock is to  synchronize the Synchronizer and the workers it 
	 * launches. 
	 * @param flag
	 * 
	 * @since 1.0.0
	 */
	public void setThreadScheduled(boolean flag);
	
	public boolean isThreadScheduled();
	
}
