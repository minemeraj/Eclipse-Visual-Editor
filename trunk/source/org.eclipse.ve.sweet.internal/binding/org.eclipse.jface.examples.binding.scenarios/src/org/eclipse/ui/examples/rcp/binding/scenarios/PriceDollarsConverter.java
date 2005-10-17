/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PriceDollarsConverter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-17 13:20:06 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.binding.IConverter;

public class PriceDollarsConverter implements IConverter {
	
	private double cents;
	public Class getModelType() { return Integer.TYPE; }
	public Class getTargetType() { return Double.TYPE; }
	public Object convertModel(Object object) {
		// Argument is an Integer representing the dollar portion.  Add to cents to make the new price
		double newPrice = cents + ((Integer)object).intValue();
		return new Double(newPrice);
	}
	public Object convertTarget(Object object){
		// Argument is a Double representing the price.  Return dollars only and remember the cents
		Double price = (Double)object;
		int dollars = price.intValue();
		cents = price.doubleValue() - dollars;
		return new Integer(dollars);						
	}
}
