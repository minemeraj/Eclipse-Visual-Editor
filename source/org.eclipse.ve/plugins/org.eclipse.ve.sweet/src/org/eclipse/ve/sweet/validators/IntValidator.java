/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 */
package org.eclipse.ve.sweet.validators;

import org.eclipse.ve.sweet.validator.IValidator;


/**
 * DoubleVerifier.  Verify data input for Doubles
 *
 * @author djo
 */
public class IntValidator implements IValidator {
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#verifyFragment(java.lang.String)
	 */
	public String isValidPartialInput(String fragment) {
		if (fragment.matches("\\-?[0-9]*"))
		    return null;
        else
            return getHint();
	}
    
    public String isValid(Object value) {
        try {
            Integer.parseInt((String)value);
            return null;
        } catch (Throwable t) {
            return getHint();
        }
    }

	private String getHint() {
		return "Please enter a number between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE + ".";
	}

}
