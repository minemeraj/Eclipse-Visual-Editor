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
 *  $RCSfile: DefaultWrapperedValidator.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */



import org.eclipse.jface.viewers.ICellEditorValidator;
/**
 * This is a default implementation for WrapperedValidators
 * that can be used.
 */

public class DefaultWrapperedValidator implements IWrapperedValidator {
	protected ICellEditorValidator[] fValidators;

	public DefaultWrapperedValidator() {
	}
	
	public DefaultWrapperedValidator(ICellEditorValidator[] validators) {
		fValidators = validators;
	}
	
	public ICellEditorValidator[] getValidators(){
		return fValidators;
	}
	
	public void setValidators(ICellEditorValidator[] validators){
		fValidators = validators;
	}
	
	public String isValid(Object value){
		if (fValidators != null) {
			for (int i=0; i<fValidators.length; i++) {
				if (fValidators[i] != null) {
					String result = fValidators[i].isValid(value);
					if (result != null && result.length() > 0)
						return result;	// One of them has stopped it
				}
			}
		}
		return null;
	}
	
}