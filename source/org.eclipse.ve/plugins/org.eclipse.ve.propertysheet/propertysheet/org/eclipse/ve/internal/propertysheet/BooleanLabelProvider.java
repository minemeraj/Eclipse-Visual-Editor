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
 *  $RCSfile: BooleanLabelProvider.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:44:29 $ 
 */


import org.eclipse.jface.viewers.LabelProvider;
/**
 * This is a label provider for booleans. Not using
 * toString so that we can NLS the displayed string.
 */
public class BooleanLabelProvider extends LabelProvider {
	public String getText(Object element) {
		if (element instanceof Boolean)
			return ((Boolean) element).booleanValue() ? PropertysheetMessages.display_true : PropertysheetMessages.display_false;
			
		return super.getText(element);
	}	

}
