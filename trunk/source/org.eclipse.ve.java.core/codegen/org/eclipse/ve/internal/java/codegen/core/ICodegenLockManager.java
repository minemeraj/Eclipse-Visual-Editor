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
 *  $Revision: 1.2 $  $Date: 2004-03-30 14:42:55 $ 
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
	
	/**
	 * Returns whether the UI is in transaction/in the middle of a command.
	 * The UI updates this flag using the setGUIUpdating(boolean) API on this 
	 * interface. When this value is true, the UI is in the middle of a command,
	 * and document events should not be processed.
	 * 
	 * @return  Whether the UI is in the middle of a command
	 * @see ICodegenLockManager#setGUIUpdating(boolean)
	 * @see IDiagramModelBuilder#startTransaction()
	 * 
	 * @since 1.0.0
	 */
	public boolean isGUIUpdating();
	/**
	 * Updates the status of the UI command execution. When set to true, the
	 * UI is beginning the execution of commands and no source to UI updates 
	 * should take place. When it is set to false, all UI to source commands have
	 * been executed, and source to UI updates can proceed. This is called by 
	 * the UI from the IDiagramModelBuilder#startTransaction() via the 
	 * JavaSourceTranslator. 
	 * 
	 * @param updating
	 * @see ICodegenLockManager#isGUIUpdating()
	 * @see IDiagramModelBuilder#startTransaction()
	 * @see IDiagramModelBuilder#commit()
	 * @since 1.0.0
	 */
	public void setGUIUpdating(boolean updating);
}
