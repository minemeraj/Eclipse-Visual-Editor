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
 *  $RCSfile: INeedData.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */


/**
 * Objects that implement this interface want to
 * know their data (the data comes from the property sheet entry). These are sometimes needed to
 * perform validations or determining valid selections
 * in a combobox depending on the data, or how to create the result.
 *
 * Currently this is checked for on CellEditors and CellEditorValidators.
 */
public interface INeedData {
	/**
	 * Set the data. 
	 */
	public void setData(Object data);
}
