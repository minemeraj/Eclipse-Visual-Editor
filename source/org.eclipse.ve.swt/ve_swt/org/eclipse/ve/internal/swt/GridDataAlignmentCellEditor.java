/*******************************************************************************
 * Copyright (c)  2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GridDataAlignmentCellEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2004-06-03 14:45:07 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.java.core.EnumeratedIntValueCellEditor;

public class GridDataAlignmentCellEditor extends EnumeratedIntValueCellEditor {
	
	public GridDataAlignmentCellEditor(Composite composite){
		super(composite,
			new String[] { SWTMessages.getString("GridDataAlignmentCellEditor.Name.Beginning") , SWTMessages.getString("GridDataAlignmentCellEditor.Name.Center"), SWTMessages.getString("GridDataAlignmentCellEditor.Name.End"), SWTMessages.getString("GridDataAlignmentCellEditor.Name.Fill") } , //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	        new Integer[] { new Integer(GridData.BEGINNING) , new Integer(GridData.CENTER), new Integer(GridData.END), new Integer(GridData.FILL) } ,			
			new String[] { "org.eclipse.swt.layout.GridData.BEGINNING" , "org.eclipse.swt.layout.GridData.CENTER", "org.eclipse.swt.layout.GridData.END", "org.eclipse.swt.layout.GridData.FILL" }  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
	}	
}
