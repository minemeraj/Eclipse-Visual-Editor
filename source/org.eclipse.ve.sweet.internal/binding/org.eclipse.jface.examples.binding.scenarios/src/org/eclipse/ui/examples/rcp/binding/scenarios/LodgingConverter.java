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
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.databinding.converter.IConverter;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
 
public class LodgingConverter implements IConverter {

	public Class getModelType(){
		return Lodging.class;
	}

	public Class getTargetType() {
		return String.class;
	}

	public Object convertTargetToModel(Object targetObject) {
		throw new RuntimeException("Not implemented yet");
	}

	public Object convertModelToTarget(Object modelObject) {
		return modelObject == null ? null : ((Lodging)modelObject).getName();
	}
}
