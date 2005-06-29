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
package org.eclipse.ve.sweet.verifiers;

import java.util.*;

import org.eclipse.ve.sweet.converters.*;
import org.eclipse.ve.sweet.verifier.*;


public class DateVerifier extends DateConversionSupport implements IVerifier {
	// TODO: Can we do any sensible (locale-independent) checking here?
	public boolean verifyFragment(String fragment) {
		return true;
	}

	public boolean verifyFullValue(String value) {
		return parse(value)!=null;
	}

	public String getHint() {
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
