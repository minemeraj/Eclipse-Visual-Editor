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
/*
 *  $RCSfile: IConstraintHandler.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */

/**
 * This is used by VisualInfoXYLayoutEditPolicy to interact with the child edit part
 * to handle the size portion of the constraint.
 *
 * It is retrieved from the getAdapter against the editpart. If the editpart doesn't
 * wish to participate in size, then it should return null. In this case the component
 * will not be resizable. The size will be whatever the figure defaults to for preferred size.
 *
 * This handler should also be returned from the getModelAdapter (see IModelAdapterFactory.getAdapter)
 * to handle new objects. What to implement in this case is shown in each method by the
 * indication "For create:" If there is no such indication, then it should do the default case,
 * such method won't be called.
 */
import org.eclipse.gef.commands.Command;
public interface IConstraintHandler {
	
	/**
	 * isResizeable
	 *
	 * Return true if the resize handles should be applied to the figure.
	 * This means that the user can initiate resize requests through direct interaction.
	 *
	 * For create: Return true if it can be sized, either through the VisualInfo or it persists size itself.
	 */
	public boolean isResizeable();
	
	/**
	 * contributeOrphanChildCommand
	 *
	 * This will be called on orphan child requests only.
	 * Return null if nothing to contribute. 
	 */
	public Command contributeOrphanChildCommand();

	/**
	 * contributeFigureSize
	 *
	 * The entire constraint in figure coordinates is passed in. The implementation
	 * can then modify the size. This is used just before being applied to figure. 
	 * It can be used to modify the size to handle zooming. Only changes to the size
	 * portion are honored. Changes to the position are ignored.
	 *
	 * For create: It won't be called.
	 */
	public void contributeFigureSize(org.eclipse.draw2d.geometry.Rectangle figureConstraint);
	
	/**
	 * contributeSizeCommand
	 *
	 * This will be called on resize requests only. The size in model coordinates will be passed in.
	 * This will be the size calculated from the resize request.
	 * If the component wishes to contribute a size change request, it should return the
	 * command. If not, it should return null. If it returns null, then the entire constraint
	 * will be stored in the visual info. If it doesn't return null, then the size portion
	 * of the constraint in the visual info will be set to (-1, -1).
	 *
	 * If size is not persisted in the component itself, it should return null.
	 *
	 * For create: If it persists size in the model, it should return a command, else return null.
	 */
	public Command contributeSizeCommand(int width, int height, EditDomain domain);
	
	/**
	 * contributeModelSize
	 *
	 * The entire constraint in model coordinates is passed in. The implementation
	 * can then modify the size. It should get the size out of its persistent storage,
	 * which is usually a property on the component. This is used to refresh the constraint,
	 * either the first time it is displayed, or if the zoom has changed. It allows the
	 * component to return the size to use.  Only changes to the size
	 * portion are honored. Changes to the position are ignored.
	 *
	 * If size is not persisted in the component itself, it should do nothing.
	 *
	 * For create: This isn't called.
	 */
	public void contributeModelSize(org.eclipse.ve.internal.cdm.model.Rectangle modelConstraint);
	
	/**
	 * addConstraintHandlerListener
	 *
	 * Add a constraint handler listener. If size is not persisted in the component then
	 * this method should do nothing with the listener.
	 *
	 * For create: This isn't called.
	 */
	public void addConstraintHandlerListener(IConstraintHandlerListener listener);
	
	/**
	 * removeConstraintHandlerListener
	 *
	 * remove a constraint handler listener. If size is not persisted in the component then
	 * this method should do nothing with the listener.
	 *
	 * For create: This isn't called.
	 */
	public void removeConstraintHandlerListener(IConstraintHandlerListener listener);
	
	
	/**
	 * Interface for handling constraint changes from the component.\
	 */
	public interface IConstraintHandlerListener {
		
		/**
		 * sizeChanged
		 *
		 * This is to be called by the component whenever the persistent size has changed.
		 * Wherever it may be stored. It is used by the policy to know that the size has
		 * changed and it needs to set the constraint on the figure with the new size.
		 */
		public void sizeChanged(int newWidth, int newHeight);
	}
}
