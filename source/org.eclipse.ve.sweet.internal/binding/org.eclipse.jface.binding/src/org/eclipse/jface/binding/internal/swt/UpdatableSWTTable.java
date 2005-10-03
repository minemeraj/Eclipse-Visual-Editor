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
package org.eclipse.jface.binding.internal.swt;

import org.eclipse.jface.binding.IUpdatableTable;
import org.eclipse.jface.binding.Updatable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class UpdatableSWTTable extends Updatable implements IUpdatableTable {

	private final Table table;

	public UpdatableSWTTable(Table table) {
		this.table = table;
	}

	public Class[] getColumnTypes() {
		int columnCount = table.getColumnCount();
		Class[] result = new Class[columnCount];
		for (int i = 0; i < result.length; i++) {
			result[i] = String.class;
		}
		return result;
	}

	public int getSize() {
		return table.getItemCount();
	}

	public int addElement(Object string, int index) {
		TableItem item = new TableItem(table, SWT.NONE, index);
		item.setText((String) string);
		return index;
	}

	public void removeElement(int index) {
		table.remove(index);
	}

	public Object getElement(int index) {
		return table.getItem(index).getText(0);
	}

	public Class getElementType() {
		return String.class;
	}

	public void setElement(int index, Object value) {
		table.getItem(index).setText(0, (String) value);
	}

	public Object[] getValues(int index) {
		int columnCount = table.getColumnCount();
		Object[] result = new Object[columnCount];
		for (int i = 0; i < columnCount; i++) {
			result[i] = table.getItem(index).getText(i);
		}
		return result;
	}

	public void setElementAndValues(int index, Object element, Object[] values) {
		setElement(index, element);
		setValues(index, values);
	}

	public int addElementWithValues(int index, Object element, Object[] values) {
		int position = addElement(element, index);
		setValues(index, values);
		return position;
	}

	public void setValues(int index, Object[] values) {
		int columnCount = table.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			table.getItem(index).setText(i, (String) values[i]);
		}
	}

}
