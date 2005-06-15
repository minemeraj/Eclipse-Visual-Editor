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
 *  $RCSfile: TableManager.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.widgets.*;
 

/**
 * Provides static Table methods.
 * @since 1.1.0
 */
public class TableManager {

	/**
	 * Get all of the column rects. It will be an array of 5-tuples. (column, x, y, width, height).
	 * @param table
	 * @return column rects or <code>null</code> if there are no columns or the columns are not visible.
	 * 
	 * @since 1.1.0
	 */
	public static Object[] getColumnRects(final Table table) {
		class GetRects implements Runnable {
			public Object[] result;

			public void run() {
				result = primGetColumnRects(table);
			}
		}
		GetRects r = new GetRects();
		table.getDisplay().syncExec(r);
		return r.result;
	}

	private static Object[] primGetColumnRects(Table table) {
		if (!table.getHeaderVisible())
			return null;	// They aren't visible. So no headers to size.
		
		TableColumn[] columns = table.getColumns();
		if (columns.length == 0)
			return null;
		int[] columnOrder = table.getColumnOrder();
		Object[] result = new Object[5*columns.length];
		int resultNdx = 0;
		// The assumption is that the columns start at (0,0) in the Table.
		int x = 0;
		Integer y = new Integer(0);	// Y never changes.
		int width = 0;
		Integer height = new Integer(table.getHeaderHeight());	// Height never changes.
		// Cover them in the order they are showing, not the order created.
		for (int condx = 0; condx < columnOrder.length; condx++) {
			TableColumn column = columns[columnOrder[condx]];
			// The list sent back is a 5-tuple (column, x, y, width, height)
			width = column.getWidth();
			result[resultNdx++]=column;
			result[resultNdx++]=new Integer(x);
			result[resultNdx++]=y;
			result[resultNdx++]=new Integer(width);
			result[resultNdx++]=height;
			x+=width;	// Next column start.
		}
		return result;
	}
}
