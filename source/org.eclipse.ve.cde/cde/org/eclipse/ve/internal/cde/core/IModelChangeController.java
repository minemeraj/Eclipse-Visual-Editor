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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
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
	 */
	public void setHoldChanges(boolean flag, String reasonMsg) ;
	
	/**
	 * Return the status of the hold state
	 */
	public boolean isHoldChanges() ;
	
	/**
	 * Return the current hold msg.
	 */
	public String getHoldMsg() ;
	
	/**
	 * Tests to see if the model controller is currently processing a transaction
	 * @return true if in a transaction
	 */
	public boolean inTransaction();
	
}
