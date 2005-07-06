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
package org.eclipse.ve.sweet.validator;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * IIValidator.  Verify data entry for some value or data type.  IValidator
 * adds the constraint that the "value" parameter to isValid will always be
 * a java.lang.String. 
 */
public interface IValidator extends ICellEditorValidator {
    /**
     * Returns a string indicating whether the given partially-entered input
     * value is valid (ie: if the current keystroke should be accepted).
     * 
     * @param fragment The current input fragment string
     * @return the error message, or <code>null</code> indicating
     * that the value is valid
     */
    public String isValidPartialInput(String fragment);
    
}
