/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementatiobn
 */
package org.eclipse.ve.sweet.converters;

import org.eclipse.ve.sweet.converter.IConverter;

/**
 * ConvertString2Boolean.
 *
 * @author djo
 */
public class ConvertString2Boolean implements IConverter {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.converter.IConverter#convert(java.lang.Object)
	 */
	public Object convert(Object source) {
        String s = (String) source;
        if (s.equals("Yes") || s.equals("yes") || s.equals("true") || s.equals("True"))
            return Boolean.TRUE;
        if (s.equals("No") || s.equals("no") || s.equals("false") || s.equals("False"))
            return Boolean.FALSE;
        
		throw new IllegalArgumentException(s + " is not a legal boolean value");
	}

}
