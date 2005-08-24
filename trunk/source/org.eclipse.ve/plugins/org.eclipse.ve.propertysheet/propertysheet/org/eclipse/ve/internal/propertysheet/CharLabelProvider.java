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

import org.eclipse.jface.viewers.LabelProvider;

/**
 * Label provider for Character/char values.
 */
public class CharLabelProvider extends LabelProvider {

    /**
     * Returns a Character as a string.
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    public String getText(Object element) {
        if(element instanceof Character) {
            return ((Character) element).toString();
        }
        return super.getText(element);
    }
    
}
