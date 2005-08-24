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
 *  $RCSfile: NoNullsValidator.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:44:29 $ 
 */



import org.eclipse.jface.viewers.ICellEditorValidator;
/**
 * This is a validator which can wrapper another validator that
 * says nulls are invalid. It will first check to see if it is
 * a null, and if it is, it will return an invalid message.
 * If it is not a null, then it will pass onto the wrappered
 * validator (if any) and let it determine if the value is valid.
 */
public class NoNullsValidator implements IWrapperedValidator {
	protected ICellEditorValidator[] fWrapperedValidators;
	
	public NoNullsValidator() {
	}

	public NoNullsValidator(ICellEditorValidator validator) {
		fWrapperedValidators = validator != null ? new ICellEditorValidator[] {validator} : null;
	}
	
	public NoNullsValidator(ICellEditorValidator[] validators) {
		fWrapperedValidators = validators;
	}
	
	public ICellEditorValidator[] getValidators() {
		return fWrapperedValidators;
	}
	
	public void setValidators(ICellEditorValidator[] validators) {
		fWrapperedValidators = validators;
	}
	
	public String isValid(Object value){
		if (value == null)
			return PropertysheetMessages.null_invalid_WARN_;

		if (fWrapperedValidators != null) {			
			for (int i=0; i<fWrapperedValidators.length; i++) {
				if (fWrapperedValidators[i] != null) {
					String result = fWrapperedValidators[i].isValid(value);
					if (result != null && result.length() > 0)
						return result;	// One of them has stopped it
				}
			}
		}
		return null;
	}
	
}
