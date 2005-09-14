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
package org.eclipse.ve.sweet.timeexample.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.ve.sweet.converter.IConverter;


public class ConvertJustDate2String implements IConverter {	
	public Object convert(Object source) {
		Date date = ((JustDate)source).getDate();
		return new SimpleDateFormat("MM/dd/yyyy").format((Date)date);
	}	
}
