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
 *  $RCSfile: IContainmentHandler.java,v $
 *  $Revision: 1.5 $  $Date: 2005-09-19 20:44:13 $ 
 */

/**
 * This is used to determine containment questions
 * about a child about to be added to a parent.
 *
 * This will be returned from the IModelAdapterFactory.getAdapter.
 * If the child doesn't care, then there is no need of the
 * handler, and it should return null.
 */
public interface IContainmentHandler {

	/**
	 * Return whether the parent is valid for this child.
	 * It is possible that the child is valid for the parent,
	 * but the parent is not acceptable to the child.
	 * <p>
	 * This will be called if the child is of a valid type for the parent.
	 * 
	 * @param parent
	 * @return 
	 * 
	 * @since 1.0.0
	 */
	public boolean isParentValid(Object parent);
	
}
