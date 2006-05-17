/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
 *  $RCSfile: NumberLabelProvider.java,v $
 *  $Revision: 1.5 $  $Date: 2006-05-17 20:16:06 $ 
 */


import org.eclipse.jface.viewers.LabelProvider;

import com.ibm.icu.text.NumberFormat;
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
