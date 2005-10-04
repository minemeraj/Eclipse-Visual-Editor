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
 *  $RCSfile: TreeManager.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 15:41:48 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.widgets.*;
 

/**
 * Provides static Tree methods.
 * @since 1.1.0
 */
public class TreeManager {

	/**
	 * Get all of the column rects. It will be an array of 5-tuples. (column, x, y, width, height).
	 * @param tree
	 * @return column rects or <code>null</code> if there are no columns or the columns are not visible.
	 * 
	 * @since 1.1.0
	 */
	public static Object[] getColumnRects(final Tree tree) {
		class GetRects implements Runnable {
			public Object[] result;

			public void run() {
				result = primGetColumnRects(tree);
			}
		}
		GetRects r = new GetRects();
		tree.getDisplay().syncExec(r);
		return r.result;
	}

	private static Object[] primGetColumnRects(Tree tree) {
		if (!tree.getHeaderVisible())
			return null;	// They aren't visible. So no headers to size.
		
		TreeColumn[] columns = tree.getColumns();
		if (columns.length == 0)
			return null;
		Object[] result = new Object[5*columns.length];
		int resultNdx = 0;
		// The assumption is that the columns start at (0,0) in the Table.
		int x = 0;
		Integer y = new Integer(0);	// Y never changes.
		int width = 0;
		Integer height = new Integer(tree.getHeaderHeight());	// Height never changes.
		for (int condx = 0; condx < columns.length; condx++) {
			TreeColumn column = columns[condx];
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
