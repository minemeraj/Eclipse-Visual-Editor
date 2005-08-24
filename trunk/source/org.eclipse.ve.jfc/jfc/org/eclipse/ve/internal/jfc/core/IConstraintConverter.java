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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: IConstraintConverter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

/**
 * This helper interface is used when a container's layout is changed
 * and the constraints for it's children need to be adjusted or added 
 * to work correctly in the new layout manager.
 *
 * Creation date: (10/9/00 5:33:16 PM)
 * @author: Peter Walker
 */
public interface IConstraintConverter {
/**
 * Return a compound command containing the constraint commands
 * for each of the container's children that were adjusted.
 * Creation date: (10/9/00 5:52:50 PM)
 */
Command getComponentConstraintsCommand();
/**
 * Set the container whose children's constraints need to be adjusted.
 * Creation date: (10/9/00 5:52:50 PM)
 */
void setContainer(IJavaObjectInstance aContainer);
}


