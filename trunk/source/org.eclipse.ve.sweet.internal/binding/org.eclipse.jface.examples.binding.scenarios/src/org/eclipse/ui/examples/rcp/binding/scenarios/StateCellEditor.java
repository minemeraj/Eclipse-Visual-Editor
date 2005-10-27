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
 *  $RCSfile: StateCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-27 15:38:51 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;
 
public class StateCellEditor extends ComboBoxCellEditor {
	
	public StateCellEditor(Composite parent){
		super(parent,StateConverter.STATE_LETTERS);
	}
	protected void doSetValue(Object value) {
		int index = -1;
		for (int i = 0; i < getItems().length; i++) {
			if(getItems()[i].equals(value)){
				index = i;
				break;
			}
		}
		super.doSetValue(new Integer(index));
	}
	protected Object doGetValue() {
		int selectedIndex = ((Integer) super.doGetValue()).intValue();
		return getItems()[selectedIndex];
	}
}
