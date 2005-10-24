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

import org.eclipse.jface.binding.IValidator;
 
public class PhoneValidator implements IValidator {
	
	public PhoneValidator(){
	}

	public String isPartiallyValid(Object value) {
		return null;
	}

	public String isValid(Object value) {
		return null;
	}

}
