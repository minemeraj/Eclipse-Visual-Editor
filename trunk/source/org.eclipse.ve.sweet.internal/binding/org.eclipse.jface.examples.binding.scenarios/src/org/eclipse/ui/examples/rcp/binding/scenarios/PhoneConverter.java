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
 
public class PhoneConverter implements IConverter {

	public Class getModelType() {
		return String.class;
	}
	public Class getTargetType() {
		return String.class;
	}

	public Object convertTargetToModel(Object targetObject) {
		// Unformat the formatted phone to a raw String
		return removePhoneSeparators((String)targetObject);
	}
	public Object convertModelToTarget(Object modelObject) {
		String unformattedPhone = (String)modelObject;
       		if(unformattedPhone == null || unformattedPhone.length() != 10){
			return unformattedPhone;
		} else {
			StringBuffer sb = new StringBuffer(13);
			sb.append(unformattedPhone.substring(0,3));
			sb.append('-');
			sb.append(unformattedPhone.subSequence(3,6));
			sb.append('-');
			sb.append(unformattedPhone.subSequence(6,10));
			return sb.toString();
		}
	}
	public static String removePhoneSeparators(String formattedPhone){
		StringBuffer sb = new StringBuffer(formattedPhone);
		for (int i = 0; i < formattedPhone.length(); i++) {
			char c = formattedPhone.charAt(i);
			if((int)'0' <= (int)c && (int)'9' >= c){
				sb.append(c);
			}
		}
		return sb.toString();		
	}
}
