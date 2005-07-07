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
 * FloatVerifier.  Verify data input for Floats
 *
 * @author djo
 */
public class FloatValidator implements IValidator {
    
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.validator.IValidator#isValidPartialInput(java.lang.String)
	 */
	public String isValidPartialInput(String fragment) {
		if (fragment.matches("\\-?[0-9]*\\.?[0-9]*([0-9]+[e|E]\\-?([0-9]+\\.)?[0-9]*)?"))
            return null;
        else
            return getHint();
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
     */
    public String isValid(Object value) {
        try {
            Float.parseFloat((String)value);
            return null;
        } catch (Exception e) {
            return getHint();
        }
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.validator.IValidator#getHint()
	 */
	public String getHint() {
		return "Please enter a number like 1.234, " + Float.MIN_VALUE + ", or " + Float.MAX_VALUE + ".";
	}

}
