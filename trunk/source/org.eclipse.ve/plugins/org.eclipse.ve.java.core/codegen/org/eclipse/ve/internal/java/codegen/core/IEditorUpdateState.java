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
/*
 *  $RCSfile: IEditorUpdateState.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.codegen.core;
 
/**
 * 
 * @since 1.0.0
 */
public interface IEditorUpdateState {
	/**
	 * The purpose of this state is to notify the top down that a
	 * a buttom up processing has started/completed.  
	 * If true, buttom up assumes full update control; no top down processing is allowed. 
	 *  
	 * @param flag
	 * 
	 * @since 1.0.0
	 */
	public void setBottomUpProcessing(boolean flag);
	
	/**
	 * Returns whether buttom up processing is in progress.  
	 * 
	 * @return  true in which case update control is assumed with Buttom up processing.
	 * 
	 * @since 1.0.0
	 */
	public boolean isBottomUpProcessing();
	
	public int getButtomUpProcessingCount();
	
	/**
	 * Change the collecting delta state.  Bottom up processing will notify here when it
	 * starts to/finishes to collect source update deltas
	 * @param flag
	 * 
	 * @since 1.0.0
	 */
	public void setCollectingDeltas(boolean flag);
	
	public boolean isCollectingDeltas();
	
	/**
	 * Returns whether the UI is in transaction/in the middle of a command.
	 * The UI updates this flag using the setGUIUpdating(boolean) API on this 
	 * interface. When this value is true, the UI is in the middle of a command,
	 * and document events should not be processed.
	 * 
	 * @return  Whether the UI is in the middle of a command
	 * @see IEditorUpdateState#setTopDownProcessing(boolean)
	 * @see IDiagramModelBuilder#startTransaction()
	 * 
	 * @since 1.0.0
	 */
	public boolean isTopDownProcessing();
	/**
	 * The purpose of this state is to notify the buttom that a top down processing has started/completed.  
	 * If true, Top down update control; no buttom up processing is allowed. 
	 *  
	 * 
	 * @param updating
	 * @see IEditorUpdateState#isTopDownProcessing()
	 * @see IDiagramModelBuilder#startTransaction()
	 * @see IDiagramModelBuilder#commit()
	 * @since 1.0.0
	 */
	public void setTopDownProcessing(boolean updating);
}
