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
package org.eclipse.ve.sweet.converters;

import org.eclipse.ve.sweet.converter.IConverter;

/**
 * TheIdentityConverter.  Returns the source value (the identity function).
 *
 * @author djo
 */
public class TheNullConverter implements IConverter {

	public static final IConverter NULL = new TheNullConverter();

	/* (non-Javadoc)
	 * @see org.eclipse.jface.binding.converter.IConverter#convert(java.lang.Object)
	 */
	public Object convert(Object source) {
        return "";
	}

}
