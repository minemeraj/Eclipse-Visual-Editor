/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.visual;
/*
 *  $RCSfile: ILayoutSwitcher.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-27 15:34:10 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

/**
 * This helper interface is used when a container's layout is changed
 * and the constraints for it's children need to be adjusted or added 
 * to work correctly in the new layout manager.
 *
 * Creation date: (10/9/00 5:33:16 PM)
 * @author: Peter Walker
 */

public interface ILayoutSwitcher {

	
	/**
	 * Return a command containing the command(s) to handle
	 * the possible constraints for each of the
	 * container's children that are to be adjusted to this layout manager
	 * and applying the layout manager setting.
	 */
	public Command getCommand(EStructuralFeature sf, IJavaObjectInstance newManager);
	
	/**
	 * Get the command to cancel the layout manager setting, the default layout manager
	 * proxy will be sent in to work with.
	 * 
	 * Return a command containing the command(s) to handle
	 * the possible constraints for each of the
	 * container's children that are to be adjusted to this default layout manager
	 * and canceling the layout manager setting.
	 */
	public Command getCancelCommand(EStructuralFeature sf, IBeanProxy defaultManager);	
}
