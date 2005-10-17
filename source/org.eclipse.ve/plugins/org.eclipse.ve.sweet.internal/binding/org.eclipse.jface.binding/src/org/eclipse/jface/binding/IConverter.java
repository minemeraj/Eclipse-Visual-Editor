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

public interface IConverter {

	/**
	 * Returns the class whose instances can be converted by this converter.
	 * @return the class whose instances can be converted
	 */
	public Class getModelType();

	/**
	 * Returns the class to which this converter can convert.
	 * @return the class to which this converter can convert
	 */
	public Class getTargetType();

	/**
	 * Returns the result of the conversion of the given object. The given
	 * object must be an instance of getFromType(), and the result must be an
	 * instance of getToType().
	 * 
	 * @param object the object to convert
	 * @return the converted object
	 */
	public Object convertModel(Object fromObject);

	/**
	 * Returns the result of the conversion of the given object. The given
	 * object must be an instance of getToType(), and the result must be an
	 * instance of getFromType().
	 * 
	 * @param object the object to convert
	 * @return the converted object
	 */	
	public Object convertTarget(Object toObject);
}
