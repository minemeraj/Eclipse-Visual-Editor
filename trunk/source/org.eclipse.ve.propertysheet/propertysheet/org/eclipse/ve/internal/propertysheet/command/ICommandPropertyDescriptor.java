package org.eclipse.ve.internal.propertysheet.command;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ICommandPropertyDescriptor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */


import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.*;

/**
 * When it is time to set the value of something from 
 * this descriptor, the property sheet can ask this 
 * interface to actually return a command that sets the value. 
 * That way depending upon the type of property ,
 * a different command can be returned, instead of 
 * always using the same command.
 */
public interface ICommandPropertyDescriptor extends IPropertyDescriptor {
	/**
	 * Return a command which will set the value
	 * on the source.
	 *
	 * Creation date: (6/9/00 1:52:56 PM)
	 * @return com.ibm.vcf.commands.ICommand
	 * @param setValue java.lang.Object
	 */
	public Command setValue(IPropertySource source, Object setValue);
	
	/**
	 * Return a command which will reset the value
	 * on the source. 
	 *
	 * Creation date: (6/9/00 1:52:56 PM)
	 * @return com.ibm.vcf.commands.ICommand
	 * @param setValue java.lang.Object
	 */
	public Command resetValue(IPropertySource source);
}
