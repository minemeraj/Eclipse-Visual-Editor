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

import java.util.StringTokenizer;

import org.eclipse.jface.binding.IColumn;
import org.eclipse.ui.examples.rcp.adventure.Account;
 
public class FullNameColumn implements IColumn {
	
	public void setValue(Object row, Object value) {
		Account account = (Account)row;
		StringTokenizer tokenizer = new StringTokenizer((String)value,",");
		account.setFirstName((String) tokenizer.nextElement());
		account.setLastName((String) tokenizer.nextElement());
	}

	public Object getValue(Object row) {
		Account account = (Account)row;
		StringBuffer sb = new StringBuffer();
		sb.append(account.getFirstName());
		sb.append(',');
		sb.append(account.getLastName());
		return sb.toString();
	}

	public Class getType(Object row) {
		return String.class;
	}

}
