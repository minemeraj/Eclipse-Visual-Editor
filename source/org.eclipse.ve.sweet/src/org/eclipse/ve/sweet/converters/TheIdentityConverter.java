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
 * TheIdentityConverter.  Returns the source value (the identity function).
 *
 * @author djo
 */
public class TheIdentityConverter implements IConverter {

	public static final IConverter IDENTITY = new TheIdentityConverter();

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.converter.IConverter#convert(java.lang.Object)
	 */
	public Object convert(Object source) {
        if (source == null) {
            return "";
        }
		return source;
	}

}
