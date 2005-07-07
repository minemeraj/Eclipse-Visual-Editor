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
package org.eclipse.ve.sweet.validators.reusable;

import org.eclipse.ve.sweet.validator.IValidator;

/**
 * ReadOnlyValidator.  The validator that is used for read-only fields.
 *
 * @author djo
 */
public class ReadOnlyValidator implements IValidator {
    
    private static ReadOnlyValidator singleton = null;
    
    public static ReadOnlyValidator getDefault() {
        if (singleton == null) {
            singleton = new ReadOnlyValidator();
        }
        return singleton;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.verifier.IVerifier#verifyFragment(java.lang.String)
	 */
	public String isValidPartialInput(String fragment) {
		// No changes are allowed
		return getHint();
	}

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
     */
    public String isValid(Object value) {
        // The current value is always valid
        return null;
    }

	public String getHint() {
		return "No changes are allowed in this field";
	}

}
