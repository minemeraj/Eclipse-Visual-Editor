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
package org.eclipse.ve.sweet.validators;

import java.util.*;

import org.eclipse.ve.sweet.converters.*;
import org.eclipse.ve.sweet.validator.*;


public class DateValidator extends DateConversionSupport implements IValidator {
	// TODO: Can we do any sensible (locale-independent) checking here?
	public String isValidPartialInput(String fragment) {
		return null;
	}
    
    public String isValid(Object value) {
        return parse((String)value)!=null ? null : getHint();
    }

	private String getHint() {
		Date sampleDate=new Date();
		StringBuffer samples=new StringBuffer();
		for(int formatterIdx=1;formatterIdx<numFormatters()-2;formatterIdx++) {
			samples.append('\'');
			samples.append(format(sampleDate,formatterIdx));
			samples.append("', ");
		}
        samples.append('\'');
        samples.append(format(sampleDate,0));
        samples.append('\'');
		return "Examples: "+samples+",...";
	}
}
