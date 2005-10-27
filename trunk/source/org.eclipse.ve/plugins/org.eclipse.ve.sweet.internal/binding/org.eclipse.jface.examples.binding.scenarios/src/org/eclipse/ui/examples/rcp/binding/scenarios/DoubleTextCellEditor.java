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
 *  $RCSfile: DoubleTextCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-27 15:38:52 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
 
public class DoubleTextCellEditor extends TextCellEditor {
	
	public DoubleTextCellEditor(Composite parent){
		super(parent);
	}
	protected void doSetValue(Object value) {
		super.doSetValue( ((Double)value).toString() );
	}
	protected Object doGetValue() {
		String textValue = (String)super.doGetValue();
		return new Double((String)textValue);
	}
}
