package org.eclipse.ve.internal.jfc.vm;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JTableManager.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:44:12 $ 
 */

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Used to manage the JTable. It is static, references
 * will have the JTable passed in.
 */
public class JTableManager {
	
	/**
	 * Remove all columns. It's easier to do this then to try
	 * to individually remove them. There are times where the
	 * old ones aren't known.
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
	 */
	public static void resetHeaderValue(JTable jtable, TableColumn column) {
		int modelColumn = column.getModelIndex();
		String columnName = jtable.getModel().getColumnName(modelColumn);
		column.setHeaderValue(columnName);
	}
}
