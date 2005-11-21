/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DoubleConverter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-11-21 16:07:40 $ 
 */
package org.eclipse.ui.examples.pojo.binding.scenarios;

import org.eclipse.jface.databinding.IConverter;
 
public class DoubleConverter implements IConverter {

	public Class getModelType() {
		return Double.TYPE;
	}

	public Class getTargetType() {
		return String.class;
	}

	public Object convertTargetToModel(Object targetObject) {
		return new Double((String)targetObject);
	}

	public Object convertModelToTarget(Object modelObject) {
		return modelObject.toString();
	}

}
