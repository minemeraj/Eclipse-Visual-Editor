/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jface.binding;

public class IdentityConverter implements IConverter {

	private final Class fromType;

	private final Class toType;

	public IdentityConverter(Class type) {
		this.fromType = type;
		this.toType = type;
	}

	/**
	 * useful for converting between, e.g., Integer.class and int.class
	 * 
	 * @param fromType
	 * @param toType
	 */
	public IdentityConverter(Class fromType, Class toType) {
		this.fromType = fromType;
		this.toType = toType;
	}

	public Object convert(Object object) {
		return object;
	}

	public Class getFromType() {
		return fromType;
	}

	public Class getToType() {
		return toType;
	}

}
