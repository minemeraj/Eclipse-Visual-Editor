package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: IModelChangeController.java,v $
 *  $Revision: 1.3 $  $Date: 2004-04-01 21:25:25 $ 
 */

/**
 * This interface is used whenever the model needs to
 * be changed. It is required that changes go through here
 * so that the model and other changes are synchronized correctly.
 * 
 * This interface is not meant to be implemented by users. 
 * It is stored in the domain for usage. The key stored in this
 * interface can be used to retrieve it from the domain.
 */
public interface IModelChangeController {
	
	public static final String MODEL_CHANGE_CONTROLLER_KEY = "org.eclipse.ve.internal.cde.core.IModelChangeController"; //$NON-NLS-1$
	
	/**
	 * Ready to process changes state.
	 */
	public static final int READY_STATE = 0;
	
	/**
	 * Change controller in busy state. Will not accept changes.
	 */
	public static final int BUSY_STATE = 1;
	
	/**
	 * Generic no update permitted state (may be read-only, or it could be some error).
	 */
	public static final int NO_UPDATE_STATE = 2;
	
	/**
	 * Call this method with a runnable. The runnable will
	 * do the actual update of the model. This method will
	 * make sure that the updates are blocked correctly to
	 * the text editor, for instance.
	 * 
	 * RunExceptions will not be squelched, they will be returned,
	 * 
	 * @param runnable - The runnable to execute.
	 * @param updatePS - Whether the property sheet should be updated when this
	 *                   is done executing successfully. When being called from
	 *                   the command stack, this should be false.
	 * @return It will return whether the runnable could be executed. For
	 *         example if the file was read-only and could not be checked out,
	 *         the update won't occur.
	 */
	public boolean run(Runnable runnable, boolean updatePS); 
	
	/**
	 * This method provides the ability to hold or resume changes to the model.
	 * @param flag when true, will hold (inhibit) changes.
	 * @param reasonMsg optional message denoting a reason for the hold request.
	 * 
	 * Note: as we go forwared, we should think of allowing multiple entities to
	 *       call setHoldChanges with some type of caller signiture, so that only when
	 *       all entities have clear the hold, would the controller keep on going.
	 * @deprecated It will query from model builder instead.
	 */
	public void setHoldChanges(boolean flag, String reasonMsg) ;
	
	/**
	 * To set it to a particular hold state.
	 * 
	 * @param stateFlag state to set it to. If <code>READY_STATE</code>, the msg will be ignored and will be reset to <code>null</code>.
	 * @param msg a msg to associate with the hold state. If <code>null</code>, then use a default msg.
	 * 
	 * @since 1.0.0
	 */
	public void setHoldState(int stateFlag, String msg);
	
	/**
	 * Return the status of the hold state
	 * @deprecated get hold state instead
	 */
	public boolean isHoldChanges() ;
	
	/**
	 * Get the hold state. There are some states that are provided by the standard interface, but
	 * the model controller implementation can provide more.
	 * 
	 * @return current state
	 * 
	 * @see IModelChangeController#READY_STATE
	 * @see IModelChangeController#BUSY_STATE
	 * @see IModelChangeController#NO_UPDATE_STATE
	 * @since 1.0.0
	 */
	public int getHoldState();
	

	/**
	 * Return the hold msg associated with the current hold state, or <code>null</code> if ready state.
	 * @return msg or <code>null</code> if in ready state.
	 * 
	 * @since 1.0.0
	 */
	public String getHoldMsg();
	
	/**
	 * Tests to see if the model controller is currently processing a transaction
	 * @return true if in a transaction
	 */
	public boolean inTransaction();
	
}
