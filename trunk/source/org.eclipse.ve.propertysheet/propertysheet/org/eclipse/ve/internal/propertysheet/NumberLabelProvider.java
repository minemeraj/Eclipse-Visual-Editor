package org.eclipse.ve.internal.propertysheet;
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
 *  $RCSfile: NumberLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */


import org.eclipse.jface.viewers.LabelProvider;
import java.text.NumberFormat;
/**
 * This will format numbers in locale dependent format.
 * (e.g. 3,000 instead of 3000)
 */
public class NumberLabelProvider extends LabelProvider {
	NumberFormat fFormatter = NumberFormat.getInstance();
	
	public String getText(Object element) {
		if (element instanceof Number)
			return fFormatter.format(element);
			
		return super.getText(element);
	}	

}