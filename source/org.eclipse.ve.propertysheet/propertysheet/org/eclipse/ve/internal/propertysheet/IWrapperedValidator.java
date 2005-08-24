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
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: IWrapperedValidator.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */



import org.eclipse.jface.viewers.ICellEditorValidator;
/**
 * This is a special validator that can wrapper several validators. It allows
 * the combination of validators. This could be useful when several things
 * need to be checked for and don't want to code them all in one.
 * It allows access to the wrappered validators so that they can be touched,
 * such as they may be ISourced and so we need to set the ISourced onto all of them.
 * 
 * Note: The implementation could itself be a complete validator, but this won't
 * be returned in the list of validators.
 */

public interface IWrapperedValidator extends ICellEditorValidator {
	
	/**
	 * Return the array of validators.
	 * Note: It may return null, meaning no wrappered validators. This 
	 * could happen if the implementation does its own validatation. That
	 * way it may validate it and pass it on to any of its wrappered validators.
	 */
	public ICellEditorValidator[] getValidators();
	public void setValidators(ICellEditorValidator[] validators);

}
