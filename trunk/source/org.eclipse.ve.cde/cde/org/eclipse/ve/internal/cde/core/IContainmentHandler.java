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
package org.eclipse.ve.internal.cde.core;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;


/*
 *  $RCSfile: IContainmentHandler.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-11 21:26:01 $ 
 */

/**
 * This is used to determine containment questions
 * about a child about to be added to a parent.
 * <p>
 * This will be returned from the IModelAdapterFactory.getAdapter.
 * If the child doesn't care, then there is no need of the
 * handler.
 * 
 * @since 1.0.0
 */
public interface IContainmentHandler extends IModelAdapter {

	/**
	 * No Add permitted exception.
	 * <p>
	 * This is thrown by {@link IContainmentHandler#contributeToRequest(Object, Object, CommandBuilder, CommandBuilder)}
	 * if the handler determines that the add/create should not occur. The message in the exception may be displayed to the
	 * user sometime in the future when such display capability is implemeted.
	 * 
	 * @since 1.2.0
	 */
	public static class NoAddException extends Exception {
		private static final long serialVersionUID = 1L;

		/**
		 * Construct with a message.
		 * <p>
		 * Some time in the future this msg may be displayed to user as to why Add/Create was not permitted by adapter.
		 * @param msg
		 * 
		 * @since 1.2.0
		 */
		public NoAddException(String msg) {
			super(msg);
		}
	}
	
	/**
	 * Handle contribution to create/add request.
	 * <p>
	 * The handler can do:
	 * <ol>
	 * <li><b>Nothing:</b> In which case it should just return the child as entered. This means it had nothing to contribute.</li>
	 * <li><b>Handle everything:</b> In which case it should return <code>null</code> and use the pre/post Cmd Builders to do it.</li>
	 * <li><b>Reject parent or some other error:</b> In which case it should throw the {@link NoAddException}. This is for when the parent is not valid for this child, or for some
	 * other reason the child could not be added as determined by this handler.</li>
	 * <li><b>Add to the request:</b> In which case it should return the child as entered. And use the pre/post Cmd builders to do the additions.</li>
	 * <li><b>Replace the request:</b> In which case it should return a different child. This different child is the one that will be added instead. In
	 * this case this different child <b>WILL NOT</b> have its IContainmentAdapter called on it. It will be assumed to be good and will be added. It 
	 * must be a valid child for the parent so that the parent ContainerPolicy can add it.
	 * <li><b>Combination of Add to request and replace the request:</b> In which case it would use the pre/post Cmd builders and return a different child.</li>
	 * </ol>
	 * <p>
	 * <b>Note:</b>Some important restrictions. For a create, at the time of the call, the child is not yet in any resourceSet. You should use either 
	 * edit domain or the parent to find the resourceSet. 
	 * @param parent parent being added to
	 * @param child child being added.
	 * @param preCmds CommandBuilder for commands to be executed before any of the child/children are added. Handler may add to this command builder any commands
	 * 	it wants to be executed before the actual adds.
	 * @param postCmds CommandBuilder for commands to be executed after all of the child/children are added. Handler may add to this command builder any commands
	 * 	it wants to be executed after the actual adds.
	 * @param creation <code>true</code> if this a creation request. <code>false</code> if this is an add request.
	 * @return child to add instead of (or the same child) as the child sent it, or <code>null</code> if no add is to be done. Though the command builders will still be added to the request.
	 * 	This child, if different than the one sent into the method, will not have an IContainmentHandler called against it. It is assumed that the
	 * 	child is fine and can be added. (The only check that will be done is if the child is a valid type for parent). Implementers must
	 *  handle if they changed the child for a non-creation required, they must decide if the child should be deleted because it should no longer
	 *  be in the model. If they decide that they must put in postCmd code to delete it. 
	 * @throws NoAddException if the handler determines that the child should not be added to the parent.
	 * 
	 * @since 1.2.0
	 */
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws NoAddException;
	
	
}
