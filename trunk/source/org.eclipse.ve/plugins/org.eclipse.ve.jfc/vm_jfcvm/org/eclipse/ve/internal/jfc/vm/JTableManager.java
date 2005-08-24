/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.vm;
/*
 *  $RCSfile: JTableManager.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.awt.Rectangle;
import java.util.*;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.*;

/**
 * Used to manage the JTable. It is static, references
 * will have the JTable passed in.
 */
public class JTableManager {
	
	/**
	 * Initialize for the table model. This will put in our special
	 * table model if the model for the table is just a default
	 * table model (since that one is created by default in JTable).
	 * It will do this if it is exactly a DefaultTableModel
	 * and the row/column count is 0. Anything else means it is
	 * specialized and we don't want to override that.
	 * @param jtable
	 * 
	 * @since 1.1.0
	 */
	public static void initializeTableModel(JTable jtable) {
		TableModel tm = jtable.getModel();
		if (tm.getClass() == DefaultTableModel.class) {
			if (tm.getColumnCount() == 0 && tm.getRowCount() == 0) {
				jtable.setModel(new PreviewTableModel());
			}
		}
	}
	
	/**
	 * Add the table column before the given column (if not null), else add to the end.
	 * @param jtable
	 * @param addColumn
	 * @param beforeColumn if not found in jtable, or if <code>null</code>, then add at end, else add before it.
	 * 
	 * @since 1.1.0
	 */
	public static void addColumnBefore(JTable jtable, TableColumn addColumn, TableColumn beforeColumn) {
		// Find the index of the before column. Need to add to end and move to that index. No
		// way to just add at that index.
		int ndx = indexOfColumn(jtable, beforeColumn);
		jtable.addColumn(addColumn);
		if (ndx != -1)
			jtable.moveColumn(jtable.getColumnCount()-1, ndx);
	}
	
	/**
	 * Return the index of the given column. If not found or if column is null, then answer -1.
	 * @param jtable
	 * @param column column to search for. If <code>null</code>, then -1 returned.
	 * @return index of column of -1 if not found or null.
	 * 
	 * @since 1.1.0
	 */
	protected static int indexOfColumn(JTable jtable, TableColumn column) {
		if (column != null) {
			TableColumnModel cm = jtable.getColumnModel();
			Enumeration e = cm.getColumns();
			int index = -1;
			while (e.hasMoreElements()) {
				index++;
				if (e.nextElement() == column)
					return index;
			}
			return -1;
		} else
			return -1;
	}

	/**
	 * Remove all columns. It's easier to do this then to try
	 * to individually remove them. There are times where the
	 * old ones aren't known.
	 * @param jtable
	 * 
	 * @since 1.1.0
	 */
	public static void removeAllColumns(JTable jtable) {
        TableColumnModel cm = jtable.getColumnModel();
        while (cm.getColumnCount() > 0) {
            cm.removeColumn(cm.getColumn(0));
        }
	}

	/**
	 * Reset the header value of a table column so that it matches the default.
	 * A helper so that we don't need to remove and add all of the columns.
	 * @param jtable
	 * @param column
	 * 
	 * @since 1.1.0
	 */
	public static void resetHeaderValue(JTable jtable, TableColumn column) {
		int modelColumn = column.getModelIndex();
		String columnName = jtable.getModel().getColumnName(modelColumn);
		column.setHeaderValue(columnName);
	}
	
	/**
	 * Get the rectangle for the given table column
	 * @param jtable
	 * @param column
	 * @return rect of tablecolumn or <code>null</code> if column not in model. 
	 * 
	 * @since 1.1.0
	 */
	public static Rectangle getColumnRect(JTable jtable, TableColumn column) {
		// Need the visual index, not the model index. So we need to get TableColumnModel and
		// calculate the index.
		int i=0;
		for (Enumeration enumer = jtable.getColumnModel().getColumns(); enumer.hasMoreElements(); i++) {
			if (enumer.nextElement() == column) {
				return jtable.getTableHeader().getHeaderRect(i);
			}
		}
		return null;
	}
	
	/**
	 * Get all of the column rects. The array returned will be 5-tuples (TableColumn, x, y, width, height, TableColumn, ...)
	 * <p>
	 * Using this format for efficiency. We can transfer this one level array in one transaction instead of
	 * asking each x,y,width,height separately.
	 *  
	 * @param jtable
	 * @return column rects as array of 5-tuples. or <code>null</code> if no columns.
	 * 
	 * @since 1.1.0
	 */
	public static Object[] getColumnRects(JTable jtable) {
		TableColumnModel columnModel = jtable.getColumnModel();
		int columnCount = columnModel.getColumnCount();
		if (columnCount == 0)
			return null;
		JTableHeader tableHeader = jtable.getTableHeader();
		List result = new ArrayList(5*columnCount);
		int i = 0;
		for (Enumeration enumer = columnModel.getColumns(); enumer.hasMoreElements(); i++) {
			TableColumn column = (TableColumn) enumer.nextElement();
			result.add(column);
			Rectangle rect = tableHeader.getHeaderRect(i);
			result.add(new Integer(rect.x));
			result.add(new Integer(rect.y));
			result.add(new Integer(rect.width));
			result.add(new Integer(rect.height));
		}
		return result.toArray();
	}
}
