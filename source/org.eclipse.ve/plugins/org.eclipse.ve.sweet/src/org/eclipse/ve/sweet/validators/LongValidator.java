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
 * LongValidator.  Verify data input for Longs
 *
 * @author djo
 */
public class LongValidator implements IValidator {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.validator.IValidator#isValidPartialInput(java.lang.String)
	 */
	public String isValidPartialInput(String fragment) {
		if (fragment.matches("\\-?[0-9]*"))
            return null;
        else
            return getHint();
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
        try {
            Long.parseLong((String)value);
            return null;
        } catch (Throwable t) {
            return getHint();
        }
    }    

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.validator.IValidator#getHint()
	 */
	public String getHint() {
		return "Please enter a number between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE + ".";
	}

}
